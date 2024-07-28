import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.lang.invoke.WrongMethodTypeException;

public class HttpHandler {
    private Storage storage;

    public HttpHandler(Storage storage) {
        this.storage = storage;
    }

    public String handleHttp(String request) {
        String[] requestParts = request.split("\r\n\r\n");
        String header = requestParts[0];
        String body = requestParts.length > 1 ? requestParts[1] : null;

        String firstLine = header.split("\n")[0];
        System.out.println("Received request: " + firstLine);

        String response;
        boolean file = false;
        try {
            if (firstLine.startsWith("GET")) {
                String operation = firstLine.substring(5);
                if (operation.startsWith("get")) {
                    String key = firstLine.split(" ")[1].substring(5);
                    String value = storage.get(key);
                    response = value != null ? value : "Value not found";
                } else if (operation.startsWith("dump")) {
                    response = storage.dump();
                    file = true;
                } else {
                    throw new WrongMethodTypeException();
                }
            } else if (firstLine.startsWith("POST")) {
                String operation = firstLine.substring(6);
                if (operation.startsWith("set")) {
                    String key = firstLine.split(" ")[1].substring(5);
                    response = storage.set(key, JsonHandler.parseDataJson(body), JsonHandler.parseTtlJson(body));
                } else if (operation.startsWith("load")) {
                    response = storage.load();
                } else {
                    throw new WrongMethodTypeException();
                }
            } else if (firstLine.startsWith("DELETE")) {
                String key = firstLine.split(" ")[1].substring(1);
                String value = storage.remove(key);
                response = value != null ? value : "Value not found";
            } else {
                throw new WrongMethodTypeException();
            }
        } catch (Exception e) {
            return buildErrorResponse(e);
        }
        return buildSuccessfulResponse(response, file);
    }

    private String buildSuccessfulResponse(String response, boolean file) {
        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("HTTP/1.1 200 OK\r\n");
        responseBuilder.append("Content-Type: text/plain\r\n");
        if (file) {
            responseBuilder.append("Content-Disposition: attachment; filename=\"dump.txt\"\r\n");
        }
        responseBuilder.append("Content-Length: " + response.length() + "\r\n\r\n");
        responseBuilder.append(response);
        return responseBuilder.toString();
    }

    private String buildErrorResponse(Exception e) {
        StringBuilder responseBuilder = new StringBuilder();
        String errorMsg;
        responseBuilder.append("HTTP/1.1 ");
        if (e instanceof ParseException) {
            errorMsg = "Wrong JSON format";
            responseBuilder.append("400 Bad Request\r\n");
        } else if (e instanceof WrongMethodTypeException) {
            errorMsg = "Method not found";
            responseBuilder.append("400 Bad Request\r\n");
        } else if (e instanceof IOException) {
            errorMsg = "Dump file is not exists";
            responseBuilder.append("428 Precondition Required\r\n");
        } else {
            errorMsg = "Server error";
            responseBuilder.append("500 Internal Server Error\r\n");
        }
        responseBuilder.append("Content-Type: text/plain\r\n");
        responseBuilder.append("Content-Length:" + errorMsg.length() + "\r\n");
        responseBuilder.append("Connection: close\r\n\r\n");
        responseBuilder.append(errorMsg);
        System.out.println(responseBuilder.toString());
        return responseBuilder.toString();
    }
}
