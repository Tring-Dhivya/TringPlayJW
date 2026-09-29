package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Response;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitForSelectorState;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;
import utils.ConfigReader;
import com.microsoft.playwright.Locator;

import java.util.Random;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.*;
import static org.testng.AssertJUnit.assertNotNull;


public class Loginpage {

    private final Page page;
    private final String baseUrl;
    private final String jwUrl;
    ConfigReader configReader = new ConfigReader();



    public Loginpage(Page page) {
        this.page = page;
        this.baseUrl = ConfigReader.getUrl();
        this.jwUrl = ConfigReader.getJwUrl();
    }
    @Step("Launch application")
    public void launchApplication() {
        page.navigate(ConfigReader.getUrl());
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForTimeout(10_000);
    }
    @Step("Launch JW Player Dashboard")
    public void launchJWApplication() {
        page.navigate(ConfigReader.getJwUrl());
        page.waitForLoadState( LoadState.DOMCONTENTLOADED );
    }
    public void verifyWatchTrailerButtonVisible() {
        assertThat(getWatchTrailerButton()).isVisible();
    }

    @Step("Verify Guest Home page is visible")
    public void verifyGuestHomeVisible() {assertThat(getGuestHome()).isVisible();}
    @Step("click Home")
    public void clickhome() {getGuestHome().click();}

    @Step("Check login error message")
    public void checkLoginError() {
        if (getLoginErrorMessage().isVisible()) {

            System.out.println("===== LOGIN ERROR =====");
            System.out.println(
                    getLoginErrorMessage().innerText()
            );
            System.out.println("=======================");
        }
    }
    @Step("click SignIn")
    public void clickSignIn() {getSignInButton().click();}
    @Step("Verify Sign Up popup is visible")
    public void verifySignUpPopupVisible() {assertThat(getSignUpPopup()).isVisible();}
    @Step("Verify Sign Up popup is hidden")
    public void verifySignUpPopupHidden() {assertThat(getSignUpPopup()).isHidden();}
    @Step("Close SignIn Popup")
    public void closeSignInPopup() {getSignInPopupCloseButton().click();}
    @Step("click SignUp")
    public void clickSignUp() {getSignUpButton().click();}
    @Step("Verify Forgot Password link is visible")
    public void verifyForgotPasswordLinkVisible() {assertThat(getForgotPasswordLink()).isVisible();}
    @Step("Verify Password Reset form is visible")
    public void verifyPasswordResetFormVisible() {assertThat(getPasswordResetForm()).isVisible();}
    @Step("Close Sign Up popup")
    public void closeSignUpPopup() {getSignUpPopupCloseButton().click();}
    @Step("Verify Sign Up button is visible")
    public void verifySignUpButtonVisible() {assertThat(getSignUpButton()).isVisible();}
    @Step("Verify Sign in button is visible")
    public void verifySignInButtonVisible() {assertThat(getSignInButton()).isVisible();}
    @Step("Verify Sign In popup is visible")
    public void verifySignInPopupVisible() {assertThat(getSignInPopup()).isVisible();}
    @Step("Verify Home menu is visible")
    public void verifyHomeMenuVisible() {assertThat(getHomeMenu()).isVisible();}
    @Step("Verify DRM menu is visible")
    public void verifyDrmMenuVisible() {assertThat(getDrmMenu()).isVisible();}
    @Step("Verify Vintage Playlist menu is visible")
    public void verifyVintagePlaylistMenuVisible() {assertThat(getVintagePlaylistMenu()).isVisible();}
    @Step("Verify Dynamic Playlist menu is visible")
    public void verifyDynamicPlaylistMenuVisible() {assertThat(getDynamicPlaylistMenu()).isVisible();}
    @Step("Verify Start Watching button is visible")
    public void verifyStartWatchingButtonVisible() {assertThat(startWatchingButton()).isVisible();}
    @Step("Verify video player is visible")
    public void verifyVideoPlayerVisible() {assertThat(getVideoPlayer()).isVisible();}
    @Step("Click Video Player")
    public void clickVideoPlayer() {getVideoPlayer().click();}

    @Step("Verify video player back button is visible")
    public void verifyVideoPlayerBackButtonVisible() {assertThat(getVideoPlayerBackButton()).isVisible();}
    @Step("Verify Sign Up button is enabled")
    public void verifySignUpButtonEnabled() {assertThat(getSignUpButton()).isEnabled();}
    @Step("Verify content screen is visible")
    public void verifySomeScreenVisible() {assertThat(getSomeScreen()).isVisible();}
    @Step("Click content screen")
    public void clickSomeScreen() {getSomeScreen().click();}
    @Step("Verify and click Zootopia 2")
    public void verifyAndClickZootopia2() {
        Locator zootopia2 = getZootopia2Card();
        assertTrue(zootopia2.isVisible(), "Zootopia 2 is not visible in New Playlist");
        assertEquals(zootopia2.locator("h3").innerText(), "Zootopia 2");
        zootopia2.click();
    }
    @Step("Click content screen")
    public void clickzooScreen() {getzooScreen().click();}
    @Step("Verify Guest Home page is hidden")
    public void verifyGuestHomeHidden() {assertThat(getGuestHome()).isHidden();}
    @Step("click Start Watching")
    public void clickStartWatching() {startWatchingButton().click();}
    @Step("Verify Sign Up button is hidden")
    public void clickContinuewatching() {getContinuewatching().click();}
    @Step("Verify Sign Up button is hidden")
    public void verifySignUpButtonHidden() {assertThat(getSignUpButton()).isHidden();}
    @Step("Verify Sign In button is enabled")
    public void verifySignInButtonEnabled() {assertThat(getSignInButton()).isEnabled();}
    @Step("Verify Sign In popup is hidden")
    public void verifySignInPopupHidden() {assertThat(getSignInPopup()).isHidden();}
    @Step("Click video player back button")
    public void clickVideoPlayerBackButton() {
        getVideoPlayerBackButton().click();
    }
    @Step("Enter username/email")
    public void enterUsername(String username) {getUsernameField().fill(username);}
    @Step("Enter username/email")
    public void enterUsername1(String username) {getUsernameField1().fill(username);}

