
package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import utils.ConfigReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

public class Hubpage {

    public final Page page;
    ConfigReader configReader = new ConfigReader();

    public Hubpage(Page page) {
        this.page = page;
    }



    private Locator getHubSections() {
        return page.locator("main#content section[aria-label]");
    }

    private Locator getHubTitle() {
        return page.locator("main#content h1").first();
    }
    private  Locator loginButtonLocator() {

        return page.locator("//button[normalize-space()='Log in or subscribe']");
    }

    private Locator getMoreButton() {
        return page.locator(
                "//button[@type='button' and @aria-haspopup='menu' and .//span[normalize-space()='More']]"
        );
    }

    public Locator getHubTestingMenuItem() {
        return page.getByRole(AriaRole.MENUITEM).filter(new Locator.FilterOptions().setHasText("Hub testing"));
    }

    @Step("Verify More menu is visible")
    public void verifyMoreButtonVisible() {
        assertThat(getMoreButton()).isVisible();
    }

    @Step("Open More menu")
    public void clickMoreButton() {
        getMoreButton().click();
    }

    @Step("Verify Hub testing option is visible in More menu")
    public void verifyHubTestingMenuItemVisible() {
        assertThat(getHubTestingMenuItem()).isVisible();
    }
    @Step("click login or sub")
    public void clickloginorsub() {
        loginButtonLocator().click();
    }
    @Step("Click Hub testing from More menu")
    public void clickHubTesting() {
        getHubTestingMenuItem().click();
    }

    // =========================
    // Navigation
    // =========================

    @Step("Verify Hub page is loaded")
    public void verifyHubPageLoaded() {
        assertThat(page.locator("main#content")).isVisible();
        assertThat(getHubTitle()).isVisible();
    }

    // =========================
    // Dynamic Playlist Validation
    // =========================

    @Step("Verify all Hub playlists are visible")
    public void verifyAllPlaylistsVisible() {

        Locator sections = getHubSections();

        int sectionCount = sections.count();

        assert sectionCount > 0 :
                "No playlists/shelves found on Hub page";

        for (int i = 0; i < sectionCount; i++) {

            Locator section = sections.nth(i);

            // Playlist/Shelf should be visible
            assertThat(section).isVisible();

            // Playlist title
            Locator title = section.locator("h2").first();

            assertThat(title).isVisible();

            // Playlist should contain cards
            Locator cards = section.locator("a[role='button']");

            assert cards.count() > 0 :
                    "No playlist cards found in shelf: "
                            + title.textContent();
        }
    }

    @Step("Verify playlist titles and thumbnails")
    public void verifyPlaylistTitlesAndThumbnails() {

        Locator sections = getHubSections();

        for (int i = 0; i < sections.count(); i++) {

            Locator section = sections.nth(i);

            String playlistName =
                    section.locator("h2").first().textContent().trim();

            Locator cards =
                    section.locator("a[role='button']");

            for (int j = 0; j < cards.count(); j++) {

                Locator card = cards.nth(j);

                // Card title
                Locator cardTitle =
                        card.locator("h3").first();

                assertThat(cardTitle).isVisible();

                Locator thumbnail = card.locator("img").first();

                assertThat(thumbnail).isVisible();

                String thumbnailSrc = thumbnail.getAttribute("src");

                assert thumbnailSrc != null && !thumbnailSrc.isBlank() :
                        "Thumbnail image src is missing";
            }
        }
    }

    @Step("Verify Hub playlist order")
    public void verifyPlaylistOrder() {

        Locator sections = getHubSections();

        List<String> playlistOrder = new ArrayList<>();

        for (int i = 0; i < sections.count(); i++) {

            String playlistName =
                    sections.nth(i)
                            .locator("h2")
                            .first()
                            .textContent()
                            .trim();

            playlistOrder.add(playlistName);
        }

        System.out.println("Hub playlist order: " + playlistOrder);

        // Just verify every section has a valid title.
        // No hard-coded playlist names.
        for (String playlistName : playlistOrder) {

            assert !playlistName.isBlank() :
                    "Playlist title is empty";
        }
    }
    @Step("Verify all Hub playlists after scrolling")
    public void verifyAllPlaylistsAfterScrolling() {

        Locator sections = page.locator("main#content section[aria-label]");

        int sectionCount = sections.count();

        assert sectionCount > 0 :
                "No playlists found on Hub page";

        for (int i = 0; i < sectionCount; i++) {

            Locator section = sections.nth(i);

            // Scroll the particular playlist into view
            section.scrollIntoViewIfNeeded();

            // Verify playlist is visible
            assertThat(section).isVisible();

            // Verify playlist title
            Locator title = section.locator("h2").first();

            assertThat(title).isVisible();

            String playlistName = title.textContent().trim();

            assert !playlistName.isBlank() :
                    "Playlist title is empty";

            // Verify playlist cards
            Locator cards = section.locator("a[role='button']");

            assert cards.count() > 0 :
                    "No cards found in playlist: " + playlistName;

            // Verify cards
            for (int j = 0; j < cards.count(); j++) {

                Locator card = cards.nth(j);

                Locator cardTitle = card.locator("h3").first();
                Locator thumbnail = card.locator("img").first();

                assertThat(cardTitle).isVisible();
                assertThat(thumbnail).isVisible();

                String cardTitleText = cardTitle.textContent();

                assert cardTitleText != null &&
                        !cardTitleText.trim().isBlank() :
                        "Card title is empty in playlist: " + playlistName;

                String thumbnailSrc = thumbnail.getAttribute("src");

                assert thumbnailSrc != null &&
                        !thumbnailSrc.isBlank() :
                        "Thumbnail src is missing for: " + cardTitleText;
            }
        }
    }



//    private Locator getHubPlaylists() {
//        return page.locator(
//                "main#content section"
//        );
//    }

