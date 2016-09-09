package com.app.tbd.ui.Model.Receive;

/**
 * Created by Dell on 9/9/2016.
 */
public class ResetPasswordReceive {

    private String Status;
    private String Message;

    public ResetPasswordReceive(ResetPasswordReceive return_data) {
        Status = return_data.getStatus();
        Message = return_data.getMessage();
    }

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

}
