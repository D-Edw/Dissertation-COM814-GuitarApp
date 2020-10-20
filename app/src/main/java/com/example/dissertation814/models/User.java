package com.example.dissertation814.models;

public class User {

    public String userId;
    public String name;
    public String email;
    public String account;

    //constructor required for calls to DataSnapshot.getValue(User.class)
    public User(){
    }

    public User(String userId, String name, String email, String account) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.account = account;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
