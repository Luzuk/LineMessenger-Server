package com.buchach.lyceum.messenger.server;

import com.buchach.lyceum.messenger.auth.AuthManager;
import com.buchach.lyceum.messenger.auth.ConnectionSession;
import com.buchach.lyceum.messenger.messaging.FriendManager;
import com.buchach.lyceum.messenger.messaging.MessageProcessor;

import java.io.*;
import java.net.Socket;

public class SocketProcessor implements Runnable {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;

    private AuthManager authManager;
    private ConnectionSession session;

    private MessageProcessor messageProcessor;
    private FriendManager friendManager;

    public SocketProcessor(Socket s) throws IOException {
        this.socket = s;
        this.inputStream = s.getInputStream();
        this.outputStream = s.getOutputStream();

        printWriter = new PrintWriter(outputStream, true);
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        authManager = new AuthManager();
        messageProcessor = new MessageProcessor();
        friendManager = new FriendManager();
    }

    @Override
    public void run() {
        try {
            String received = bufferedReader.readLine();
            if (received.startsWith("/auth@") && auth(received)) {
                if (authManager.getSession() != null) {
                    session = authManager.getSession();
                    session.getUser()
                            .getFriends()
                            .addAll(friendManager.parseFriends(session.getUser().getLogin()));
                }
                while (true) {
                    String message = bufferedReader.readLine();
                    if (!checkRegistration(message) & !checkForFriends(message)
                            & !checkDirectChat(message) & !checkForAddFriend(message)) {
                        messageProcessor.startMessaging(message);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDirectChat(String received) {
        if (received != null) {
            if (received.startsWith("/user@")) {
                messageProcessor.sendMessage(received, messageProcessor.startMessagingWith(received).getOutputStream(),
                        session.getUser().getLogin());
                return true;
            }
        }
        return false;
    }

    private boolean checkForFriends(String received) {
        if (received != null) {
            if (received.startsWith("/getFriends")) {
                StringBuilder result = new StringBuilder();
                for (String friend : session.getUser().getFriends()) {
                    result.append("[").append(friend).append("]");
                }
                printWriter.println("Friends: " + result.toString());
                return true;
            }
        }
        return false;
    }

    private boolean checkForAddFriend(String received) throws IOException {
        if (received != null) {
            if (received.startsWith("/addFriend")) {
                printWriter.println(friendManager.addFriend(session.getUser().getLogin(),
                        received.substring(received.indexOf("@") + 1)));
                session.getUser().getFriends().clear();
                session.getUser()
                        .getFriends()
                        .addAll(friendManager.parseFriends(session.getUser().getLogin()));
                return true;
            }
        }
        return false;
    }

    private boolean checkRegistration(String received) throws IOException {
        if (received != null) {
            if (received.startsWith("/register&")) {
                printWriter.println(authManager.registerUser(received.substring(received.indexOf('&') + 1, received.length())));
                return true;
            }
        }
        return false;
    }

    private boolean auth(String authString) throws IOException {
        if (authManager.loginUser(authString.substring(authString.indexOf('@')+ 1, authString.indexOf('%')),
                authString.substring(authString.indexOf('%') + 1, authString.length()))) {
            printWriter.println("Login Successful");
            return true;
        } else {
            printWriter.println("Authorization Failure");
            return false;
        }
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public ConnectionSession getSession() {
        return session;
    }
}