    @Step("Enter password")
    public void enterPassword(String password) {getPasswordField().fill(password);}
    @Step("Enter wrong password")
    public void enterWrongpassword(String Wrongpassword) {getPasswordField().fill(Wrongpassword);}
    @Step("Click Sign In button")
    public void clickSubmit() {getSubmitButton().click();}
    @Step("Verify Sign In button is hidden")
    public void verifySignInButtonHidden() {assertThat(getSignInButton()).isHidden();}
    @Step("Verify Profile icon is visible")
    public void verifyProfileIconVisible() {assertThat(getProfileIcon()).isVisible();}
    public Locator getProfileIcon() { return page.getByAltText("Profile icon"); }
    @Step("click Profile Icon")
    public void clickProfileIcon() { getProfileIcon().click(); }
    @Step("Enter FirstName")
    public void enterFirstName(String firstName) {getFirstNameField().fill(firstName);}
    @Step("Enter last name")
    public void enterLastName(String lastName) {getLastNameField().fill(lastName);}
    @Step("Enter email address")
    public void enterEmail(String email) {getEmailField().fill(email);}
    @Step("Enter confirm password")
    public void enterConfirmPassword(String password) {getConfirmPasswordField().fill(password);}
    @Step("Click Sign Up submit button")
    public void clickSignUpSubmit() {getSignUpSubmitButton().click();}
    @Step("Verify Terms and Privacy checkbox is not checked")
    public void verifyTermsPrivacyCheckboxNotChecked() {assertThat(getTermsPrivacyCheckbox()).not().isChecked();}
    @Step("Verify Terms and Privacy checkbox is checked")
    public void verifyTermsPrivacyCheckboxChecked() {assertThat(getTermsPrivacyCheckbox()).isChecked();}
    @Step("Click Terms and Privacy checkbox")
    public void ClickTermsandPrivacycheckbox() {getTermsPrivacyCheckbox().click();}



    @Step("Verify Terms and Privacy error message is visible")
    public void verifyTermsPrivacyErrorVisible() {assertThat(getTermsPrivacyError()).isVisible();}
    @Step("Click Forgot Password link")
    public void clickForgotPassword() {
        getForgotPasswordLink().click();
    }
    @Step("Verify Live Check section is visible")
    public void verifyPlaylistSectionVisible() {assertThat(getPlaylistSection()).isVisible();}
    @Step("Verify Live Check previous arrow is hidden")
    public void verifyPlaylistPreviousArrowHidden() {assertThat(getLiveCheckPreviousArrow()).isHidden();}
    @Step("Verify Live Check next arrow is visible")
    public void verifyPlaylistNextArrowVisible() {assertThat(getLiveCheckNextArrow()).isVisible();}
    @Step("Verify Live Check next arrow is hidden")
    public void verifyPlaylistNextArrowHidden() {assertThat(getLiveCheckNextArrow()).isHidden();}
    @Step("Verify Live Check previous arrow is visible")
    public void verifyPlaylistPreviousArrowVisible() {assertThat(getLiveCheckPreviousArrow()).isVisible();}
    @Step("Verify Privacy Policy Link Visible")
    public void verifyPrivacyPolicyLinkVisible() {assertThat(getPrivacyPolicyLink()).isVisible();}
    @Step("Verify Search Button is hidden")
    public void verifySearchButtonHidden() {assertThat(getsearchButton()).isHidden();}
    @Step("click Privacy Policy")
    public Page clickPrivacyPolicy() {
        getPrivacyPolicyLink().click();
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        return page;
    }
    @Step("Verify Export Data button is visible")
    public void verifyExportDataButtonVisible() {assertThat(getExportDataButton()).isVisible();}
    @Step("Click Export Data")
    public void clickExportData() {
        getExportDataButton().click();
    }
    @Step("Verify Export Data popup is visible")
    public void verifyExportDataPopupVisible() {
        assertThat(getExportDataPopup()).isVisible();
    }
    @Step("Verify empty password validation error is displayed")
    public void verifyExportPasswordRequiredError() {
        assertThat(getExportPasswordRequiredError()).isVisible();
    }
    @Step("Click Export Data submit button")
    public void clickExportDataSubmit() {
        getExportDataSubmitButton().click();
    }
    @Step("Verify Export Data password field is empty")
    public void verifyExportPasswordEmpty() {
        assertThat(getExportPassword()).hasValue("");
    }
    @Step("Verify Term Of Use Visible")
    public void verifyTermofuseVisible() {assertThat(gettermsOfUse()).isVisible();}
    @Step("Click Terms of Use link")
    public Page clickTermsOfUse() {
        gettermsOfUse().click();

        page.waitForLoadState(LoadState.DOMCONTENTLOADED);

        return page;
    }
    @Step("Verify Terms of Use URL")
    public void verifyTermsOfUseUrl(Page termsPage) {

        String actualUrl = termsPage.url();

        assertTrue(
                actualUrl.endsWith("/terms-of-use"),
                "Expected URL to end with /terms-of-use, but actual URL was: " + actualUrl
        );
    }
    @Step("Click Sign Out Button")
    public void clickSignOutbutton() {
        page.locator("div[role='button']")
                .filter(new Locator.FilterOptions().setHasText("Sign Out"))
                .click();
    }@Step("Enter search text: {searchText}")
    public void enterSearch(String searchText) {
        page.getByRole(
                AriaRole.TEXTBOX,
                new Page.GetByRoleOptions().setName("Search")
        ).fill(searchText);
    }


    @Step("Verify search result is visible: {title}")
    public void verifySearchResultVisible(String title) {

        Locator searchResult = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName(title)
                        .setExact(true)
        );

        assertTrue(
                searchResult.isVisible(),
                "Search result '" + title + "' should be visible"
        );
    }
    @Step("Verify Privacy Policy URL")
    public void verifyPrivacyPolicyurl(Page PrivacyPolicy) {

        String actualUrl = PrivacyPolicy.url();

        assertTrue(
                actualUrl.endsWith("/privacy-policy"),
                "Expected URL to end with /Privacy Policy, but actual URL was: " + actualUrl
        );
    }
    @Step("Verify and click The Rings of Power trailer")
    public void verifyAndClickRingsOfPower() {

        Locator ringsOfPower = page.locator(
                "section[aria-label='Trailers'] a[role='button']",
                new Page.LocatorOptions().setHasText("The Rings of Power")
        );

        Assert.assertTrue(
                ringsOfPower.isVisible(),
                "The Rings of Power should be visible in the Trailers section"
        );

        Assert.assertEquals(
                ringsOfPower.locator("h3").innerText(),
                "The Rings of Power",
                "Incorrect trailer title"
        );

        ringsOfPower.click();
    }
    public Locator getRingsOfPowerTrailerCards() {
        return page.locator(
                "section[aria-label='Trailers'] a[role='button']",
                new Page.LocatorOptions().setHasText("The Rings of Power")
        );
    }
    @Step("Wait for Start Watching button")
    public void waitForStartWatching() {

        Locator startWatching = page.locator("//button[contains(., 'Start watching')]");

        startWatching.waitFor(
                new Locator.WaitForOptions()
                        .setState(WaitForSelectorState.VISIBLE)
                        .setTimeout(30_000)
        );
    } @Step("Verify The Rings of Power appears only once in Trailers")
    public void verifyRingsOfPowerAppears() {

        scrollToContinueWatching();

        Locator cards = getRingsOfPowerContinueWatchingCards();

        int count = cards.count();

        System.out.println("The Rings of Power entries: " + count);

        assertEquals(
                count,
                1,
                "The Rings of Power should appear only once in Trailers"
        );
    }
    @Step("Click The media content in Trailers")
    public void Clickmedia() {
        getRingsOfPowerContinueWatchingCards().click();
    }
    public Locator getRingsOfPowerContinueWatchingCards() {
        return page.locator(
                "section[aria-label='Continue watching'] a[role='button']",
                new Page.LocatorOptions()
                        .setHasText("The Rings of Power")
        );
    }
    @Step("Verify The Rings of Power does not appear in Continue Watching")
    public void verifyRingsOfPowerNotInContinueWatching() {

        scrollToContinueWatching();

        Locator ringsOfPowerCards = getRingsOfPowerContinueWatchingCards();

        int count = ringsOfPowerCards.count();

        System.out.println("The Rings of Power entries in Continue Watching: " + count);

        assertEquals(
                count,
                0,
                "The Rings of Power should not appear in Continue Watching"
        );
    }


    //    public Locator getContinueWatchingShelf() {
