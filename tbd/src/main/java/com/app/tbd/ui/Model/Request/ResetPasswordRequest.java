package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 9/9/2016.
 */
public class ResetPasswordRequest {


    private String TicketId;
    private String UserName;
    private String Password;
    private String NewPassword;

    public ResetPasswordRequest(){

    }

    public ResetPasswordRequest(ResetPasswordRequest obj){
        TicketId = obj.getTicketId();
        UserName = obj.getUserName();
        Password = obj.getOldPassword();
        NewPassword = obj.getNewPassword();
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    public String getOldPassword() {
        return Password;
    }

    public void setOldPassword(String oldPassword) {
        Password = oldPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

}
