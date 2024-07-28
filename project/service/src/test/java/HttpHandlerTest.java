import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class HttpHandlerTest {
    private TestStorage storage;
    private HttpHandler httpHandler;

    @BeforeEach
    void setUp() {
        storage = new TestStorage(); // Используем тестовую реализацию Storage
        httpHandler = new HttpHandler(storage);
    }

    @Test
    void testGetRequestReturnsValue() {
        storage.set("key1", "value1", 1000);

        String request = "GET /get/key1\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("value1"));
    }

    @Test
    void testGetRequestReturnsNotFound() {
        String request = "GET /get/nonExistingKey\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Value not found"));
    }

    @Test
    void testPostSetRequest() {
        String requestBody = "{\"data\":\"newValue\", \"ttl\":1000}";
        String request = "POST /set/key2\r\n\r\n" + requestBody;
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Value has been set successfully"));
    }

    @Test
    void testPostLoadRequest() {
        // Изначально хранилище пустое.
        String request = "POST /load\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Storage loaded successfully"));
    }

    @Test
    void testDeleteRequest() {
        storage.set("key3", "value3", 1000);

        String request = "DELETE /key3\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("value3"));
    }

    @Test
    void testDeleteNonExistingKey() {
        String request = "DELETE /nonExistingKey\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 200 OK"));
        assertTrue(response.contains("Value not found"));
    }

    @Test
    void testServerError() {
        storage.setShouldThrowError(true);

        String request = "POST /load\r\n\r\n";
        String response = httpHandler.handleHttp(request);

        assertTrue(response.contains("HTTP/1.1 500 Internal Server Error"));
        assertTrue(response.contains("Server error"));
    }

    private static class TestStorage extends Storage {
        private boolean shouldThrowError = false;

        public void setShouldThrowError(boolean shouldThrowError) {
            this.shouldThrowError = shouldThrowError;
        }

        @Override
        public String load() {
            if (shouldThrowError) {
                throw new RuntimeException("Server Error");
            }
            return "Storage loaded successfully";
        }
    }
}
