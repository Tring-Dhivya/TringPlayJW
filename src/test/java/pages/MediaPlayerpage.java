package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MediaPlayerpage {
    private final Page page;

    public MediaPlayerpage(Page page) {
        this.page = page;
    }

    public Locator getSeriesMediaCard() {
        return page.locator(
                "//a[@role='button'][.//h3[normalize-space()='Toolpati']]"
        );
    }
    public Locator getSettingsButton() {
        return page.locator(
                "//a[@href='/u/settings' and .//span[normalize-space()='Settings']]"
        ).first();
    }
    public Locator getAutoplayNextVideoSwitch() {
        return page.locator(
                "//h3[normalize-space()='Autoplay Next Video']" +
                        "/following-sibling::span//input[@role='switch']"
        );
    }
    private Locator getAutoplayNextVideoSwitchButton() {
        return page.locator(
                "//h3[normalize-space()='Autoplay Next Video']" +
                        "/following-sibling::span" +
                        "//div[contains(@class,'react-switch-bg')]"
        );
    }

    @Step("Verify Autoplay Next Video is OFF")
    public void verifyAutoplayNextVideoIsOff() {
        assertThat(getAutoplayNextVideoSwitch())
                .hasAttribute("aria-checked", "false");
    }
    @Step("Enable Autoplay Next Video")
    public void enableAutoplayNextVideo() {
        getAutoplayNextVideoSwitchButton().click();
    }
    @Step("Verify Autoplay Next Video is ON")
    public void verifyAutoplayNextVideoIsOn() {
        assertThat(getAutoplayNextVideoSwitch())
                .hasAttribute("aria-checked", "true");
    }
    @Step("Verify Toolpati series media is visible")
    public void verifySeriesMediaVisible() {
        assertThat(getSeriesMediaCard()).isVisible();
    }
    @Step("Scroll to Series section")
    public void scrollToSeriesSection() {

        Locator heading = page.locator(
                "//h2[normalize-space()='Space']"
        ).first();

        for (int i = 0; i < 9; i++) {

            if (heading.isVisible()) {
                System.out.println("Series section found");
                heading.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Series section was not found after scrolling"
        );
    }
    @Step("Verify and click Toolpati from Series")
    public void verifyAndClickSeries() {

        Locator toolpati = page.locator(
                "section[aria-label='Space'] a[role='button']"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText("Toolpati")
        ).first();

        assertThat(toolpati).isVisible();
        assertThat(toolpati.locator("h3")).hasText("Toolpati");

        toolpati.click();
    }
    @Step("Click Toolpati series media")
    public void clickSeriesMedia() {
        getSeriesMediaCard().click();
    }
    @Step("Verify Start Watching button is displayed after video completion")
    public void verifyStartWatchingAfterCompletion() {
        assertThat(
                page.getByRole(
                        AriaRole.BUTTON,
                        new Page.GetByRoleOptions().setName("Start Watching")
                )
        ).isVisible();
    }
    private Locator getNextVideoOverlay() {
        return page.locator(
                "img[alt='next video image']"
        );
    }
    @Step("Verify next video countdown is displayed")
    public void verifyNextVideoCountdownDisplayed() {

        assertThat(getNextVideoOverlay()).isVisible();
    }
    @Step("Verify next video starts playing automatically")
    public void verifyNextVideoStartsAutomatically() {

        page.waitForFunction(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video && !video.paused && !video.ended;" +
                        "}"
        );
    }
    @Step("Scroll page and find Toolpati series media")
    public void scrollAndFindSeriesMedia() {

        Locator seriesMedia = getSeriesMediaCard();

        for (int i = 0; i < 10; i++) {

            if (seriesMedia.count() > 0) {
                seriesMedia.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 700);
            page.waitForTimeout(500);
        }

        throw new AssertionError("Toolpati series media was not found after scrolling");
    }
    @Step("Verify Settings option is visible")
    public void verifySettingsVisible() {
        assertThat(getSettingsButton()).isVisible();
    }

    @Step("Click Settings")
    public void clickSettings() {
        getSettingsButton().click();
    }
    @Step("Wait until video reaches the autoplay popup window")
    public void waitUntilAutoplayPopupWindow() {
        page.waitForFunction(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "if (!video || !video.duration) return false;" +
                        "const remaining = video.duration - video.currentTime;" +
                        "return remaining <= 10 && remaining > 0;" +
                        "}"
        );
    }
    // Playback Speed menu
    private Locator getPlaybackSpeedMenu() {
        return page.locator(
                "//button[@aria-label='Playback speed' and @shaka-status='1x']"
        );
    }

    // Speed options
    public Locator getSpeed05x() {
        return page.locator(
                "//button[@data-cinema-close-bound and .//span[normalize-space()='0.5x']]"
        );
    }

    public Locator getSpeed1x() {
        return page.locator(
                "//button[@data-cinema-close-bound and .//span[normalize-space()='1x']]"
        );
    }

    public Locator getSpeed15x() {
        return page.locator(
                "//button[@data-cinema-close-bound and .//span[normalize-space()='1.5x']]"
        );
    }
    // ==================== Subtitles / Audio Tracks ====================

    public Locator closedCaptionsButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Closed Captions")
        );
    }

    public Locator subtitleEnglishOption() {
        return page.locator(".shaka-combined-column.shaka-combined-text")
                .locator("button")
                .filter(new Locator.FilterOptions().setHasText("English"));
    }

    public Locator subtitleOptions() {
        return page.locator(
                "#jw-player-2-settings-submenu-captions button[role='menuitemradio']"
        );
    }

    public Locator audioTracksButton() {
        return page.locator(".shaka-controls-container")
                .locator("button")
                .filter(new Locator.FilterOptions().setHasText("Audio"));
    }

    public Locator audioEnglishOption() {
        return page.locator(
                "#jw-player-2-settings-submenu-audioTracks button[aria-label='English']"
        );
    }

    public Locator audioJapaneseOption() {
        return page.locator(
                "#jw-player-2-settings-submenu-audioTracks button[aria-label='Japanies']"
        );
    }

    public Locator audioTrackOptions() {
        return page.locator(
                "#jw-player-2-settings-submenu-audioTracks button[role='menuitemradio']"
        );
    }
    @Step("Verify Playback Speed menu is visible")
    public void verifyPlaybackSpeedMenuVisible() {
        assertThat(getPlaybackSpeedMenu()).isVisible();
    }
    @Step("click Speed Option")
    public void clickSpeedOption() {getPlaybackSpeedMenu().click();
    }
    @Step("Verify 0.5x speed option is visible")
    public void verifySpeed05xVisible() {
        assertThat(getSpeed05x()).isVisible();
    }

    @Step("Verify 1x speed option is visible")
    public void verifySpeed1xVisible() {
        assertThat(getSpeed1x()).isVisible();
    }

    @Step("Verify 1.5x speed option is visible")
    public void verifySpeed15xVisible() {
        assertThat(getSpeed15x()).isVisible();
    }
    @Step("Verify 1x is selected as the default playback speed")
    public void verify1xIsDefaultSpeed() {
        assertThat(getSpeed1x())
                .hasAttribute("aria-selected", "true");
    }
    // ==================== Subtitles / Audio Tracks ====================
    public Locator audioLanguageColumn() {
        return page.locator(
                ".shaka-combined-column.shaka-combined-audio"
        );
    }

    public Locator audioLanguageOptions() {
        return page.locator(
                ".shaka-combined-column.shaka-combined-audio button"
        );
    }
    @Step("Inspect Shaka Player controls")
    public void inspectShakaControls() {

        Locator controls = page.locator(".shaka-controls-container").first();

        controls.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        System.out.println("Shaka control HTML:");
        System.out.println(controls.innerHTML());
    }
    @Step("Open Closed Captions")
    public void openClosedCaptions() {

        Locator shakaControls =
                page.locator(".shaka-controls-container").first();

        shakaControls.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        shakaControls.hover();

        page.waitForTimeout(1000);

        Locator ccButton = page.locator(
                "[aria-label*='Caption'], [aria-label*='caption']"
        ).first();

        System.out.println("CC buttons: "
                + page.locator(
                "[aria-label*='Caption'], [aria-label*='caption']"
        ).count());

        Assert.assertTrue(
                ccButton.isVisible(),
                "Closed Captions button is not displayed"
        );

        ccButton.click();

        System.out.println("Closed Captions menu opened");
    }
    @Step("Select English subtitle")
    public void selectEnglishSubtitle() {

        Locator englishOptions = subtitleEnglishOption();

        Assert.assertTrue(
                englishOptions.count() > 0,
                "English subtitle option is not available"
        );

        englishOptions.first().click();
    }
    @Step("Verify English subtitle is available")
    public void verifyEnglishSubtitleAvailable() {

        Locator englishOptions = subtitleEnglishOption();

        System.out.println("English subtitle options: " + englishOptions.count());

        Assert.assertTrue(
                englishOptions.count() > 0,
                "English subtitle option is not present"
        );

        Assert.assertTrue(
                englishOptions.first().isVisible(),
                "English subtitle option is not displayed"
        );

        System.out.println("English subtitle option is displayed");
    }



    @Step("Open Audio Tracks options")
    public void openAudioTracks() {

        assertTrue(
                audioTracksButton().isVisible(),
                "Audio Tracks button is not visible"
        );

        audioTracksButton().click();

        page.waitForTimeout(500);

        assertTrue(
                page.locator("#jw-player-2-settings-submenu-audioTracks")
                        .isVisible(),
                "Audio Tracks submenu is not visible"
        );
    }

    @Step("Verify Audio Language is displayed")
    public void verifyAudioLanguageDisplayed() {

        Locator audioLanguage = page.locator(
                ".shaka-combined-column.shaka-combined-audio"
        );

        Assert.assertTrue(
                audioLanguage.count() > 0,
                "Audio Language section is not present"
        );

        String text = audioLanguage.innerText();

        System.out.println("Audio Language section: " + text);

        Assert.assertTrue(
                text.contains("Audio Language"),
                "Audio Language heading is not displayed"
        );

        Assert.assertTrue(
                text.contains("English"),
                "English audio language is not displayed"
        );

        Assert.assertTrue(
                text.contains("Japanies"),
                "Japanies audio language is not displayed"
        );

        System.out.println("Audio Language is displayed");
    }
    @Step("Select Japanies audio track")
    public void selectJapaniesAudio() {

        assertTrue(
                audioJapaneseOption().isVisible(),
                "Japanies audio track is not visible"
        );

        audioJapaneseOption().click();

        page.waitForTimeout(1000);
    }

    @Step("Verify Japanies audio track is selected")
    public void verifyJapaniesAudioSelected() {

        assertEquals(
                audioJapaneseOption().getAttribute("aria-checked"),
                "true",
                "Japanies audio track was not selected"
        );

        assertEquals(
                audioEnglishOption().getAttribute("aria-checked"),
                "false",
                "English audio track is still selected"
        );
    }
    public Locator resolutionButton() {
        return page.locator(
                "button.shaka-resolution-button[aria-label='Resolution']"
        );
    }

    public Locator qualityMenu() {
        return page.locator(
                "div.shaka-settings-menu.shaka-resolutions"
        );
    }

    public Locator qualityOptions() {
        return qualityMenu().locator(
                "button.explicit-resolution"
        );
    }

    @Step("Open video quality settings")
    public void openQualityMenu() {

        Locator resolution = resolutionButton();

        Assert.assertTrue(
                resolution.isVisible(),
                "Resolution button is not displayed"
        );

        resolution.click();

        qualityMenu().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        System.out.println("Video quality menu opened");
    }

    @Step("Verify available video quality options")
    public void verifyQualityOptionsDisplayed() {

        Locator options = qualityOptions();

        int count = options.count();

        System.out.println("Available quality options: " + count);

        Assert.assertTrue(
                count > 0,
                "No video quality options are displayed"
        );

        for (int i = 0; i < count; i++) {

            Locator option = options.nth(i);

            String quality = option.innerText().trim();

            System.out.println("Quality option: " + quality);

            Assert.assertTrue(
                    option.isVisible(),
                    "Quality option is not visible: " + quality
            );
        }
    }




    @Step("Verify playback continues after quality change")
    public void verifyPlaybackContinues() {

        Locator video = page.locator("video").first();

        Assert.assertTrue(
                video.isVisible(),
                "Video element is not visible"
        );

        double timeBefore = ((Number) video.evaluate(
                "video => video.currentTime"
        )).doubleValue();

        page.waitForTimeout(3000);

        double timeAfter = ((Number) video.evaluate(
                "video => video.currentTime"
        )).doubleValue();

        System.out.println("Playback time before: " + timeBefore);
        System.out.println("Playback time after: " + timeAfter);

        Assert.assertTrue(
                timeAfter > timeBefore,
                "Playback did not continue after changing video quality"
        );
    }
    @Step("Select video quality: {quality}")
    public void selectQuality(String quality) {

        Locator qualityOption = qualityMenu()
                .locator("button.explicit-resolution")
                .filter(new Locator.FilterOptions().setHasText(quality));

        Assert.assertTrue(
                qualityOption.count() > 0,
                "Video quality option is not available: " + quality
        );

        Assert.assertTrue(
                qualityOption.first().isVisible(),
                "Video quality option is not visible: " + quality
        );

        qualityOption.first().click();

        System.out.println("Selected video quality: " + quality);
    }
}
