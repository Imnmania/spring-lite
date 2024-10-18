package me.niloybiswas.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