//        return page.locator("//h2[normalize-space()='Continue watching']").first();
//    }
    public Locator tringPlayLogo(){
        return page.locator("img[alt='logo'][src*='tringplay']");
    }
    public Locator clientLogo(){
        return page.locator("img[alt='logo']").first();
    }

    @Step("Verify Tring Play branding logo is visible")
    public void verifyTringPlayBranding() {
        assertTrue(
                tringPlayLogo().isVisible(),
                "Tring Play branding logo is not visible"
        );
    }

    @Step("Verify client logo is visible")
    public void verifyClientLogoVisible() {
        assertTrue(
                clientLogo().isVisible(),
                "Client logo is not visible"
        );
    }
    @Step("Scroll to Continue Watching shelf")
    public void scrollToContinueWatching() {

        Locator continueWatching = getContinueWatchingShelf();

        for (int i = 0; i < 15; i++) {

            if (continueWatching.isVisible()) {
                continueWatching.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 800);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Continue Watching shelf was not found"
        );
    }
    public Locator getPawfectMomentContinueWatchingCards() {
        return page.locator(
                "a[role='button'][href*='/m/LybFIQnm/pawfect-moment']"
        );
    }
    @Step("Verify Pawfect Moment appears only once in Continue Watching")
//    public void verifyPawfectMomentAppears() {
//
//        scrollToContinueWatching();
//
//        Locator cards = getPawfectMomentContinueWatchingCards();
//
//        int count = cards.count();
//
//        System.out.println("Pawfect Moment entries: " + count);
//
//        assertEquals(
//                count,
//                1,
//                "Pawfect Moment should appear only once in Continue Watching"
//        );
//    }
//
    public Locator getPawfectMomentCard() {
        return page.locator("section[aria-label='Continue watching']")
                .locator("h3")
                .filter(new Locator.FilterOptions()
                        .setHasText("Pawfect Moment"));
    }

    public void verifyPawfectMomentAppears() {
        Locator pawfectMoment = getPawfectMomentCard();

        int count = pawfectMoment.count();

        System.out.println("Pawfect Moment entries: " + count);

        Assert.assertEquals(
                count,
                1,
                "Pawfect Moment should appear only once in Continue Watching"
        );

        Assert.assertTrue(
                pawfectMoment.first().isVisible(),
                "Pawfect Moment should be visible in Continue Watching"
        );
    }


    //    public Locator getPawfectMomentContinueWatching() {
//        return page.locator(
//                "//a[.//h3[normalize-space()='Pawfect Moment']]"
//        );
//    }
    public Locator getContinueWatchingShelf() {
        Locator heading = page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("Continue watching")
                        .setExact(true)
        );

        // h2 -> header div -> shelf div
        return heading.locator("xpath=../..").first();
    }

    public Locator getPawfectMomentContinueWatching() {
        return getContinueWatchingShelf()
                .locator("a")
                .filter(new Locator.FilterOptions().setHasText("Pawfect Moment"));
    }

    @Step("Verify Pawfect Moment is removed from Continue Watching")
    public void verifyPawfectMomentRemoved() {

        Locator cards = getPawfectMomentContinueWatching();

        int count = cards.count();

        System.out.println(
                "Pawfect Moment entries in Continue Watching: " + count
        );

        Assert.assertEquals(
                count,
                0,
                "Pawfect Moment should not be present in Continue Watching"
        );
    }
