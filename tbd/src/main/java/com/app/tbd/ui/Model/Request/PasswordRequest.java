package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 11/4/2015.
 */
public class PasswordRequest {


    String UserName;

    /*Initiate Class*/
    public PasswordRequest() {
    }

    public PasswordRequest(PasswordRequest data) {
        UserName = data.getUserName();
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

}
