
package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Hubpage;
import pages.Loginpage;
import utils.ClientExpectationReader;
import utils.FirebaseRemoteConfigClient;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class Hub extends BaseTest {

    // TC : 40  Verify Hub page renders all playlists correctly
    // TR_JWP_433
    @Test
    @Epic("Hub")
    @Feature("Hub Layout")
    @Story("Verify Hub page renders all playlists correctly")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Validate that the Hub page displays all playlists correctly, including thumbnails, titles, and order")
    public void verifyHubPageRendersAllPlaylistsCorrectly() throws Exception {
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
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
                loginPage.launchApplication();
                loginPage.verifyGuestHomeVisible();
                hubPage.verifyAndClickHub();
                hubPage.verifyHubPageLoaded();
                hubPage.verifyAllPlaylistsVisible();
                hubPage.verifyPlaylistTitlesAndThumbnails();
                hubPage.verifyPlaylistOrder();
                hubPage.verifyAllPlaylistsAfterScrolling();
            } catch (Throwable e) {
                System.err.println("Test result: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
        }
    // TC : 41  Verify clicking a playlist inside Hub opens corresponding media
    // TR_JWP_434
    @Test
    @Epic("Hub")
    @Feature("Hub Playlist Navigation")
    @Story("Verify clicking a playlist inside Hub opens corresponding media")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify selecting playlists inside Hub opens the corresponding playlist page with media thumbnails and titles")
    public void verifyHubPlaylistNavigation() throws Exception {
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
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true"); loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            hubPage.verifyAndClickHub();
            String firstContent = hubPage.clickPlaylistContent(0);
            hubPage.verifyMediaPageLoaded();
            page.waitForTimeout(10_000);
            hubPage.verifyOpenedMediaTitle(firstContent);
            hubPage.verifyMediaThumbnail();
            page.goBack();
            String secondContent = hubPage.clickPlaylistContent(1);
            hubPage.verifyMediaPageLoaded();
            hubPage.verifyOpenedMediaTitle(secondContent);
            hubPage.verifyMediaThumbnail();
            page.waitForTimeout(10_000);
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 42 Verify clicking a playlist inside Hub opens corresponding media
    // TR_JWP_434
    @Test
    @Epic("Hub")
    @Feature("Movie Playback")
    @Story("Verify movie inside playlist plays correctly")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify selecting a movie thumbnail inside a Hub playlist opens the movie and starts video playback with audio and video functioning correctly")
    public void verifyMovieInsidePlaylistPlaysCorrectly() throws Exception {
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
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true"); loginPage.launchApplication();
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            hubPage.verifyAndClickHub();
//            hubPage.verifyFullMoviesPlaylistVisible();
            hubPage.clickLucyTop10Movie();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            loginPage.verifyVideoPlayerVisible();
            hubPage.verifyVideoElementExists();
            page.waitForTimeout(20_000);
            hubPage.verifyVideoIsNotPaused();
            double initialPlaybackTime = hubPage.getCurrentPlaybackTime();
            page.waitForTimeout(30_000);
            hubPage.verifyPlaybackProgressed( initialPlaybackTime );
            hubPage.verifyVideoHasNoError();
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // TC : 43  Verify guest user cannot play restricted content
    // TR_JWP_441 need to work on
    @Test
    @Epic("Hub")
    @Feature("Guest Restriction")
    @Story("Verify guest user cannot play restricted content")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify guest user cannot play restricted content")
    public void Verifyguestusercannotplayrestrictedcontent() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        String originalAuthenticationType = firebase.getStagingString("common.authentication_type");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            String authenticationType = firebase.getStagingString("common.authentication_type");
            assertEquals(authenticationType, "inplayer", "Authentication type was not updated correctly");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            hubPage.verifyAndClickHub();
            hubPage.verifyHubPageLoaded();
            hubPage.verifyAllPlaylistsVisible();
            hubPage.verifyAndClickVikingsTop10Movie();
            hubPage.clickLoginOrSubscribe();
            loginPage.verifySignInPopupVisible();

        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 52 Verify media title, duration, and content type tags
    // TR_JWP_601
    @Test
    @Epic("Search")
    @Feature("Search Page")
    @Story("Verify media title, duration, and content type tags")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify Zootopia search results display thumbnail, title, duration and applicable content type tags correctly")
    public void verifySearchMediaMetadata() throws Exception {
            Loginpage loginPage = new Loginpage(page);
            Hubpage hubPage = new Hubpage(page);
            FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
            FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
            boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean originalSearch = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_search_enabled");
            try {
                boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
                boolean ISSearch = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_search_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(ISSearch, "feature_flags.is_search_enabled");
                loginPage.launchApplication();
                loginPage.verifyGuestHomeVisible();
                hubPage.clickSearch();
                hubPage.searchFormedia();
                page.waitForTimeout(10_000);
                hubPage.verifySearchResultsLoaded();
                hubPage.verifyMediaMetadata();
            } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 44 Verify menu displays items based on JW Player dashboard
    // TR_JWP_458
    @Test
    @Epic("Menu Bar")
    @Feature("Navigation")
    @Story("Verify menu displays items based on JW Player dashboard")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify all menu items configured in JW Player Dashboard " + "are displayed in the OTT application in the correct order")
    public void verifyMenuDisplaysItemsBasedOnJWPlayerDashboard() throws Exception {
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
                boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
                assertTrue(clientActive, "is_client_active should be true");
                assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
                assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true"); loginPage.launchApplication();
                loginPage.launchJWApplication();
                hubPage.login();
                hubPage.openApps();
                hubPage.openTringTringStagingConfig();
                hubPage.openMenu();
                List<String> expectedMenuItems = hubPage.getConfiguredMenuItemNames();
                loginPage.launchApplication();
                loginPage.verifyGuestHomeVisible();
                page.waitForTimeout(10_000);
                hubPage.verifyMenuMatchesDashboard(expectedMenuItems);
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


}