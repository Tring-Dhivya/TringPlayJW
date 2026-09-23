package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class RecommendRelatedcontentpages {
    public final Page page;

    public RecommendRelatedcontentpages(Page page) {
        this.page = page;
    }

    public Locator getSeriesDetailTitle() {
        return page.locator(
                "//main[@id='content']" +
                        "//header[@id='video-details']" +
                        "//h1"
        );
    }

    public Locator getEpisodesHeading() {
        return page.locator(
                "//main[@id='content']" +
                        "//h2[normalize-space()='Episodes']"
        );
    }
    public Locator getEpisodeCards() {
        return page.locator(
                "//main[@id='content']" +
                        "//h2[normalize-space()='Episodes']" +
                        "/ancestor::div[contains(@class,'relatedVideos')]" +
                        "//a[@role='button'][.//div[contains(@class,'_tag_')]]"
        );
    }

    public Locator getSeasonSelector() {
        return page.locator(
                "//*[self::button or self::select or self::div]" +
                        "[contains(normalize-space(), 'Season')]"
        );
    }

    public Locator getRecommendedContentSection() {
        return page.locator(
                "//main[@id='content']" +
                        "//*[self::h1 or self::h2 or self::h3]" +
                        "[normalize-space()='Recommended Content' " +
                        "or normalize-space()='Related Content']"
        );
    }

    @Step("Verify Series detail page is visible")
    public void verifySeriesDetailPageVisible() {

        assertThat(getSeriesDetailTitle()).isVisible();

        assertThat(getSeriesDetailTitle()).hasText("Avengers");
    }


    @Step("Verify Episodes section is visible")
    public void verifyEpisodesSectionVisible() {
        assertThat(getEpisodesHeading()).isVisible();
    }
    @Step("Verify episode list is visible")
    public void verifyEpisodeListVisible() {assertThat(getEpisodeCards()).hasCount(3);}
    @Step("Scroll through Series detail page")
    public void scrollSeriesDetailPage() {

        page.locator("#content").evaluate(
                "element => element.scrollTo(0, element.scrollHeight)"
        );

        page.waitForTimeout(500);
    }
    @Step("Verify Recommended Content is not displayed on Series detail page")
    public void verifyRecommendedContentNotVisible() {
        assertThat(getRecommendedContentSection()).hasCount(0);
    }
}
