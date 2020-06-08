package com.example.mymovie.Data;

public class ChatData {

    private String userName;
    private String message;

    // users does not define no argument constructor  생성자를 하나더 추가해주어야한다.
    public ChatData(){}

    public ChatData(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
