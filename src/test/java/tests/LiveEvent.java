package tests;

import base.BaseTest;
import com.microsoft.playwright.options.LoadState;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Hubpage;
import pages.LiveEventPage;
import pages.Loginpage;
import pages.RecommendRelatedcontentpages;
import utils.FirebaseRemoteConfigClient;

import static org.testng.Assert.assertTrue;

public class LiveEvent extends BaseTest {

    // TC : 30 Verify scheduled card shows "Scheduled" tag and start date & time
    // TR_JWP_324
    @Test(groups = "liveEvent")
    @Epic("Home Page")
    @Feature("Live Event")
    @Story("Scheduled Event Card Verification")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that a currently streaming scheduled event displays correctly in swimlane with scheduled tag and title")
    public void VerifyscheduledCardDisplaysTagAndTitle() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            loginPage.launchJWApplication();
            hubPage.login();
            liveEventPage.verifyBroadcastLiveLinkVisible();
            liveEventPage.clickBroadcastLive();
            liveEventPage.verifyCreateLiveStreamButtonVisible();
            liveEventPage.clickCreateLiveStream();
            liveEventPage.verifyLiveEventRadioButtonVisible();
            liveEventPage.clickLiveEventRadioButton();
            liveEventPage.verifyLiveEventRadioButtonIsChecked();
            liveEventPage.verifyNameInputFieldVisible();
            liveEventPage.enterRandomEventName();
           liveEventPage.openIngestFormatDropdown();
            page.waitForTimeout(5_000);
            liveEventPage.selectIngestFormatSRT();
            liveEventPage.verifyAdvancedSettingsVisible();
            liveEventPage.clickAdvancedSettingsChevron();
            liveEventPage.verifyContentTypeDropdownVisible();
            page.waitForTimeout(10_000);
            liveEventPage.selectContentTypeLiveEvent();
            page.waitForTimeout(30_000);
            liveEventPage.clickConfirmCreateBroadcastButton();
//            page.onDialog(dialog -> {
//                System.out.println("Dialog message: " + dialog.message());
//                dialog.accept();
//            });
//            liveEventPage.verifyStreamIdVisible();
//            String streamId = liveEventPage.getAndVerifyStreamId(); // Stores value in variable
//            liveEventPage.verifyPlaylistsLinkVisible();
//            liveEventPage.verifyNewPlaylistButtonVisible();
//            liveEventPage.clickNewPlaylistButton();
//            liveEventPage.verifyAddMediaButtonVisible();
//            liveEventPage.clickAddMediaButton();
//            liveEventPage.addMediaToTopOfPlaylist(streamId);
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(180_000);
            page.reload();
            liveEventPage.scrollToLive();
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifyscheduledtag();
            liveEventPage.VerifyTimeschedule();
        } catch (Throwable e) {
            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 29 Verify live card shows "Live" tag and title under thumbnail
    // TR_JWP_323
    @Test(groups = "liveEvent",dependsOnMethods = "verifyScheduledButtonSwitchesToCountdown", alwaysRun = true)
    @Epic("Home Page")
    @Feature("Live Event")
    @Story("Live Event Card Verification")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that a currently streaming live event displays correctly in swimlane with Live tag and title")
    public void verifyLiveCardDisplaysTagAndTitle() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(10_000);
            liveEventPage.scrollToLive();
            page.waitForTimeout(260_000);
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifylivetag();
            liveEventPage.VerifyTimeschedule();
        } catch (Throwable e) {
            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 31 Verify scheduled button switches to countdown within 5 minutes
// TR_JWP_332
    @Test(groups = "liveEvent",dependsOnMethods = "VerifyscheduledCardDisplaysTagAndTitle", alwaysRun = true)
    @Epic("Details Page")
    @Feature("Live Event")
    @Story("Scheduled Event Countdown")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that when a scheduled live event is less than 5 minutes from its start time, the Scheduled button changes to a live countdown label in MM:SS format")
    public void verifyScheduledButtonSwitchesToCountdown() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            liveEventPage.scrollToLive();
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifyscheduledtag();
            page.waitForTimeout(360_000);
            liveEventPage.clickLiveContent();
            liveEventPage.verifyStreamStartTimer();
        } catch (Throwable e) {
            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 32 Verify Watch Live launches live player and starts playback
    // TR_JWP_336
    @Test()
    @Epic("Details Page")
    @Feature("Live Event")
    @Story("Watch Live launches player and starts playback")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Watch Live launches the live player and starts playback for a currently streaming live event")
    public void verifyWatchLiveLaunchesLivePlayerAndStartsPlayback() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        Process ffmpegProcess = null;

        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            page.waitForTimeout(30_000);
            loginPage.launchJWApplication();
            page.waitForTimeout(30_000);
            hubPage.login();
            page.waitForTimeout(10_000);
            liveEventPage.verifyBroadcastLiveLinkVisible();
            liveEventPage.clickBroadcastLive();
            page.waitForTimeout(10_000);
            liveEventPage.verifyCreateLiveStreamButtonVisible();
            liveEventPage.clickCreateLiveStream();
            liveEventPage.verifyLiveEventRadioButtonVisible();
            liveEventPage.clickLiveEventRadioButton();
            liveEventPage.verifyLiveEventRadioButtonIsChecked();
            liveEventPage.verifyNameInputFieldVisible();
            liveEventPage.enterRandomEventName();
            liveEventPage.openIngestFormatDropdown();
            liveEventPage.selectIngestFormatSRT();
            liveEventPage.verifyAdvancedSettingsVisible();
            liveEventPage.clickAdvancedSettingsChevron();
            liveEventPage.verifyContentTypeDropdownVisible();
            liveEventPage.selectContentTypeLiveEvent();
            page.waitForTimeout(30_000);
            liveEventPage.clickConfirmCreateBroadcastButton();
            // The Stream ID is different for every execution.
            // We read it automatically from the page.
            liveEventPage.verifyStreamIdVisible();
            String streamId = liveEventPage.getAndVerifyStreamId();
            String srtUrl = liveEventPage.getAndVerifyStreamUrlFromClipboard();
            page.waitForTimeout(120_000);
            ffmpegProcess = liveEventPage.startFFmpeg(srtUrl);
            page.waitForTimeout(10_000);
            if (!ffmpegProcess.isAlive()) {
                throw new RuntimeException(
                        "FFmpeg stopped. SRT streaming failed. Check the FFmpeg log above."
                );
            }
            page.waitForTimeout(30_000);
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(180_000);
            page.reload();
            liveEventPage.scrollToLive();
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifyscheduledtag();
            page.waitForTimeout(360_000);
            liveEventPage.clickLiveContent();
            liveEventPage.verifyStreamStartTimer();
            page.waitForTimeout(90_000);
            liveEventPage.verifyWatchLiveButtonVisible();
            liveEventPage.clickWatchLive();
            page.waitForTimeout(12_000);
            loginPage.verifyVideoPlayerVisible();
            System.out.println("Live video playback started");
        } catch (Throwable e) {
            System.err.println("Test result failure: " + e.getMessage());
            e.printStackTrace();
            throw e;

        } finally {

            if (ffmpegProcess != null && ffmpegProcess.isAlive()) {

                System.out.println("Stopping FFmpeg...");

                ffmpegProcess.destroy();

                if (ffmpegProcess.isAlive()) {
                    ffmpegProcess.destroyForcibly();
                }

                System.out.println("FFmpeg stopped");
            }


        }
    }
    // TC : 33
    // Verify Start Watching button transitions during live event
    // TR_JWP_374
    @Test()
    @Epic("Details Page")
    @Feature("Live Event")
    @Story("Verify Scheduled tag, countdown, and Watch Live button transition")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify the Scheduled tag and countdown before the live event starts, " +
            "and validate that the Watch Live button appears when the countdown completes")
    public void verifyStartWatchingButtonTransitionsDuringLiveEvent() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        Process ffmpegProcess = null;

        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            boolean isGuestModeEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean isAuthenticationEnabled = firebase.getAutomationBoolean(activeTemplate.getJson(), "authentication.is_authentication_enabled");
            assertTrue(isClientActive, "Client should be active in Firebase");
            assertTrue(isGuestModeEnabled, "Guest mode should be enabled in Firebase");
            assertTrue(isAuthenticationEnabled, "Authentication should be enabled in Firebase");
            page.waitForTimeout(30_000);
            loginPage.launchJWApplication();
            page.waitForTimeout(30_000);
            hubPage.login();
            page.waitForTimeout(10_000);
            liveEventPage.verifyBroadcastLiveLinkVisible();
            liveEventPage.clickBroadcastLive();
            page.waitForTimeout(10_000);
            liveEventPage.verifyCreateLiveStreamButtonVisible();
            liveEventPage.clickCreateLiveStream();
            liveEventPage.verifyLiveEventRadioButtonVisible();
            liveEventPage.clickLiveEventRadioButton();
            liveEventPage.verifyLiveEventRadioButtonIsChecked();
            liveEventPage.verifyNameInputFieldVisible();
            liveEventPage.enterRandomEventName();
            liveEventPage.openIngestFormatDropdown();
            liveEventPage.selectIngestFormatSRT();
            liveEventPage.verifyAdvancedSettingsVisible();
            liveEventPage.clickAdvancedSettingsChevron();
            liveEventPage.verifyContentTypeDropdownVisible();
            liveEventPage.selectContentTypeLiveEvent();
            page.waitForTimeout(30_000);
            liveEventPage.clickConfirmCreateBroadcastButton();
            liveEventPage.verifyStreamIdVisible();
            String streamId = liveEventPage.getAndVerifyStreamId();
            String srtUrl = liveEventPage.getAndVerifyStreamUrlFromClipboard();
            page.waitForTimeout(120_000);
            ffmpegProcess = liveEventPage.startFFmpeg(srtUrl);
            page.waitForTimeout(10_000);
            assertTrue(ffmpegProcess != null && ffmpegProcess.isAlive(),
                    "FFmpeg should be running and sending the live stream");

            page.waitForTimeout(30_000);
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(180_000);
            page.reload();
            liveEventPage.scrollToLive();
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifyscheduledtag();
            page.waitForTimeout(360_000);
            liveEventPage.clickLiveContent();
            liveEventPage.verifyStreamStartTimer();
            page.waitForTimeout(90_000);
            liveEventPage.verifyWatchLiveButtonVisible();
        } catch (Throwable e) {

            System.err.println("Failed: " + e.getMessage());
            e.printStackTrace();
            throw e;}
        finally {
            if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
                System.out.println("Stopping FFmpeg...");
                ffmpegProcess.destroy();
                try {
                    if (!ffmpegProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                        ffmpegProcess.destroyForcibly();
                    }
                } catch (InterruptedException e) {
                    ffmpegProcess.destroyForcibly();
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    // TC : 89
// Verify recommended content does not appear on Live content detail page
// TR_JWP_1043

    @Test()
    @Epic("Details Page")
    @Feature("Recommend / Related Content")
    @Story("Recommended content should not appear on Live content detail page")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that Recommended Content or Related Content is not displayed " +
            "on the Live Content detail page when the recommendation feature is enabled")
    public void verifyRecommendedContentDoesNotAppearOnLiveContentDetailPage() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        LiveEventPage liveEventPage = new LiveEventPage(page);
        RecommendRelatedcontentpages recommendrelatedcontent = new RecommendRelatedcontentpages(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        Process ffmpegProcess = null;
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean isClientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(isClientActive, "Client should be active in Firebase");
            boolean RecommendedContent = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_recommended_content_enabled");
            assertTrue(RecommendedContent, "Firebase recommended content flag should be true");
            page.waitForTimeout(30_000);
            loginPage.launchJWApplication();
            page.waitForTimeout(30_000);
            hubPage.login();
            page.waitForTimeout(10_000);
            liveEventPage.verifyBroadcastLiveLinkVisible();
            liveEventPage.clickBroadcastLive();
            page.waitForTimeout(10_000);
            liveEventPage.verifyCreateLiveStreamButtonVisible();
            liveEventPage.clickCreateLiveStream();
            liveEventPage.verifyLiveEventRadioButtonVisible();
            liveEventPage.clickLiveEventRadioButton();
            liveEventPage.verifyLiveEventRadioButtonIsChecked();
            liveEventPage.verifyNameInputFieldVisible();
            liveEventPage.enterRandomEventName();
            liveEventPage.openIngestFormatDropdown();
            liveEventPage.selectIngestFormatSRT();
            liveEventPage.verifyAdvancedSettingsVisible();
            liveEventPage.clickAdvancedSettingsChevron();
            liveEventPage.verifyContentTypeDropdownVisible();
            liveEventPage.selectContentTypeLiveEvent();
            page.waitForTimeout(30_000);
            liveEventPage.clickConfirmCreateBroadcastButton();
            // The Stream ID is different for every execution.
            // We read it automatically from the page.
            liveEventPage.verifyStreamIdVisible();
            String streamId = liveEventPage.getAndVerifyStreamId();
            String srtUrl = liveEventPage.getAndVerifyStreamUrlFromClipboard();
            page.waitForTimeout(120_000);
            ffmpegProcess = liveEventPage.startFFmpeg(srtUrl);
            page.waitForTimeout(10_000);
            if (!ffmpegProcess.isAlive()) {
                throw new RuntimeException(
                        "FFmpeg stopped. SRT streaming failed. Check the FFmpeg log above."
                );
            }
            page.waitForTimeout(30_000);
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(180_000);
            page.reload();
            liveEventPage.scrollToLive();
            liveEventPage.verifyLiveContent();
            liveEventPage.Verifyscheduledtag();
            page.waitForTimeout(360_000);
            liveEventPage.clickLiveContent();
            liveEventPage.verifyStreamStartTimer();
            page.waitForTimeout(90_000);
            liveEventPage.verifyWatchLiveButtonVisible();
            liveEventPage.clickWatchLive();
            page.waitForTimeout(12_000);
            loginPage.verifyVideoPlayerVisible();
            loginPage.verifyVideoPlayerBackButtonVisible();
            loginPage.clickVideoPlayerBackButton();
            recommendrelatedcontent.verifyRecommendedContentNotVisible();

        } catch (Throwable e) {
            System.err.println(" Failed: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

}

