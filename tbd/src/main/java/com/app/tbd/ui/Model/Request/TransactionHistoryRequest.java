package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 9/13/2016.
 */
public class TransactionHistoryRequest {


    private String UserName;
    private String TicketId;
    private String StartDate;
    private String EndDate;

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

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

    public TransactionHistoryRequest() {

    }

    public TransactionHistoryRequest(TransactionHistoryRequest data) {
        UserName = data.getUserName();
        TicketId = data.getTicketId();
        StartDate = data.getStartDate();
        EndDate = data.getEndDate();
    }

}
