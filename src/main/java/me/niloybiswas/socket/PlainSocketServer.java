package me.niloybiswas.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.UUID;

public class PlainSocketServer {

    public static void main(String[] args) {
        int port = 9999;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // Accept incoming client connections
                Socket socket = serverSocket.accept();
                writeClientInfo(socket);
                // Handle the client request in a new thread
                new Thread(() -> handleRequest(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleRequest(Socket socket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            OutputStream outputStream = socket.getOutputStream();

            String header = readHttpHeader(reader);
            if (header == null) return;

            String body = readHttpBody(reader, header);

            System.out.println("\n\nIncoming HTTP Request: \n");
            System.out.println("[HEADER]: " + header);
            System.out.println("[BODY]: " + body);

            String response = writeHttpResponse(outputStream);
            System.out.println("\n\nOutgoing HTTP Response: \n" + response);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String readHttpHeader(BufferedReader reader) throws IOException {
        StringBuilder headerBuilder = new StringBuilder();
        try {
            String line;
            while((line = reader.readLine()) != null && !line.isEmpty()) {
                headerBuilder.append(line).append("\r\n");
            }
            if (headerBuilder.toString().isEmpty()) return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return headerBuilder.toString();
    }

    private static String readHttpBody(BufferedReader reader, String header) {
        StringBuilder bodyBuilder = new StringBuilder();
        try {
            // Read the body (if any)
            if (header.startsWith("POST")) {
                // The body length is specified by the "Content-Length" header
                String contentLengthHeader = header.lines()
                        .filter(line -> line.toLowerCase().startsWith("content-length"))
                        .findFirst().orElse("");
                int contentLength = 0;
                if (!contentLengthHeader.isEmpty()) {
                    contentLength = Integer.parseInt(contentLengthHeader.split(":")[1].trim());
                }
                // Read the specified number of bytes from the input stream
                char[] bodyBuffer = new char[contentLength];
                reader.read(bodyBuffer, 0, contentLength);
                bodyBuilder.append(bodyBuffer);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bodyBuilder.toString();
    }

    private static String writeHttpResponse(OutputStream outputStream) {
        try {
            String sessionId = UUID.randomUUID().toString();
            // Send a response back to the client
            String response = "HTTP/1.1 200 OK\r\n"
                    + "Content-Type: text/html\r\n"
                    + "Content-Length: " + "22\r\n"
                    + "Set-Cookie: JSESSIONID=" + sessionId + "; HttpOnly\r\n"
                    + "\r\n"
                    + "Received your request!";
            outputStream.write(response.getBytes());
            outputStream.flush();
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void writeClientInfo(Socket socket) {
        String clientIP = socket.getInetAddress().getHostAddress();
        int clientPort = socket.getPort();
        System.out.println("ClientInfo: " + clientIP + ":" + clientPort);
    }
}
