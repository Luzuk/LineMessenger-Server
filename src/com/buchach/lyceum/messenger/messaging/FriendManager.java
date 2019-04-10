package com.buchach.lyceum.messenger.messaging;

import com.buchach.lyceum.messenger.auth.model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FriendManager {
    private final File FRIENDS_FILE = new File("friends.txt");

    public List<String> parseFriends(String username) throws IOException {
        List<String> friends = new ArrayList<>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(FRIENDS_FILE));

        String string;
        while ((string = bufferedReader.readLine()) != null && string.trim().length() != 0) {
            String firstPart = string.substring(0, string.indexOf('%'));
            String secondPart = string.substring(string.indexOf('%') + 1, string.length() - 1);
            if (firstPart.equals(username)) {
                friends.add(secondPart);
            } else if (secondPart.equals(username)) {
                friends.add(firstPart);
            }
        }
        return friends;
    }

    public String addFriend(String currentUser, String userToAdd) throws IOException {
        PrintWriter printWriter = new PrintWriter(new FileWriter(FRIENDS_FILE, true));
        if (checkAvailability(currentUser, userToAdd)) {
            printWriter.flush();
            printWriter.println(currentUser + "%" + userToAdd + "%");
            printWriter.close();
            return "Friend added";
        }
        return "Friend is not added";
    }

    private boolean checkAvailability(String currentUser, String friendToAdd) throws IOException {
        for(String friend : parseFriends(currentUser)){
            if(friend.equals(friendToAdd))
                return false;
        }
        return true;
    }
}
