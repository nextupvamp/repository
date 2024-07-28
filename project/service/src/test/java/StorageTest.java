import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    private Storage storage;

    @BeforeEach
    void setUp() {
        storage = new Storage();
    }

    @Test
    void testSetAndGet() {
        String result = storage.set("key1", "value1", 1000);
        assertEquals("Value has been set successfully", result);

        String value = storage.get("key1");
        assertEquals("value1", value);
    }

    @Test
    void testSetWithNullKey() {
        String result = storage.set(null, "value", 1000);
        assertEquals("Value setting failure", result);
    }

    @Test
    void testGetNonExistingKey() {
        String value = storage.get("nonExistingKey");
        assertNull(value);
    }

    @Test
    void testRemove() {
        storage.set("key2", "value2", 1000);
        String removedValue = storage.remove("key2");
        assertEquals("value2", removedValue);
        assertNull(storage.get("key2"));
    }

    @Test
    void testRemoveNonExistingKey() {
        String removedValue = storage.remove("nonExistingKey");
        assertNull(removedValue);
    }

    @Test
    void testDumpAndLoad() throws IOException {
        storage.set("key3", "value3", 1000);
        String dumpContent = storage.dump();

        assertNotNull(dumpContent);
        assertTrue(dumpContent.contains("key3"));

        storage.remove("key3");

        assertEquals("Storage loaded successfully", storage.load());

        String loadedValue = storage.get("key3");
        assertEquals("value3", loadedValue);
    }

    @Test
    void testRemoveExpired() throws InterruptedException {
        storage.set("key4", "value4", 500);
        Thread.sleep(2000);

        assertNull(storage.get("key4"));
    }
}
