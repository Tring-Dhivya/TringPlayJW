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
import java.util.concurrent.TimeUnit;

public class BaseTest {

    protected Playwright playwright;
    protected Browser browser;
    protected BrowserContext context;
    protected Page page;
    private Process ffmpegProcess;

    protected String baseUrl;
    protected List<String> consoleErrors;

    public Page getPage() {
        return page;
    }

    @BeforeMethod(alwaysRun = true)
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

        // Combine permissions AND video recording in a SINGLE BrowserContext
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setPermissions(Arrays.asList(
                                "clipboard-read",
                                "clipboard-write"
                        ))
                        .setRecordVideoDir(
                                Paths.get("allure-results/videos")
                        )
        );

        // Create page from context
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

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        /*
         * Attach screenshot BEFORE closing the page.
         */
        if (page != null) {

            try {

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

            } catch (Exception e) {

                System.out.println(
                        "Screenshot attachment failed: "
                                + e.getMessage()
                );
            }
        }

        /*
         * Close page.
         * If Playwright connection is already closed,
         * don't let teardown fail.
         */
        if (page != null) {

            try {
                page.close();
            } catch (Exception e) {

                System.out.println(
                        "Page cleanup skipped: "
                                + e.getMessage()
                );
            }
        }

        /*
         * Close browser context.
         */
        if (context != null) {

            try {
                context.close();
            } catch (Exception e) {

                System.out.println(
                        "Context cleanup skipped: "
                                + e.getMessage()
                );
            }
        }

        /*
         * Close browser.
         */
        if (browser != null) {

            try {
                browser.close();
            } catch (Exception e) {

                System.out.println(
                        "Browser cleanup skipped: "
                                + e.getMessage()
                );
            }
        }

        /*
         * Close Playwright.
         */
        if (playwright != null) {

            try {
                playwright.close();
            } catch (Exception e) {

                System.out.println(
                        "Playwright cleanup skipped: "
                                + e.getMessage()
                );
            }
        }

        /*
         * Clear references.
         */
        page = null;
        context = null;
        browser = null;
        playwright = null;
    }
    @AfterMethod
    public void stopFFmpeg() {

        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {

            System.out.println(
                    "========== STOPPING FFMPEG =========="
            );

            ffmpegProcess.destroy();

            try {

                if (!ffmpegProcess.waitFor(
                        5,
                        TimeUnit.SECONDS
                )) {

                    System.out.println(
                            "FFmpeg did not stop normally."
                                    + " Force stopping..."
                    );

                    ffmpegProcess.destroyForcibly();
                }

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                ffmpegProcess.destroyForcibly();
            }

            System.out.println(
                    "FFmpeg process stopped."
            );

            System.out.println(
                    "====================================="
            );
        }
    }
}