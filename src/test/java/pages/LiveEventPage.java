package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;

public class LiveEventPage {
    private final Page page;

    public LiveEventPage(Page page) {
        this.page = page;
    }

    // ==========================================
    // LOCATOR GETTERS
    // ==========================================


    public Locator getBroadcastLiveLink() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Broadcast live"));
    }

    public Locator getCreateLiveStreamButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Create live stream"));
    }

    public Locator getLiveEventRadioButton() {
        return page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("Live Event"));
    }

    public Locator getNameInputField() {
        // Option A: Try standard HTML input attributes (Update 'title' to the actual name attribute if different)
        return page.locator("input[name='title'], input[name='name'], input[placeholder*='Name' i]");
    }

    public Locator getIngestFormatDropdown() {
        // Targets the visible custom dropdown component containing the text "Ingest format"
        return page.locator("label:has-text('Ingest format'), wui-select:has-text('Ingest format')");
    }


    // Use CSS selector so Playwright automatically pierces the Shadow DOM
    public Locator getIngestFormatSelect() {
        return page.locator("select[name='ingestFormat']");
    }


    public Locator getIngestFormatSRTOption() {
        return page.locator("//wui-list-item[contains(., 'SRT')] | //option[@value='srt']");
    }

    public Locator getAdvancedSettingsHeader() {
        return page.locator("header:has-text('Advanced settings')");
    }

    public Locator getAdvancedSettingsChevron() {
        return page.locator("//header[.//h4[text()='Advanced settings']]//wui-button[@icon='chevron-down']");
    }

    public Locator getContentTypeDropdown() {
        return page.locator("//wui-select-input[@data-test='select-content-type']");
    }

    public Locator getContentTypeLiveEventOption() {
        return page.locator("//wui-list-item[contains(., 'Live Event')] | //option[@value='live_event']");
    }
    public Locator getConfirmCreateBroadcastButton() {
        return page.locator("//wui-button[@data-test='confirm-create-broadcast-live-and-close-slideout']");
    }
    public Locator getStreamIdCodeSnippet() {
        return page.locator("//wui-code-snippet[@data-test='show-stream-id']");
    }
    public Locator getPlaylistsLink() {
        return page.locator("//a[@data-test='main-nav-link-playlistList']");
    }
    // 1. New Playlist Locator
    public Locator getNewPlaylistButton() {
        return page.locator("//span[@class='label' and text()='New playlist']");
    }

    // 2. Add Media Button Locator (using data-test)
    public Locator getAddMediaButton() {
        return page.locator("button.inner")
                .filter(new Locator.FilterOptions().setHasText("Add media"))
                .first();
    }
    // Search Input Field
    public Locator getSearchInputField() {
        return page.locator("//input[@type='search' and @placeholder='Search by title, description, tags or media ID']");
    }

    // Media Checkbox
    public Locator getMediaCheckbox() {
        return page.locator("//div[contains(@class, 'checkbox') and contains(@class, 'input')]");
    }

    // Add to Top Button
    public Locator getAddToTopButton() {
        return page.locator("//button[.//span[text()='Add to top']]");
    }

    // ==========================================
    // STEP & ACTION METHODS
    // ==========================================

    @Step("Verify 'Broadcast live' link is visible")
    public void verifyBroadcastLiveLinkVisible() {
        assertThat(getBroadcastLiveLink()).isVisible();
    }

    @Step("Click on 'Broadcast live' link")
    public void clickBroadcastLive() {
        getBroadcastLiveLink().waitFor();
        getBroadcastLiveLink().click();
    }

    @Step("Verify 'Create live stream' button is visible")
    public void verifyCreateLiveStreamButtonVisible() {
        assertThat(getCreateLiveStreamButton()).isVisible();
    }

    @Step("Click on 'Create live stream' button")
    public void clickCreateLiveStream() {
        getCreateLiveStreamButton().waitFor();
        getCreateLiveStreamButton().click();
    }

    @Step("Verify Live Event radio button is visible")
    public void verifyLiveEventRadioButtonVisible() {
        assertThat(getLiveEventRadioButton()).isVisible();
    }

    @Step("Click Live Event radio button")
    public void clickLiveEventRadioButton() {
        getLiveEventRadioButton().waitFor();
        getLiveEventRadioButton().click();
    }

    @Step("Verify Live Event radio button is checked")
    public void verifyLiveEventRadioButtonIsChecked() {
        // Use isChecked() for standard HTML radio buttons
        assertThat(getLiveEventRadioButton()).isChecked();
    }

    @Step("Verify Name input field is visible")
    public void verifyNameInputFieldVisible() {
        assertThat(getNameInputField()).isVisible();
    }

    @Step("Enter random event name into Name field")
    public String enterRandomEventName() {
        Locator input = getNameInputField().first();
        input.waitFor();
        String randomName = "testautomationliveevent_" + UUID.randomUUID().toString().substring(0, 8);
        input.fill(randomName);
        return randomName;
    }

    @Step("Verify 'Ingest Format' dropdown container is visible")
    public void verifyIngestFormatDropdownVisible() {
        assertThat(getIngestFormatDropdown().first()).isVisible();
    }

    public void selectIngestFormatSRT() {

        // Open the ingest format dropdown
        Locator ingestFormatDropdown = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("RTMP")
        );

        assertTrue(
                ingestFormatDropdown.isVisible(),
                "Ingest format dropdown should be visible"
        );

        ingestFormatDropdown.click();

        // Select EXACTLY "SRT", not "SRT (Pull)"
        Locator srtOption = page.locator("span.label").filter(
                new Locator.FilterOptions().setHasText(
                        java.util.regex.Pattern.compile("^SRT$")
                )
        );

        assertTrue(
                srtOption.isVisible(),
                "SRT option should be visible"
        );

        srtOption.click();
    }

    @Step("Verify 'Advanced settings' header is visible")
    public void verifyAdvancedSettingsVisible() {
        assertThat(getAdvancedSettingsHeader()).isVisible();
    }
    public void clickAdvancedSettingsChevron() {

        Locator chevron = page.locator(
                "button.inner:has(wui-icon[name='chevron-down'])"
        ).last();

        assertTrue(
                chevron.isVisible(),
                "Advanced settings chevron should be visible"
        );

        chevron.click();
    }

    @Step("Verify 'Content Type' dropdown is visible")
    public void verifyContentTypeDropdownVisible() {
        assertThat(getContentTypeDropdown()).isVisible();
    }

    @Step("Click Content Type dropdown and select 'Live Event'")
    public void selectContentTypeLiveEvent() {
        Locator contentType = page.locator("[data-test='select-content-type']");

        assertTrue(
                contentType.isVisible(),
                "Content Type dropdown should be visible"
        );

        // Open the Content Type dropdown
        contentType.click();

        // Select exactly "Live Event"
        Locator liveEventOption = page.locator(
                "[data-test='select-content-type'] span"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText("Live Event")
        ).first();

        assertTrue(
                liveEventOption.isVisible(),
                "Live Event option should be visible"
        );

        liveEventOption.click();
    }

    @Step("Click on 'Confirm Create Broadcast' button and verify action")
    public void clickConfirmCreateBroadcastButton() {
        assertThat(getConfirmCreateBroadcastButton()).isVisible();
        assertThat(getConfirmCreateBroadcastButton()).isEnabled();
        getConfirmCreateBroadcastButton().click();
        assertThat(getConfirmCreateBroadcastButton()).isHidden();
    }
    @Step("Verify Stream ID snippet is visible")
    public void verifyStreamIdVisible() {
        assertThat(getStreamIdCodeSnippet()).isVisible();
    }

    @Step("Verify and copy Stream ID text")
    public String getAndVerifyStreamId() {

        Locator streamIdSnippet = page.locator(
                "wui-code-snippet[data-test='show-stream-id']"
        );

        assertTrue(
                streamIdSnippet.isVisible(),
                "Stream ID section should be visible"
        );

        // Find the copy button inside the Stream ID component
        Locator copyButton = streamIdSnippet.locator(
                "button.inner:has(wui-icon[name='copy'])"
        );

        assertTrue(
                copyButton.isVisible(),
                "Stream ID copy button should be visible"
        );

        // Click Copy
        copyButton.click();

        // Read the value copied to clipboard
        String streamId = (String) page.evaluate(
                "() => navigator.clipboard.readText()"
        );

        streamId = streamId.trim();

        System.out.println("========== STREAM ID ==========");
        System.out.println("Copied Stream ID: [" + streamId + "]");
        System.out.println("===============================");

        assertFalse(
                streamId.isEmpty(),
                "Copied Stream ID should not be empty"
        );

        return streamId;
    }
    @Step("Verify 'Playlists' link is visible")
    public void verifyPlaylistsLinkVisible() {

        Locator playlistsLink = page.getByText(
                "Playlists",
                new Page.GetByTextOptions().setExact(true)
        ).first();

        assertTrue(
                playlistsLink.isVisible(),
                "Playlists link should be visible"
        );
        playlistsLink.click();
    }

    @Step("Click on 'Playlists' link")
    public void clickPlaylistsLink() {
        // 1. Assert element is visible before interaction
        assertThat(getPlaylistsLink()).isVisible();

        // 2. Click the Playlists link
        getPlaylistsLink().click();

        // 3. Assert navigation success (e.g., URL contains '/playlists')
        assertThat(page).hasURL(java.util.regex.Pattern.compile(".*/playlists.*"));
    }

    @Step("Verify 'New playlist' button is visible")
    public void verifyNewPlaylistButtonVisible() {

        Locator newPlaylist = page.locator(
                "span.label[title='New playlist']"
        ).first();

        // Wait for the element to exist
        newPlaylist.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.ATTACHED)
        );

        assertTrue(
                newPlaylist.isVisible(),
                "New playlist button should be visible"
        );
    }
    @Step("Click on 'New playlist' button")
    public void clickNewPlaylistButton() {

        Locator newPlaylist = page.locator(
                "span.label[title='New playlist']"
        ).first();

        assertTrue(
                newPlaylist.isVisible(),
                "New playlist button should be visible"
        );

        newPlaylist.click();
    }


    @Step("Verify 'Add media' button is visible")
    public void verifyAddMediaButtonVisible() {
        Locator addMediaButton = getAddMediaButton();

        assertThat(addMediaButton).isVisible();
    }

    @Step("Click on 'Add media' button")
    public void clickAddMediaButton() {
        Locator addMediaButton = getAddMediaButton();

        assertThat(addMediaButton).isVisible();
        assertThat(addMediaButton).isEnabled();

        addMediaButton.click();
    }
    @Step("Search media by Stream ID: {streamId}")
    public void searchMediaByStreamId(String streamId) {

        Locator searchInput = page.locator(
                "input.input[type='search'][placeholder='Search by title, description, tags or media ID']"
        );

        assertTrue(searchInput.isVisible(), "Search input should be visible");
        assertTrue(searchInput.isEnabled(), "Search input should be enabled");

        // Enter the Stream ID obtained from the live event
        searchInput.fill(streamId);

        // Verify the Stream ID was entered
        assertEquals(
                searchInput.inputValue(),
                streamId,
                "Stream ID should be entered in the search field"
        );

        // Wait for search results
        page.waitForTimeout(1000);
    }

    @Step("Verify and click the media item checkbox")
    public void selectMediaCheckbox() {

        Locator checkbox = page.locator(
                "div.checkbox-inner"
        ).first();

        assertTrue(
                checkbox.isVisible(),
                "Media checkbox should be visible"
        );

        checkbox.click();

        System.out.println("Media checkbox selected");
    }

    @Step("Verify and click 'Add to top' button")
    public void clickAddToTopButton() {

        Locator addToTopButton = getAddToTopButton();

        assertThat(addToTopButton).isVisible();
        assertThat(addToTopButton).isEnabled();

        addToTopButton.click();
    }

    @Step("Perform search, select media, and add to top for Stream ID: {streamId}")
    public void addMediaToTopOfPlaylist(String streamId) {

        searchMediaByStreamId(streamId);
        page.waitForTimeout(20_000);
        selectMediaCheckbox();
        page.waitForTimeout(50_000);
        clickAddToTopButton();
    }
    @Step("Scroll to Live")
    public void scrollToLive() {

        Locator liveTabLocator = page.locator("//*[normalize-space()='Live playlist']");
        for (int i = 0; i < 12; i++) {

            if (liveTabLocator.isVisible()) {
                System.out.println("live section found");
                liveTabLocator.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "section was not found after scrolling"
        );
    }

    public Locator getliveCard() {
        return page.locator("//h2[contains(text(),'Live playlist')]/following-sibling::div//a[1]");
    }
    // Locates the first card/container in the Live playlist section
    public Locator gettitleElement() {
        return page.locator("//h2[contains(text(),'Live playlist')]/following-sibling::div//a[1]//h3");
    }
    public Locator getliveBadge() {
        return page.locator("//*[text()='LIVE']");
    }
    public Locator getscheduleTimeElement() {
        return page.locator("//*[contains(text(), 'Today') or contains(text(), 'AM') or contains(text(), 'PM')]");
    }
    public Locator scheduledStatusLocator() {
        return page.locator("xpath=//*[translate(text(), 'SCHEDULED', 'scheduled')='scheduled']");
    }
        // ==================== Live Content Locators ====================

    public Locator liveSection() {
        return page.locator("section[aria-label='Live playlist']");
    }

    public Locator eventCard() {
        return liveSection().locator("a[role='button']").first();
    }

    public Locator eventTitle() {
        return eventCard().locator("h3");
    }

    public Locator scheduledTime() {
        return eventCard().locator("h3 + div");
    }
    @Step("Verify live content is visible")
    public void verifyLiveContent() {

        assertThat(liveSection()).isVisible();
        assertThat(eventCard()).isVisible();
        assertThat(eventTitle()).isVisible();
        assertThat(scheduledTime()).isVisible();

    }
    @Step("Verify and click live content")
    public void clickLiveContent() {
        assertThat(eventCard()).isVisible();
        eventCard().click();
    }
    public Locator streamStartTimer() {
        return page.locator("button[aria-disabled='false']")
                .filter(new Locator.FilterOptions()
                        .setHasText(Pattern.compile("Stream start in\\s*:\\s*\\d{2}:\\d{2}")));
    }
    public Locator getstreamUrl() {
        return  page.locator("wui-code-snippet[data-test='show-stream-url']");}
    @Step("Verify stream start timer is visible")
    public void verifyStreamStartTimer() {
        assertThat(streamStartTimer()).isVisible();

        System.out.println("Stream start timer: " + streamStartTimer().innerText());
    }





    @Step("Verify live Tag")
    public void Verifylivetag() {
        Locator liveSection = page.locator("section[aria-label='Live playlist']");

        assertThat(liveSection).isVisible();

        Locator liveTag = liveSection.getByText(
                "LIVE",
                new Locator.GetByTextOptions().setExact(true)
        );

        assertThat(liveTag).isVisible();
    }



    @Step("Verify live Tag")
    public void Verifyscheduledtag() {
        assertThat(scheduledStatusLocator()).isVisible();
    }
    @Step("Verify Time schedule")
    public void VerifyTimeschedule() {
        Locator liveSection = page.locator(
                "section[aria-label='Live playlist']"
        );
        Locator eventCard = liveSection.locator("a[role='button']").first();

        Locator scheduledTime = eventCard.locator("h3 + div");
        assertThat(scheduledTime).isVisible();
        System.out.println("Schedule: " + scheduledTime.innerText());
    }
    @Step("VerifyStreamUrl")
    public String getAndVerifyStreamUrl() {

        getstreamUrl().waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
        );

        System.out.println("========== STREAM URL DEBUG ==========");
        System.out.println("Text: " + getstreamUrl().innerText());
        System.out.println("TextContent: " + getstreamUrl().textContent());
        System.out.println("HTML: " + getstreamUrl().innerHTML());
        System.out.println("======================================");

        String url = getstreamUrl().innerText().trim();

        assertFalse(
                url.isEmpty(),
                "SRT Stream URL should not be empty"
        );

        return url;
    }
    public Process startFFmpeg(String srtUrl) throws IOException {

        String ffmpegPath =
                "C:\\Users\\Dhivya.S\\Downloads\\ffmpeg-9.0.2-essentials_build\\ffmpeg-9.0.2-essentials_build\\bin\\ffmpeg.exe";

        String mediaFile =
                "C:\\Users\\Dhivya.S\\Downloads\\test-video.mp4";

        ProcessBuilder processBuilder = new ProcessBuilder(
                ffmpegPath,
                "-re",
                "-stream_loop", "-1",
                "-i", mediaFile,
                "-c", "copy",
                "-f", "mpegts",
                srtUrl
        );

        processBuilder.redirectErrorStream(true);

        Process process = processBuilder.start();

        Thread ffmpegLogThread = new Thread(() -> {
            try (BufferedReader reader =
                         new BufferedReader(
                                 new InputStreamReader(
                                         process.getInputStream()))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println("[FFmpeg] " + line);
                }

            } catch (IOException e) {
                System.err.println(
                        "FFmpeg log error: " + e.getMessage()
                );
            }
        });

        ffmpegLogThread.setDaemon(true);
        ffmpegLogThread.start();

        return process;
    }


}