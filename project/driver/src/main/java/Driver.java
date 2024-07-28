import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Driver {
    private static final int BUFFER_SIZE = 1024;

    private int port;
    private String address;

    public Driver(String address, int port) throws Exception {
        this.address = address;
        this.port = port;
    }

    public String get(String key) throws Exception {
        return send(HttpBuilder.buildGetRequest(key)).split("\r\n\r\n")[1];
    }

    public String set(String key, String value, long ttl) throws Exception {
        return send(HttpBuilder.buildSetRequest(key, value, ttl)).split("\r\n\r\n")[1];
    }

    public String set(String key, String value) throws Exception {
        return send(HttpBuilder.buildSetRequest(key, value)).split("\r\n\r\n")[1];
    }

    public String load() throws Exception {
        return send(HttpBuilder.buildLoadRequest()).split("\r\n\r\n")[1];
    }

    public String dump() throws Exception {
        return send(HttpBuilder.buildDumpRequest()).split("\r\n\r\n")[1];
    }

    public String remove(String key) throws Exception {
        return send(HttpBuilder.buildRemoveRequest(key)).split("\r\n\r\n")[1];
    }

    private String send(String request) throws IOException {
        Socket socket = new Socket(address, port);

        try (OutputStream outputStream = socket.getOutputStream();
             InputStream in = socket.getInputStream()) {
            outputStream.write(request.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

}
