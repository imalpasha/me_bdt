package com.app.tbd.ui.Model.Request.TBD;

/**
 * Created by Dell on 11/4/2015.
 */
public class BigPointRequest {

    String UserName;
    String TicketId;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    /*Initiate Class*/
    public BigPointRequest() {
    }

    public BigPointRequest(BigPointRequest data) {
        UserName = data.getUserName();
        TicketId = data.getTicketId();
    }

}
