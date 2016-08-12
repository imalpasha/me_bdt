package com.metech.tbd.ui.Model.Request;

/**
 * Created by Dell on 1/21/2016.
 */
public class GetFlightAvailability {


    private String pnr;
    private String booking_id;
    private String signature;

    public GetFlightAvailability(){

    }

    public GetFlightAvailability(GetFlightAvailability data){
        booking_id = data.getBooking_id();
        signature = data.getSignature();
        pnr = data.getPnr();
    }

    public String getPnr() {
        return pnr;
    }

    public void setPnr(String pnr) {
        this.pnr = pnr;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

}
