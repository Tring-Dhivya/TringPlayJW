package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();
    static {
        try (FileInputStream file =
                     new FileInputStream("src/test/resources/config.properties")) {

            properties.load(file);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to read config.properties", e
            );
        }
    }
    public static String getUrl() {
        return properties.getProperty("url");
    }
    public static String getBrowser() {
        return properties.getProperty("browser", "chromium");
    }
    public static boolean isHeadless() {
        return Boolean.parseBoolean(
                properties.getProperty("headless", "false")
        );
    }
    public static String getJwUrl() { return properties.getProperty("jwUrl"); }
    public static String getUsername() {
        return properties.getProperty("username");
    }
    public static String getUsername1() {
        return properties.getProperty("username1");
    }
    public static String getPassword() {
        return properties.getProperty("password");
    }
    public static String getJWUsername() {
        return properties.getProperty("jwusername");
    }

    public static String getJWPassword() {
        return properties.getProperty("jwpassword");
    }
    public static String getWrongpassword() {
        return properties.getProperty("Wrongpassword");
    }

    public static String getFirstNameField() {
        return properties.getProperty("firstName");
    }

    public static String getLastNameField() {
        return properties.getProperty("lastName");
    }
}