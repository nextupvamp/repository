public class JSONBuilder {
    public static String buildJson(String value, long ttl) {
        return "{\"data\": \"" + value + "\", \"ttl\": " + ttl + "}";
    }

    public static String buildJson(String value) {
        return "{\"data\": \"" + value + "\"}";
    }
}
