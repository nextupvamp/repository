public class HttpBuilder {
    public static String buildGetRequest(String key) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("GET /get/");
        requestBuilder.append(key);
        requestBuilder.append(" HTTP/1.1");
        return requestBuilder.toString();
    }

    public static String buildDumpRequest() {
        return "GET /dump HTTP/1.1";
    }

    public static String buildSetRequest(String key, String value, long ttl) {
        StringBuilder requestBuilder = new StringBuilder();
        String json = JSONBuilder.buildJson(value, ttl);
        requestBuilder.append("POST /set/" + key + " HTTP/1.1\r\n");
        requestBuilder.append("Content-Type: application/json\r\n");
        requestBuilder.append("Content-Length: " + json.length() + "\r\n\r\n");
        requestBuilder.append(json);
        return requestBuilder.toString();
    }

    public static String buildSetRequest(String key, String value) {
        StringBuilder requestBuilder = new StringBuilder();
        String json = JSONBuilder.buildJson(value);
        requestBuilder.append("POST /set/" + key + " HTTP/1.1\r\n");
        requestBuilder.append("Content-Type: application/json\r\n");
        requestBuilder.append("Content-Length: " + json.length() + "\r\n\r\n");
        requestBuilder.append(json);
        return requestBuilder.toString();
    }

    public static String buildLoadRequest() {
        return "POST /load HTTP/1.1";
    }

    public static String buildRemoveRequest(String key) {
        StringBuilder requestBuilder = new StringBuilder();
        requestBuilder.append("DELETE /");
        requestBuilder.append(key);
        requestBuilder.append(" HTTP/1.1");
        return requestBuilder.toString();
    }
}