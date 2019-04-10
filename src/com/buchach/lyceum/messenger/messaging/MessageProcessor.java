package com.buchach.lyceum.messenger.messaging;

import com.buchach.lyceum.messenger.server.SocketProcessor;
import com.buchach.lyceum.messenger.util.Util;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class MessageProcessor {

    public void startMessaging(String message) {
        for (SocketProcessor connection : Util.getInstance().getAllConnections()) {
            new PrintWriter(connection.getOutputStream(), true).println(message);
        }
    }

    public SocketProcessor startMessagingWith(String received) {
        String username = received.substring(received.indexOf('@') + 1, received.indexOf('%'));
        List<SocketProcessor> singleton = Util.getInstance()
                .getAllConnections()
                .stream()
                .filter(socketProcessor -> socketProcessor.getSession().getUser().getLogin().equals(username))
                .collect(Collectors.toList());
        return singleton.get(0);
    }


    public boolean sendMessage(String message, OutputStream receiver, String sender) {
        new PrintWriter(receiver, true).println(sender + ": " + parseMessage(message));
        return true;
    }

    private String parseMessage(String received) {
        return received.substring(received.indexOf('%') + 1);
    }
}
