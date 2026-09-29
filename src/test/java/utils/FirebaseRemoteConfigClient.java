package utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FirebaseRemoteConfigClient {

    // FIREBASE PROJECT
    private static final String PROJECT_ID = "tringplay-65e37";
    private static final String QUOTA_PROJECT_ID = PROJECT_ID;
    private static final String REMOTE_CONFIG_URL = "https://firebaseremoteconfig.googleapis.com/v1/projects/" + PROJECT_ID + "/remoteConfig";
    // FIREBASE PARAMETER
    private static final String AUTOMATION_PARAMETER = "global_remote_config_Automation";
    // AUTHENTICATION JSON PATH
    private static final String AUTHENTICATION_PATH = "authentication";
    private static final String AUTHENTICATION_ENABLED = "is_authentication_enabled";
    private static final String SIGN_IN_ENABLED = "can_show_signin_button";
    private static final String SIGN_UP_ENABLED = "can_show_signup_button";
    private static final String COMMON_PATH = "common";
    private static final String CLIENT_ACTIVE = "is_client_active";
    private static final String GUEST_MODE_ENABLED = "is_guest_mode_enabled";
    // HTTP / JSON
    private final HttpClient httpClient;
    private final Gson gson;
    public FirebaseRemoteConfigClient() {
        httpClient = HttpClient.newHttpClient();
        gson = new GsonBuilder().setPrettyPrinting().create();
    }
    // ACCESS TOKEN
    private String getAccessToken() throws IOException {
        GoogleCredentials credentials = GoogleCredentials
                        .getApplicationDefault()
                        .createScoped(
                                "https://www.googleapis.com/auth/cloud-platform"
                        );
        credentials.refreshIfExpired();
        if (credentials.getAccessToken() == null) {
            throw new RuntimeException(
                    "Unable to obtain Google Cloud access token."
            );
        }
        return credentials
                .getAccessToken()
                .getTokenValue();
    }
    // GET CURRENT REMOTE CONFIG
    public RemoteConfigTemplate getRemoteConfig()
            throws IOException, InterruptedException {
        String accessToken = getAccessToken();
        HttpRequest request =
                HttpRequest.newBuilder()
                        .uri(URI.create(REMOTE_CONFIG_URL))
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .header(
                                "Accept-Encoding",
                                "gzip"
                        )
                        .header(
                                "x-goog-user-project",
                                QUOTA_PROJECT_ID
                        )
                        .GET()
                        .build();
        HttpResponse<byte[]> response =
                httpClient.send(request, HttpResponse.BodyHandlers.ofByteArray());
        if (response.statusCode() != 200) {
            String errorBody =
                    new String(
                            response.body(),
                            java.nio.charset.StandardCharsets.UTF_8
                    );
            throw new RuntimeException(
                    "Failed to GET Firebase Remote Config.\n"
                            + "Status: "
                            + response.statusCode()
                            + "\nResponse: "
                            + errorBody
            );
        }
        String responseBody = decodeResponseBody(response);
        JsonObject json;
        try {
            json =
                    JsonParser
                            .parseString(responseBody)
                            .getAsJsonObject();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Firebase Remote Config response is not valid JSON.\n"
                            + "Content-Encoding: "
                            + response.headers()
                            .firstValue("Content-Encoding")
                            .orElse("none")
                            + "\nResponse preview: "
                            + responseBody.substring(
                            0,
                            Math.min(
                                    responseBody.length(),
                                    500
                            )
                    ),
                    e
            );
        }
        String etag =
                response.headers()
                        .firstValue("ETag")
                        .orElse(null);

        return new RemoteConfigTemplate(
                json,
                etag
        );
    }
    private String decodeResponseBody(
            HttpResponse<byte[]> response
    ) throws IOException {

        byte[] body = response.body();

        String contentEncoding =
                response.headers()
                        .firstValue("Content-Encoding")
                        .orElse("")
                        .toLowerCase();

        if (contentEncoding.contains("gzip")) {

            try (
                    java.util.zip.GZIPInputStream gzipInputStream =
                            new java.util.zip.GZIPInputStream(
                                    new java.io.ByteArrayInputStream(body)
                            );

                    java.io.ByteArrayOutputStream output =
                            new java.io.ByteArrayOutputStream()
            ) {

                byte[] buffer =
                        new byte[8192];

                int length;

                while (
                        (length = gzipInputStream.read(buffer))
                                != -1
                ) {

                    output.write(
                            buffer,
                            0,
                            length
                    );
                }

                return output.toString(
                        java.nio.charset.StandardCharsets.UTF_8
                );
            }
        }
        return new String(
                body,
                java.nio.charset.StandardCharsets.UTF_8
        );
    }
    // GET ETAG
    private String getEtag(
            HttpResponse<?> response
    ) {
        return response.headers()
                .firstValue("etag")
                .orElseGet(
                        () ->
                                response.headers()
                                        .firstValue("ETag")
                                        .orElse(null)
                );
    }

    // GET AUTOMATION JSON
    public JsonObject getAutomationConfig(JsonObject remoteConfig) {
        if (!remoteConfig.has("parameters")) {
            throw new RuntimeException("Firebase Remote Config does not contain 'parameters'.");}
        JsonObject parameters = remoteConfig.getAsJsonObject("parameters");
        if (!parameters.has(AUTOMATION_PARAMETER)) {
            throw new RuntimeException(
                    "Firebase parameter not found: "
                            + AUTOMATION_PARAMETER
            );
        }
        JsonObject automationParameter = parameters.getAsJsonObject(AUTOMATION_PARAMETER);
        if (!automationParameter.has("defaultValue")) {
            throw new RuntimeException(
                    "Firebase parameter '"
                            + AUTOMATION_PARAMETER
                            + "' does not contain defaultValue."
            );
        }
        JsonObject defaultValue = automationParameter.getAsJsonObject("defaultValue");
        if (!defaultValue.has("value")) {
            throw new RuntimeException(
                    "Firebase parameter '"
                            + AUTOMATION_PARAMETER
                            + "' does not contain defaultValue.value."
            );
        }
        String value = defaultValue.get("value").getAsString();
        if (value == null || value.isBlank()) {
            throw new RuntimeException(
                    "Firebase parameter '"
                            + AUTOMATION_PARAMETER
                            + "' has an empty value."
            );
        }
        try {
            return JsonParser
                    .parseString(value)
                    .getAsJsonObject();

        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to parse JSON stored inside Firebase parameter '"
                            + AUTOMATION_PARAMETER
                            + "'.",
                    e
            );
        }
    }
    // GET BOOLEAN FROM AUTOMATION CONFIG
    public boolean getAutomationBoolean(JsonObject remoteConfig, String path) {
        JsonObject automationConfig = getAutomationConfig(remoteConfig);
        JsonElement current = automationConfig;
        for (String key : path.split("\\.")) {
            if (!current.isJsonObject()) {
                throw new RuntimeException("Invalid Firebase JSON path: "
                                + path
                );
            }
            JsonObject object = current.getAsJsonObject();
            if (!object.has(key)) {
                throw new RuntimeException(
                        "Firebase JSON path not found: "
                                + path
                );
            }
            current = object.get(key);
        }
        if (current.isJsonPrimitive()) {
            return current.getAsBoolean();
        }
        throw new RuntimeException(
                "Firebase value at path '"
                        + path
                        + "' is not a boolean."
        );
    }
    // BACKWARD COMPATIBLE getBoolean()
    public boolean getBoolean(
            JsonObject root,
            String path
    ) {
        return getAutomationBoolean(
                root,
                path
        );
    }
    public void updateClientActive(boolean enabled)
            throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(template.getJson());

        JsonObject common;

        if (automationConfig.has("common")) {
            common = automationConfig.getAsJsonObject("common");
        } else {
            common = new JsonObject();
            automationConfig.add("common", common);
        }

        common.addProperty("is_client_active", enabled);

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(template);
    }
    // UPDATE AUTHENTICATION CONFIGURATION 2 test case
    public void updateAuthenticationConfiguration(
            boolean authenticationEnabled,
            boolean signInEnabled,
            boolean signUpEnabled
    ) throws IOException, InterruptedException {
        RemoteConfigTemplate template = getRemoteConfig();
        JsonObject automationConfig = getAutomationConfig(template.getJson());
        JsonObject authentication;
        if (automationConfig.has(AUTHENTICATION_PATH)
        ) {authentication =
                    automationConfig
                            .getAsJsonObject(
                                    AUTHENTICATION_PATH
                            );

        } else {

            authentication =
                    new JsonObject();

            automationConfig.add(
                    AUTHENTICATION_PATH,
                    authentication
            );
        }

        authentication.addProperty(
                AUTHENTICATION_ENABLED,
                authenticationEnabled
        );

        authentication.addProperty(
                SIGN_IN_ENABLED,
                signInEnabled
        );

        authentication.addProperty(
                SIGN_UP_ENABLED,
                signUpEnabled
        );

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(
                template
        );


    }
    //3 testcase
    public void updateAuthenticationEnabled(
            boolean enabled
    ) throws IOException, InterruptedException {

        RemoteConfigTemplate template =
                getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(
                        template.getJson()
                );

        JsonObject authentication;

        if (automationConfig.has(AUTHENTICATION_PATH)) {

            authentication =
                    automationConfig.getAsJsonObject(
                            AUTHENTICATION_PATH
                    );

        } else {

            authentication =
                    new JsonObject();

            automationConfig.add(
                    AUTHENTICATION_PATH,
                    authentication
            );
        }

        authentication.addProperty(
                AUTHENTICATION_ENABLED,
                enabled
        );

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(
                template
        );
    }
    //4 test case
    public void updateGuestModeEnabled(
            boolean enabled
    ) throws IOException, InterruptedException {

        RemoteConfigTemplate template =
                getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(template.getJson());

        JsonObject common;

        if (automationConfig.has(AUTHENTICATION_PATH)) {

            common =
                    automationConfig.getAsJsonObject(
                            AUTHENTICATION_PATH
                    );

        } else {

            common = new JsonObject();

            automationConfig.add(
                    AUTHENTICATION_PATH,
                    common
            );
        }

        common.addProperty(
                GUEST_MODE_ENABLED,
                enabled
        );

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(template);
    }
    public void updateGuestModeAndAuthentication(
            boolean guestModeEnabled,
            boolean authenticationEnabled
    ) throws IOException, InterruptedException {

        RemoteConfigTemplate template =
                getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(
                        template.getJson()
                );

        JsonObject authentication;

        if (automationConfig.has(AUTHENTICATION_PATH)) {

            authentication =
                    automationConfig.getAsJsonObject(
                            AUTHENTICATION_PATH
                    );

        } else {

            authentication =
                    new JsonObject();

            automationConfig.add(
                    AUTHENTICATION_PATH,
                    authentication
            );
        }

        // Update authentication flag
        authentication.addProperty(
                AUTHENTICATION_ENABLED,
                authenticationEnabled
        );

        // Update guest mode flag
        authentication.addProperty(
                "is_guest_mode_enabled",
                guestModeEnabled
        );

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(
                template
        );
    }
    // UPDATE AUTOMATION PARAMETER
    private void updateAutomationParameterValue(
            JsonObject remoteConfig,
            JsonObject automationConfig
    ) {

        if (!remoteConfig.has("parameters")) {

            throw new RuntimeException(
                    "Remote Config does not contain parameters."
            );
        }

        JsonObject parameters =
                remoteConfig
                        .getAsJsonObject(
                                "parameters"
                        );

        if (!parameters.has(AUTOMATION_PARAMETER)) {

            throw new RuntimeException(
                    "Firebase parameter not found: "
                            + AUTOMATION_PARAMETER
            );
        }

        JsonObject automationParameter =
                parameters
                        .getAsJsonObject(
                                AUTOMATION_PARAMETER
                        );

        JsonObject defaultValue =
                automationParameter
                        .getAsJsonObject(
                                "defaultValue"
                        );

        defaultValue.addProperty(
                "value",
                gson.toJson(
                        automationConfig
                )
        );
    }

    // PUT REMOTE CONFIG
    private void putRemoteConfig(
            RemoteConfigTemplate template
    ) throws IOException, InterruptedException {

        String etag =
                template.getEtag();

        if (etag == null || etag.isBlank()) {

            throw new RuntimeException(
                    "Cannot update Firebase Remote Config because ETag is missing."
            );
        }

        String accessToken =
                getAccessToken();

        HttpRequest request =
                HttpRequest
                        .newBuilder()
                        .uri(
                                URI.create(
                                        REMOTE_CONFIG_URL
                                )
                        )
                        .header(
                                "Authorization",
                                "Bearer " + accessToken
                        )
                        .header(
                                "Content-Type",
                                "application/json; charset=UTF-8"
                        )
                        .header(
                                "Accept",
                                "application/json"
                        )
                        .header(
                                "Accept-Encoding",
                                "gzip"
                        )
                        .header(
                                "x-goog-user-project",
                                QUOTA_PROJECT_ID
                        )
                        .header(
                                "If-Match",
                                etag
                        )
                        .PUT(
                                HttpRequest.BodyPublishers.ofString(
                                        gson.toJson(
                                                template.getJson()
                                        )
                                )
                        )
                        .build();

        HttpResponse<String> response =
                httpClient.send(
                        request,
                        HttpResponse.BodyHandlers.ofString()
                );

        if (response.statusCode() != 200) {

            throw new RuntimeException(
                    "Failed to UPDATE Firebase Remote Config.\n"
                            + "Status: "
                            + response.statusCode()
                            + "\nResponse: "
                            + response.body()
            );
        }

        String newEtag =
                getEtag(response);

        if (newEtag == null || newEtag.isBlank()) {

            throw new RuntimeException(
                    "Firebase PUT succeeded but no new ETag was returned."
            );
        }

        template.setEtag(
                newEtag
        );
    }

    // REMOTE CONFIG TEMPLATE
    public static class RemoteConfigTemplate {

        private final JsonObject json;

        private String etag;

        public RemoteConfigTemplate(
                JsonObject json,
                String etag
        ) {

            this.json = json;

            this.etag = etag;
        }

        public JsonObject getJson() {

            return json;
        }

        public String getEtag() {

            return etag;
        }

        public void setEtag(
                String etag
        ) {

            this.etag = etag;
        }
    }

    public JsonObject getAutomationStagingConfig()
            throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();

        JsonObject root = template.getJson();

        // Firebase parameters are inside "parameters"
        JsonObject parameters = root.getAsJsonObject("parameters");

        if (parameters == null) {
            throw new IllegalArgumentException(
                    "Firebase 'parameters' object not found"
            );
        }

        // Get global_remote_config_Automation parameter
        JsonObject stagingParameter =
                parameters.getAsJsonObject("global_remote_config_Automation");

        if (stagingParameter == null) {
            throw new IllegalArgumentException(
                    "Firebase parameter 'global_remote_config_Automation' not found"
            );
        }

        // Get the parameter's default value
        JsonObject defaultValue =
                stagingParameter.getAsJsonObject("defaultValue");

        if (defaultValue == null || !defaultValue.has("value")) {
            throw new IllegalArgumentException(
                    "Default value not found for 'global_remote_config_Automation'"
            );
        }

        String jsonValue =
                defaultValue.get("value").getAsString();

        // Convert parameter string into JSON
        return JsonParser.parseString(jsonValue)
                .getAsJsonObject();
    }
    public String getStagingString(String path)
            throws IOException, InterruptedException {

        JsonObject stagingConfig = getAutomationStagingConfig();

        String[] parts = path.split("\\.");
        JsonObject current = stagingConfig;

        for (int i = 0; i < parts.length - 1; i++) {
            String part = parts[i];

            if (!current.has(part) || !current.get(part).isJsonObject()) {
                throw new IllegalArgumentException(
                        "JSON key not found: " + path
                );
            }

            current = current.getAsJsonObject(part);
        }

        String finalKey = parts[parts.length - 1];

        if (!current.has(finalKey)) {
            throw new IllegalArgumentException(
                    "JSON key not found: " + path
            );
        }

        return current.get(finalKey).getAsString();
    }
    public JsonObject getStagingObject(String path)
            throws IOException, InterruptedException {

        JsonObject stagingConfig = getAutomationStagingConfig();

        String[] parts = path.split("\\.");
        JsonElement current = stagingConfig;

        for (String part : parts) {

            if (!current.isJsonObject()) {
                throw new IllegalArgumentException(
                        "Expected JSON object while reading path: " + path
                );
            }

            JsonObject object = current.getAsJsonObject();

            if (!object.has(part)) {
                throw new IllegalArgumentException(
                        "JSON key not found: " + part +
                                " while reading path: " + path
                );
            }

            current = object.get(part);
        }

        if (!current.isJsonObject()) {
            throw new IllegalArgumentException(
                    "Expected JSON object at path: " + path
            );
        }

        return current.getAsJsonObject();
    }
    public void validateLegalDocs()
            throws IOException, InterruptedException {

        JsonObject legalDocs =
                getStagingObject("common.legal_docs");

        if (!legalDocs.has("privacy_policy")) {
            throw new IllegalArgumentException(
                    "privacy_policy is not configured"
            );
        }

        if (!legalDocs.has("terms_of_use")) {
            throw new IllegalArgumentException(
                    "terms_of_use is not configured"
            );
        }

        getStagingString(
                "common.legal_docs.privacy_policy.en"
        );

        getStagingString(
                "common.legal_docs.terms_of_use.en"
        );
    }

    public void updateAuthenticationAndFavorite(
            boolean authenticationEnabled,
            boolean favoriteEnabled
    ) throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(template.getJson());


        JsonObject authentication;

        if (automationConfig.has("authentication")) {
            authentication =
                    automationConfig.getAsJsonObject("authentication");
        } else {
            authentication = new JsonObject();
            automationConfig.add("authentication", authentication);
        }

        authentication.addProperty(
                "is_authentication_enabled",
                authenticationEnabled
        );



        JsonObject featureFlags;

        if (automationConfig.has("feature_flags")) {
            featureFlags =
                    automationConfig.getAsJsonObject("feature_flags");
        } else {
            featureFlags = new JsonObject();
            automationConfig.add("feature_flags", featureFlags);
        }

        featureFlags.addProperty(
                "is_favorite_enabled",
                favoriteEnabled
        );


        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(template);
    }
    public void updateDeleteAccount(boolean enabled)
            throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(template.getJson());

        // Get platforms
        JsonObject platforms;

        if (automationConfig.has("platforms")
                && automationConfig.get("platforms").isJsonObject()) {

            platforms = automationConfig.getAsJsonObject("platforms");

        } else {

            platforms = new JsonObject();
            automationConfig.add("platforms", platforms);
        }

        // Get web
        JsonObject web;

        if (platforms.has("web")
                && platforms.get("web").isJsonObject()) {

            web = platforms.getAsJsonObject("web");

        } else {

            web = new JsonObject();
            platforms.add("web", web);
        }

        // Update platforms.web.delete_account
        web.addProperty(
                "delete_account",
                enabled
        );

        // Update automation parameter
        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        // Publish to Firebase
        putRemoteConfig(template);
    }
    public boolean getStagingDeleteAccount() {
        try {
            String value = getStagingString(
                    "platforms.web.delete_account"
            );

            return Boolean.parseBoolean(value);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Firebase staging path not found: " +
                            "global_remote_config_Automation.platforms.web.delete_account",
                    e
            );
        }
    }
    public void updateSearchEnabled(
            boolean enabled
    ) throws IOException, InterruptedException {

        RemoteConfigTemplate template =
                getRemoteConfig();

        JsonObject automationConfig =
                getAutomationConfig(
                        template.getJson()
                );

        JsonObject featureFlags;

        if (automationConfig.has("feature_flags")) {

            featureFlags =
                    automationConfig.getAsJsonObject(
                            "feature_flags"
                    );

        } else {

            featureFlags =
                    new JsonObject();

            automationConfig.add(
                    "feature_flags",
                    featureFlags
            );
        }

        featureFlags.addProperty(
                "is_search_enabled",
                enabled
        );

        updateAutomationParameterValue(
                template.getJson(),
                automationConfig
        );

        putRemoteConfig(
                template
        );
    }
    public void updateAutoplayDefaultValue(String value) throws IOException, InterruptedException {
        RemoteConfigTemplate template = getRemoteConfig();
        JsonObject automationConfig = getAutomationConfig(template.getJson());

        JsonObject featureFlags = automationConfig.has("feature_flags")
                ? automationConfig.getAsJsonObject("feature_flags")
                : new JsonObject();

        JsonObject settings = featureFlags.has("settings")
                ? featureFlags.getAsJsonObject("settings")
                : new JsonObject();

        settings.addProperty("autoplay_default_value", value);

        featureFlags.add("settings", settings);
        automationConfig.add("feature_flags", featureFlags);

        updateAutomationParameterValue(template.getJson(), automationConfig);
        putRemoteConfig(template);
    }


    public void updateStagingString(String path, String newValue)
            throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();

        JsonObject root = template.getJson();

        // Get parameters
        if (!root.has("parameters")
                || !root.get("parameters").isJsonObject()) {

            throw new IllegalStateException(
                    "Firebase Remote Config does not contain parameters"
            );
        }

        JsonObject parameters =
                root.getAsJsonObject("parameters");

        // Get global_remote_config_staging
        if (!parameters.has("global_remote_config_staging")
                || !parameters
                .get("global_remote_config_staging")
                .isJsonObject()) {

            throw new IllegalStateException(
                    "global_remote_config_staging not found"
            );
        }

        JsonObject staging =
                parameters.getAsJsonObject(
                        "global_remote_config_staging"
                );

        // Get defaultValue
        JsonObject defaultValue =
                staging.getAsJsonObject("defaultValue");

        // Get the actual configuration JSON string
        String rawValue =
                defaultValue.get("value").getAsString();

        // Convert JSON string into JsonObject
        JsonObject config =
                JsonParser.parseString(rawValue)
                        .getAsJsonObject();

        // Navigate through path
        String[] keys = path.split("\\.");

        JsonObject current = config;

        for (int i = 0; i < keys.length - 1; i++) {

            String key = keys[i];

            if (!current.has(key)
                    || !current.get(key).isJsonObject()) {

                throw new IllegalArgumentException(
                        "Firebase config path not found: " + path
                );
            }

            current =
                    current.getAsJsonObject(key);
        }

        // Update final key
        String finalKey =
                keys[keys.length - 1];

        current.addProperty(
                finalKey,
                newValue
        );

        // Put modified JSON back into Firebase parameter
        defaultValue.addProperty(
                "value",
                config.toString()
        );

        // Publish updated Remote Config
        putRemoteConfig(template);

        System.out.println(
                "Firebase staging config updated: "
                        + path
                        + " = "
                        + newValue
        );
    }


    public List<String> getStagingStringList(String path)
            throws IOException, InterruptedException {

        RemoteConfigTemplate template = getRemoteConfig();
        JsonObject json = template.getJson();

        // Firebase Remote Config parameters
        JsonObject parameters = json.getAsJsonObject("parameters");

        // Your staging configuration is stored here
        JsonObject stagingParameter =
                parameters.getAsJsonObject("global_remote_config_Automation");

        // Get defaultValue
        JsonObject defaultValue =
                stagingParameter.getAsJsonObject("defaultValue");

        // The actual configuration is stored as a JSON string
        String rawValue =
                defaultValue.get("value").getAsString();

        // Convert the JSON string into a JsonObject
        JsonObject configJson =
                JsonParser.parseString(rawValue).getAsJsonObject();

        // Navigate using the requested path
        String[] keys = path.split("\\.");
        JsonElement current = configJson;

        for (String key : keys) {

            if (!current.isJsonObject()) {
                return Collections.emptyList();
            }

            JsonObject currentObject = current.getAsJsonObject();

            if (!currentObject.has(key)) {
                return Collections.emptyList();
            }

            current = currentObject.get(key);
        }

        // Make sure the final value is an array
        if (!current.isJsonArray()) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();

        for (JsonElement element : current.getAsJsonArray()) {
            result.add(element.getAsString());
        }

        return result;
    }




}