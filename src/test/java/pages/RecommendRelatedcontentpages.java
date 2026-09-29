package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Step;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RecommendRelatedcontentpages {
    public final Page page;

    public RecommendRelatedcontentpages(Page page) {
        this.page = page;
    }

    public Locator getSeriesDetailTitle() {
        return page.locator(
                "//main[@id='content']" +
                        "//header[@id='video-details']" +
                        "//h1"
        );
    }

    public Locator getEpisodesHeading() {
        return page.locator(
                "//main[@id='content']" +
                        "//h2[normalize-space()='Episodes']"
        );
    }

    public Locator getEpisodeCards() {
        return page.locator(
                "//main[@id='content']" +
                        "//h2[normalize-space()='Episodes']" +
                        "/ancestor::div[contains(@class,'relatedVideos')]" +
                        "//a[@role='button'][.//div[contains(@class,'_tag_')]]"
        );
    }

    public Locator getSeasonSelector() {
        return page.locator(
                "//*[self::button or self::select or self::div]" +
                        "[contains(normalize-space(), 'Season')]"
        );
    }

    public Locator getRecommendedContentSection() {
        return page.locator(
                "//main[@id='content']" +
                        "//*[self::h1 or self::h2 or self::h3]" +
                        "[normalize-space()='Recommended Content' " +
                        "or normalize-space()='Related Content']"
        );
    }

    @Step("Verify Series detail page is visible")
    public void verifySeriesDetailPageVisible() {

        assertThat(getSeriesDetailTitle()).isVisible();

        assertThat(getSeriesDetailTitle()).hasText("Avengers");
    }


    @Step("Verify Episodes section is visible")
    public void verifyEpisodesSectionVisible() {
        assertThat(getEpisodesHeading()).isVisible();
    }

    @Step("Verify episode list is visible")
    public void verifyEpisodeListVisible() {
        assertThat(getEpisodeCards()).hasCount(3);
    }

    @Step("Scroll through Series detail page")
    public void scrollSeriesDetailPage() {

        page.locator("#content").evaluate(
                "element => element.scrollTo(0, element.scrollHeight)"
        );

        page.waitForTimeout(500);
    }

    @Step("Verify Recommended Content is not displayed on Series detail page")
    public void verifyRecommendedContentNotVisible() {
        assertThat(getRecommendedContentSection()).hasCount(0);
    }

    public Locator getAdvertisingTab() {
        return page.locator("a").filter(
                new Locator.FilterOptions().setHasText("Advertising")
        );
    }

    @Step("Open Advertising tab")
    public void openAdvertisingTab() {

        assertThat(getAdvertisingTab()).isVisible();

        getAdvertisingTab().click();

        page.waitForTimeout(2000);
    }

    public void ensureAdConfigSelected(Page page) {
        // Preferred locator using accessible label text matching the UI ("Ad config")
        Locator adConfigRadio = page.getByLabel("Ad config");

        // Verify if it is currently selected
        if (!adConfigRadio.isChecked()) {
            System.out.println("'Ad config' radio button is not selected. Selecting it now...");
            adConfigRadio.check();
        } else {
            System.out.println("'Ad config' radio button is already selected.");
        }
    }

    @Step("verify And Select AdConfig")
    public void verifyAndSelectAdConfig() {
        // Example usage within a TestNG test
        ensureAdConfigSelected(page);
    }


    @Step("Ensure OTTA is selected")
    public void ensureOTTASelected(Page page) {

        // Custom dropdown button that displays the currently selected ad type
        Locator adConfigDropdown = page.locator("button.box")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("wui-icon[name='chevron-down']")))
                .first();

        adConfigDropdown.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        // Read the currently selected value from the button
        String selectedAdType = adConfigDropdown
                .locator("span.label")
                .innerText()
                .trim();

        System.out.println("Currently selected ad type: " + selectedAdType);

        // If OTTA is already selected, do nothing
        if ("OTTA".equalsIgnoreCase(selectedAdType)) {
            System.out.println("'OTTA' is already selected.");
            return;
        }

        // Otherwise open the dropdown
        adConfigDropdown.click();

        // Select OTTA from the visible dropdown
        Locator ottaOption = page.getByText("OTTA", new Page.GetByTextOptions()
                .setExact(true));

        ottaOption.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        ottaOption.click();

        // Verify the dropdown now shows OTTA
        String updatedAdType = adConfigDropdown
                .locator("span.label")
                .innerText()
                .trim();

        Assert.assertEquals(
                updatedAdType,
                "OTTA",
                "Failed to set ad config to 'OTTA'"
        );

        System.out.println("'OTTA' selected successfully.");
    }


    @Step("Ensure Client Side is selected")
    public void ensureClientSideSelected(Page page) {

        // Custom Delivery Method dropdown
        Locator deliveryMethodDropdown = page.locator("button.box")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("wui-icon[name='chevron-down']")))
                .filter(new Locator.FilterOptions()
                        .setHasText("Client side"))
                .first();

        deliveryMethodDropdown.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        // Read the currently selected value
        String selectedOption = deliveryMethodDropdown
                .locator("span.label")
                .innerText()
                .trim();

        System.out.println("Currently selected delivery method: " + selectedOption);

        // Already selected - nothing to do
        if ("Client side (CSAI)".equalsIgnoreCase(selectedOption)) {
            System.out.println("'Client side (CSAI)' is already selected.");
            return;
        }

        // Open the dropdown
        deliveryMethodDropdown.click();

        // Select Client side (CSAI)
        Locator clientSideOption = page.getByText(
                "Client side (CSAI)",
                new Page.GetByTextOptions().setExact(true)
        );

        clientSideOption.waitFor(new Locator.WaitForOptions()
                .setState(WaitForSelectorState.VISIBLE));

        clientSideOption.click();

        // Verify selected value
        String updatedOption = deliveryMethodDropdown
                .locator("span.label")
                .innerText()
                .trim();

        Assert.assertEquals(
                updatedOption,
                "Client side (CSAI)",
                "Failed: 'Client side (CSAI)' is not selected in Delivery method dropdown."
        );

        System.out.println("'Client side (CSAI)' selected successfully.");
    }


    @Step("Verify and save advertising configuration")
    public void verifyAndSaveAdvertisingConfig(Page page) {

        Locator adConfigRadio = page.getByRole(
                AriaRole.RADIO,
                new Page.GetByRoleOptions().setName("Ad config")
        );

        // Custom Ad Config dropdown
        Locator adConfigDropdown = page.locator("button.box")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("wui-icon[name='chevron-down']")))
                .first();

        // Custom Delivery Method dropdown
        // The second custom dropdown on the advertising configuration page
        Locator deliveryMethodDropdown = page.locator("button.box")
                .filter(new Locator.FilterOptions()
                        .setHas(page.locator("wui-icon[name='chevron-down']")))
                .nth(1);

        Locator saveButton = page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Save")
        );

        boolean isModified = false;

        // ---------------------------------------------------------
        // 1. Check and select "Ad config" radio button
        // ---------------------------------------------------------
        if (!adConfigRadio.isChecked()) {

            System.out.println("Selecting 'Ad config' radio button...");

            adConfigRadio.check();

            isModified = true;

        } else {

            System.out.println("'Ad config' radio button is already selected.");
        }

        // ---------------------------------------------------------
        // 2. Check and select "OTTA"
        // ---------------------------------------------------------
        String selectedAdType = adConfigDropdown
                .locator("span.label")
                .innerText()
                .trim();

        System.out.println("Currently selected ad type: " + selectedAdType);

        if (!"OTTA".equalsIgnoreCase(selectedAdType)) {

            System.out.println("Selecting 'OTTA' in Ad config dropdown...");

            adConfigDropdown.click();

            Locator ottaOption = page.getByText(
                    "OTTA",
                    new Page.GetByTextOptions().setExact(true)
            );

            ottaOption.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE));

            ottaOption.click();

            isModified = true;

            // Verify
            String updatedAdType = adConfigDropdown
                    .locator("span.label")
                    .innerText()
                    .trim();

            Assert.assertEquals(
                    updatedAdType,
                    "OTTA",
                    "Failed to select 'OTTA' in Ad config dropdown."
            );

            System.out.println("'OTTA' selected successfully.");

        } else {

            System.out.println("'OTTA' is already selected.");
        }

        // ---------------------------------------------------------
        // 3. Check and select "Client side (CSAI)"
        // ---------------------------------------------------------
        String selectedDeliveryMethod = deliveryMethodDropdown
                .locator("span.label")
                .innerText()
                .trim();

        System.out.println(
                "Currently selected delivery method: "
                        + selectedDeliveryMethod
        );

        if (!"Client side (CSAI)".equalsIgnoreCase(selectedDeliveryMethod)) {

            System.out.println(
                    "Selecting 'Client side (CSAI)' in Delivery Method dropdown..."
            );

            deliveryMethodDropdown.click();

            Locator clientSideOption = page.getByText(
                    "Client side (CSAI)",
                    new Page.GetByTextOptions().setExact(true)
            );

            clientSideOption.waitFor(new Locator.WaitForOptions()
                    .setState(WaitForSelectorState.VISIBLE));

            clientSideOption.click();

            isModified = true;

            // Verify
            String updatedDeliveryMethod = deliveryMethodDropdown
                    .locator("span.label")
                    .innerText()
                    .trim();

            Assert.assertEquals(
                    updatedDeliveryMethod,
                    "Client side (CSAI)",
                    "Failed to select 'Client side (CSAI)' in Delivery Method dropdown."
            );

            System.out.println("'Client side (CSAI)' selected successfully.");

        } else {

            System.out.println(
                    "'Client side (CSAI)' is already selected."
            );
        }

        // ---------------------------------------------------------
        // 4. Save only when something was changed
        // ---------------------------------------------------------
        if (isModified) {

            System.out.println(
                    "Settings were modified. Checking Save button..."
            );

            Assert.assertTrue(
                    saveButton.isEnabled(),
                    "Save button should be enabled after making changes."
            );

            saveButton.click();

            System.out.println("Clicked Save successfully.");

        } else {

            System.out.println(
                    "All default values ('Ad config', 'OTTA', "
                            + "'Client side') are already selected."
            );

            Assert.assertTrue(
                    saveButton.isDisabled(),
                    "Save button should remain disabled when default values are unchanged."
            );
        }
    }


    @Step("verify And Save AdvertisingConfig")
    public void testAdConfigSaveBehavior() {
        verifyAndSaveAdvertisingConfig(page);
    }

    public Locator getAdTitleHeading() {
        return page.locator(
                "//div[contains(@class, 'ad-overlay')]" +
                        "//*[normalize-space()='Advertising']"
        );
    }


    @Step("Verify CSAI advertisement is playing")
    public void verifyAdIsPlaying() {

        Locator adBadge = page.locator("span[class*='_adChromeBadge_']");

        assertThat(adBadge).isVisible(
                new LocatorAssertions.IsVisibleOptions()
                        .setTimeout(10000)
        );

        System.out.println("CSAI advertisement is currently playing.");
    }
}



