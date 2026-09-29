package tests;

import base.BaseTest;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import com.microsoft.playwright.options.WaitUntilState;
import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.LanguageSettingspage;
import pages.Loginpage;
import utils.ClientExpectationReader;
import utils.FirebaseRemoteConfigClient;

import java.io.IOException;
import java.util.List;

import static org.testng.Assert.*;

public class LanguageSettings extends BaseTest {

    // TC : 79 Verify globe icon visibility when Firebase multi-language support is enabled
    // TR_JWP_971
    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Epic("Language Settings")
    @Feature("Firebase Configuration")
    @Story("Multi-Language Support")
    @Description("Verify multi-language support configuration and options in application")
    public void verifyMultiLanguageSupport() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        LanguageSettingspage languageSettingsPage = new LanguageSettingspage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalMultiLanguageSupport = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_multi_language_support");
        String originalDefaultLanguage = firebase.getStagingString("common.default_language");
        List<String> originalSupportedLanguages = firebase.getStagingStringList("common.supported_languages");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean multiLanguageSupport = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.is_multi_language_support");
            assertTrue(multiLanguageSupport, "Multi-language support should be enabled in Firebase");
            String defaultLanguage = firebase.getStagingString("common.default_language");
            assertEquals(defaultLanguage, "en", "Default language should be English");
            List<String> supportedLanguages = firebase.getStagingStringList("common.supported_languages");
            assertNotNull(supportedLanguages, "Supported languages should not be null");
            assertTrue(supportedLanguages.contains("en"), "Supported languages should contain 'en'. Actual: " + supportedLanguages);
            assertTrue(supportedLanguages.contains("es"), "Supported languages should contain 'es'. Actual: " + supportedLanguages);
            assertEquals(supportedLanguages.size(), 2, "Supported languages should contain exactly 2 languages. Actual: " + supportedLanguages);
            loginPage.launchApplication();
            page.waitForTimeout(10_000);
            loginPage.verifyGuestHomeVisible();
            languageSettingsPage.verifyLanguageGlobeIconVisible();
            languageSettingsPage.clickLanguageGlobeIcon();
            languageSettingsPage.verifyEnglishLanguageOptionVisible("English");
            languageSettingsPage.verifySpanishLanguageOptionVisible("Spanish");
        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
    // TC : 80 Verify UI updates completely to Spanish after language selection
    // TR_JWP_975