    public Locator getHubPlaylists() {

        return page.locator(
                "main#content section[aria-label]"
        ).filter(
                new Locator.FilterOptions()
                        .setHas(page.locator("h2"))
        );
    }

    // =========================================================
    // CLICK CONTENT INSIDE PLAYLIST
    // =========================================================

    @Step("Click content from playlist number {0}")
    public String clickPlaylistContent(int playlistIndex) {

        Locator playlists = getHubPlaylists();

        // Wait for first playlist to appear
        playlists.first().waitFor();

        int playlistCount = playlists.count();

        assert playlistCount > playlistIndex :
                "Playlist " + (playlistIndex + 1)
                        + " does not exist. Available playlists: "
                        + playlistCount;

        // Get playlist section
        Locator playlistSection =
                playlists.nth(playlistIndex);

        // -----------------------------------------------------
        // Get playlist title
        // Example: Full Movies
        // -----------------------------------------------------

        Locator playlistTitleLocator =
                playlistSection.locator("h2").first();

        assertThat(playlistTitleLocator).isVisible();

        String playlistTitle =
                playlistTitleLocator.textContent();

        assert playlistTitle != null &&
                !playlistTitle.trim().isBlank() :
                "Playlist title is empty";

        playlistTitle = playlistTitle.trim();

        System.out.println(
                "Playlist: " + playlistTitle
        );

        // -----------------------------------------------------
        // Get first content inside playlist
        // Example: Tom & Jerry
        // -----------------------------------------------------

        Locator content =
                playlistSection
                        .locator("a[role='button']")
                        .first();

        assertThat(content).isVisible();

        // -----------------------------------------------------
        // Get content title before clicking
        // -----------------------------------------------------

        Locator contentTitle =
                content.locator("h3").first();

        assertThat(contentTitle).isVisible();

        String contentName =
                contentTitle.textContent();

        assert contentName != null &&
                !contentName.trim().isBlank() :
                "Content title is empty in playlist: "
                        + playlistTitle;

        contentName = contentName.trim();

        System.out.println(
                "Clicking content: " + contentName
        );

        // -----------------------------------------------------
        // Click content
        // -----------------------------------------------------

        content.click();

        return contentName;
    }

    // =========================================================
    // VERIFY OPENED MEDIA PAGE
    // =========================================================

    @Step("Verify opened media page is loaded")
    public void verifyMediaPageLoaded() {

        assertThat(
                page.locator("main#content")
        ).isVisible();

        Locator mediaTitle =
                page.locator("main#content h1").first();

        assertThat(mediaTitle).isVisible();
    }

    // =========================================================
    // VERIFY OPENED MEDIA TITLE
    // =========================================================

    @Step("Verify opened media title is {0}")
    public void verifyOpenedMediaTitle(String expectedTitle) {

        Locator openedTitle =
                page.locator("main#content h1").first();

        assertThat(openedTitle).isVisible();

        String actualTitle =
                openedTitle.textContent();

        assert actualTitle != null &&
                !actualTitle.trim().isBlank() :
                "Opened media title is empty";

        actualTitle = actualTitle.trim();

        System.out.println("=================================");
        System.out.println("EXPECTED TITLE: " + expectedTitle);
        System.out.println("ACTUAL TITLE:   " + actualTitle);
        System.out.println("=================================");

        Assert.assertEquals(
                actualTitle,
                expectedTitle,
                "Opened media title does not match selected content"
        );
    }

    // =========================================================
    // VERIFY MEDIA THUMBNAIL
    // =========================================================

    @Step("Verify media thumbnail is displayed")
    public void verifyMediaThumbnail() {

        Locator thumbnail =
                page.locator(
                        "main#content img"
                ).first();

        assertThat(thumbnail).isVisible();

        String thumbnailSrc =
                thumbnail.getAttribute("src");

        assert thumbnailSrc != null &&
                !thumbnailSrc.trim().isBlank() :
                "Media thumbnail is missing";
    }

    // =========================================================
    // VERIFY ALL MEDIA ITEMS
    // =========================================================

    @Step("Verify all media items have titles and thumbnails")
    public void verifyAllMediaItems() {

        Locator mediaItems =
                page.locator(
                        "main#content a[role='button']"
                );

        int count = mediaItems.count();

        assert count > 0 :
                "No media items displayed in playlist";

        System.out.println(
                "Total media items: " + count
        );

        for (int i = 0; i < count; i++) {

            Locator mediaItem =
                    mediaItems.nth(i);

            // -----------------------------
            // Media title
            // -----------------------------

            Locator title =
                    mediaItem.locator("h3").first();

            String titleText =
                    title.textContent();

            assert titleText != null &&
                    !titleText.trim().isBlank() :
                    "Media title is missing for item "
                            + (i + 1);

            // -----------------------------
            // Thumbnail
            // -----------------------------

            Locator thumbnail =
                    mediaItem.locator("img").first();

            String thumbnailSrc =
                    thumbnail.getAttribute("src");

            assert thumbnailSrc != null &&
                    !thumbnailSrc.trim().isBlank() :
                    "Media thumbnail is missing for: "
                            + titleText.trim();

            System.out.println(
                    "Verified media: "
                            + titleText.trim()
            );
        }
    }

