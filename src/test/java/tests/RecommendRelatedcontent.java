package tests;

import base.BaseTest;
import io.qameta.allure.*;
import org.testng.annotations.Test;
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
    @Severity(SeverityLevel.BLOCKER)
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
            boolean guestModeEnabled = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_guest_mode_enabled");
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
}
