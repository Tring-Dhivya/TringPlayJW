package utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.testng.SkipException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class ClientExpectationReader {

    private final JsonObject expectation;

    public ClientExpectationReader() {

        InputStream inputStream =
                getClass()
                        .getClassLoader()
                        .getResourceAsStream(
                                "client_expectation/ClientExpectation.json"
                        );

        if (inputStream == null) {
            throw new RuntimeException(
                    "ClientExpectation.json not found in src/test/resources"
            );
        }

        expectation =
                JsonParser.parseReader(
                                new InputStreamReader(
                                        inputStream,
                                        StandardCharsets.UTF_8
                                )
                        )
                        .getAsJsonObject();
    }

    public boolean getBoolean(String path) {

        String[] parts = path.split("\\.");

        JsonObject current = expectation;

        for (int i = 0; i < parts.length - 1; i++) {

            if (!current.has(parts[i])) {
                throw new RuntimeException(
                        "Path not found in ClientExpectation.json: "
                                + path
                );
            }

            current =
                    current.getAsJsonObject(parts[i]);
        }

        String lastKey =
                parts[parts.length - 1];

        if (!current.has(lastKey)) {
            throw new RuntimeException(
                    "Path not found in ClientExpectation.json: "
                            + path
            );
        }

        return current
                .get(lastKey)
                .getAsBoolean();
    }
    public boolean hasPath(String path) {

        String[] parts = path.split("\\.");
        JsonObject current = expectation;

        for (int i = 0; i < parts.length - 1; i++) {

            if (!current.has(parts[i])
                    || !current.get(parts[i]).isJsonObject()) {
                return false;
            }

            current = current.getAsJsonObject(parts[i]);
        }

        return current.has(parts[parts.length - 1]);
    }


    public void requirePath(String path) {

        if (!hasPath(path)) {
            throw new SkipException(
                    "Skipping test: '" + path +
                            "' is missing in ClientExpectation.json"
            );
        }
    }
}