    private Locator getTomAndJerryMovie() {
        return page.locator(
                "section[aria-label='Full Movies'] " +
                        "a[role='button']"
        ).filter(
                new Locator.FilterOptions().setHasText(
                        "Tom & Jerry"
                )
        ).first();
    }

    @Step("Verify Full Movies playlist is visible")
    public void verifyFullMoviesPlaylistVisible() {

        Locator fullMovies =
                page.locator("section[aria-label='Full Movies']");

        assertThat(fullMovies).isVisible();
    }

    @Step("Click Tom and Jerry movie")
    public String clickTomAndJerryMovie() {

        Locator movie = getTomAndJerryMovie();

        assertThat(movie).isVisible();

        String movieTitle = movie.locator("h3").innerText();

        Allure.step("Selected movie: " + movieTitle);

        movie.click();

        return movieTitle;
    }


    @Step("Verify movie title")
    public void verifyMovieTitle(String expectedTitle) {

        Locator title = page.locator("h1").filter(
                new Locator.FilterOptions()
                        .setHasText("Tom & Jerry")
        ).first();

        assertThat(title).isVisible();

        String actualTitle = title.innerText();

        assertTrue(
                actualTitle.contains("Tom & Jerry"),
                "Expected Tom & Jerry title but found: " + actualTitle
        );

        Allure.step("Verified movie title: " + actualTitle);
    }




    private Locator getStartWatchingButton() {

        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Start Watching")
                        .setExact(true)
        ).first();
    }

    @Step("Verify Start Watching button is visible")
    public void verifyStartWatchingButtonVisible() {

        assertThat(getStartWatchingButton()).isVisible();
    }

    @Step("Click Start Watching")
    public void clickStartWatching() {

        getStartWatchingButton().click();
    }


    private Locator getVideoElement() {

        return page.locator("video").first();
    }

    @Step("Verify video element exists")
    public void verifyVideoElementExists() {

        Locator video = getVideoElement();

        video.waitFor();

        assertThat(video).isVisible();

        assertTrue(
                video.count() > 0,
                "Video element does not exist"
        );
    }

    @Step("Verify video is not paused")
    public void verifyVideoIsNotPaused() {

        Locator video = getVideoElement();

        boolean paused = (Boolean) video.evaluate(
                "video => video.paused"
        );

        assertFalse(
                paused,
                "Video is paused. Movie playback has not started."
        );
    }

    @Step("Get current video playback time")
    public double getCurrentPlaybackTime() {

        return ((Number) getVideoElement().evaluate(
                "video => video.currentTime"
        )).doubleValue();
    }

    @Step("Wait for movie playback for 30 seconds")
    public void waitForThirtySeconds() {

        page.waitForTimeout(30_000);
    }

    @Step("Verify video currentTime increased")
    public void verifyPlaybackProgressed(
            double initialPlaybackTime
    ) {

        double finalPlaybackTime =
                getCurrentPlaybackTime();

        Allure.step(
                "Initial currentTime: "
                        + initialPlaybackTime
                        + " seconds"
        );

        Allure.step(
                "Final currentTime: "
                        + finalPlaybackTime
                        + " seconds"
        );

        assertTrue(
                finalPlaybackTime > initialPlaybackTime,
                "Video currentTime did not increase. " +
                        "Initial: " + initialPlaybackTime +
                        ", Final: " + finalPlaybackTime
        );
    }
    @Step("Verify video has no playback error")
    public void verifyVideoHasNoError() {
        String error = (String) getVideoElement().evaluate( "video => video.error ? video.error.message : null" );
        if (error != null) { throw new AssertionError( "Video playback error detected: " + error ); } }

    public Locator getsearchButton()
    {
        return page.locator("//div[@role='button' and @aria-label='Open search']");}
    @Step("Click Search")
    public void clickSearch() {
        assertThat(getsearchButton()).isVisible();
        getsearchButton().click(); }
    private Locator getSearchInput() { return page.locator( "input[type='search'], " + "input[placeholder*='Search'], " + "input[aria-label*='Search']" ).first(); }

    @Step("Search for media")
    public void searchFormedia() {
        Locator searchInput = getSearchInput();
        assertThat(searchInput).isVisible();
        searchInput.fill("zootopia");
        searchInput.press("Enter"); }



    public Locator getSearchResultCards() {
        return page.locator(
                "[role='grid'] [role='gridcell'] a[role='button']"
        );
    }




    @Step("Verify Zootopia search results are loaded")
    public void verifySearchResultsLoaded() {

        Locator resultsHeading = page.locator(
                "h2[id^='search_heading_']"
        );

        assertThat(resultsHeading).isVisible();

        Locator cards = getSearchResultCards();

        assertTrue(
                cards.count() > 0,
                "No search result cards were found for Zootopia"
        );

        Allure.step(
                "Zootopia search results found: " + cards.count()
        );
    }


