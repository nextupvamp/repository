import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class HttpService {
    private static final int BUFFER_SIZE = 1024;

    private HttpHandler httpHandler;

    public HttpService(Storage storage) {
        httpHandler = new HttpHandler(storage);
    }

    public void start() throws IOException, ParseException, ExecutionException, InterruptedException {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        server.bind(new InetSocketAddress("127.0.0.1", 8080));
        while (true) {
            Future<AsynchronousSocketChannel> future = server.accept();
            handle(future);
        }
    }

    public void handle(Future<AsynchronousSocketChannel> future)
            throws ExecutionException, InterruptedException, IOException {
        AsynchronousSocketChannel clientChannel = future.get();

        StringBuilder builder = new StringBuilder();
        while (clientChannel != null && clientChannel.isOpen()) {
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            boolean keepReading = true;
            while (keepReading) {
                clientChannel.read(buffer).get();

                int position = buffer.position();
                keepReading = position == BUFFER_SIZE;

                byte[] array = keepReading ? buffer.array() : Arrays.copyOf(buffer.array(), position);

                builder.append(new String(array));
                buffer.clear();
            }

            ByteBuffer responseBuffer = ByteBuffer.wrap(httpHandler.handleHttp(builder.toString()).getBytes());
            clientChannel.write(responseBuffer);
            clientChannel.close();
        }
    }

    public static void main(String[] args) {
        try {
            new HttpService(new Storage()).start();
        } catch (IOException | ParseException | ExecutionException | InterruptedException e) {
            System.out.println("Unhandled error occurred");
            e.printStackTrace();
        }
    }
}

