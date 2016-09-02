package com.app.tbd.ui.Model.Receive;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class ForgotPasswordReceive {

    private final ForgotPasswordReceive userObj;

    private String Status;
    private String Message;


    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ForgotPasswordReceive(ForgotPasswordReceive param_userObj) {
        this.userObj = param_userObj;
    }

    public ForgotPasswordReceive getUserObj() {
        return userObj;
    }


}
