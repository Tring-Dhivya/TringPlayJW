package listeners;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.testng.ITestListener;
import org.testng.ITestResult;
import base.BaseTest;

import java.io.ByteArrayInputStream;

public class AllureScreenshotListener implements ITestListener {

    private void captureScreenshot(ITestResult result, String status) {

        Object instance = result.getInstance();

        if (!(instance instanceof BaseTest)) {
            return;
        }

        BaseTest test = (BaseTest) instance;
        Page page = test.getPage();

        if (page == null) {
            System.err.println("Page is null. Screenshot not captured.");
            return;
        }

        try {

            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setFullPage(true)
            );

            String testName = result.getMethod()
                    .getMethodName();

            Allure.addAttachment(
                    status + " Screenshot - " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

            System.out.println(
                    status + " screenshot attached for: " + testName
            );

        } catch (Exception e) {

            System.err.println(
                    "Unable to attach " + status +
                            " screenshot: " + e.getMessage()
            );
        }
    }

    @Override
    public void onTestSuccess(ITestResult result) {

        captureScreenshot(
                result,
                "PASS"
        );
    }

    @Override
    public void onTestFailure(ITestResult result) {

        captureScreenshot(
                result,
                "FAIL"
        );
    }
}
