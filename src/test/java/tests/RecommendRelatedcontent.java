package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.Hubpage;
import pages.Loginpage;
import pages.RecommendRelatedcontentpages;
import utils.ClientExpectationReader;
import utils.FirebaseRemoteConfigClient;

import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class RecommendRelatedcontent extends BaseTest {


    @Test()
    @Feature("Recommend / Related content")
    @Story("Remote Config")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify recommended content does not appear on Series detail page")
    public void verifyRecommendedContentNotVisibleOnSeriesDetailPage() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        RecommendRelatedcontentpages recommendrelatedcontent = new RecommendRelatedcontentpages(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        boolean originalRecommendedContent = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_recommended_content_enabled");

        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            boolean RecommendedContent = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_recommended_content_enabled");
            assertTrue(clientActive, "is_client_active should be true");
            assertTrue(authenticationEnabled, "is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "is_guest_mode_enabled should be true");
            assertTrue(RecommendedContent, "Firebase recommended content flag should be true");
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            loginPage.scrollToSection();
            loginPage.findMovieInMoviesCarousel("Avengers");
            page.waitForTimeout(5_000);
            loginPage.clickContent("Avengers");
            recommendrelatedcontent.verifySeriesDetailPageVisible();
            recommendrelatedcontent.verifyEpisodesSectionVisible();
            recommendrelatedcontent.verifyEpisodeListVisible();
            recommendrelatedcontent.scrollSeriesDetailPage();
            recommendrelatedcontent.verifyRecommendedContentNotVisible();
        } catch (Throwable e) {
            System.err.println("Failed to restore original Firebase configuration: " + e.getMessage());
            e.printStackTrace();
        }

    }

    // TC : 58 Verify CSAI preroll ad playback
// TR_JWP_702
    @Test()
    @Epic("Media Player")
    @Feature("Ads (CSAI)")
    @Story("Verify CSAI preroll ad playback")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CSAI preroll ad plays and the main video resumes after the advertisement")
    public void verifyCSAIPrerollAdPlayback() throws Exception {
        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        RecommendRelatedcontentpages recommendrelatedcontent = new RecommendRelatedcontentpages(page);

        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "common.is_client_active should be true");
            assertTrue(authenticationEnabled, "authentication.is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "authentication.is_guest_mode_enabled should be true");
            loginPage.launchJWApplication();
            page.waitForTimeout(20_000);
            hubPage.login();
            page.waitForTimeout(10_000);
            hubPage.openApps();
            hubPage.openTringTringStagingConfig();
            page.waitForTimeout(30_000);
            recommendrelatedcontent.openAdvertisingTab();
            recommendrelatedcontent.verifyAndSelectAdConfig();
            page.waitForTimeout(10_000);
            recommendrelatedcontent.ensureOTTASelected(page);
            recommendrelatedcontent.ensureClientSideSelected(page);
            recommendrelatedcontent.testAdConfigSaveBehavior();
            page.waitForTimeout(10_000);
            loginPage.launchApplication();
            page.waitForTimeout(80_000);
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(30_000);
            loginPage.verifyAndClickZootopia2();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            recommendrelatedcontent.verifyAdIsPlaying();

        } catch (Throwable e) {
            System.err.println("Test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;

        }
    }
    // TC : 59 Verify CSAI midroll ad playback
    // TR_JWP_703
    @Test()
    @Epic("Media Player")
    @Feature("Ads (CSAI)")
    @Story("Verify CSAI midroll ad playback")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify CSAI midroll ad interrupts the main video and the main video resumes after the advertisement")
    public void verifyCSAIMidrollAdPlayback() throws Exception {

        Loginpage loginPage = new Loginpage(page);
        Hubpage hubPage = new Hubpage(page);
        RecommendRelatedcontentpages recommendrelatedcontent = new RecommendRelatedcontentpages(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalGuestMode = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
        boolean originalAuthentication = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
        try {
            boolean clientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
            boolean authenticationEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_authentication_enabled");
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "authentication.is_guest_mode_enabled");
            assertTrue(clientActive, "common.is_client_active should be true");
            assertTrue(authenticationEnabled, "authentication.is_authentication_enabled should be true");
            assertTrue(guestModeEnabled, "authentication.is_guest_mode_enabled should be true");
            loginPage.launchJWApplication();
            page.waitForTimeout(10_000);
            hubPage.login();
            page.waitForTimeout(10_000);
            hubPage.openApps();
            hubPage.openTringTringStagingConfig();
            page.waitForTimeout(30_000);
            recommendrelatedcontent.openAdvertisingTab();
            recommendrelatedcontent.verifyAndSelectAdConfig();
            page.waitForTimeout(10_000);
            recommendrelatedcontent.ensureOTTASelected(page);
            recommendrelatedcontent.ensureClientSideSelected(page);
            recommendrelatedcontent.testAdConfigSaveBehavior();
            page.waitForTimeout(10_000);
            loginPage.launchApplication();
            page.waitForTimeout(100_000);
            loginPage.verifyGuestHomeVisible();
            page.waitForTimeout(30_000);
            loginPage.verifyAndClickZootopia2();
            loginPage.verifyStartWatchingButtonVisible();
            loginPage.clickStartWatching();
            recommendrelatedcontent.verifyAdIsPlaying();

        } catch (Throwable e) {
            System.err.println("CSAI midroll test failed: " + e.getMessage());
            e.printStackTrace();
            throw e;

        }


    }

}
