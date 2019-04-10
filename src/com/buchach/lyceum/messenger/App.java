package com.buchach.lyceum.messenger;

import com.buchach.lyceum.messenger.server.SocketProcessor;
import com.buchach.lyceum.messenger.util.Util;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class App {
    private ServerSocket serverSocket;
    private Socket clientSocket;

    public void start(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started successfully");
        while (true) {
            clientSocket = serverSocket.accept();
            System.out.println("Client Accepted");

            SocketProcessor socketProcessor = new SocketProcessor(clientSocket);
            Util.getInstance().addConnection(socketProcessor);
            new Thread(socketProcessor).start();
        }
    }

    public void stop() throws IOException {
        clientSocket.close();
        serverSocket.close();
    }

    public static void main(String[] args) {
        App server = new App();
        try {
            server.start(6666);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                server.stop();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
