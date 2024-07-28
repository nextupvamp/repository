import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonHandler {
    public static String parseDataJson(String json) throws ParseException {
        Object obj = new JSONParser().parse(json);
        JSONObject jsonObject = (JSONObject) obj;

        String data = (String) jsonObject.get("data");
        if (data == null) throw new ParseException(1);

        return data;
    }

    public static long parseTtlJson(String json) throws ParseException {
        Object obj = new JSONParser().parse(json);
        JSONObject jsonObject = (JSONObject) obj;

        Long ttl = null;
        try {
            ttl = (Long) jsonObject.get("ttl");
        } catch (ClassCastException e) {
            throw new ParseException(1);
        }

        return ttl != null ? ttl : Record.DEFAULT_TTL;
    }
}
