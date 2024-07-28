import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonHandlerTest {

    @Test
    void testParseDataJsonSuccessfully() throws ParseException {
        String json = "{\"data\":\"value\",\"ttl\":1000}";
        String result = JsonHandler.parseDataJson(json);
        assertEquals("value", result);
    }

    @Test
    void testParseDataJsonThrowsParseException() {
        String json = "{\"ttl\":1000}";
        assertThrows(ParseException.class, () -> JsonHandler.parseDataJson(json));
    }

    @Test
    void testParseTtlJsonSuccessfully() throws ParseException {
        String json = "{\"data\":\"value\",\"ttl\":2000}";
        long ttl = JsonHandler.parseTtlJson(json);
        assertEquals(2000, ttl);
    }

    @Test
    void testParseTtlJsonUsesDefaultTtl() throws ParseException {
        String json = "{\"data\":\"value\"}";
        long ttl = JsonHandler.parseTtlJson(json);
        assertEquals(Record.DEFAULT_TTL, ttl);
    }

    @Test
    void testParseTtlJsonThrowsParseException() {
        String json = "{\"data\":\"value\",\"ttl\":\"notALong\"}";
        assertThrows(ParseException.class, () -> JsonHandler.parseTtlJson(json));
    }
}