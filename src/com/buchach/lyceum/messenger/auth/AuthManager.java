package com.buchach.lyceum.messenger.auth;

import com.buchach.lyceum.messenger.auth.model.User;
import com.buchach.lyceum.messenger.messaging.FriendManager;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AuthManager {
    private final File usersFile = new File("users.txt");
    private ConnectionSession session = null;

    public boolean loginUser(String login, String pass) throws IOException {
        for (User user : parseUsers()) {
            if (user.getLogin().equals(login) && user.getPass().equals(pass)) {
                setSession(user);
                return true;
            }
        }
        return false;
    }

    private List<User> parseUsers() throws IOException {
        List<User> users = new ArrayList<>();
        BufferedReader bf = new BufferedReader(new FileReader(usersFile));
        String string;
        while ((string = bf.readLine()) != null) {
            users.add(parseUser(string));
        }

        return users;
    }

    private User parseUser(String received) {
        String name = received.substring(0, received.indexOf('%'));
        received = received.substring(received.indexOf('%') + 1, received.length());
        String login = received.substring(0, received.indexOf('%'));
        received = received.substring(received.indexOf('%') + 1, received.length());
        String pass = received.substring(0, received.indexOf('%'));

        return new User(name, login, pass);
    }

    public String registerUser(String registerString) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(usersFile, true));
        if(checkAvailability(registerString)) {
            printWriter.append("\n").append(registerString);
            printWriter.close();
            return "Registered Successful";
        }
        return "Registration Failure";
    }

    private boolean checkAvailability(String registerString) throws IOException {
        User toRegister = parseUser(registerString);
        for (User user : parseUsers()) {
            if (user.getLogin().equals(toRegister.getLogin())) {
                return false;
            }
        }
        return true;
    }

    private void setSession(User user){
        session = new ConnectionSession(user);
    }

    public ConnectionSession getSession() {
        return this.session;
    }
}
