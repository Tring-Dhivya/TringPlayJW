package utils;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class AllureUtils {

    // ============================================================
    // SCREENSHOT
    // ============================================================

    public static void attachScreenshot(Page page, String name) {

        if (page == null) {
            return;
        }

        try {

            byte[] screenshot = page.screenshot(
                    new Page.ScreenshotOptions()
                            .setFullPage(true)
            );

            Allure.addAttachment(
                    name,
                    "image/png",
                    new ByteArrayInputStream(screenshot),
                    ".png"
            );

        } catch (Exception e) {

            System.err.println(
                    "Unable to attach screenshot to Allure: "
                            + e.getMessage()
            );
        }
    }


    // ============================================================
    // VIDEO
    // ============================================================

    public static void attachVideo(Path videoPath, String name) {

        if (videoPath == null) {
            return;
        }

        try {

            if (!Files.exists(videoPath)) {
                System.err.println(
                        "Video file does not exist: " + videoPath
                );
                return;
            }

            try (InputStream inputStream =
                         Files.newInputStream(videoPath)) {

                Allure.addAttachment(
                        name,
                        "video/webm",
                        inputStream,
                        ".webm"
                );
            }

        } catch (IOException e) {

            System.err.println(
                    "Unable to attach video to Allure: "
                            + e.getMessage()
            );
        }
    }
}