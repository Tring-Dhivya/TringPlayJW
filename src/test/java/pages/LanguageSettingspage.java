
package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LanguageSettingspage {

    private final Page page;

    public LanguageSettingspage(Page page) {
        this.page = page;
    }

    public Locator getLanguageGlobeIcon() {
        return page.locator("//div[@aria-label='Open language menu']");
    }
    public Locator getEnglishLanguageOption() {
        return page.locator(
                "//*[@id='language-panel']//a[normalize-space()='English']"
        );
    }
    public Locator getSpanishLanguageOption() {
        return page.locator(
                "//*[@id='language-panel']//a[normalize-space()='Spanish']"
        );
    }
    public Locator getHomeText() {
        return page.locator("//span[normalize-space()='Inicio']").first();
    }

    public Locator getMoviesText() {
        return page.locator("//span[normalize-space()='Películas']").first();
    }
    public Locator getSignInButton() {
        return page.locator(
                "//button[.//span[normalize-space()='Iniciar sesión']]"
        ).first();
    }

    public Locator getSignUpButton() {
        return page.locator(
                "//button[.//span[normalize-space()='Registrarse']]"
        ).first();
    }
    public Locator getPrivacyPolicyLink() {
        return page.locator(
                "//div[contains(@class,'footerLegalDocsPart')]//a[normalize-space()='política de privacidad']"
        );
    }

    public Locator getTermsOfUseLink() {
        return page.locator(
                "//div[contains(@class,'footerLegalDocsPart')]//a[normalize-space()='Condiciones de uso']"
        );
    }
    public Locator getGuestHome() {
        return page.locator("nav").getByRole(AriaRole.LINK, new Locator.GetByRoleOptions()
                .setName("Home").setExact(true));
    }
    public Locator getSignInButtonInEnglish() {
        return page.locator("#root").getByRole(AriaRole.BUTTON,
                new Locator.GetByRoleOptions().setName("Sign In"));
    }
    public Locator getSignUpButtonInEnglish() {
        return page.locator("#root")
                .getByRole(
                        AriaRole.BUTTON,
                        new Locator.GetByRoleOptions()
                                .setName("Sign Up")
                );
    }
    public Locator getPrivacyPolicyLinkinenglish() {
        return page.locator("//a[normalize-space()='Privacy Policy']").first();
    }
    public Locator gettermsOfUse() {return page.locator("//a[normalize-space()='Terms of Use']").first();
    }
    public Locator getSeriesMediaCard() {
        return page.locator(
                "//a[@role='button'][.//h3[normalize-space()='Toolpati']]"
        );
    }
    @Step("Verify Privacy Policy Link Visible")
    public void verifyPrivacyPolicyInEnglish() {assertThat(getPrivacyPolicyLinkinenglish()).isVisible();}
    @Step("Verify Term Of Use Visible")
    public void verifyTermsOfUseInEnglish() {assertThat(gettermsOfUse()).isVisible();}
    @Step("Verify Guest Home page is visible")
    public void verifyGuestHomeVisible() {assertThat(getGuestHome()).isVisible();}
    @Step("Verify language globe icon is visible")
    public void verifyLanguageGlobeIconVisible() {assertThat(getLanguageGlobeIcon()).isVisible();}
    @Step("Click language globe icon")
    public void clickLanguageGlobeIcon() {getLanguageGlobeIcon().click();}
    @Step("Verify English language option is visible")
    public void verifyEnglishLanguageOptionVisible(String languageCode) {assertThat(getEnglishLanguageOption()).isVisible();}
    @Step("Click English Language")
    public void ClickEnglishLanguage() {getEnglishLanguageOption().click();}
    @Step("Verify Spanish language option is visible")
    public void verifySpanishLanguageOptionVisible(String languageCode) {assertThat(getSpanishLanguageOption()).isVisible();}
    @Step("Click Spanish Language")
    public void ClickSpanishLanguage() {getSpanishLanguageOption().click();}
    @Step("Verify Home is translated to Spanish as Inicio")
    public void verifyHomeInSpanish() {assertThat(getHomeText()).isVisible();}
    @Step("Verify Movies is translated to Spanish as Películas")
    public void verifyMoviesInSpanish() {assertThat(getMoviesText()).isVisible();}
    @Step("Verify Sign In button is translated to Iniciar sesión")
    public void verifySignInButtonInSpanish() {assertThat(getSignInButton()).isVisible();}
    @Step("Verify Sign Up button is translated to Registrarse")
    public void verifySignUpButtonInSpanish() {assertThat(getSignUpButton()).isVisible();}
    @Step("Verify footer Privacy Policy is translated to Spanish")
    public void verifyPrivacyPolicyInSpanish() {assertThat(getPrivacyPolicyLink()).isVisible();}
    @Step("Verify footer Terms of Use is translated to Spanish")
    public void verifyTermsOfUseInSpanish() {assertThat(getTermsOfUseLink()).isVisible();}
    @Step("Scroll to footer")
    public void scrollToFooter() {

        for (int i = 0; i < 15; i++) {

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
    @Step("Scroll page and find Toolpati series media")
    public void scrollAndFindSeriesMedia() {

        Locator seriesMedia = getSeriesMediaCard();

        for (int i = 0; i < 10; i++) {

            if (seriesMedia.count() > 0) {
                seriesMedia.scrollIntoViewIfNeeded();
                return;
            }

            page.mouse().wheel(0, 700);
            page.waitForTimeout(500);
        }

        throw new AssertionError("Toolpati series media was not found after scrolling");
    }
    public void scrollToFooterInEnglish() {

        for (int i = 0; i < 15; i++) {

            if (getPrivacyPolicyLinkinenglish().isVisible()) {
                return;
            }

            page.mouse().wheel(0, 800);
            page.waitForTimeout(500);
        }

        throw new AssertionError(
                "Footer was not found after scrolling"
        );
    }
    @Step("Verify Sign Up button is visible")
    public void verifySignUpButtonInEnglish() {assertThat(getSignUpButtonInEnglish()).isVisible();}
    @Step("Verify Sign in button is visible")
    public void verifySignInButtonInEnglish() {assertThat(getSignInButtonInEnglish()).isVisible();}



}