    @Test()
    @Severity(SeverityLevel.CRITICAL)
    @Feature("Language Settings")
    @Story("Language Change")
    @Description("Verify UI updates completely to Spanish after language selection")
    public void verifySpanishLanguageChange() throws IOException, InterruptedException {
        Loginpage loginPage = new Loginpage(page);
        LanguageSettingspage languageSettingsPage = new LanguageSettingspage(page);
        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
        boolean originalMultiLanguageSupport = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_multi_language_support");
        String originalDefaultLanguage = firebase.getStagingString("common.default_language");
        List<String> originalSupportedLanguages = firebase.getStagingStringList("common.supported_languages");
        try {
            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
            assertTrue(clientActive, "Client should be active in Firebase");
            boolean multiLanguageSupport = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.is_multi_language_support");
            assertTrue(multiLanguageSupport, "Multi-language support should be enabled in Firebase");
            String defaultLanguage = firebase.getStagingString("common.default_language");
            assertEquals(defaultLanguage, "en", "Default language should be English");
            List<String> supportedLanguages = firebase.getStagingStringList("common.supported_languages");
            assertNotNull(supportedLanguages, "Supported languages should not be null");
            assertTrue(supportedLanguages.contains("en"), "Supported languages should contain 'en'. Actual: " + supportedLanguages);
            assertTrue(supportedLanguages.contains("es"), "Supported languages should contain 'es'. Actual: " + supportedLanguages);
            assertEquals(supportedLanguages.size(), 2, "Supported languages should contain exactly 2 languages. Actual: " + supportedLanguages);
            loginPage.launchApplication();
            loginPage.verifyGuestHomeVisible();
            languageSettingsPage.verifyLanguageGlobeIconVisible();
            languageSettingsPage.clickLanguageGlobeIcon();
            languageSettingsPage.verifyEnglishLanguageOptionVisible("English");
            languageSettingsPage.verifySpanishLanguageOptionVisible("Spanish");
            languageSettingsPage.ClickSpanishLanguage();
            languageSettingsPage.verifyHomeInSpanish();
            languageSettingsPage.verifySignInButtonInSpanish();
            languageSettingsPage.verifySignUpButtonInSpanish();
            languageSettingsPage.scrollToFooter();
            page.waitForTimeout(20_000);
            languageSettingsPage.verifyPrivacyPolicyInSpanish();
            languageSettingsPage.verifyTermsOfUseInSpanish();

        } catch (Throwable e) {
            System.err.println("Test result: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

//    // TC : 81 Verify UI reverts completely to English after language selection
//// TR_JWP_976
//    @Test
//    @Severity(SeverityLevel.CRITICAL)
//    @Feature("Language Settings")
//    @Story("Language Change")
//    @Description("Verify UI reverts completely to English after language selection")
//    public void verifyUILanguageRevertsToEnglish() throws IOException, InterruptedException {
//
//        Loginpage loginPage = new Loginpage(page);
//        LanguageSettingspage languageSettingsPage = new LanguageSettingspage(page);
//        FirebaseRemoteConfigClient firebase = new FirebaseRemoteConfigClient();
//        FirebaseRemoteConfigClient.RemoteConfigTemplate originalTemplate = firebase.getRemoteConfig();
//        boolean originalClientActive = firebase.getAutomationBoolean(originalTemplate.getJson(), "common.is_client_active");
//        boolean originalMultiLanguageSupport = firebase.getAutomationBoolean(originalTemplate.getJson(), "feature_flags.is_multi_language_support");
//        String originalDefaultLanguage = firebase.getStagingString("common.default_language");
//        List<String> originalSupportedLanguages = firebase.getStagingStringList("common.supported_languages");
//        try {
//            FirebaseRemoteConfigClient.RemoteConfigTemplate activeTemplate = firebase.getRemoteConfig();
//            boolean clientActive = firebase.getAutomationBoolean(activeTemplate.getJson(), "common.is_client_active");
//            assertTrue(clientActive, "Client should be active in Firebase");
//            boolean multiLanguageSupport = firebase.getAutomationBoolean(activeTemplate.getJson(), "feature_flags.is_multi_language_support");
//            assertTrue(multiLanguageSupport, "Multi-language support should be enabled in Firebase");
//            String defaultLanguage = firebase.getStagingString("common.default_language");
//            assertEquals(defaultLanguage, "en", "Default language should be English");
//            List<String> supportedLanguages = firebase.getStagingStringList("common.supported_languages");
//            assertNotNull(supportedLanguages, "Supported languages should not be null");
//            assertTrue(supportedLanguages.contains("en"), "Supported languages should contain 'en'. Actual: " + supportedLanguages);
//            assertTrue(supportedLanguages.contains("es"), "Supported languages should contain 'es'. Actual: " + supportedLanguages);
//            assertEquals(supportedLanguages.size(), 2, "Supported languages should contain exactly 2 languages. Actual: " + supportedLanguages);
//            firebase.updateStagingString(
//                    "common.default_language",
//                    "es"
//            );
//
//            System.out.println(
//                    "Firebase default language after update: "
//                            + firebase.getStagingString("common.default_language")
//            );
//
//            // Wait for Firebase configuration update
//            page.waitForTimeout(15_000);
//
//// Clear current browser session
//            context.clearCookies();
//
//// Reload the application
//            page.reload(
//                    new Page.ReloadOptions()
//                            .setWaitUntil(WaitUntilState.DOMCONTENTLOADED)
//            );
//            loginPage.launchApplication();
//            page.waitForTimeout(120_000);
//            loginPage.launchApplication();
//            languageSettingsPage.verifyHomeInSpanish();
//            languageSettingsPage.verifyLanguageGlobeIconVisible();
//            languageSettingsPage.clickLanguageGlobeIcon();
//            languageSettingsPage.verifyEnglishLanguageOptionVisible("English");
//            languageSettingsPage.ClickEnglishLanguage();
//            languageSettingsPage.verifyGuestHomeVisible();
//            languageSettingsPage.verifySignInButtonInEnglish();
//
//            languageSettingsPage.verifySignUpButtonInEnglish();
//
//            languageSettingsPage.scrollToFooterInEnglish();
//
//            page.waitForTimeout(20_000);
//
//            languageSettingsPage.verifyPrivacyPolicyInEnglish();
//
//            languageSettingsPage.verifyTermsOfUseInEnglish();
//
//        } finally {
//
//            // Restore original Firebase configuration
//            try {
//
//                firebase.updateStagingString(
//                        "common.default_language",
//                        originalDefaultLanguage
//                );
//
//            } catch (Throwable e) {
//
//                System.err.println(
//                        "Failed to restore original default language: "
//                                + e.getMessage()
//                );
//
//                e.printStackTrace();
//
//                throw e;
//            }
//        }
//    }
//}
