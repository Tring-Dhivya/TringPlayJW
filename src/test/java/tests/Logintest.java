package tests;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Hubpage;
import pages.Loginpage;
import utils.ClientExpectationReader;
import utils.ConfigReader;
import utils.FirebaseRemoteConfigClient;

import java.io.IOException;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Logintest extends BaseTest {

    // TC : 45 Ensure no duplicate entries appear in continue watching shelf
    // TR_JWP_510
    @Test
    @Epic("Continue Watching")
    @Feature("Duplicate Handling")
    @Story("Ensure no duplicate entries appear in Continue Watching shelf")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that playing the same video multiple times does not create duplicate Continue Watching entries")
    public void verifyNoDuplicateContinueWatchingEntries() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalContinueWatching = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_continue_watching_enabled");
        try {
            assertTrue(originalClientActive, "is_client_active should be true");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalContinueWatching, "is_continue_watching_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            hubPage.scrollToTrailersSection();
            loginPage.verifyAndClickRingsOfPower();
//            loginPage.clickPawfectMoment();
            loginPage.waitForStartWatching();
            loginPage.clickStartWatching();
            page.waitForTimeout(20_000);
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            loginPage.clickContinuewatching();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            page.waitForTimeout(30_000);
            loginPage.clickContinuewatching();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            loginPage.returnToHomePage();
            loginPage.scrollToContinueWatching();
            page.waitForTimeout(10_000);
            loginPage.verifyRingsOfPowerAppears();
            loginPage.Clickmedia();
//            loginPage.clickContinuewatching();
            page.waitForTimeout(120_000);
            loginPage.returnToHomePage();
            loginPage.verifyGuestHomeVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
     //Tc : 46 Verify fully watched video is removed from continue watching shelf
    // TR_JWP_511
    @Test
    @Epic("Continue Watching")
    @Feature("Completion Removal")
    @Description("Verify fully watched video is removed from Continue Watching shelf")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Continue Watching - Completion Removal")
    public void verifyFullyWatchedVideoRemovedFromContinueWatching() throws IOException, InterruptedException {

        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalContinueWatching = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_continue_watching_enabled");
        try {
            assertTrue(originalClientActive, "is_client_active should be true");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalContinueWatching, "is_continue_watching_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername1(ConfigReader.getUsername1());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            hubPage.scrollToTrailersSection();
            loginPage.verifyAndClickRingsOfPower();
            loginPage.waitForStartWatching();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(130_000);
//            loginPage.clickVideoPlayerBackButton();
            loginPage.returnToHomePage();
            loginPage.scrollToContinueWatching();
            page.waitForTimeout(10_000);
            loginPage.verifyRingsOfPowerNotInContinueWatching();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 47 Verify playback resumes at exact saved timestamp
    // TR_JWP_531
    @Test
    @Epic("Continue Watching")
    @Feature("Resume Accuracy")
    @Description("Verify playback resumes at exact saved timestamp")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Continue Watching - Resume Accuracy")
    public void verifyPlaybackResumesAtExactSavedTimestamp() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalContinueWatching = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_continue_watching_enabled");
        try {
            assertTrue(originalClientActive, "is_client_active should be true");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalContinueWatching, "is_continue_watching_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.scrollToPawfectMoment();
            loginPage.clickPawfectMoment();
            loginPage.clickStartWatching();
            page.waitForTimeout(12_000);
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(5_000);
            double savedTimestamp = loginPage.getCurrentVideoTimestamp();
            loginPage.pauseVideo();
            loginPage.clickVideoPlayerBackButton();
            loginPage.clickContinuewatching();
            page.waitForTimeout(1_000);
            loginPage.verifyVideoResumesAtTimestamp(savedTimestamp);
        }
        catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }


    }

    // TC: 48 Verify Privacy Policy link navigates to correct page
    // TC: TR_JWP_541
    @Test
    @Epic("Footer")
    @Feature("Privacy Policy")
    @Story("Verify Privacy Policy link navigates to correct page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Privacy Policy link navigates to the correct English HTML Privacy Policy page")
    public void verifyPrivacyPolicyNavigation() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        String expectedPrivacyPolicyUrl = firebase.getStagingString( "common.legal_docs.privacy_policy.en" );
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "is_client_active should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToFooter();
            loginPage.verifyPrivacyPolicyLinkVisible();
            Page PrivacyPolicy = loginPage.clickPrivacyPolicy();
            loginPage.verifyPrivacyPolicyurl(PrivacyPolicy);
            loginPage.verifyPrivacyPolicyLinkVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    //TC : 49 Verify Terms of Use link navigates to correct page
    // TR_JWP_542
    @Test
    @Epic("Footer")
    @Feature("Terms of Use")
    @Story("Verify Terms of Use navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Terms of Use link navigates to the correct configured English page")
    public void verifyTermsOfUseNavigation() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        String expectedTermsOfUseUrl = firebase.getStagingString("common.legal_docs.terms_of_use.en");
        try {
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToFooter();
            loginPage.verifyTermofuseVisible();
            Page termsPage = loginPage.clickTermsOfUse();
            loginPage.verifyTermsOfUseUrl(termsPage);
            loginPage.verifyTermofuseVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    //TC:50 Verify error message on empty/invalid password
    // TC : TR_JWP_575
    @Test()
    @Epic("Account Page")
    @Feature("Export Data")
    @Description("Verify error message is displayed when Export Data is submitted with an empty password")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Empty Password Validation")
    public void verifyExportDataEmptyPasswordValidation() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalExportAccount = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.export_account");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean ExportAccount = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.export_account");
            assertTrue(clientActive, "Client should be active");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertTrue(ExportAccount, "export_account should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            loginPage.verifyExportDataButtonVisible();
            loginPage.clickExportData();
            loginPage.verifyExportDataPopupVisible();
            loginPage.verifyExportPasswordEmpty();
            loginPage.clickExportDataSubmit();
            loginPage.verifyExportPasswordRequiredError();
        }  catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 51 Verify search button hidden if Firebase config disabled
    //  TR_JWP_593
    @Test()
    @Epic("Search")
    @Feature("Header")
    @Description("Verify search button is hidden when is_search_enabled is false")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Search button visibility")
    public void verifySearchButtonHiddenWhenSearchDisabled() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalSearchEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_search_enabled");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            firebase.updateSearchEnabled(false);
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForTimeout(30_000);
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(10_000);
            loginPage.verifySearchButtonHidden();
        } finally {
            try {
                firebase.updateSearchEnabled(originalSearchEnabled);
                 } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    //Tc : 53 Verify whether Google login button is visible when Google provider is enabled in Firebase?
    // TR_JWP_639
    @Test()
    @Epic("Social Login")
    @Feature("Google Login")
    @Description("Verify Google login button displays on Sign-In modal when Google provider flag is enabled in Firebase")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Google Login Button Visibility")
    public void verifyGoogleLoginButtonVisibleWhenGoogleProviderEnabled() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalsocialLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "platforms.web.is_social_login_enabled");
        boolean originalGoogleLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.social_login_providers.google");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalsocialLoginEnabled, "Social login should be enabled");
            assertTrue(originalGoogleLoginEnabled, "Google login provider should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.verifyGoogleLoginButtonVisible();
            loginPage.verifyGoogleLoginButtonEnabled();
        }
        catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    //TC:54 Verify whether clicking Google opens OAuth popup navigating to Google's authentication page?
    // TC : TR_JWP_641
    @Test()
    @Epic("Social Login")
    @Feature("Google Login")
    @Description("Verify clicking Google opens OAuth popup and navigates to Google's authentication page")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Google OAuth Popup")
    public void verifyGoogleLoginOAuthPopup() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalsocialLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "platforms.web.is_social_login_enabled");
        boolean originalGoogleLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.social_login_providers.google");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalsocialLoginEnabled, "Social login should be enabled");
            assertTrue(originalGoogleLoginEnabled, "Google login provider should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.verifyGoogleLoginButtonVisible();
            loginPage.verifyGoogleLoginButtonEnabled();
            loginPage.verifyGoogleLoginButtonVisible();
            loginPage.verifyGoogleLoginButtonEnabled();
            loginPage.clickGoogleLogin();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGoogleOAuthPageOpened(page);
            loginPage.verifyGooglesigninPage(page);
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    //TC : 55 Verify whether Facebook login button is visible when Facebook provider flag is enabled?
    // TR_JWP_649
    @Test()
    @Epic("Social Login")
    @Feature("Facebook Login")
    @Description("Verify Facebook login button displays on Sign-In modal when Facebook provider flag is enabled in Firebase")
    @Severity(SeverityLevel.CRITICAL)
    @Story("Facebook Login Button Visibility")
    public void verifyFacebookLoginButtonVisibleWhenFacebookProviderEnabled() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalsocialLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "platforms.web.is_social_login_enabled");
        boolean originalFacebookLoginEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.social_login_providers.facebook");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            assertTrue(originalAuthentication, "is_authentication_enabled should be true");
            assertTrue(originalsocialLoginEnabled, "Social login should be enabled");
            assertTrue(originalFacebookLoginEnabled, "Facebook login provider should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.verifyFacebookLoginButtonVisible();
            loginPage.verifyFacebookLoginButtonEnabled();
        }
        catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }






}