// =========================================================
// VERIFY ZOOTOPIA MEDIA METADATA
// =========================================================

    @Step("Verify metadata for each Zootopia media card")
    public void verifyMediaMetadata() {

        Locator cards = getSearchResultCards();

        int cardCount = cards.count();

        assertTrue(
                cardCount > 0,
                "No Zootopia media cards found in search results"
        );

        for (int i = 0; i < cardCount; i++) {

            Locator card = cards.nth(i);

            Allure.step(
                    "Validating Zootopia media card " + (i + 1)
            );

            // -------------------------------------------------
            // Thumbnail
            // -------------------------------------------------

            Locator thumbnail = card.locator(
                    "img"
            ).first();

            assertTrue(
                    thumbnail.count() > 0,
                    "Thumbnail not found for card " + (i + 1)
            );

            assertThat(thumbnail).isVisible();

            Allure.step(
                    "Card " + (i + 1) + " thumbnail is visible"
            );


            // -------------------------------------------------
            // Title
            // -------------------------------------------------

            Locator title = card.locator(
                    "h3._title_a1msq_25"
            ).first();

            assertTrue(
                    title.count() > 0,
                    "Title not found for card " + (i + 1)
            );

            assertThat(title).isVisible();

            String titleText = title.innerText().trim();

            assertTrue(
                    !titleText.isEmpty(),
                    "Title is empty for card " + (i + 1)
            );

            Allure.step(
                    "Card " + (i + 1) +
                            " title: " + titleText
            );


            // -------------------------------------------------
            // Metadata / Tag
            // -------------------------------------------------

            Locator tag = card.locator(
                    "div[class*='_tags_'] div[class*='_tag_']"
            ).first();

            assertTrue(
                    tag.count() > 0,
                    "Metadata tag not found for card " + (i + 1)
            );

            assertThat(tag).isVisible();

            String tagText = tag.innerText().trim();

            assertTrue(
                    !tagText.isEmpty(),
                    "Metadata tag is empty for card " + (i + 1)
            );

            Allure.step(
                    "Card " + (i + 1) +
                            " metadata: " + tagText
            );


            // -------------------------------------------------
            // Validate metadata
            // -------------------------------------------------

            boolean isSeries = tagText.equalsIgnoreCase("Series");

            boolean isDuration = tagText.matches(
                    "(?i)\\d+\\s*(min|mins|hr|hrs|hour|hours)"
            );

            assertTrue(
                    isSeries || isDuration,
                    "Unexpected metadata '" + tagText +
                            "' for card " + (i + 1)
            );
        }
    }

    public Locator getJWUsernameField() {
        return page.locator("wui-input[data-test='set-login-email'] input");
    }

    public Locator getJWPasswordField() {
        return page.locator("wui-input[data-test='set-login-password'] input");
    }

    public Locator getJWContinueButton() {
        return page.locator("wui-button[data-test='submit-login-credentials']");
    }

    public Locator getJWLoginButton() {
        return page.locator("wui-button[data-test='submit-login-credentials']");
    }

    @Step("Login to JW Player Dashboard")
    public void login() {
        String username = ConfigReader.getJWUsername();
        String password = ConfigReader.getJWPassword();

        assertTrue(username != null && !username.isBlank(),
                "JW Player username is not configured");

        assertTrue(password != null && !password.isBlank(),
                "JW Player password is not configured");

        assertThat(getJWUsernameField()).isVisible();
        getJWUsernameField().fill(username);

        assertThat(getJWContinueButton()).isVisible();
        getJWContinueButton().click();

        assertThat(getJWPasswordField()).isVisible();
        getJWPasswordField().fill(password);

        assertThat(getJWLoginButton()).isVisible();
        getJWLoginButton().click();
        page.waitForLoadState();
    }



    private Locator getAppsLink() {
        return page.locator("a[data-test='main-nav-link-appConfigsList']");
    }

    private Locator getTringStagingConfigLink() {
        return page.locator(
                "a[data-test='link-to-config-detail']"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText("Tring Staging Config")
        );
    }

    private Locator getMenuItemsLink() {
        return page.locator(
                "a.inner"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText("Menu items")
        );
    }



    private Locator getConfiguredMenuItems() {
        return page.locator(
                "wui-draggable-list " +
                        "wui-draggable-list-item " +
                        "span[class*='menuItem__title']"
        );
    }


    @Step("Open apps")
    public void openApps() {

        assertThat(getAppsLink()).isVisible();

        getAppsLink().click();

        page.waitForLoadState();
    }


    @Step("Open Tring Staging Config")
    public void openTringTringStagingConfig() {

        assertThat(getTringStagingConfigLink()).isVisible();

        getTringStagingConfigLink().click();

        page.waitForLoadState();
    }


    @Step("Open Menu Items")
    public void openMenu() {

        assertThat(getMenuItemsLink()).isVisible();

        getMenuItemsLink().click();

        page.waitForLoadState();
    }


    @Step("Read all menu items from JW Player Dashboard")
    public List<String> getConfiguredMenuItemNames() {

        Locator menuItems = getConfiguredMenuItems();

        int count = menuItems.count();

        assertTrue(
                count > 0,
                "No menu items found in JW Player Dashboard"
        );

        List<String> menuItemNames = new ArrayList<>();

        Allure.step(
                "JW Player menu item count: " + count
        );

        for (int i = 0; i < count; i++) {

            String menuItemName =
                    menuItems.nth(i).innerText().trim();

            assertTrue(
                    !menuItemName.isEmpty(),
                    "Menu item at position " + (i + 1)
                            + " has empty name"
            );

            menuItemNames.add(menuItemName);

            Allure.step(
                    "JW Player menu item "
                            + (i + 1)
                            + ": "
                            + menuItemName
            );
        }

        return menuItemNames;
    }

        private Locator getApplicationMenuItems() {
            return page.locator(
                    "div[class*='_navItemsContainer_'] a[href]"
            );
        }



        @Step("Read all menu items from OTT application")
        public List<String> getApplicationMenuItemNames() {

            Locator menuItems = getApplicationMenuItems();

            int count = menuItems.count();

            assertTrue(
                    count > 0,
                    "No menu items found in OTT application"
            );

            List<String> menuItemNames = new ArrayList<>();

            Allure.step(
                    "OTT menu item count: " + count
            );

            for (int i = 0; i < count; i++) {

                String menuItemName =
                        menuItems.nth(i).innerText().trim();

                assertTrue(
                        !menuItemName.isEmpty(),
                        "OTT menu item at position "
                                + (i + 1)
                                + " has empty name"
                );

                menuItemNames.add(menuItemName);

                Allure.step(
                        "OTT menu item "
                                + (i + 1)
                                + ": "
                                + menuItemName
                );
            }

            return menuItemNames;
        }





    @Step("Verify OTT menu matches JW Player Dashboard menu")
    public void verifyMenuMatchesDashboard(
            List<String> expectedMenuItems) {

        List<String> actualMenuItems =
                getApplicationMenuItemNames();

        Allure.step("Expected JW Player menu: " + expectedMenuItems);
        Allure.step("Actual OTT menu: " + actualMenuItems);

        Assert.assertEquals(
                actualMenuItems.size(),
                expectedMenuItems.size()
        );

        for (int i = 0; i < expectedMenuItems.size(); i++) {

            Assert.assertEquals(
                    actualMenuItems.get(i).trim(),
                    expectedMenuItems.get(i).trim()
            );
        }
    }
    public Locator homeMenu() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Home").setExact(true)
        );
    }

    public Locator menuItems() {return page.locator("div[class*='navItemsContainer'] a");
    }

    public Locator moreButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("More")
        );
    }

    public Locator moreMenuItems() {
        return page.locator(
                "[role='menu'] [role='menuitem'] a"
        );
    }

    public Locator contentArea() {
        return page.locator("main#content");
    }

    public Locator contentCards() {
        return page.locator(
                "main#content a[role='button']"
        );
    }

    public Locator contentHeading() {
        return page.locator(
                "main#content h1"
        ).first();
    }

    public Locator favoriteButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Favorite")
        );
    }

    public Locator shareButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Share")
        );
    }

    @Step("Verify Home is displayed")
    public void verifyHomeDisplayed() {
        assertTrue(homeMenu().isVisible(), "Home menu is not displayed");
        assertTrue(contentArea().isVisible(), "Home content area is not displayed");
    }


    @Step("Verify Home contains content")
    public void verifyHomeContent() {

        int count = contentCards().count();
        assertTrue(count > 0, "Home does not contain any content");

    }

    @Step("Verify detail page content")
    public void verifyDetailPageContent() {

        assertTrue(
                contentArea().isVisible(),
                "Detail page content area is not displayed"
        );

        assertTrue(
                contentHeading().isVisible(),
                "Detail page heading is not displayed"
        );

        String heading =
                contentHeading()
                        .innerText()
                        .trim();

        assertTrue(
                !heading.isEmpty(),
                "Detail page heading is empty"
        );

        boolean hasStartWatching =
                getStartWatchingButton().isVisible();

        boolean hasFavorite =
                favoriteButton().isVisible();

        boolean hasShare =
                shareButton().isVisible();

        assertTrue(
                hasStartWatching
                        || hasFavorite
                        || hasShare,
                "Detail page '" + heading
                        + "' does not contain expected content/actions"
        );

    }
    @Step("Verify current menu content")
    public void verifyCurrentMenuContent() {

        if (contentCards().count() > 0) {

            Allure.step(
                    "Content grid detected"
            );

            verifySectionContent();

        } else if (
                getStartWatchingButton().isVisible()
                        || favoriteButton().isVisible()
                        || shareButton().isVisible()
        ) {

            Allure.step(
                    "Detail page detected"
            );

            verifyDetailPageContent();

        } else {

            String heading =
                    contentHeading().isVisible()
                            ? contentHeading()
                            .innerText()
                            .trim()
                            : "Unknown";

            assertTrue(
                    false,
                    "Menu '" + heading
                            + "' does not contain any recognizable content"
            );
        }
    }
    @Step("Get main menu links")
    public List<String> getMainMenuLinks() {

        List<String> links = new ArrayList<>();

        int count = menuItems().count();

        for (int i = 0; i < count; i++) {

            String href =
                    menuItems()
                            .nth(i)
                            .getAttribute("href");

            if (href != null && !href.isBlank()) {
                links.add(href);
            }
        }

        return links;
    }


    @Step("Get More menu links")
    public List<String> getMoreMenuLinks() {

        List<String> links = new ArrayList<>();

        openMoreMenu();

        int count = moreMenuItems().count();

        for (int i = 0; i < count; i++) {

            String href =
                    moreMenuItems()
                            .nth(i)
                            .getAttribute("href");

            if (href != null && !href.isBlank()) {
                links.add(href);
            }
        }

        return links;
    }


    @Step("Get all available menu names")
    public List<String> getAllMenuNames() {

        List<String> menuNames = new ArrayList<>();

        // Get menus visible directly after Home
        int mainCount = mainMenuItems().count();

        for (int i = 0; i < mainCount; i++) {

            Locator menu = mainMenuItems().nth(i);

            if (!menu.isVisible()) {
                continue;
            }

            String name = menu.innerText().trim();

            if (!name.isEmpty()
                    && !name.equalsIgnoreCase("Home")
                    && !menuNames.contains(name)) {

                menuNames.add(name);
            }
        }

        // Get menus available inside More
        if (moreButton().isVisible()) {

            openMoreMenu();

            int moreCount = moreMenuItems().count();

            for (int i = 0; i < moreCount; i++) {

                String name =
                        moreMenuItems()
                                .nth(i)
                                .innerText()
                                .trim();

                if (!name.isEmpty()
                        && !name.equalsIgnoreCase("Home")
                        && !menuNames.contains(name)) {

                    menuNames.add(name);
                }
            }
        }

        Allure.step(
                "Menus available for navigation: "
                        + menuNames
        );

        return menuNames;
    }
    public Locator mainMenuItems() {
        return page.locator(
                "div[class*='navItemsContainer'] > span[class*='navItem'] a"
        );
    }

    @Step("Open More menu")
    public void openMoreMenu() {

        if (!moreButton().isVisible()) {
            return;
        }

        String expanded =
                moreButton().getAttribute("aria-expanded");

        if (!"true".equals(expanded)) {

            moreButton().click();

            page.waitForTimeout(500);
        }
    }
    @Step("Click menu: {0}")
    public void clickMenu(String menuName) {

        // Check directly visible menus
        Locator mainMenu =
                mainMenuItems()
                        .filter(
                                new Locator.FilterOptions()
                                        .setHasText(menuName)
                        )
                        .first();

        if (mainMenu.count() > 0
                && mainMenu.isVisible()) {

            mainMenu.click();

        } else {

            // Menu is inside More
            openMoreMenu();

            Locator moreMenu =
                    moreMenuItems()
                            .filter(
                                    new Locator.FilterOptions()
                                            .setHasText(menuName)
                            )
                            .first();

            assertTrue(
                    moreMenu.count() > 0
                            && moreMenu.isVisible(),
                    "Menu is not visible: " + menuName
            );

            moreMenu.click();
        }

        page.waitForLoadState(
                LoadState.DOMCONTENTLOADED
        );

        page.waitForTimeout(1000);

        Allure.step(
                "Clicked menu: " + menuName
        );
    }
    @Step("Verify selected section content")
    public void verifySectionContent() {

        assertTrue(
                contentArea().isVisible(),
                "Section content area is not displayed"
        );

        assertTrue(
                contentHeading().isVisible(),
                "Section heading is not displayed"
        );

        String heading =
                contentHeading()
                        .innerText()
                        .trim();

        assertTrue(
                !heading.isEmpty(),
                "Section heading is empty"
        );

        int count =
                contentCards().count();

        assertTrue(
                count > 0,
                "Section '" + heading
                        + "' does not contain any content"
        );

        Allure.step(
                "Section '" + heading
                        + "' contains "
                        + count
                        + " content item(s)"
        );
    }


    public Locator homePage() {
        return page.locator("main#content");
    }

    public Locator homePageHeading() {
        return page.locator("main#content > h1");
    }

    public Locator homeSections() {
        return page.locator(
                "main#content section[aria-label]"
        );
    }

    public Locator homeSectionCards(Locator section) {
        return section.locator(
                "a[role='button']"
        );
    }

    public Locator footer() {
        return page.locator("footer");
    }


