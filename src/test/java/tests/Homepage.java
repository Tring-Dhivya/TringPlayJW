package tests;

import base.BaseTest;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Hubpage;
import pages.Loginpage;
import utils.AllureUtils;
import utils.ClientExpectationReader;
import utils.ConfigReader;
import utils.FirebaseRemoteConfigClient;
import java.io.IOException;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class Homepage extends BaseTest {
  


    // TC: 01  Validate whether user is able to launch the website successfully without any error
    // TR_JWP_01
    @Test
    @Epic("Launch")
    @Feature("Initial Launch")
    @Story("verifyClientActive")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate application behaviour when client is active and inactive")
    public void verifyClientActiveBehaviour() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            assertNoConsoleErrors();
            firebase.updateClientActive(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate inactiveTemplate = firebase.getRemoteConfig();
            boolean updatedClientActive = firebase.getAutomationBoolean(inactiveTemplate.getJson(), "common.is_client_active");
            assertFalse(updatedClientActive, "Client should be inactive in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeHidden();
            assertNoConsoleErrors();
        } finally {
            try {
                firebase.updateClientActive(originalClientActive);
             } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    // TC: 02 Validate the Sign In and Sign Up buttons appear in the menu
    //TR_JWP_07
    @Test
    @Epic("Launch")
    @Feature("Top Menu")
    @Story("verifySignInAndSignUp")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Scenario - Validate Sign In and Sign Up buttons on Homepage")
    public void verifySignInAndSignUpButtons() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalSignIn = firebase.getAutomationBoolean(originalTemplate.getJson(),"authentication.can_show_signin_button");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
       boolean originalSignUp = firebase.getAutomationBoolean(originalTemplate.getJson(),"authentication.can_show_signup_button");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean signIn = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.can_show_signin_button");
            assertTrue(signIn, "Sign In button should be enabled in Firebase");
            boolean authentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(authentication, "Authentication should be enabled in Firebase");
            boolean signUp = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.can_show_signup_button");
            assertTrue(signUp, "Sign Up button should be enabled in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.verifySignInButtonEnabled();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.closeSignInPopup();
            loginPage.verifySignInPopupHidden();
            loginPage.verifySignUpButtonVisible();
            loginPage.verifySignUpButtonEnabled();
            loginPage.clickSignUp();
            loginPage.verifySignUpPopupVisible();
            loginPage.closeSignUpPopup();
            loginPage.verifySignUpPopupHidden();
            firebase.updateAuthenticationConfiguration(false, false, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate inactiveTemplate = firebase.getRemoteConfig();
            boolean signInDisabled = firebase.getAutomationBoolean(inactiveTemplate.getJson(), "authentication.can_show_signin_button");
            boolean authenticationDisabled = firebase.getAutomationBoolean(inactiveTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean signUpDisabled = firebase.getAutomationBoolean(inactiveTemplate.getJson(), "authentication.can_show_signup_button");
            assertFalse(signInDisabled, "Sign In button should be disabled in Firebase");
            assertFalse(authenticationDisabled, "Authentication should be disabled in Firebase");
            assertFalse(signUpDisabled, "Sign Up button should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonHidden();
            loginPage.verifySignUpButtonHidden();
        }
        finally {
            try {
                firebase.updateAuthenticationConfiguration(originalAuthentication,originalSignIn,originalSignUp);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }
    //TC:03  Validate whether user is able to continue as guest
    // TR_JWP_08
    @Test
    @Epic("Launch")
    @Feature("Continue As Guest")
    @Story("verifyGuestModeAndAuthentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate Guest Home and authentication UI based on Firebase Remote Config")
    public void verifyGuestModeAndAuthenticationBehaviour() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        try {
            boolean isClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled");
            assertTrue(isAuthenticationEnabled, "Authentication should initially be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.verifySignUpButtonVisible();
            firebase.updateAuthenticationEnabled(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean updatedAuthentication = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            assertFalse(updatedAuthentication, "Authentication should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonHidden();
            loginPage.verifySignUpButtonHidden();
        } finally {
            try {
                firebase.updateAuthenticationEnabled(originalAuthentication);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }

    }

    // TC: 04 Validate guest user can access all possible screens and menus
    // TR_JWP_09  need to work onthe menu drm and content click alo
    @Test
    @Epic("Guest")
    @Feature("Home Screen")
    @Story("verifyGuestUserNavigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate guest user can access all possible screens and menus")
    public void verifyGuestUserNavigationAndAccess() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            boolean isClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.verifySignUpButtonVisible();
//            loginPage.verifyHomeMenuVisible();
//            loginPage.verifyDrmMenuVisible();
//            loginPage.verifyVintagePlaylistMenuVisible();
//            loginPage.verifyDynamicPlaylistMenuVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            page.waitForTimeout(30_000);
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            firebase.updateGuestModeEnabled(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate guestDisabledTemplate = firebase.getRemoteConfig();
            boolean updatedGuestMode = firebase.getAutomationBoolean(guestDisabledTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertFalse(updatedGuestMode, "Guest mode should be disabled");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.clickStartWatching();
            loginPage.verifySignUpPopupVisible();
            loginPage.closeSignUpPopup();
            loginPage.verifySignUpPopupHidden();
            firebase.updateGuestModeAndAuthentication(true, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean updatedAuthentication = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            assertFalse(updatedAuthentication, "Authentication should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonHidden();
            loginPage.verifySignUpButtonHidden();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
        } finally {
            try {
                firebase.updateGuestModeEnabled(originalGuestMode);
                firebase.updateAuthenticationEnabled(originalAuthentication);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }


    //TC: 05  Validate the user can access web content available for guest users
    // TR_JWP_10
    @Test()
    @Epic("Guest")
    @Feature("Continue As Guest")
    @Story("Continue As Guest")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate the user can access web content available for guest users")
    public void verifyGuestUserContentAccess() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.verifySignUpButtonVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            page.waitForTimeout(30_000);
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            firebase.updateGuestModeEnabled(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate guestDisabledTemplate = firebase.getRemoteConfig();
            boolean updatedGuestMode = firebase.getAutomationBoolean(guestDisabledTemplate.getJson(), "common.is_guest_mode_enabled");
            assertFalse(updatedGuestMode, "Guest mode should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.clickStartWatching();
            loginPage.verifySignUpPopupVisible();
            loginPage.closeSignUpPopup();
            loginPage.verifySignUpPopupHidden();
            firebase.updateGuestModeAndAuthentication(true, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate noAuthTemplate = firebase.getRemoteConfig();
            boolean updatedAuthentication = firebase.getAutomationBoolean(noAuthTemplate.getJson(), "authentication.is_authentication_enabled");
            assertFalse(updatedAuthentication, "Authentication should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonHidden();
            loginPage.verifySignUpButtonHidden();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
        } catch (Throwable e) {
            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            try {
                firebase.updateGuestModeEnabled(originalGuestMode);
                firebase.updateAuthenticationEnabled(originalAuthentication);
            } catch (Throwable e) {
                System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // TC: 06 Validate successful login with valid credentials
    // TR_JWP_20
    @Test
    @Epic("Sign In")
    @Feature("Sign In")
    @Story("SuccessfulLogin")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate successful login with valid InPlayer credentials")
    public void verifySuccessfulLogin() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(updatedTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
            }
    }
    // TC: 07   Validate Terms & Privacy checkbox
    // TR_JWP_35
    @Test
    @Epic("Sign Up")
    @Feature("Sign Up")
    @Story("verifyTermsPrivacy")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate Terms & Privacy checkbox")
    public void verifyTermsPrivacyCheckbox() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertTrue(clientActive, "Client should be active");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type should be inplayer"
            );
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignUpButtonVisible();
            loginPage.clickSignUp();
            loginPage.verifySignUpPopupVisible();
            loginPage.enterFirstName(ConfigReader.getFirstNameField());
            loginPage.enterLastName(ConfigReader.getLastNameField());
            loginPage.enterEmail(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.enterConfirmPassword(ConfigReader.getPassword());
            loginPage.verifyTermsPrivacyCheckboxNotChecked();
            loginPage.clickSignUpSubmit();
            loginPage.verifyTermsPrivacyErrorVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 08  Validate that clicking on Forgot Password link opens password reset form
    // TR_JWP_49
    @Test
    @Epic("Forgot Password")
    @Feature("ForgotPasswordFlow")
    @Story("verifyForgotPassword")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that clicking Forgot Password opens the password reset form")
    public void verifyForgotPasswordNavigation() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalSignIn = firebase.getAutomationBoolean(originalTemplate.getJson(),"authentication.can_show_signin_button");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean signInButtonEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.can_show_signin_button");
            assertTrue(clientActive, "Client should be active");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertTrue(signInButtonEnabled, "Sign In button should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.verifyForgotPasswordLinkVisible();
            loginPage.clickForgotPassword();
            loginPage.verifyPasswordResetFormVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

//    // TC: 09 need to update from the team side
////    @Test
////    @Description("Validate system behavior when valid registered email is used in Forgot Password")
////    public void verifyForgotPasswordWithRegisteredEmail() throws Exception {
////        Loginpage loginPage = new Loginpage(page);
////        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
////        FirebaseRemoteConfigClient.RemoteConfigTemplate template = firebase.getRemoteConfig();
////        try {
////            boolean clientActive = firebase.getAutomationBoolean(template.getJson(), "common.is_client_active");
////            boolean authenticationEnabled = firebase.getAutomationBoolean(template.getJson(), "authentication.is_authentication_enabled");
////            boolean signInEnabled = firebase.getAutomationBoolean(template.getJson(), "authentication.can_show_signin_button");
////            assertTrue(clientActive, "Client should be active");
////            assertTrue(authenticationEnabled, "Authentication should be enabled");
////            assertTrue(signInEnabled, "Sign In button should be enabled");
////            loginPage.launchApplication();
////            assertThat(loginPage.getGuestHome()).isVisible();
////            assertThat(loginPage.getSignInButton()).isVisible();
////            loginPage.clickSignIn();
////            assertThat(loginPage.getSignInPopup()).isVisible();
////            assertThat(loginPage.getForgotPasswordLink()).isVisible();
////            loginPage.clickForgotPassword();
////            assertThat(loginPage.getPasswordResetForm()).isVisible();
////            assertThat(loginPage.getForgotPasswordEmail()).isVisible();
////            assertThat(loginPage.getResetPasswordButton()).isVisible();
////            loginPage.enterForgotPasswordEmail(ConfigReader.getUsername());
////            loginPage.clickResetPassword();
////            assertThat(loginPage.getResetPasswordConfirmation()).isVisible();
////
////        } catch (Exception e) {
////
////            e.printStackTrace();
////            throw e;
////        }
////    }


       //TC: 10  Verify vertical scrolling of the Home screen
       //HOME-004
@Test
@Epic("Homepage")
@Feature("Carousel/Swimlane")
@Story("Verify vertical scrolling of Home screen")
@Severity(SeverityLevel.NORMAL)
@Description("Verify that users can vertically scroll through all configured carousels/swimlanes on the Home screen without focus loss, UI corruption, or unexpected navigation")
public void verifyHomeVerticalScrolling() throws Exception {
    Loginpage loginPage = new Loginpage(page);
    FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
    FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
    boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
    try {
        boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        assertTrue(clientActive, "Client should be active");
        loginPage.launchApplication();
        loginPage.verifyGuestHomeVisible();
        loginPage.verifyHomeVerticalScrolling();
    } catch (Throwable e) {
        System.err.println("Test result: " + e.getMessage());
        e.printStackTrace();
        throw e;
    }
}
    // TC: 11 Verify the right and left  arrow Scrolling to the end of the playlist
    // TR_JWP_83
    @Test
    @Epic("Swimlanes")
    @Feature("slider feature")
    @Story("verifyPlaylistRightScroll")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the right arrow scrolling to the end of the playlist")
    public void verifyPlaylistRightScrollToEnd() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifyLiveCheckNextArrowVisible();
            loginPage.clickRightArrowUntilEnd();
            loginPage.verifyLiveCheckNextArrowHidden();
            loginPage.verifyLiveCheckPreviousArrowVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 11 Verify the right and left  arrow Scrolling to the end of the playlist
    // TR_JWP_83
    @Test
    @Epic("Swimlanes")
    @Feature("slider feature")
    @Story("verifyPlaylistLeftScroll")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the left arrow scrolling on the Live check playlist")
    public void verifyPlaylistLeftArrowScroll() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
        boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        assertTrue(clientActive, "Client should be active");
        loginPage.launchApplication();
        loginPage.verifyGuestHomeVisible();
        loginPage.verifyLiveCheckSectionVisible();
        loginPage.verifyLiveCheckPreviousArrowHidden();
        loginPage.verifyLiveCheckNextArrowVisible();
        loginPage.clickRightArrowUntilEnd();
        loginPage.verifyLiveCheckPreviousArrowVisible();
        loginPage.verifyLiveCheckNextArrowHidden();
        loginPage.clickLeftArrowUntilStart();
        loginPage.verifyLiveCheckPreviousArrowHidden();
        loginPage.verifyLiveCheckNextArrowVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 12 Verify "See More" button is displayed for swimlanes with more than the configured content limit
    // TR_JWP_102
    @Test
    @Epic("Home Page")
    @Feature("See More")
    @Story("Verify See More button for swimlanes exceeding content limit")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the See More button is displayed for swimlanes ")
    public void verifySeeMoreButtonForSwimlane() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifyRandomTwoSeeMoreSwimlanes();

        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 13 Verify Start Watching and Watch Trailer CTA
    // DETAILS-003
    @Test()
    @Epic("Content Details")
    @Feature("CTA")
    @Story("trailer")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the appropriate playback CTA, such as Start Watching or Watch Trailer, is displayed based on content type and entitlement")
    public void verifyStartWatchingAndWatchTrailerCTA() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");

        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(isClientActive, "Client should be active in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Avengers");
            page.waitForTimeout(10_000);
            loginPage.clickContent("Avengers");
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.returnToHomePage();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Demon Slayer");
            page.waitForTimeout(10_000);
            loginPage.clickContent("Demon Slayer");
            loginPage.verifyWatchTrailerButtonVisible();
            loginPage.returnToHomePage();
            loginPage.verifyGuestHomeVisible();
   } catch (Throwable e) {

       System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 14 Validate guest cannot add content to Favorites
    // TR_JWP_119
    @Test
    @Epic("Favorites")
    @Feature("Guest Mode")
    @Story("verifyAddToFavorites")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate guest cannot add content to Favorites")
    public void verifyGuestCannotAddToFavorites() throws Exception {
        try {
            Loginpage loginPage = new Loginpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            loginPage.verifyFavoriteButtonVisible();
            loginPage.clickFavorite();
            loginPage.verifySignUpPopupVisible();
            loginPage.closeSignUpPopup();
            loginPage.verifySignUpPopupHidden();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }


    }
    // TC: 15  Validate Favorites button not visible for guest when auth disabled
    // TR_JWP_120
    @Test
    @Epic("Favorites")
    @Feature("Guest Mode")
    @Story("Validate Favorites button appears")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate Favorites button visibility for guest users")
    public void verifyFavoriteButtonForGuest() throws Exception {

            Loginpage loginPage = new Loginpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean originalFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
        try {
            boolean isClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean isFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled");
            assertTrue(isFavoriteEnabled, "Favorite feature should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            if (!isAuthenticationEnabled) {
                loginPage.verifyFavoriteButtonHidden();

            } else {
                if (isFavoriteEnabled) {

                    loginPage.verifyFavoriteButtonVisible();

                } else {
                    loginPage.verifyFavoriteButtonHidden();
                }
            }
            firebase.updateAuthenticationAndFavorite(false, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate disabledTemplate = firebase.getRemoteConfig();
            boolean authenticationDisabled = firebase.getAutomationBoolean(disabledTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean favoriteDisabled = firebase.getAutomationBoolean(disabledTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertFalse(authenticationDisabled, "Authentication should be disabled");
            assertFalse(favoriteDisabled, "Favorite should be disabled");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyFavoriteButtonHidden();
        } finally {
            try {
                firebase.updateAuthenticationAndFavorite(originalAuthenticationEnabled, originalFavoriteEnabled);

            }  catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    // TC: 16 Validate Favorites button appears when is_favorite_enabled is true and logged-in user can add content to Favorites
    // TR_JWP_127
    @Test
    @Epic("Favourites")
    @Feature("Guest Mode")
    @Story("Verify Favorite button is hidden")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate Favorites button does not appear when is_favorite_enabled is false")
    public void verifyFavoriteButtonHiddenWhenDisabled() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
        try {
            boolean isClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean isFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled");
            assertTrue(isFavoriteEnabled, "Favorite feature should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            if (!isAuthenticationEnabled) {
                loginPage.verifyFavoriteButtonHidden();

            } else {
                if (isFavoriteEnabled) {

                    loginPage.verifyFavoriteButtonVisible();

                } else {
                    loginPage.verifyFavoriteButtonHidden();
                }
            }
            firebase.updateAuthenticationAndFavorite(false, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate disabledTemplate = firebase.getRemoteConfig();
            boolean authenticationDisabled = firebase.getAutomationBoolean(disabledTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean favoriteDisabled = firebase.getAutomationBoolean(disabledTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertFalse(authenticationDisabled, "Authentication should be disabled");
            assertFalse(favoriteDisabled, "Favorite should be disabled");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyFavoriteButtonHidden();
        } finally {
            try {
                firebase.updateAuthenticationAndFavorite(originalAuthenticationEnabled, originalFavoriteEnabled);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }
     // extra not in test case
//    @Test
//    @Epic("Favourites")
//    @Feature("Favorites")
//    @Story("Add Content to Favorites")
//    @Severity(SeverityLevel.CRITICAL)
//    @Description("Validate that user can add content to Favorites")
//    public void verifyContentCanBeAddedToFavorites() throws Exception {
//        Loginpage loginPage = new Loginpage(page);
//        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
//        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
//      try {
//            boolean isClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
//            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
//            boolean isFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
//            assertTrue(isClientActive, "Client should be active");
//            assertTrue(isAuthenticationEnabled, "Authentication should be enabled");
//            assertTrue(isFavoriteEnabled, "Favorite feature should be enabled");
//            loginPage.launchApplication();
//            loginPage.verifyGuestHomeVisible();
//            loginPage.clickSignIn();
//            loginPage.enterUsername(ConfigReader.getUsername());
//            loginPage.enterPassword(ConfigReader.getPassword());
//            loginPage.clickSubmit();
//            loginPage.verifySomeScreenVisible();
//            loginPage.clickSomeScreen();
//            loginPage.verifyFavoriteButtonVisible();
//            loginPage.clickFavorite();
//            loginPage.verifyFavoriteButtonHighlighted();
//            loginPage.verifyFavoritesIconVisible();
//            loginPage.clickFavoritesIcon();
//            loginPage.verifyMyFavoritesPageDisplayed();
//            loginPage.verifyFavoritedContentDisplayed("Marvel Studios Avengers Endgame");
//      }
//      catch (Exception e) {
//          System.err.println("Test failed: " + e.getMessage());
//          e.printStackTrace();
//
//          throw e;
//      }
//     }
    // TC: 17 Validate Favorites button does not appear when is_favorite_enabled is false
    // TR_JWP_128
    @Test
    @Epic("Favourites")
    @Feature("Guest Mode")
    @Story("Validate Favorites button not visible when Guest Mode is disabled")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that the Favorites button is not displayed for guest users when is_guest_mode_enabled is false")
    public void verifyFavoriteButtonHiddenForGuestWhenDisabled() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            assertTrue(originalClientActive, "Client should be active in Firebase");
            firebase.updateAuthenticationEnabled(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean updatedAuthentication = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            assertFalse(updatedAuthentication, "Authentication should be disabled in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickSomeScreen();
            assertThat(loginPage.getStartWatchingButton()).isVisible();
            loginPage.verifyFavoriteButtonHidden();

        } finally {
            try {
                firebase.updateAuthenticationEnabled(originalAuthenticationEnabled);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }
    // TC: 18 Validate Favorites swimlane appears when enabled in JW Dashboard
    // TR_JWP_132
    @Test
    @Epic("Favourites")
    @Feature("Logged In Mode")
    @Story("Validate Favorites swimlane appears when enabled")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that the Favorites swimlane is displayed on the Home Page ")
    public void verifyFavoritesSwimlaneWhenEnabled() throws Exception {
          Loginpage loginPage = new Loginpage(page);
          FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
          FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
        try {
              boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
              boolean favoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
              assertTrue(clientActive, "is_client_active should be true");
              assertTrue(favoriteEnabled, "is_favorite_enabled should be true");
              loginPage.launchApplication();
              loginPage.verifyGuestHomeVisible();
              loginPage.verifySignInButtonVisible();
              loginPage.clickSignIn();
              loginPage.verifySignInPopupVisible();
              loginPage.enterUsername(ConfigReader.getUsername());
              loginPage.enterPassword(ConfigReader.getPassword());
              loginPage.clickSubmit();
              loginPage.verifyProfileIconVisible();
              loginPage.verifyZooScreenVisible();
              loginPage.clickzooScreen();
              loginPage.clickFavorite();
              loginPage.returnToHomePage();
              loginPage.verifyGuestHomeVisible();
              page.waitForTimeout(20_000);
              loginPage.verifyFavoritesSwimlaneVisible();
              loginPage.verifyFavoritesContentVisible();
              loginPage.clickFavoritesIcon();
              loginPage.clickClearFavourites();
        } catch (Throwable e) {
              System.err.println("Test result: " + e.getMessage());
              e.printStackTrace();
              throw e;
          }
    }
    // TC: 19  Verify Sign Out works from Profile pop-up
    // TR_JWP_140
    @Test
    @Epic("Home Page")
    @Feature("Profile")
    @Story("Verify Sign Out works from Profile pop-up")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that Sign Out logs out the user from the web profile")
    public void verifySignOutFromProfile() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(updatedTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertTrue(clientActive, "Client should be active");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickSignIn();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            loginPage.verifySignOutVisible();
            loginPage.clickSignOut();
            loginPage.verifySignOutPanelVisible();
            loginPage.verifySignOutConfirmationMessage();
            loginPage.verifySignOutNoButtonVisible();
            loginPage.clickSignOutNo();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyProfileIconVisible();
            loginPage.clickProfileIcon();
            loginPage.verifySignOutVisible();
            loginPage.clickSignOut();
            loginPage.verifySignOutPanelVisible();
            loginPage.verifySignOutYesButtonVisible();
            loginPage.clickSignOutYes();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifyProfileIconHidden();

        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 20 Verify API error response for invalid Current Password
    // TR_JWP_190
    @Test
    @Epic("Account")
    @Feature("Change Password")
    @Description("Verify API error response for invalid Current Password")
    @Story("Change Password")
    @Severity(SeverityLevel.CRITICAL)
    public void verifyInvalidLoginPasswordApiError() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertTrue(clientActive, "Client should be active");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickSignIn();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterWrongpassword(ConfigReader.getWrongpassword());
            loginPage.VerifyInvalidLogin();
            loginPage.verifyLoginErrorMessage();
        }  catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }
    // TC : 21  Verify Delete Account option availability based on Firebase config
    // TR_JWP_192
    @Test
    @Epic("Account")
    @Feature("Delete Account")
    @Story("Verify Delete Account option based on Firebase configuration")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate Delete Account option visibility based on platforms.delete_account Firebase configuration")
    public void verifyDeleteAccountBasedOnFirebaseConfig() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        boolean originalDeleteAccount = firebase.getStagingDeleteAccount();
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            boolean deleteAccountEnabled = firebase.getStagingDeleteAccount();
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            assertTrue(deleteAccountEnabled, "delete_account should be true in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickSignIn();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.checkLoginError();
            assertThat(loginPage.getSignInButton()).isHidden();
            assertThat(loginPage.getProfileIcon()).isVisible();
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            loginPage.verifyDeleteAccountVisible();
            firebase.updateDeleteAccount(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate disabledTemplate = firebase.getRemoteConfig();
            boolean deleteAccountDisabled = firebase.getStagingDeleteAccount();
            assertFalse(deleteAccountDisabled, "delete_account should be false in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            page.waitForTimeout(30_000);
            loginPage.verifyDeleteAccountHidden();
        } finally {
            try {
                firebase.updateDeleteAccount(originalDeleteAccount);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    }

    // TC : 22 Verify selecting a Series opens Series Details
// SERIES-002
    @Test()
    @Epic("Series")
    @Feature("Series Details")
    @Story("Series Details Navigation")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that selecting a Series content item opens the correct Series Details page with the corresponding Series information")
    public void verifySelectingSeriesOpensSeriesDetails() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
            // Verify client is active
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(isClientActive, "Client should be active in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Avengers");
            loginPage.clickContent("Avengers");
            loginPage.verifyHeroImageVisible();
            loginPage.verifyTitleVisible();
            loginPage.verifyDescriptionVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            loginPage.returnToHomePage();
        } catch (Throwable e) {

            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    // TC : 23  Verify Back button returns to previous page
    // TR_JWP_232
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Epic("Details Page")
    @Feature("Series Details Page")
    @Story("Back button navigation")
    @Description("Validate that the Back button on the Series Details page ")
    public void verifyBackButtonNavigationFromSeriesDetails() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestEnabled, "is_guest_mode_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            firebase.updateGuestModeEnabled(false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate guestDisabledTemplate = firebase.getRemoteConfig();
            boolean updatedGuestMode = firebase.getAutomationBoolean(guestDisabledTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertFalse(updatedGuestMode, "Guest mode should be disabled");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifySignInButtonVisible();
            loginPage.verifySignUpButtonVisible();
            loginPage.scrollToMoviesSection();
            loginPage.findMovieInMoviesCarousel("Demon Slayer Ep - 01");
            loginPage.clickContentByName("Demon Slayer Ep - 01");
            loginPage.clickStartWatching();
            loginPage.verifySignUpPopupVisible();
            loginPage.closeSignUpPopup();
            loginPage.verifySignUpPopupHidden();
            firebase.updateGuestModeAndAuthentication(true, false);
            FirebaseRemoteConfigClient.RemoteConfigTemplate Template = firebase.getRemoteConfig();
            boolean updatedAuthentication = firebase.getAutomationBoolean(Template.getJson(), "authentication.is_authentication_enabled");
            assertFalse(updatedAuthentication, "Authentication should be disabled in Firebase");
            page.reload();
            page.waitForLoadState(LoadState.DOMCONTENTLOADED);
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            loginPage.returnToHomePage();
        } finally {
            try {
                firebase.updateGuestModeEnabled(guestModeEnabled);
                firebase.updateAuthenticationEnabled(originalAuthenticationEnabled);
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }

        }
    }
    // Tc : 24 Verify episode details page displays correctly for Logged-in users
    // TR_JWP_269
    @Test()
    @Epic("Details Page")
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Episode Details Page")
    @Story("Logged-in user episode details")
    @Description("Verify that logged-in users can view all episode details ")
    public void verifyEpisodeDetailsPageForLoggedInUser() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            boolean isFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            assertTrue(isFavoriteEnabled, "Favorite feature should be enabled");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
            loginPage.verifySomeScreenVisible();
            loginPage.clickSomeScreen();
            loginPage.verifyDuration();
            loginPage.verifyBackButtonVisible();
            loginPage.returnToHomePage();
            page.waitForTimeout(30_000);
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Avengers");
            loginPage.clickContent("Avengers");
            loginPage.verifyHeroImageVisible();
            loginPage.verifyTitleVisible();
            loginPage.verifyDescriptionVisible();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.verifyFavoriteButtonVisible();
            loginPage.verifyShareButtonVisibleAndEnabled();
        }catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC: 25 Check that Guest user can view all episode details
    // TR_JWP_270
    @Test
    @Epic("Details Page")
    @Feature("Episode Details Page")
    @Story("Check that Guest user can view all episode details")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that all required elements are visible and clickable on the Episode Details page for a Guest user")
    public void verifyGuestCanViewAllEpisodeDetails() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalFavoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean favoriteEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_favorite_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            assertTrue(favoriteEnabled, "is_favorite_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickzooScreen();
            // Verify all required elements
            loginPage.verifyBackButtonVisible();
            loginPage.verifyHeroImageVisible();
            loginPage.verifyTitleVisible();
//            loginPage.verifyDurationVisible();
            loginPage.verifyDescriptionVisible();
            page.waitForTimeout(30_000);
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.verifyFavoriteButtonVisible();
            loginPage.verifyShareButtonVisibleAndEnabled();
            // Verify console errors
            assertNoConsoleErrors();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 26 Verify that guest users can share content from details page and open the shared link on another device
    // TR_JWP_283
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Epic("Details Page")
    @Feature("Share Feature")
    @Story("Guest user can share content from details page")
    @Description("Verify that a guest user can share content from the details page ")
    public void verifyGuestUserCanShareContent() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Avengers");
            loginPage.clickContent("Avengers");
            loginPage.verifyShareButtonVisibleAndEnabled();
            loginPage.clickShareButton();
            page.waitForTimeout(20_000);
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    // TC : 27  Validate More option visibility when description exceeds 3 lines
    // TR_JWP_289
    @Test
    @Epic("Details Page")
    @Feature("More")
    @Story("Verify More option for long description")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that More option is displayed when the description exceeds 3 lines")
    public void verifyMoreOptionForLongDescription() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "is_client_active should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.clickzooScreen();
            loginPage.verifyTitleVisible();
            loginPage.verifyDescriptionVisible();
            loginPage.verifyMoreOptionVisible();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 28 Verify the presence of social media buttons (Facebook, Twitter, Instagram, etc.) below the metadata
    //  TR_JWP_311
    @Test
    @Epic("MovieDetailsPage")
    @Feature("MovieDetails")
    @Story("Verify social media sharing options")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify social media buttons are displayed when sharing an asset")
    public void verifySocialMediaSharing() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToMoviesSection();
            loginPage.findMovieInMoviesCarousel("Demon Slayer Ep - 01");
            loginPage.clickContentByName("Demon Slayer Ep - 01");
            loginPage.verifyShareButtonVisibleAndEnabled();
            loginPage.clickShareButton();
            page.waitForTimeout(20_000);
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Smoke Testing TC : 01
    @Test
    @Epic("Authentication")
    @Feature("Sign Up")
    @Story("Sign Up using valid input")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a user can successfully create an account when all required registration details are valid.")
    public void VerifythatanewusercancompleteSignUpusingvalidinput() throws Exception {
            Loginpage loginPage = new Loginpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
            try {
                FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
                boolean clientActive = firebase.getAutomationBoolean(updatedTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
                String authenticationType = firebase.getStagingString("common.authentication_type");
                assertTrue(authenticationEnabled, "Authentication should be enabled");
                assertEquals(authenticationType,
                        "inplayer",
                        "Authentication type was not updated correctly"
                );
                loginPage.launchApplication();
                loginPage.verifyGuestHomeVisible();
                loginPage.verifySignUpButtonVisible();
                loginPage.clickSignUp();
                loginPage.verifySignUpPopupVisible();
                loginPage.enterFirstName(ConfigReader.getFirstNameField());
                loginPage.enterLastName(ConfigReader.getLastNameField());
                loginPage.enterEmail(ConfigReader.getUsername());
                loginPage.enterPassword(ConfigReader.getPassword());
                loginPage.enterConfirmPassword(ConfigReader.getPassword());
                loginPage.ClickTermsandPrivacycheckbox();
                loginPage.verifyTermsPrivacyCheckboxChecked();
                loginPage.clickSignUpSubmit();
                loginPage.verifySignUpButtonHidden();
                loginPage.verifyProfileIconVisible();
                loginPage.clickProfileIcon();
                loginPage.verifyGuestHomeVisible();

        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // Smoke Testing TC : 02
    @Test
    @Epic("Branding")
    @Feature("Client Assets & Branding")
    @Story("Verify client branding and assets")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that configured client assets and branding are displayed correctly across the application.")
    public void Verifyclientassetsandbrandingaredisplayedcorrectly() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalAuthenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate updatedTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(updatedTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(updatedTemplate.getJson(), "authentication.is_authentication_enabled");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertTrue(authenticationEnabled, "Authentication should be enabled");
            assertEquals(authenticationType,
                    "inplayer",
                    "Authentication type was not updated correctly"
            );
            loginPage.launchApplication();
            loginPage.launchApplication();
            page.waitForTimeout(10_000);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifyClientLogoVisible();
            loginPage.verifyTringPlayBranding();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // Smoke Testing TC : 03
    @Test
    @Epic("Navigation")
    @Feature("Section Navigation")
    @Story("Verify navigation between available sections")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Home and dynamically configured menu sections " + "contain content")
    public void verifySectionNavigation() throws Exception {

            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                page.waitForTimeout(5000);
                loginPage.verifyGuestHomeVisible();
                hubPage.verifyHomeContent();
                List<String> menuLinks = hubPage.getAllMenuNames();
                assertTrue(!menuLinks.isEmpty(), "No menu sections found");
                for (String menuLink : menuLinks) {
                    Allure.step("Testing menu: " + menuLink);
                    if (menuLink.equals("/")
                            || menuLink.equals("/home")) {
                        continue;
                    }
                    hubPage.clickMenu(menuLink);
                    hubPage.verifyCurrentMenuContent();
                    hubPage.clickHome();
                    hubPage.verifyHomeAfterNavigation();

                }


        } catch (Throwable e) {

            e.printStackTrace();

            throw e;
        }
    }

    // Smoke Testing TC : 04
    @Test
    @Epic("Home")
    @Feature("Home Screen")
    @Story("Verify that the Home screen loads successfully")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the Home screen loads its configured content and primary UI elements without errors.")
    public void verifyHomeScreenLoadsSuccessfully() throws Exception {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            loginPage.launchApplication();
            page.waitForTimeout(5000);
            loginPage.verifyGuestHomeVisible();
            hubPage.verifyAllHomeSections();
            hubPage.scrollHomePageToFooter();
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw e;}
}
    // Smoke Testing TC : 05
    @Test
    @Epic("Home")
    @Feature("Banner & Thumbnail Navigation")
    @Story("Verify that banners and thumbnails open the correct destination")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that selectable banners and media thumbnails navigate to the correct configured destinationand that the destination content matches the selected item.")
    public void verifyBannerAndThumbnailNavigation() throws Exception {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            loginPage.launchApplication();
            page.waitForTimeout(5000);
            loginPage.verifyGuestHomeVisible();
            hubPage.verifyAllHomeSections();
            hubPage.verifyContentNavigation();
        }
        catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }
    // Smoke Testing TC : 06
    @Test
    @Epic("Playback")
    @Feature("Playback Controls")
    @Story("Verify that video playback controls are responsive and functional")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that core playback controls perform their expected actions")
    public void verifyPlaybackControlsAreResponsive() throws Exception {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                page.waitForTimeout(5000);
                loginPage.verifyGuestHomeVisible();
                hubPage.verifyAllHomeSections();
                hubPage.scrollToTrailersSection();
                hubPage.verifyPawfectMomentDisplayed();
                hubPage.clickPawfectMoment();
                loginPage.verifyStartWatchingButtonVisible();
                loginPage.clickStartWatching();
                page.waitForTimeout(2000);
                assertTrue(hubPage.videoPlayer().isVisible(), "Video player is not visible");
                page.waitForTimeout(1000);
                hubPage.pauseVideoPlayback();
                hubPage.verifyVideoIsPaused();
                hubPage.resumeVideoPlayback();
                hubPage.verifyVideoIsPlaying();
                double beforeForward = hubPage.getCurrentPlaybackPosition();
                hubPage.clickForward();
                hubPage.verifyForward(beforeForward);
                double beforeRewind = hubPage.getCurrentPlaybackPosition();
                hubPage.clickRewind();
                hubPage.verifyRewind(beforeRewind);
                hubPage.muteVideo();
                hubPage.verifyVideoIsMuted();
                hubPage.unmuteVideo();
                hubPage.verifyVideoIsUnmuted();
                hubPage.enterFullscreen();
                hubPage.verifyFullscreenEnabled();
                hubPage.exitFullscreen();
                hubPage.verifyFullscreenDisabled();
//            hubPage.verifyVideoIsPlaying();
                loginPage.verifyStartWatchingButtonVisible();
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
        }
    }
    // Smoke Testing TC : 07
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Series Feature")
    @Story("Guest user can view episode information from series details page")
    @Description("Verify that episode cards display season/episode information, duration, thumbnail, and play icon correctly")
    public void verifyEpisodeCardDetailsForSeries() throws IOException, InterruptedException {
            Loginpage loginPage = new Loginpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                page.waitForTimeout(10_000);
                loginPage.verifyGuestHomeVisible();
                loginPage.scrollToSection();
                loginPage.findMovieInMoviesCarousel("Avengers");
                loginPage.clickContent("Avengers");
                loginPage.scrollToseries();
                loginPage.verifyEpisodesSectionVisible();
                loginPage.verifyEpisodeCardsDisplayed();
                loginPage.verifyEpisodeThumbnailsDisplayed();
                loginPage.verifySeasonAndEpisodeInformation();
            } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
            }
    }
    // Smoke Testing TC : 08
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Series Feature")
    @Story("Guest user can play an episode from the series details page")
    @Description("Verify that selecting an episode starts playback of the correct video")
    public void verifyEpisodePlayback() throws IOException, InterruptedException {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");

            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                loginPage.verifyGuestHomeVisible();
                loginPage.scrollToSection();
                loginPage.findMovieInMoviesCarousel("Avengers");
                page.waitForTimeout(20_000);
                loginPage.clickContent("Avengers");
                loginPage.scrollToseries();
                loginPage.verifyEpisodesSectionVisible();
                loginPage.verifyEpisodeCardsDisplayed();
                loginPage.clickEpisode();
                page.waitForTimeout(10_000);
                loginPage.verifySelectedEpisodeIsPlaying();
            } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // Smoke Testing TC : 09
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Featured")
    @Story("Featured carousel automatically advances")
    @Description("Verify that the Featured carousel automatically advances one media card at the configured interval")
    public void verifyFeaturedCarouselAutomaticallyAdvances() throws IOException, InterruptedException {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                page.waitForTimeout(10_000);
                loginPage.verifyGuestHomeVisible();
                loginPage.verifyFeaturedCarouselAutomaticallyAdvances();
                loginPage.verifyFeaturedPaginationAdvances();
            } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


}












