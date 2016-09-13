package com.app.tbd.ui.Model.Receive;

/**
 * Created by Dell on 9/13/2016.
 */
public class TransactionHistoryReceive {

    private String Message;
    private String Status;

    private String ReturnValue;
    private String ExpiryPts1;
    private String ExpiryPts2;
    private String ExpiryPts3;
    private String ExpiryPts4;
    private String ExpiryPts5;
    private String ExpiryPts6;
    private String ExpiryMonth1;
    private String ExpiryMonth2;
    private String ExpiryMonth3;
    private String ExpiryMonth4;
    private String ExpiryMonth5;
    private String ExpiryMonth6;

    public String getReturnValue() {
        return ReturnValue;
    }

    public void setReturnValue(String returnValue) {
        ReturnValue = returnValue;
    }

    public String getExpiryPts1() {
        return ExpiryPts1;
    }

    public void setExpiryPts1(String expiryPts1) {
        ExpiryPts1 = expiryPts1;
    }

    public String getExpiryPts2() {
        return ExpiryPts2;
    }

    public void setExpiryPts2(String expiryPts2) {
        ExpiryPts2 = expiryPts2;
    }

    public String getExpiryPts3() {
        return ExpiryPts3;
    }

    public void setExpiryPts3(String expiryPts3) {
        ExpiryPts3 = expiryPts3;
    }

    public String getExpiryPts4() {
        return ExpiryPts4;
    }

    public void setExpiryPts4(String expiryPts4) {
        ExpiryPts4 = expiryPts4;
    }

    public String getExpiryPts5() {
        return ExpiryPts5;
    }

    public void setExpiryPts5(String expiryPts5) {
        ExpiryPts5 = expiryPts5;
    }

    public String getExpiryPts6() {
        return ExpiryPts6;
    }

    public void setExpiryPts6(String expiryPts6) {
        ExpiryPts6 = expiryPts6;
    }

    public String getExpiryMonth1() {
        return ExpiryMonth1;
    }

    public void setExpiryMonth1(String expiryMonth1) {
        ExpiryMonth1 = expiryMonth1;
    }

    public String getExpiryMonth2() {
        return ExpiryMonth2;
    }

    public void setExpiryMonth2(String expiryMonth2) {
        ExpiryMonth2 = expiryMonth2;
    }

    public String getExpiryMonth3() {
        return ExpiryMonth3;
    }

    public void setExpiryMonth3(String expiryMonth3) {
        ExpiryMonth3 = expiryMonth3;
    }

    public String getExpiryMonth4() {
        return ExpiryMonth4;
    }

    public void setExpiryMonth4(String expiryMonth4) {
        ExpiryMonth4 = expiryMonth4;
    }

    public String getExpiryMonth5() {
        return ExpiryMonth5;
    }

    public void setExpiryMonth5(String expiryMonth5) {
        ExpiryMonth5 = expiryMonth5;
    }

    public String getExpiryMonth6() {
        return ExpiryMonth6;
    }

    public void setExpiryMonth6(String expiryMonth6) {
        ExpiryMonth6 = expiryMonth6;
    }


    public TransactionHistoryReceive() {

    }

    public TransactionHistoryReceive(TransactionHistoryReceive data) {
        Message = data.getMessage();
        Status = data.getStatus();

        ReturnValue = data.getReturnValue();
        ExpiryPts1 = data.getExpiryPts1();
        ExpiryPts2 = data.getExpiryPts2();
        ExpiryPts3 = data.getExpiryPts3();
        ExpiryPts4 = data.getExpiryPts4();
        ExpiryPts5 = data.getExpiryPts5();
        ExpiryPts6 = data.getExpiryPts6();
        ExpiryMonth1 = data.getExpiryMonth1();
        ExpiryMonth2 = data.getExpiryMonth2();
        ExpiryMonth3 = data.getExpiryMonth3();
        ExpiryMonth4 = data.getExpiryMonth4();
        ExpiryMonth5 = data.getExpiryMonth5();
        ExpiryMonth6 = data.getExpiryMonth6();

    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


}