//    @Step("Verify Pawfect Moment is removed from Continue Watching")
//    public void verifyPawfectMomentRemoved() {
//
//        Locator cards =
//                getPawfectMomentContinueWatching();
//
//        int count = cards.count();
//
//        System.out.println(
//                "Pawfect Moment entries in Continue Watching: "
//                        + count
//        );
//
//        assertEquals(
//                count,
//                0,
//                "Pawfect Moment should be removed from Continue Watching after the video is fully watched"
//        );
//    }




    @Step("Click right arrow until the end")
    public void clickRightArrowUntilEnd() {
        Locator rightArrow = getLiveCheckNextArrow();
        for (int i = 0; i < 20; i++) {
            if (!rightArrow.isVisible()) {
                break;
            }
            rightArrow.click();
            page.waitForTimeout(300);
        }
    }
    @Step("click Left Arrow UntilStart")
    public void clickLeftArrowUntilStart() {
        Locator previousArrow = getLiveCheckPreviousArrow();
        int maxClicks = 50;
        for (int i = 0; i < maxClicks; i++) {
            if (!previousArrow.isVisible()) {
                break;
            }
            previousArrow.click();
            page.waitForTimeout(300);
        }
    }
    @Step("Verify Favorite button is hidden")
    public void verifyFavoriteButtonHidden() {
        assertThat(getFavoriteButton()).isHidden();
    }
    @Step("Verify Account option is visible")
    public void verifyAccountVisible() {assertThat(getAccountLink()).isVisible();}
    @Step("Click Account")
    public void clickAccount() {getAccountLink().click();}
    @Step("Verify Sign Out option is visible")
    public void verifySignOutVisible() {
        assertThat(getSignOutButton()).isVisible();
    }
    @Step("Click Sign Out")
    public void clickSignOut() {
        getSignOutButton().click();
    }
    @Step("Verify Sign Out confirmation panel is visible")
    public void verifySignOutPanelVisible() {assertThat(getSignOutPanel()).isVisible();}
    @Step("Verify Sign Out confirmation message")
    public void verifySignOutConfirmationMessage() {assertThat(getSignOutConfirmationMessage()).isVisible();}
    @Step("Verify No button is visible")
    public void verifySignOutNoButtonVisible() {
        assertThat(getSignOutNoButton()).isVisible();
    }
    @Step("Click No on Sign Out confirmation")
    public void clickSignOutNo() {
        getSignOutNoButton().click();
    }
    @Step("Verify Yes button is visible")
    public void verifySignOutYesButtonVisible() {assertThat(getSignOutYesButton()).isVisible();}
    @Step("Click Yes to confirm Sign Out")
    public void clickSignOutYes() {getSignOutYesButton().click();}
    @Step("Verify Profile icon is visible")
    public void verifyProfileIconHidden() {
        assertThat(getProfileIcon()).isHidden();
    }
    @Step("Verify Favorite button is visible")
    public void verifyFavoriteButtonVisible() {assertThat(getFavoriteButton()).isVisible();}
    @Step("Click Favorite button")
    public void clickFavorite() {getFavoriteButton().click();}
    @Step("Verify Favorite button is highlighted")
    public void verifyFavoriteButtonHighlighted() {assertThat(getFavoriteButton()).hasAttribute("aria-pressed", "true");}
    @Step("Verify Favorites icon is visible")
    public void verifyFavoritesIconVisible() {assertThat(getFavoritesIcon()).isVisible();}
    public Locator getFavoritesIcon() {
        return page.locator("a[href='/favorites']");
    }
    @Step("Click Favorites icon")
    public void clickFavoritesIcon() {getFavoritesIcon().click();}
    @Step("Verify My Favorites page is displayed")
    public void verifyMyFavoritesPageDisplayed() { assertThat(getMyFavoritesHeading()).isVisible(); }
    @Step("Verify favorited content is displayed")
    public void verifyFavoritedContentDisplayed(String contentTitle)
    { assertThat( page.getByText( contentTitle, new Page.GetByTextOptions().setExact(true) ) ).isVisible(); }
    @Step("Verify Delete Account option is visible")
    public void verifyDeleteAccountVisible() {assertThat(getDeleteAccountButton()).isVisible();}
    @Step("Verify Delete Account option is hidden")
    public void verifyDeleteAccountHidden() {assertThat(getDeleteAccountButton()).isHidden();}
    @Step("Verify at least 2 swimlanes with See More are available")
    public void verifySeeMoreAvailable() {
        int count = getSwimlanesWithSeeMore().count();
        assertTrue(
                count >= 2,
                "Expected at least 2 swimlanes with See More, but found: "
                        + count
        );
    }
    @Step("Click See More in swimlane")
    public void clickSeeMore(Locator swimlane) {
        Locator seeMore = getSeeMoreButton(swimlane);
        assertThat(seeMore).isVisible();
        seeMore.scrollIntoViewIfNeeded();
        seeMore.click();
    }
    @Step("Verify content is displayed after clicking See More")
    public void verifyContentDisplayedAfterSeeMore() {
        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
        page.waitForTimeout(1000);
        Locator content = page.locator("main img");
        int contentCount = content.count();
        assertTrue(
                contentCount > 0,
                "FAIL: No content is displayed after clicking See More."
        );
        boolean contentVisible = false;
        for (int i = 0; i < contentCount; i++) {
            if (content.nth(i).isVisible()) {
                contentVisible = true;
                break;
            }
        }
        assertTrue(
                contentVisible,
                "FAIL: Content is not visible after clicking See More."
        );
    }

    @Step("Verify content for 2 randomly selected See More swimlanes")
    public void verifyRandomTwoSeeMoreSwimlanes() {

        // Launch application only once
        launchApplication();

        // Scroll through the page to load all lazy-loaded swimlanes
        scrollToLoadAllSwimlanes();

        Locator swimlanes = getSwimlanesWithSeeMore();

        int totalSwimlanes = swimlanes.count();

        System.out.println("Total See More swimlanes found: " + totalSwimlanes);

        assertTrue(
                totalSwimlanes >= 2,
                "Expected at least 2 swimlanes with See More, but found: "
                        + totalSwimlanes
        );

        // Select first random swimlane
        Random random = new Random();

        int firstIndex = random.nextInt(totalSwimlanes);

        // Select second different random swimlane
        int secondIndex;

        do {
            secondIndex = random.nextInt(totalSwimlanes);
        } while (secondIndex == firstIndex);

        System.out.println(
                "Selected See More swimlanes: "
                        + firstIndex + " and " + secondIndex
        );

        int[] selectedIndexes = {firstIndex, secondIndex};

        for (int index : selectedIndexes) {

            // Get fresh locator after navigation/back
            swimlanes = getSwimlanesWithSeeMore();

            Locator swimlane = swimlanes.nth(index);

            // Scroll the selected swimlane into view
            swimlane.scrollIntoViewIfNeeded();

            assertThat(swimlane).isVisible();

            Locator seeMoreButton = getSeeMoreButton(swimlane);

            assertThat(seeMoreButton).isVisible();

            System.out.println(
                    "Verifying See More swimlane index: " + index
            );

            // Click See More
            clickSeeMore(swimlane);

            // Verify content displayed after clicking See More
            verifyContentDisplayedAfterSeeMore();

            // Go back for the second swimlane
            if (index == firstIndex) {
                page.goBack();
                page.waitForLoadState();

                // Reload lazy-loaded swimlanes
                scrollToLoadAllSwimlanes();
            }
        }
    }


    @Step("Scroll page to load all See More swimlanes")
    private void scrollToLoadAllSwimlanes() {

        int previousCount = 0;
        int stableCount = 0;

        for (int i = 0; i < 20; i++) {

            Locator swimlanes = getSwimlanesWithSeeMore();

            int currentCount = swimlanes.count();

            System.out.println(
                    "Scroll " + (i + 1)
                            + " - See More swimlanes found: "
                            + currentCount
            );

            if (currentCount == previousCount) {
                stableCount++;
            } else {
                stableCount = 0;
            }

            // If no new swimlanes appear after several scrolls,
            // assume all swimlanes are loaded.
            if (stableCount >= 3) {
                System.out.println("No new See More swimlanes found. Scrolling stopped.");
                break;
            }

            previousCount = currentCount;

            // Scroll down
            page.mouse().wheel(0, 800);

            // Wait for lazy-loaded content
            page.waitForTimeout(1000);
        }
    }


    public Locator getFavouritesSwimlane() {
        return page.locator(
                "//section[.//h2[normalize-space()='Favourites']]"
        ).filter(new Locator.FilterOptions().setVisible(true)).first();
    }
    @Step("Verify Favourites swimlane is visible")
    public void verifyFavouritesSwimlaneVisible() {

        assertTrue(
                getFavouritesSwimlane().isVisible(),
                "Favourites swimlane is not visible"
        );
    }
    @Step("Verify Google OAuth page is opened")
    public void verifyGoogleOAuthPageOpened(Page googlePage) {

        assertTrue(
                googlePage.url().contains("accounts.google.com"),
                "Google OAuth page was not opened. Actual URL: "
                        + googlePage.url()
        );
    }
    @Step("Verify Google Choose an account page is visible")
    public void verifyGooglesigninPage(Page googlePopup) {

        assertThat(
                googlePopup.getByText(
                        "Sign in ",
                        new Page.GetByTextOptions().setExact(true)
                )
        ).isVisible();
    }

    @Step("Verify Zootopia 2 is visible in Favourites")
    public void verifyZootopia2IsVisibleInFavourites() {

        Locator zootopia2 = getZootopia2FavouriteCard();

        assertTrue(
                zootopia2.isVisible(),
                "Zootopia 2 is not visible in Favourites"
        );
    }
    @Step("Click Back button")
    public void returnToHomePage() {
        Locator backButton = getBackButton();
        assertThat(backButton).isVisible();
        backButton.click();
    }
    @Step("Verify Back button is visible")
    public void verifyBackButtonVisible() {
        Locator backButton = getBackButton();
        assertThat(backButton).isVisible();
    }
    @Step("Verify More option is visible")
    public void verifyMoreOptionVisible() {
        assertThat(getMoreOption()).isVisible();
    }
    @Step("Verify Share button is visible and enabled")
    public void verifyShareButtonVisibleAndEnabled() {
        assertThat(getShareButton()).isVisible();
        assertThat(getShareButton()).isEnabled();
    }
    @Step("Verify hero image is visible")
    public void verifyHeroImageVisibles() {

        Locator heroImage = page.locator(
                "img[src*='background.webp']"
        ).first();

        assertThat(heroImage).isVisible();
    }
    @Step("Verify hero image is visible")
    public void verifyHeroRingImageVisible() {

        Locator heroImage = page.locator(
                "div[class*='_main_'] img[src*='background.webp']"
        ).first();

        assertThat(heroImage).isVisible();
    }
    @Step("Verify description is visible")
    public void verifyDescriptionVisible() {
        assertThat(getDescription()).isVisible();
    }
    @Step("Verify duration and age rating are visible")
    public void verifyDuration() {

        Locator durationAndRating = page.locator(
                "//span[@aria-hidden='true' and normalize-space()='|']/parent::div"
        ).first();

        assertThat(durationAndRating).isVisible();
    }
    @Step("Verify episode title is visible")
    public void verifyTitleVisible() {
        assertThat(getTitle()).isVisible();
    }
    @Step("Verify episode title is visible")
    public void verifyTitleVisibles() {
        assertThat(getTitles()).isVisible();
    }
    @Step("Scroll to Movies section")
    public void scrollToMoviesSection() {

        Locator moviesHeading = page.locator(
                "//h2[normalize-space()='Top 10 Movies']"
        ).first();
        //h2[normalize-space()='Spaace']

        for (int i = 0; i < 9; i++) {

            if (moviesHeading.isVisible()) {
                System.out.println("Top 10 Movies section found");
                moviesHeading.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Top 10 Movies section was not found after scrolling"
        );
    }
    @Step("Scroll to Movies section")
    public void scrollToSection() {

        Locator Heading = page.locator(
                " //h2[normalize-space()='Space']"
        ).first();
        for (int i = 0; i < 9; i++) {

            if (Heading.isVisible()) {
                System.out.println("space section found");
                Heading.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "section was not found after scrolling"
        );
    }


    @Step("Find movie '{movieName}' in Movies carousel")
    public void findMovieInMoviesCarousel(String movieName) {
        Locator movie = page.getByText( movieName, new Page.GetByTextOptions()
                .setExact(true) ).first(); Locator nextButton = page.getByRole( AriaRole.BUTTON,
                new Page.GetByRoleOptions() .setName("Next") .setExact(true) ).first();
        for (int i = 0; i < 10; i++) { if (movie.isVisible())
        { System.out.println( "Movie found: " + movieName ); movie.scrollIntoViewIfNeeded();
            assertThat(movie).isVisible(); return; } if (nextButton.isVisible())
        { System.out.println( "Movie not found. Clicking Next. Attempt: " + (i + 1) ); nextButton.click();
            page.waitForTimeout(500); } else
        { System.out.println( "Next button is not visible. " + "Stopping carousel search." );
            break; } } throw
                new AssertionError( "Movie '" + movieName + "' was not found in Movies carousel" ); }
    public Locator getContentCardByName(String contentName) {
        return page.locator("a[role='button']")
                .filter(new Locator.FilterOptions()
                        .setHasText(contentName));
    }
    @Step("Click content '{contentName}'")
    public void clickContentByName(String contentName) {

        Locator movie = page.getByLabel("Top 10 Movies")
                .locator("//h3[normalize-space()='" + contentName + "']")
                .first();

        assertThat(movie).isVisible();

        movie.scrollIntoViewIfNeeded();

        movie.click();
    }

    @Step("Click content '{contentName}'")
    public void clickContent(String contentName) {

        Locator movie = page.getByLabel("Space")
                .locator("h3")
                .filter(new Locator.FilterOptions().setHasText(contentName))
                .first();

        assertThat(movie).isVisible();

        movie.click();
    }

    @Step("Click password eye icon")
    public void clickExportDataPasswordEye() {
        getExportDataPasswordEye().click();
    }

    @Step("Verify Export Data password is visible")
    public void verifyExportDataPasswordVisible() {
        assertThat(page.locator("input[name='password']")).isVisible();
    }

    @Step("Verify Export Data password is hidden")
    public void verifyExportDataPasswordHidden() {
        assertThat(page.locator("input[type='password']")).isVisible();
    }
    @Step("Click Share button")
    public void clickShareButton() {

        Locator shareButton = getShareButton();
        shareButton.click();
    }
    public Locator getClearFavourites() {
        return page.locator("//span[normalize-space()='Clear favourites']");
    }
    @Step("Click Clear favourites")
    public void clickClearFavourites() {
        Locator clearFavourites = getClearFavourites();

        assertThat(clearFavourites).isVisible();
        clearFavourites.click();
    }

    @Step("Verify invalid login error message")
    public void VerifyInvalidLogin() {

        clickSubmit();

        Locator errorMessage = page.locator(
                "div[aria-live='assertive']"
        );

        assertThat(errorMessage).isVisible();

        assertThat(errorMessage).hasText(
                "Incorrect email/password combination"
        );
    }
    @Step("Verify login UI error message")
    public void verifyLoginErrorMessage() {

        assertThat(getProfileIcon()).isHidden();
        assertThat(getLoginErrorMessage()).isVisible();
        String uiError = getLoginErrorMessage().textContent();
        assertNotNull(uiError, "Login error message should be displayed");
        assertFalse(uiError.trim().isEmpty(), "Login error message should not be empty");
    }

    public Locator getGuestHome() {
        return page.locator("nav").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions()
                        .setName("Home").setExact(true));
    }
    public Locator getLoginErrorMessage() {
        return page.getByText(
                "Incorrect email/password combination",
                new Page.GetByTextOptions().setExact(true)
        );
    }
    public Locator getWatchTrailerButton() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions()
                .setName("Watch the trailer").setExact(true));
    }
    public Locator getSignInButton() {
        return page.locator("#root").getByRole(AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName("Sign In"));
    }
    public Locator getSignInPopup() {
        return page.locator(
                "div[aria-modal='true'][aria-label='Dialog']"
        ).filter(
                new Locator.FilterOptions()
                        .setHasText("Sign In")
        );
    }
    public Locator getInvalidExportDataPasswordError() {
        return page.getByText(
                "Invalid credentials.",
                new Page.GetByTextOptions().setExact(true)
        );
    }
    public Locator getSignInPopupCloseButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Close panel")
        );
    }
    public Locator getSignUpButton() {
        return page.locator("#root")
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Sign Up")
                );
    }
    public Locator getSignUpPopup() {
        return page.locator(
                "div[aria-modal='true'][aria-label='Dialog']"
        );
    }
    public Locator getSignUpPopupCloseButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Close panel")
        );
    }
    public Locator getHomeMenu() {
        return page.getByRole(
                AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("Home")
        );
    }
    public Locator getDrmMenu() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("DRM"));
    }
    public Locator getExportDataPopup() {
        return page.getByText(
                "Are you sure you want to export your data? Please confirm by entering your password to proceed."
        );
    }
    public Locator getExportPassword() {
        return page.locator(
                "//input[@type='password']"
        );
    }
    public Locator getExportDataSubmitButton() {
        return page.locator(
                "//button[@type='submit' and .//span[normalize-space()='Export Data']]"
        );
    }
    public Locator getExportPasswordRequiredError() {
        return page.getByText(
                "This field is required, please fill in a valid password.",
                new Page.GetByTextOptions().setExact(true)
        );
    }
    public Locator getExportDataPasswordEye() {
        return page.locator(
                "//input[@name='password']/following-sibling::*"
        ).last();
    }
    public Locator getVintagePlaylistMenu() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Vintage playlist"));
    }

    public Locator getDynamicPlaylistMenu() {
        return page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Dynamic Playlist"));
    }
    public Locator getSomeScreen() {
        return page.locator("//li[@aria-hidden='false']//a[@role='button' and contains(@href,'/m/Qt5WGvBY/')]");
    }
    public Locator getzooScreen() {
        return  page.locator(
                "//li[.//img[contains(@src,'37TxhXRy/poster.jpg')]]"
        ).filter(new Locator.FilterOptions().setVisible(true)).first();
    }
    public Locator getZootopia2Card() {
        return page.locator(
                "//section[@aria-label='New Playlist']//li[.//img[contains(@src,'HtHzM30e')]]"
        ).filter(new Locator.FilterOptions().setVisible(true)).first();
    }
    public Locator getZootopia2FavouriteCard() {
        return page.locator(
                "section[aria-label='Favourites'] a[role='button']"
        ).filter(new Locator.FilterOptions().setHasText("Zootopia 2"));
    }
    public Locator getStartWatchingButton() {
        return page.getByRole(AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Start watching").setExact(true));
    }
    public Locator startWatchingButton() {
        return page.locator("//button[contains(., 'Start watching')]");
    }
    public Locator getContinuewatching() {
        return page.locator("//button[.//span[normalize-space()='Continue watching']]");
    }
    public Locator getVideoPlayer() {
        return page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Pause").setExact(true)
                ).first();
    }
    public Locator getVideoPlayerBackButton() {
        return page
                .getByLabel("Video player")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Back").setExact(true)
                );
    }
    public Locator getUsernameField() {
        return page.locator("input[name='email']");
    }
    public Locator getUsernameField1() {
        return page.locator("input[name='email']");
    }
    public Locator getPasswordField() {
        return page.locator("input[name='password']");
    }
    public Locator getSubmitButton() {
        return page.locator("button[type='submit']").filter(new Locator.FilterOptions().setHasText("Sign In"));
    }
    public Locator getFirstNameField() {return page.locator("input[name='firstName']");}
    public Locator getLastNameField() {return page.locator("input[name='lastName']");}
    public Locator getEmailField() {return page.locator("input[name='email']");}
    public Locator getConfirmPasswordField() {return page.locator("input[name='confirmPassword']");}
    public Locator getTermsPrivacyCheckbox() {return page.getByRole(
            AriaRole.CHECKBOX
        );
    }
    public Locator getTermsPrivacyError() {
        return page.getByText(
                "This field is required",
                new Page.GetByTextOptions().setExact(true)
        );
    }

    public Locator getSignUpSubmitButton() {
        return page.locator(
                "//button[@type='submit' and .//span[normalize-space()='Sign Up']]"
        );
    }
    public Locator getForgotPasswordLink() {
        return page.locator("//a[@href='/?u=forgot-password' and normalize-space()='Forgot Password?']");
    }
    public Locator getPasswordResetForm() {
        return page.locator(
                "//div[@aria-modal='true' and @aria-label='Dialog']" +
                        "[.//h2[normalize-space()='Forgot Password']]"
        );
    }
    public Locator getPlaylistSection() {
        return page.getByRole(
                AriaRole.REGION,
                new Page.GetByRoleOptions().setName("New Playlist")
        );
    }

    public Locator getLiveCheckNextArrow() {
        return getPlaylistSection().getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Next slide")
        );
    }

    public Locator getLiveCheckPreviousArrow() {
        return getPlaylistSection().getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Previous slide")
        );
    }
    public Locator getExportDataButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Export Data")
        );
    }
    public Locator getFavoriteButton() {
        return page.locator("button[aria-label='Favorite']");
    }
    public Locator getAccountLink() {
        return page.getByRole(
                AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("Account")
                        .setExact(true)
        );
    }
    public Locator getSignOutButton() {
        return page.getByRole(
                AriaRole.LINK,
                new Page.GetByRoleOptions()
                        .setName("Sign Out")
                        .setExact(true)
        );
    }
    public Locator getSignOutPanel() {
        return page.locator(
                "//section[.//h2[normalize-space()='Sign Out']]"
        );
    }
    public Locator getSignOutConfirmationMessage() {
        return getSignOutPanel().getByText(
                "Are you sure you want to sign out of your current account?",
                new Locator.GetByTextOptions().setExact(true)
        );
    }
    public Locator getSignOutNoButton() {
        return getSignOutPanel().getByRole(
                AriaRole.LINK,
                new Locator.GetByRoleOptions()
                        .setName("No")
                        .setExact(true)
        );
    }
    public Locator getSignOutYesButton() {
        return getSignOutPanel().getByRole(
                AriaRole.BUTTON,
                new Locator.GetByRoleOptions()
                        .setName("Yes")
                        .setExact(true)
        );
    }
    public Locator getMyFavoritesHeading() {
        return page.getByRole( AriaRole.HEADING, new Page.GetByRoleOptions() .setName("My favourites") .setExact(true) ); }
    public Locator getDeleteAccountButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions()
                        .setName("Delete Account")
                        .setExact(true)
        );
    }

    public Locator getSwimlanesWithSeeMore() {return page.locator("section").filter(
            new Locator.FilterOptions().setHas(page.getByText("See More", new Page.GetByTextOptions().setExact(true))));
    }
    public Locator getSeeMoreButton(Locator swimlane) {return swimlane.getByText("See More",
                new Locator.GetByTextOptions().setExact(true));
    }

    public Locator getBackButton() {
        return page.locator("#video-details")
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Back")
                                .setExact(true)
                );
    }
    public Locator getShareButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Share").setExact(true)
        );
    }
    public Locator getMoreOption() {
        return page.locator("#video-details")
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions().setName("More")
                );
    }
    public Locator getHeroImage() {
        return page.locator(
                "//img[contains(@src,'37TxhXRy') and contains(@src,'background.webp')]"
        );
    }
    // Hero image
    public Locator getSeriesHeroImage() {
        return page.locator("//header[@id='video-details']//img");
    }

    // Series title
    public Locator getSeriesTitle() {
        return page.locator("//header[@id='video-details']//h1");
    }

    // Description
    public Locator getSeriesDescription() {
        return page.locator("//header[@id='video-details']//div[contains(@class,'collapsibleText')]//p");
    }
    public Locator getDescription() {
        return page.locator(
                "div._collapsibleText_200pc_4 p._textContainer_200pc_7"
        );
    }

    public Locator getTitle() {
        return page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("The Rings of Power")
        );
    }
    public Locator getTitles() {
        return page.getByRole(
                AriaRole.HEADING,
                new Page.GetByRoleOptions()
                        .setName("Avengers")
        );
    }
    public Locator getsearchButton()
    {
        return page.locator("//div[@role='button' and @aria-label='Open search']");}
    // Locator for Privacy Policy link
    public Locator getPrivacyPolicyLink() {
        return page.locator("//a[normalize-space()='Privacy Policy']").first();
    }
    public Locator gettermsOfUse() {return page.locator("//a[normalize-space()='Terms of Use']").first();
    }
    public Locator getPawfectMoment() {
        return page.locator(
                "a[href*='/m/LybFIQnm/pawfect-moment']"
        ).first();
    }
    public Locator getgoogleLogin () {
        return page.locator("//a[@aria-label='Sign in with Google']");
    }
    @Step("Verify Sign in with Google button is visible")
    public void verifyGoogleLoginButtonVisible() {
        assertThat(getgoogleLogin()).isVisible();
    }
    @Step("Verify Series Details hero image is visible")
    public void verifySeriesHeroImageVisible() {
        Locator heroImage = getSeriesHeroImage();

        heroImage.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

        assertTrue(
                heroImage.isVisible(),
                "Hero image should be visible on Series Details page"
        );
    }
    @Step("Verify Series Details title is visible")
    public void verifySeriesTitleVisible() {
        Locator title = getSeriesTitle();

        title.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

        assertTrue(
                title.isVisible(),
                "Series title should be visible on Series Details page"
        );
    }
    @Step("Verify Series Details description is visible")
    public void verifySeriesDescriptionVisible() {
        Locator description = getSeriesDescription();

        description.waitFor(new Locator.WaitForOptions()
                .setState(com.microsoft.playwright.options.WaitForSelectorState.VISIBLE));

        assertTrue(
                description.isVisible(),
                "Series description should be visible on Series Details page"
        );
    }
    @Step("verify Forgot Password Link")
    public void verifyForgotPasswordLink() {
        assertThat(getForgotPasswordLink()).isVisible();
    }
    @Step("Verify Password ResetForm")
    public void VerifyPasswordResetForm() {
        assertThat(getPasswordResetForm()).isVisible();
    }
    public Locator getResetPasswordButton() {
        return page.locator(
                "//button[@type='submit' and .//span[normalize-space()='Reset Passwor']]"
        );
    }
    @Step("Click Reset Password button")
    public void clickResetPasswordButton() {
        getResetPasswordButton().click();
    }
    @Step("Verify Sign in with Google button is enabled")
    public void verifyGoogleLoginButtonEnabled() {
        assertThat(getgoogleLogin()).isEnabled();
    }
    @Step("Verify incorrect password error is displayed")
    public void verifyInvalidExportDataPasswordError() {
        assertThat(getInvalidExportDataPasswordError()).isVisible();
    }
    @Step("click Google Login")
    public void clickGoogleLogin() {getgoogleLogin().click();}
    //a[@aria-label='Sign in with Facebook']
    public Locator getFacebookLogin () {
        return page.locator("//a[@aria-label='Sign in with Facebook']");
    }
    @Step("Verify Sign in with Google button is visible")
    public void verifyFacebookLoginButtonVisible() {assertThat(getFacebookLogin()).isVisible();}
    @Step("Verify Sign in with Google button is enabled")
    public void verifyFacebookLoginButtonEnabled() {
        assertThat(getFacebookLogin()).isEnabled();
    }

    @Step("Scroll and find Pawfect Moment")
    public void scrollToPawfectMoment() {

        Locator pawfectMoment = getPawfectMoment();

        for (int i = 0; i < 10; i++) {

            if (pawfectMoment.isVisible()) {
                pawfectMoment.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 800);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Pawfect Moment video was not found on the Home page"
        );
    }
    @Step("Click Pawfect Moment video")
    public void clickPawfectMoment() {

        getPawfectMoment().click();

        page.waitForLoadState(LoadState.DOMCONTENTLOADED);
    }
    @Step("Play Pawfect Moment 3 times")
    public void playPawfectMoment() {

        for (int i = 1; i <= 3; i++) {

            Allure.step("Play Pawfect Moment - iteration " + i);

        }
    }

    @Step("Scroll to footer")
    public void scrollToFooter() {

        for (int i = 0; i < 20; i++) {

            if (getPrivacyPolicyLink().isVisible()) {
                return;
            }

            page.mouse().wheel(0, 800);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Footer was not found after scrolling"
        );
    }
    public Locator getVideoCurrentTime() {
        return page.locator("button.shaka-current-time");
    }
    @Step("Get current playback position")
    public int getCurrentPlaybackSeconds() {

        String timestamp = getVideoCurrentTime().innerText();

        // Example: "0:07 / 0:24"
        String currentTime = timestamp.split("/")[0].trim();

        String[] parts = currentTime.split(":");

        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);

        int totalSeconds = (minutes * 60) + seconds;

        System.out.println(
                "Current playback position: " + totalSeconds + " seconds"
        );

        return totalSeconds;
    }
    private int parsePlaybackSeconds(String timeText) {

        String current = timeText.split("/")[0].trim();

        String[] parts = current.split(":");

        if (parts.length == 2) {

            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);

            return minutes * 60 + seconds;
        }

        throw new IllegalArgumentException(
                "Invalid playback time: " + timeText
        );
    }
    public double getCurrentVideoTimestamp() {

        Locator video = page.locator("video.shaka-video");

        video.waitFor();

        return (double) video.evaluate(
                "video => video.currentTime"
        );
    }

    @Step("Pause video")
    public void pauseVideo() {

        Locator video = page.locator("video.shaka-video");

        video.waitFor();

        video.evaluate("video => video.pause()");

        Boolean paused = (Boolean) video.evaluate("video => video.paused");

        assertTrue(paused, "Video was not paused");
    }

    @Step("Verify video resumes at saved timestamp")
    public void verifyVideoResumesAtTimestamp(double savedTimestamp) {

        double actualTimestamp = getCurrentVideoTimestamp();

        System.out.println("Expected timestamp: " + savedTimestamp);
        System.out.println("Actual timestamp: " + actualTimestamp);

        assertTrue(
                Math.abs(actualTimestamp - savedTimestamp) <= 2,
                "Video should resume near the saved timestamp. " +
                        "Expected: " + savedTimestamp +
                        ", Actual: " + actualTimestamp
        );
    }
    public Locator episodesSection() {
        return page.locator("div._relatedVideos_vnwhx_66")
                .filter(new Locator.FilterOptions()
                        .setHasText("Episodes"))
                .first();
    }

    public Locator episodesHeading() {
        return episodesSection()
                .getByRole(
                        AriaRole.HEADING,
                        new Locator.GetByRoleOptions()
                                .setName("Episodes")
                );
    }

    public Locator episodeCards() {
        return episodesSection()
                .locator("a._card_a1msq_4[role='button']");
    }

    public Locator episodeTitle(Locator episodeCard) {
        return episodeCard.locator("h3._title_a1msq_25");
    }

    public Locator episodeThumbnail(Locator episodeCard) {
        return episodeCard.locator("img._posterImage_a1msq_118");
    }
    @Step("Locate Episodes section")
    public void locateEpisodesSection() {
        episodesHeading().scrollIntoViewIfNeeded();
    }


    @Step("Scroll to Episodes")
    public void scrollToseries() {

        Locator heading = page.locator(
                "//h2[normalize-space()='Episodes']"
        ).first();

        for (int i = 0; i < 9; i++) {

            if (heading.isVisible()) {
                System.out.println("Episodes section found");
                heading.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 600);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Episodes section was not found after scrolling"
        );
    }
    public Locator episodeNumber(Locator episodeCard) {
        return episodeCard.locator("div._tag_a1msq_63");
    }
    @Step("Verify Episodes section is visible")
    public void verifyEpisodesSectionVisible() {
        assertTrue(
                episodesHeading().isVisible(),
                "Episodes section is not visible"
        );
    }

    @Step("Verify episode cards are displayed")
    public void verifyEpisodeCardsDisplayed() {
        assertTrue(
                episodeCards().count() > 0,
                "No episode cards are displayed"
        );
    }

    @Step("Verify episode thumbnails are displayed")
    public void verifyEpisodeThumbnailsDisplayed() {
        int count = episodeCards().count();

        for (int i = 0; i < count; i++) {
            Locator thumbnail = episodeThumbnail(
                    episodeCards().nth(i)
            );

            assertTrue(
                    thumbnail.isVisible(),
                    "Episode thumbnail is not visible for card: " + (i + 1)
            );
        }
    }

    @Step("Verify season and episode information is displayed")
    public void verifySeasonAndEpisodeInformation() {
        int count = episodeCards().count();

        for (int i = 0; i < count; i++) {
            Locator episodeNumber = episodeNumber(
                    episodeCards().nth(i)
            );

            assertTrue(
                    episodeNumber.isVisible(),
                    "Season/episode information is not visible for card: " + (i + 1)
            );

            String value = episodeNumber.innerText().trim();

            assertTrue(
                    value.matches("S\\d+:E\\d+"),
                    "Invalid season/episode format: " + value
            );

            System.out.println(
                    "Episode " + (i + 1) + ": " + value
            );
        }
    }
    public Locator firstEpisodeCard() {
        return page.locator(
                "//h2[normalize-space()='Episodes']" +
                        "/following::a[@role='button'][.//h3]"
        ).first();
    }

    public Locator videoPlayer() {
        return page.locator("video").first();
    }
    @Step("Click first episode")
    public void clickEpisode() {
        firstEpisodeCard().click();
    }
    @Step("Verify selected episode is playing")
    public void verifySelectedEpisodeIsPlaying() {

        Boolean playing = (Boolean) page.evaluate(
                "() => {" +
                        "const video = document.querySelector('video');" +
                        "return video ? !video.paused && !video.ended : false;" +
                        "}"
        );

        assertTrue(
                playing,
                "Selected episode is not playing"
        );
    }
    public Locator featuredSection() {
        return page.locator(
                "section._featured_5z9m7_28"
        ).first();
    }

    public Locator featuredVisibleCard() {
        return featuredSection()
                .locator("li[aria-hidden='false']")
                .first();
    }

    public Locator featuredVisibleCardImage() {
        return featuredVisibleCard()
                .locator("img")
                .first();
    }

    public Locator featuredVisibleCardDuration() {
        return featuredVisibleCard()
                .locator("div._tag_a1msq_63")
                .first();
    }
    public Locator featuredPagination() {
        return featuredSection()
                .locator("div[aria-live='polite']");
    }
    public Locator fullScreenButton() {
        return page.getByRole(
                AriaRole.BUTTON,
                new Page.GetByRoleOptions().setName("Full screen")
        );
    }

    @Step("Click Full Screen button")
    public void clickFullScreen() {

        assertTrue(
                fullScreenButton().isVisible(),
                "Full Screen button is not visible"
        );

        assertTrue(
                fullScreenButton().isEnabled(),
                "Full Screen button is not enabled"
        );

        fullScreenButton().click();

        page.waitForTimeout(1000);
    }

    @Step("Verify Full Screen mode")
    public void verifyFullScreenEnabled() {

        boolean isFullScreen = (Boolean) page.evaluate(
                "() => document.fullscreenElement !== null"
        );

        assertTrue(
                isFullScreen,
                "Application did not enter Full Screen mode"
        );
    }

    public Locator featuredNextButton() {
        return featuredSection()
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Next slide")
                )
                .first();
    }
    @Step("Get currently displayed Featured media")
    public String getCurrentFeaturedMedia() {
        return featuredVisibleCard()
                .locator("a[role='button']")
                .getAttribute("href");
    }
    @Step("Verify Featured carousel automatically advances")
    public void verifyFeaturedCarouselAutomaticallyAdvances() {

        String firstMedia = getCurrentFeaturedMedia();

        System.out.println(
                "Initial Featured media: " + firstMedia
        );

        page.waitForTimeout(8_000);

        String secondMedia = getCurrentFeaturedMedia();

        System.out.println(
                "Featured media after 8 seconds: " + secondMedia
        );

        assertNotEquals(
                secondMedia,
                firstMedia,
                "Featured carousel did not automatically advance after 8 seconds"
        );
    }
    @Step("Verify Featured carousel pagination advances")
    public void verifyFeaturedPaginationAdvances() {

        String firstPage = featuredPagination().innerText();

        System.out.println(
                "Initial Featured pagination: " + firstPage
        );

        page.waitForTimeout(8_000);

        String secondPage = featuredPagination().innerText();

        System.out.println(
                "Featured pagination after 8 seconds: " + secondPage
        );

        assertNotEquals(
                secondPage,
                firstPage,
                "Featured carousel pagination did not advance automatically"
        );
    }
    @Step("Verify vertical navigation through Home carousels")
    public void verifyHomeVerticalScrolling() {

        page.keyboard().press("Home");
        page.waitForTimeout(1000);

        for (int i = 0; i < 30; i++) {

            String focusedElement = page.evaluate(
                    "() => document.activeElement ? document.activeElement.outerHTML : ''"
            ).toString();

            assertFalse(
                    focusedElement.isEmpty(),
                    "Focus was lost while navigating Home screen"
            );
            page.keyboard().press("ArrowDown");
            page.waitForTimeout(500);
        }

        // Navigate back upward
        for (int i = 0; i < 20; i++) {

            String focusedElement = page.evaluate(
                    "() => document.activeElement ? document.activeElement.outerHTML : ''"
            ).toString();

            assertFalse(
                    focusedElement.isEmpty(),
                    "Focus was lost while navigating back through Home screen"
            );

            page.keyboard().press("ArrowUp");
            page.waitForTimeout(500);
        }
    }



}