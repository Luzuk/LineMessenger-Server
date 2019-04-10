package com.buchach.lyceum.messenger.auth.model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String login;
    private String pass;
    private List<String> friends;

    public User(String name, String login, String pass) {
        this.name = name;
        this.login = login;
        this.pass = pass;
        friends = new ArrayList<>();
    }

    public String getName(){
        return this.name;
    }
    public String getLogin(){
        return this.login;
    }

    public String getPass(){
        return this.pass;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }
}
