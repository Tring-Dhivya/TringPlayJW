package base;

import com.microsoft.playwright.*;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utils.AllureUtils;
import utils.ConfigReader;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;

    protected String baseUrl;
    protected List<String> consoleErrors;

    public Page getPage() {
        return page;
    }

    @BeforeMethod
    public void setUp() {

        // Create Playwright
        playwright = Playwright.create();

        // Select browser
        BrowserType browserType;

        switch (ConfigReader.getBrowser().toLowerCase()) {

            case "firefox":
                browserType = playwright.firefox();
                break;

            case "webkit":
                browserType = playwright.webkit();
                break;

            case "chromium":
            default:
                browserType = playwright.chromium();
                break;
        }

        // Launch browser
        browser = browserType.launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(ConfigReader.isHeadless())
                        .setArgs(Arrays.asList("--start-maximized"))
        );

        // Create ONLY ONE context
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setRecordVideoDir(
                                Paths.get("allure-results/videos")
                        )
        );

        // Create page from the same context
        page = context.newPage();

        // Base URL
        baseUrl = ConfigReader.getUrl();

        // Console error list
        consoleErrors = new ArrayList<>();

        // Capture browser console errors
        page.onConsoleMessage(message -> {

            if ("error".equalsIgnoreCase(message.type())) {

                consoleErrors.add(message.text());
            }
        });
    }

    protected void assertNoConsoleErrors() {

        if (!consoleErrors.isEmpty()) {

            Assert.fail(
                    "Console errors found:\n" +
                            String.join("\n\n", consoleErrors)
            );
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {

        // Attach screenshot before closing page
        if (page != null) {

            String status;

            if (result.getStatus() == ITestResult.SUCCESS) {
                status = "PASS";
            } else if (result.getStatus() == ITestResult.FAILURE) {
                status = "FAIL";
            } else {
                status = "SKIP";
            }

            AllureUtils.attachScreenshot(
                    page,
                    status + " Screenshot - " + result.getName()
            );
        }

        // Close page
        if (page != null) {
            page.close();
        }

        // Close context
        if (context != null) {
            context.close();
        }

        // Close browser
        if (browser != null) {
            browser.close();
        }

        // Close Playwright
        if (playwright != null) {
            playwright.close();
        }
    }
}

