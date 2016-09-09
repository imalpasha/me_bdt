package com.app.tbd.ui.Model.Request;

public class ViewUserRequest {

    String UserName;
    String Password;
    String TicketId;

    public ViewUserRequest() { }

    public ViewUserRequest(ViewUserRequest data) {
        UserName = data.getUserName();
        Password = data.getPassword();
        TicketId = data.getTicketId();
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }


    /*Response Data From Server*/
    String status;

    public String getStatus() {
        return status;
    }
}