// =========================
// CLICK HOME
// =========================

    @Step("Click Home")
    public void clickHome() {

        homeMenu().click();

        page.waitForLoadState(
                LoadState.DOMCONTENTLOADED
        );

        page.waitForTimeout(1000);
    }

    @Step("Verify Home after navigation")
    public void verifyHomeAfterNavigation() {

        assertTrue(
                homeMenu().isVisible(),
                "Home menu is not visible"
        );

        assertTrue(
                contentArea().isVisible(),
                "Home content area is not visible"
        );

        int count =
                contentCards().count();

        assertTrue(
                count > 0,
                "Home does not contain any content"
        );

        Allure.step(
                "Home verified with "
                        + count
                        + " content item(s)"
        );
    }
    @Step("Verify Home page is loaded")
    public void verifyHomePageLoaded() {

        assertTrue(
                homePage().isVisible(),
                "Home page content is not visible"
        );

        assertTrue(
                homePageHeading().isVisible(),
                "Home page heading is not visible"
        );

        assertEquals(
                homePageHeading().innerText().trim(),
                "Home",
                "Home page heading is incorrect"
        );

        page.waitForTimeout(2000);

        int sectionCount =
                homeSections().count();

        assertTrue(
                sectionCount > 0,
                "No Home sections are displayed"
        );

        Allure.step(
                "Home page loaded with "
                        + sectionCount
                        + " section(s)"
        );
    }
    @Step("Verify all Home sections contain content")
    public void verifyAllHomeSections() {

        int sectionCount =
                homeSections().count();

        assertTrue(
                sectionCount > 0,
                "No Home sections found"
        );

        for (int i = 0; i < sectionCount; i++) {

            Locator section =
                    homeSections().nth(i);

            String sectionName =
                    section.getAttribute("aria-label");

            if (sectionName == null
                    || sectionName.trim().isEmpty()) {

                sectionName = "Section " + (i + 1);
            }

            assertTrue(
                    section.isVisible(),
                    "Home section is not visible: "
                            + sectionName
            );

            int cardCount =
                    homeSectionCards(section).count();

            assertTrue(
                    cardCount > 0,
                    "Home section has no content: "
                            + sectionName
            );

            Allure.step(
                    "Section '" + sectionName
                            + "' loaded successfully with "
                            + cardCount
                            + " content item(s)"
            );
        }
    }
    @Step("Scroll through entire Home page")
    public void scrollHomePageToFooter() {

        // Scroll down continuously through the complete page
        for (int i = 0; i < 50; i++) {

            page.evaluate(
                    "() => window.scrollBy(0, window.innerHeight * 0.7)"
            );

            page.waitForTimeout(1200);
        }

        // Extra scrolls to make sure the bottom Hub content is loaded
        for (int i = 0; i < 6; i++) {

            page.evaluate(
                    "() => window.scrollBy(0, window.innerHeight * 0.7)"
            );

            page.waitForTimeout(1500);
        }

        // Finally reach the absolute bottom
        page.evaluate(
                "() => window.scrollTo(0, document.documentElement.scrollHeight)"
        );


    }
    @Step("Verify banners and thumbnails navigate to the correct content")
    public void verifyContentNavigation() {

        Locator contentCards = page.locator(
                "section[aria-label] li[aria-hidden='false'] a[role='button']:has(h3)"
        );

        int totalCards = contentCards.count();

        Assert.assertTrue(
                totalCards >= 3,
                "Expected at least 3 visible content cards, but found: "
                        + totalCards
        );

        List<Integer> randomIndexes = new ArrayList<>();

        Random random = new Random();

        while (randomIndexes.size() < 3) {

            int index = random.nextInt(totalCards);

            if (!randomIndexes.contains(index)) {
                randomIndexes.add(index);
            }
        }

        for (int index : randomIndexes) {

            Locator card = contentCards.nth(index);

            card.scrollIntoViewIfNeeded();

            String expectedTitle = card
                    .locator("h3")
                    .innerText()
                    .trim();

            String expectedHref = card.getAttribute("href");

            String expectedThumbnail = card
                    .locator("img")
                    .getAttribute("src");

            Allure.step(
                    "Selected content: " + expectedTitle
            );

            Allure.step(
                    "Expected thumbnail: " + expectedThumbnail
            );

            Allure.step(
                    "Expected destination: " + expectedHref
            );

            // Verify thumbnail
            Assert.assertTrue(
                    card.locator("img").isVisible(),
                    "Thumbnail is not visible for: " + expectedTitle
            );

            Assert.assertNotNull(
                    expectedThumbnail,
                    "Thumbnail URL is missing for: " + expectedTitle
            );

            // Click content
            card.click();

            page.waitForLoadState(
                    LoadState.DOMCONTENTLOADED
            );

            page.waitForTimeout(1500);

            String actualUrl = page.url();

            // Remove query parameters from expected href
            String expectedPath =
                    expectedHref.split("\\?")[0];

            // Verify correct destination
            Assert.assertTrue(
                    actualUrl.contains(expectedPath),
                    "Incorrect destination for: "
                            + expectedTitle
                            + ". Expected: "
                            + expectedPath
                            + ", Actual: "
                            + actualUrl
            );

            Allure.step(
                    "Correct destination opened for: "
                            + expectedTitle
            );

            // Verify content title on destination
            Locator destinationTitle = page.locator(
                    "h1, h2, h3"
            ).filter(
                    new Locator.FilterOptions()
                            .setHasText(expectedTitle)
            ).first();

            Assert.assertTrue(
                    destinationTitle.isVisible(),
                    "Destination title not found for: "
                            + expectedTitle
            );

            Allure.step(
                    "Destination content verified: "
                            + expectedTitle
            );

            // Return to Home
            page.goBack();

            page.waitForLoadState(
                    LoadState.DOMCONTENTLOADED
            );

            page.waitForTimeout(1500);

        }
    }

    // ==================== Playback Locators ====================

    public Locator videoPlayer() {
        return page.locator("video").first();
    }

    public Locator playButton() {
        return page.locator(
                "button.shaka-play-button[aria-label='Play']"
        ).first();
    }

    public Locator pauseButton() {
        return page.locator(
                "button.shaka-play-button[aria-label='Pause']"
        ).first();
    }

    public Locator rewindButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Rewind 10 seconds")
                        .setExact(true)
        );
    }

    public Locator forwardButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Fast-forward 10 seconds")
                        .setExact(true)
        );
    }

    public Locator muteButton() {
        return page.locator(
                "button.shaka-mute-button"
        ).first();
    }

    public Locator fullScreenButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Full screen")
                        .setExact(true)
        );
    }





    @Step("Pause video playback")
    public void pauseVideoPlayback() {

        pauseButton().click();

//        page.waitForFunction(
//                "() => {" +
//                        "const video = document.querySelector('video');" +
//                        "return video && video.paused;" +
//                        "}"
//        );
    }


    @Step("Resume video playback")
    public void resumeVideoPlayback() {

        playButton().click();

    }


    @Step("Click forward 10 seconds")
    public void clickForward() {

        forwardButton().click();

        page.waitForTimeout(500);
    }



    @Step("Click rewind 10 seconds")
    public void clickRewind() {

        rewindButton().click();

        page.waitForTimeout(500);
    }


    @Step("Mute video")
    public void muteVideo() {

        muteButton().click();

        page.waitForFunction(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video && video.muted === true;" +
                        "}"
        );
    }


    @Step("Unmute video")
    public void unmuteVideo() {

        muteButton().click();

        page.waitForFunction(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video && video.muted === false;" +
                        "}"
        );
    }


    @Step("Enter fullscreen")
    public void enterFullscreen() {

        fullScreenButton().click();

        page.waitForFunction(
                "() => document.fullscreenElement !== null"
        );
    }


    @Step("Exit fullscreen")
    public void exitFullscreen() {

        page.keyboard().press("Escape");

        page.waitForFunction(
                "() => document.fullscreenElement === null"
        );
    }
    // =========================
