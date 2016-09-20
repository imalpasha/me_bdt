package com.app.tbd.ui.Model.Receive.TBD;

/*
 * Created by ImalPasha on 11/6/2015.
 */

 /* Response From API */

public class LoginReceive {

    private final LoginReceive userObj;
    private String Status;
    private String Message;
    private String CustomerNumber;
    private String FirstName;
    private String LastName;
    private String Token;
    private String UserName;
    private String Hash;


    public LoginReceive(LoginReceive returnData) {
        this.userObj = returnData;
        Message = returnData.getMessage();
        Status = returnData.getStatus();
        CustomerNumber = returnData.getStatus();
        FirstName = returnData.getStatus();
        LastName = returnData.getStatus();
        Token = returnData.getToken();
        UserName = returnData.getStatus();
        Hash = returnData.getStatus();
    }


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
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

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }

    public LoginReceive getUserObj() {
        return userObj;
    }


}
