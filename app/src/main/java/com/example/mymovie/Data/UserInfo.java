package com.example.mymovie.Data;

public class UserInfo {

    private String id;
    private String pw;
    private String nickName;

    public UserInfo(){}

    public UserInfo(String id, String pw, String nickName) {
        this.id = id;
        this.pw = pw;
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
