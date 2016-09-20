package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 9/9/2016.
 */
public class ResetPasswordRequest {


    private String Token;
    private String UserName;
    private String Password;
    private String NewPassword;

    public ResetPasswordRequest(){

    }

    public ResetPasswordRequest(ResetPasswordRequest obj){
        Token = obj.getToken();
        UserName = obj.getUserName();
        Password = obj.getOldPassword();
        NewPassword = obj.getNewPassword();
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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
