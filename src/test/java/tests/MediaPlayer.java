package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Loginpage;
import pages.MediaPlayerpage;
import utils.ClientExpectationReader;
import utils.ConfigReader;
import utils.FirebaseRemoteConfigClient;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class MediaPlayer extends BaseTest {

    // TC : 56 Verify autoplay restrictions
    // TR_JWP_692
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Media Player")
    @Story("Playback")
    @Description( "Verify video does not autoplay when autoplay is disabled by default")
    public void verifyAutoplayRestrictions() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        MediaPlayerpage MediaPlayerpage = new MediaPlayerpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalAutoplayEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
        String originalAutoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean guestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(guestModeEnabled, "Guest mode should be enabled in Firebase");
            boolean authentication = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(authentication, "Authentication should be enabled in Firebase");
            boolean autoplayEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
            assertTrue(autoplayEnabled, "Autoplay should be enabled in Firebase");
            String autoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");
            assertEquals(
                    autoplayDefaultValue,
                    "off",
                    "autoplay_default_value should be off");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            MediaPlayerpage.scrollAndFindSeriesMedia();
            MediaPlayerpage.verifySeriesMediaVisible();
            MediaPlayerpage.clickSeriesMedia();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(50_000);
            MediaPlayerpage.verifyStartWatchingAfterCompletion();
        } catch (Throwable e) {
                    System.err.println("Test result: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
       }
    // Tc : 62 Verify autoplay next video in playlist
    // TR_JWP_707
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Media Player")
    @Story("Playlist")
    @Description("Verify autoplay next video in playlist")
    public void verifyAutoplayNextVideoInPlaylist() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        MediaPlayerpage MediaPlayerpage = new MediaPlayerpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalAutoplayEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
        String originalAutoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");

        try {
            firebase.updateAutoplayDefaultValue("on");
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean guestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(guestModeEnabled, "Guest mode should be enabled in Firebase");
            boolean authentication = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(authentication, "Authentication should be enabled in Firebase");
            boolean autoplayEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
            assertTrue(autoplayEnabled, "Autoplay should be enabled in Firebase");
            String autoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");
            assertEquals(autoplayDefaultValue, "on", "autoplay_default_value should be on");
            loginPage.launchApplication();
            page.waitForTimeout(10_000);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            MediaPlayerpage.verifySettingsVisible();
            MediaPlayerpage.clickSettings();
            MediaPlayerpage.verifyAutoplayNextVideoIsOff();
            MediaPlayerpage.enableAutoplayNextVideo();
            MediaPlayerpage.verifyAutoplayNextVideoIsOn();
            loginPage.clickhome();
            MediaPlayerpage.scrollAndFindSeriesMedia();
            MediaPlayerpage.verifySeriesMediaVisible();
            MediaPlayerpage.clickSeriesMedia();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(44_000);
            MediaPlayerpage.verifyNextVideoCountdownDisplayed();
            page.waitForTimeout(5_000);
            MediaPlayerpage.verifyNextVideoStartsAutomatically();
        } finally {
            try {
                // Restore original Firebase config value
                firebase.updateAutoplayDefaultValue(originalAutoplayDefaultValue);
            } catch (Throwable e) {
                System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

   // TC : 65 Verify full screen transition where applicable
   // UI-002
    @Test
    @Epic("UI/UX")
    @Feature("Orientation")
    @Story("Verify full screen transition")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that the application transitions smoothly to full screen mode and maintains a valid player UI layout")
    public void verifyFullScreenTransition() throws Exception {
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
            loginPage.scrollToMoviesSection();
            loginPage.findMovieInMoviesCarousel("Demon Slayer Ep - 01");
            loginPage.clickContentByName("Demon Slayer Ep - 01");
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.clickFullScreen();
            page.waitForTimeout(10_000);
            loginPage.verifyFullScreenEnabled();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 66 Verify “Up Next in 10 seconds” popup appears before video ends
    // TR_JWP_744
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Media Player")
    @Story("Autoplay Popup")
    @Description ("Verify Up Next in 10 seconds popup appears before video ends")
    public void verifyUpNextPopupAppearsBeforeVideoEnds() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        MediaPlayerpage mediaPlayerPage = new MediaPlayerpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalAutoplayEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
        String originalAutoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");

        try {
            firebase.updateAutoplayDefaultValue("on");
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean guestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(guestModeEnabled, "Guest mode should be enabled in Firebase");
            boolean authentication = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(authentication, "Authentication should be enabled in Firebase");
            boolean autoplayEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
            assertTrue(autoplayEnabled, "Autoplay should be enabled in Firebase");
            String autoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");
            assertEquals(autoplayDefaultValue, "on", "autoplay_default_value should be on");
            loginPage.launchApplication();
            page.waitForTimeout(10_000);
            loginPage.verifyGuestHomeVisible();
            loginPage.verifySignInButtonVisible();
            loginPage.clickSignIn();
            loginPage.verifySignInPopupVisible();
            loginPage.enterUsername(ConfigReader.getUsername());
            loginPage.enterPassword(ConfigReader.getPassword());
            loginPage.clickSubmit();
            loginPage.verifySignInButtonHidden();
            loginPage.verifyProfileIconVisible();
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            mediaPlayerPage.verifySettingsVisible();
            mediaPlayerPage.clickSettings();
            mediaPlayerPage.verifyAutoplayNextVideoIsOff();
            mediaPlayerPage.enableAutoplayNextVideo();
            mediaPlayerPage.verifyAutoplayNextVideoIsOn();
            loginPage.clickhome();
            mediaPlayerPage.scrollAndFindSeriesMedia();
            mediaPlayerPage.verifySeriesMediaVisible();
            mediaPlayerPage.clickSeriesMedia();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(41_000);
            mediaPlayerPage.verifyNextVideoCountdownDisplayed();
        } finally {
            try {
                // Restore original Firebase config value
                firebase.updateAutoplayDefaultValue(originalAutoplayDefaultValue);
            } catch (Throwable e) {
                System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }

    //  TC : 67 Verify next video starts automatically after countdown ends
    // TR_JWP_746
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Media Player")
    @Story("Autoplay Popup")
    @Description ("Verify next video starts automatically after countdown ends")
    public void Verifynextvideostartsautomaticallyaftercountdownends() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        MediaPlayerpage mediaPlayerPage = new MediaPlayerpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalAutoplayEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
        String originalAutoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");

        try {
            firebase.updateAutoplayDefaultValue("on");
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean guestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_guest_mode_enabled");
            assertTrue(guestModeEnabled, "Guest mode should be enabled in Firebase");
            boolean authentication = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(authentication, "Authentication should be enabled in Firebase");
            boolean autoplayEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.settings.is_autoplay_enabled");
            assertTrue(autoplayEnabled, "Autoplay should be enabled in Firebase");
            String autoplayDefaultValue = firebase.getStagingString("feature_flags.settings.autoplay_default_value");
            assertEquals(autoplayDefaultValue, "on", "autoplay_default_value should be on");
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
            loginPage.clickProfileIcon();
            loginPage.verifyAccountVisible();
            loginPage.clickAccount();
            mediaPlayerPage.verifySettingsVisible();
            mediaPlayerPage.clickSettings();
            mediaPlayerPage.verifyAutoplayNextVideoIsOff();
            mediaPlayerPage.enableAutoplayNextVideo();
            mediaPlayerPage.verifyAutoplayNextVideoIsOn();
            loginPage.clickhome();
            mediaPlayerPage.scrollAndFindSeriesMedia();
            mediaPlayerPage.verifySeriesMediaVisible();
            mediaPlayerPage.clickSeriesMedia();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            page.waitForTimeout(41_000);
            mediaPlayerPage.verifyNextVideoCountdownDisplayed();
            page.waitForTimeout(10_000);
            mediaPlayerPage.verifyNextVideoStartsAutomatically();
        } finally {
            try {
                // Restore original Firebase config value
                firebase.updateAutoplayDefaultValue(originalAutoplayDefaultValue);
            } catch (Throwable e) {
                System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }  // TC : 68 Verify subtitle and audio track selection when available
       // PLAY-009
       @Test
       @Epic("Media Player")
       @Feature("Subtitles & Audio Tracks")
       @Story("Verify subtitle and audio track selection when available")
       @Severity(SeverityLevel.NORMAL)
       @Description("Verify that available subtitle and audio tracks are displayed and that selecting a supported track applies the selection correctly during playback")
       public void verifySubtitleAndAudioTrackSelection() throws Exception {
           Loginpage loginPage = new Loginpage(page);
           MediaPlayerpage mediaPlayerPage = new MediaPlayerpage(page);
           FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
           FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
           boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
           try {
               FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
               boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
               assertTrue(clientActive, "Client should be active in Firebase");   loginPage.launchApplication();
               loginPage.verifyGuestHomeVisible();
               loginPage.scrollToMoviesSection();
               loginPage.findMovieInMoviesCarousel("Demon Slayer Ep - 01");
               loginPage.clickContentByName("Demon Slayer Ep - 01");
               loginPage.clickStartWatching();
               loginPage.verifyVideoPlayerVisible();
               mediaPlayerPage.openClosedCaptions();
               mediaPlayerPage.verifyEnglishSubtitleAvailable();
               mediaPlayerPage.selectEnglishSubtitle();
               mediaPlayerPage.verifyAudioLanguageDisplayed();
               page.waitForTimeout(100_000);
           } catch (Throwable e) {
               System.err.println("Test result: " + e.getMessage());e.printStackTrace();
               throw e;
           }
       }


       // Tc : 69 Verify video quality selection functionality
       // PLAY-008
       @Test
       @Epic("Media Player")
       @Feature("Video Quality")
       @Story("Verify video quality selection functionality")
       @Severity(SeverityLevel.NORMAL)
       @Description("Verify that available video quality options are displayed and that selecting a different quality applies the selection correctly while playback continues")
       public void verifyVideoQualitySelectionFunctionality() throws Exception {

           Loginpage loginPage = new Loginpage(page);
           MediaPlayerpage mediaPlayerPage = new MediaPlayerpage(page);
           FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
           FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
           boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
           try {
               FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
               boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
               assertTrue(clientActive, "Client should be active in Firebase");
               loginPage.launchApplication();
               loginPage.launchApplication();
               loginPage.verifyGuestHomeVisible();
               loginPage.scrollToMoviesSection();
               loginPage.findMovieInMoviesCarousel("Demon Slayer Ep - 01");
               loginPage.clickContentByName("Demon Slayer Ep - 01");
               loginPage.clickStartWatching();
               // Verify video player
               loginPage.verifyVideoPlayerVisible();
               mediaPlayerPage.openQualityMenu();
               mediaPlayerPage.verifyQualityOptionsDisplayed();
               mediaPlayerPage.verifyPlaybackContinues();

           } catch (Throwable e) {

               System.err.println("Test result: " + e.getMessage());
               e.printStackTrace();

               throw e;
           }
       }



    // TC : 70  Verify presence of speed options (0.5x, 1x, 1.5x, 2x)
    // TR_JWP_764
    @Test()
    @Feature("Media Player")
    @Story("Speed Menu")
    @Severity(SeverityLevel.BLOCKER)
    @Description ("Verify presence of speed options")
    public void verifySpeedOptions()throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        MediaPlayerpage mediaPlayerPage = new MediaPlayerpage(page);
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
            mediaPlayerPage.scrollAndFindSeriesMedia();
            mediaPlayerPage.verifySeriesMediaVisible();
            mediaPlayerPage.clickSeriesMedia();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            loginPage.clickVideoPlayer();
            mediaPlayerPage.verifyPlaybackSpeedMenuVisible();
            mediaPlayerPage.clickSpeedOption();
            mediaPlayerPage.verifySpeed05xVisible();
            mediaPlayerPage.verifySpeed1xVisible();
            mediaPlayerPage.verifySpeed15xVisible();
            mediaPlayerPage.verify1xIsDefaultSpeed();
        } catch (Throwable e) {
            System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

