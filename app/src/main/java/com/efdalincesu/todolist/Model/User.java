package com.efdalincesu.todolist.Model;

public class User {
    private String user_id;
    private String user_mail;

    public User() {

    }

    public User(String user_id, String user_mail) {
        this.user_id = user_id;
        this.user_mail = user_mail;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

    public void set(){
        user_id="-1";
        user_mail="-1";
    }
}
