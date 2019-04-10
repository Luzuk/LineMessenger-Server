package com.buchach.lyceum.messenger.auth;

import com.buchach.lyceum.messenger.auth.model.User;

public class ConnectionSession {
    private long id;
    private User user;

    private static long idCount = 0;

    public ConnectionSession(User user) {
        this.user = user;
        idCount++;
        this.id = idCount;
    }

    public long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }
}
