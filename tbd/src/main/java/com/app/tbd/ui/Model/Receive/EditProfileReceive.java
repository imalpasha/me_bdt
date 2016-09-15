package com.app.tbd.ui.Model.Receive;

public class EditProfileReceive {
    private final EditProfileReceive userObj;
    private String status;
    private String Message;

    public EditProfileReceive getUserObj() {
        return userObj;
    }

    public EditProfileReceive(EditProfileReceive param_userObj) {
        this.userObj = param_userObj;
        status = param_userObj.getStatus();
        Message = param_userObj.getMessage();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}