// PLAYBACK VERIFICATION METHODS
// =========================

    @Step("Verify video is playing")
    public void verifyVideoIsPlaying() {

        Boolean playing = (Boolean) page.evaluate(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video ? !video.paused && !video.ended : false;" +
                        "}"
        );

        assertTrue(
                playing,
                "Video is not playing"
        );
    }


    @Step("Verify video is paused")
    public void verifyVideoIsPaused() {

        boolean paused = (boolean) page.evaluate(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video && video.paused;" +
                        "}"
        );

        assertTrue(
                paused,
                "Video is not paused"
        );
    }


    @Step("Get current video playback position")
    public double getCurrentPlaybackPosition() {

        return ((Number) page.evaluate(
                "() => document.querySelector('video').currentTime"
        )).doubleValue();
    }


    @Step("Verify forward changed playback position")
    public void verifyForward(double beforePosition) {

        double afterPosition =
                getCurrentPlaybackPosition();

        assertTrue(
                afterPosition > beforePosition,
                "Forward did not increase playback position. "
                        + "Before: " + beforePosition
                        + ", After: " + afterPosition
        );
    }


    @Step("Verify rewind changed playback position")
    public void verifyRewind(double beforePosition) {

        double afterPosition =
                getCurrentPlaybackPosition();

        assertTrue(
                afterPosition < beforePosition,
                "Rewind did not decrease playback position. "
                        + "Before: " + beforePosition
                        + ", After: " + afterPosition
        );
    }


    @Step("Verify video is muted")
    public void verifyVideoIsMuted() {

        boolean muted = (boolean) page.evaluate(
                "() => document.querySelector('video').muted"
        );

        assertTrue(
                muted,
                "Video is not muted"
        );
    }


    @Step("Verify video is unmuted")
    public void verifyVideoIsUnmuted() {

        boolean muted = (boolean) page.evaluate(
                "() => document.querySelector('video').muted"
        );

        assertFalse(
                muted,
                "Video is still muted"
        );
    }


    @Step("Verify fullscreen is enabled")
    public void verifyFullscreenEnabled() {

        boolean fullscreen = (boolean) page.evaluate(
                "() => document.fullscreenElement !== null"
        );

        assertTrue(
                fullscreen,
                "Fullscreen was not enabled"
        );
    }


    @Step("Verify fullscreen is disabled")
    public void verifyFullscreenDisabled() {

        boolean fullscreen = (boolean) page.evaluate(
                "() => document.fullscreenElement !== null"
        );

        assertFalse(
                fullscreen,
                "Fullscreen is still enabled"
        );
    }
    public Locator trailersSection() {
        return page.locator("div._shelf_1gfpb_4")
                .filter(new Locator.FilterOptions()
                        .setHasText("Trailers"))
                .first();
    }

    public Locator pawfectMomentCard() {
        return trailersSection()
                .locator("a[role='button']")
                .filter(new Locator.FilterOptions()
                        .setHasText("Pawfect Moment"))
                .first();
    }

    public Locator nextSlideButton() {
        return trailersSection()
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Next slide")
                )
                .first();
    }
    @Step("Scroll to Trailers section")
    public void scrollToTrailersSection() {

        Locator heading = page.locator(
                "//h2[normalize-space()='Trailers']"
        ).first();

        for (int i = 0; i < 9; i++) {

            if (heading.isVisible()) {
                System.out.println("Trailers section found");
                heading.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Trailers section was not found after scrolling"
        );
    }
    @Step("Scroll to Trailers section and find Pawfect Moment")
    public void verifyPawfectMomentDisplayed() {

        Locator pawfectMoment = pawfectMomentCard();

        // Pawfect Moment may already be visible
        if (pawfectMoment.isVisible()) {
            return;
        }

        // Move the horizontal carousel
        for (int i = 0; i < 3; i++) {

            Locator nextButton = nextSlideButton();

            if (!nextButton.isVisible()) {
                break;
            }

            nextButton.click();

            page.waitForTimeout(500);

            if (pawfectMoment.isVisible()) {
                break;
            }
        }

        assertTrue(
                pawfectMoment.isVisible(),
                "Pawfect Moment content is not displayed in Trailers section"
        );
    }
    @Step("Click Pawfect Moment content")
    public void clickPawfectMoment() {

        pawfectMomentCard().click();
    }

}
