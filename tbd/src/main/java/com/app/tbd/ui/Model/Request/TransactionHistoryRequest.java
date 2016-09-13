package com.app.tbd.ui.Model.Request;

/**
 * Created by Dell on 9/13/2016.
 */
public class TransactionHistoryRequest {

    private String CustomerNumber;
    private String Hash;

    public TransactionHistoryRequest() {

    }

    public TransactionHistoryRequest(TransactionHistoryRequest data) {
        CustomerNumber = data.getCustomerNumber();
        Hash = data.getHash();
    }

    public String getCustomerNumber() {
        return CustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        CustomerNumber = customerNumber;
    }

    public String getHash() {
        return Hash;
    }

    public void setHash(String hash) {
        Hash = hash;
    }


}
