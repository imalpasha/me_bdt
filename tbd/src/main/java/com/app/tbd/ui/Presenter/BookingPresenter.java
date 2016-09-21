package com.app.tbd.ui.Presenter;

import android.util.Log;

import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.SignatureReceive;
import com.app.tbd.ui.Model.Request.SearchFlightRequest;
import com.app.tbd.ui.Model.Request.SignatureRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BookingPresenter {

    public interface RemoveCCView {
    }

    public interface SearchFlightView {
        void onSearchFlightReceive(SearchFlightReceive obj);

        void onSignatureReceive(SignatureReceive obj);
    }

    /*public interface DeleteFamilyView {
        //void onBookingDataReceive();
        void onDeleteFF(DeleteFFReceive obj);
    }

    public interface EditFamilyFriendView {
        //void onBookingDataReceive();
        void onEditFF(SelectFlightReceive obj);
    }



    public interface ListFlightView {
        void onSeletFlightReceive(SelectFlightReceive obj);
        void onLoginSuccess(LoginReceive obj);
        void onChangeFlightSuccess(ManageChangeContactReceive obj);
    }

    public interface PassengerInfoView{
        void onPassengerInfo(PassengerInfoReveice obj);
        void onLoginSuccess(LoginReceive obj);
        void onRequestPasswordSuccess(ForgotPasswordReceive obj);
    }

    public interface ContactInfoView{
        void onContactInfo(ContactInfoReceive obj);
    }

    public interface SeatSelectionView{
        void onSeatSelect(SeatSelectionReveice obj);

    }

    public interface ItinenaryView{
        void onContactInfo(ContactInfoReceive obj);
        //void onSeatSelect();
    }

    public interface PaymentFlightView{
       void onPaymentInfoReceive(PaymentInfoReceive obj);
       void onPaymentReceive(PaymentReceive obj);
       void onRemoveCC(DeleteCCReceive obj);
    }

    public interface FlightSummaryView{
        void onFlightSummary(FlightSummaryReceive obj);
    }
*/

    private SearchFlightView searchFlightView;
   /* private ListFlightView view2;
    private PassengerInfoView view3;
    private ContactInfoView view4;
    private SeatSelectionView view5;
    private ItinenaryView view6;
    private PaymentFlightView view7;
    private FlightSummaryView view8;
    private EditFamilyFriendView view9;
    private DeleteFamilyView view10;*/

    private final Bus bus;

    public BookingPresenter(SearchFlightView view, Bus bus) {
        this.searchFlightView = view;
        this.bus = bus;
    }

    /*AAB - Search Flight*/
    public void onSearchFlight(SearchFlightRequest flightObj) {
        bus.post(new SearchFlightRequest(flightObj));
    }

    /*AAB - Search Flight*/
    public void onRequestSignature(SignatureRequest event) {
        bus.post(new SignatureRequest());
    }

    @Subscribe
    public void onSearchFlightSuccess(SearchFlightReceive event) {
        searchFlightView.onSearchFlightReceive(event);
    }

    @Subscribe
    public void onSignatureReceive(SignatureReceive event) {
        searchFlightView.onSignatureReceive(event);
    }


   /* public BookingPresenter(DeleteFamilyView view, Bus bus) {
        this.view10 = view;
        this.bus = bus;
    }

    public BookingPresenter(EditFamilyFriendView view, Bus bus) {
        this.view9 = view;
        this.bus = bus;
    }



    public BookingPresenter(ListFlightView view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public BookingPresenter(PassengerInfoView view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }


    public BookingPresenter(ContactInfoView view, Bus bus) {
        this.view4 = view;
        this.bus = bus;
    }

    public BookingPresenter(SeatSelectionView view, Bus bus) {
        this.view5 = view;
        this.bus = bus;
    }

    public BookingPresenter(ItinenaryView view, Bus bus) {
        this.view6 = view;
        this.bus = bus;
    }

    public BookingPresenter(PaymentFlightView view, Bus bus) {
        this.view7 = view;
        this.bus = bus;
    }

    public BookingPresenter(FlightSummaryView view, Bus bus) {
        this.view8 = view;
        this.bus = bus;
    }


    public void onRemoveCC(Signature data) {
        bus.post(new DeleteCCRequest(data));
    }

    public void onRequestEditFF(PassengerInfo data) {
        bus.post(new PassengerInfo(data));
    }*/

    /*Forgot Password*/
    /*public void onDeleteFF(FriendFamilyDelete deleteID) {
        bus.post(new FriendFamilyDelete(deleteID));
    }

    public void forgotPassword(PasswordRequest data) {
        bus.post(new PasswordRequest(data));
    }



    public void selectFlight(SelectFlight flightObj) {
        bus.post(new SelectFlight(flightObj));
    }

    public void passengerInfo(Passenger obj) {
        bus.post(new Passenger(obj));
    }

    public void contactInfo(ContactInfo obj) {
        bus.post(new ContactInfo(obj));
    }

    public void seatSelect(SeatSelection obj) {
        bus.post(new SeatSelection(obj));
    }

    public void paymentInfo(Signature obj) {
        bus.post(new Signature(obj));
    }

    public void paymentRequest(Payment obj) {
        bus.post(new Payment(obj));
    }

    public void requestFlightSummary(Signature obj){
        bus.post(new FlightSummary(obj));
    }

    public void changeFlight(SelectChangeFlight obj){
        bus.post(new SelectChangeFlight(obj));
    }

    public void requestLogin(LoginRequest data) {
        bus.post(new LoginRequest(data));
    }

    @Subscribe
    public void onUserSuccessLogin(LoginReceive event) {

        if(view3 != null){
            view3.onLoginSuccess(event.getUserObj());
        }

        if(view2 != null){
            view2.onLoginSuccess(event.getUserObj());
        }

    }


    @Subscribe
    public void onEditFamilyFriendsReceive(SelectFlightReceive event) {
        try{
            view9.onEditFF(event);
        }catch (Exception e){
            Log.e("DumpData","True");
        }
    }

    @Subscribe
    public void onPaymentInfoReceive(PaymentInfoReceive event) {
        view7.onPaymentInfoReceive(event);
    }

    @Subscribe
    public void onSearchFlight(SearchFlightReceive event) {
        view.onBookingDataReceive(event);
    }

    @Subscribe
    public void onSelectFlight(SelectFlightReceive event) {

        try{
            view2.onSeletFlightReceive(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onDeleteFF(DeleteFFReceive event){

        try{
            view10.onDeleteFF(event);
        }catch (Exception e){

        }
    }


    @Subscribe
    public void onDeleteCCReceive(DeleteCCReceive event) {
        view7.onRemoveCC(event);
    }

    @Subscribe
    public void onPassengerInfo(PassengerInfoReveice event) {
        view3.onPassengerInfo(event);
    }

    @Subscribe
    public void onUserSuccessReqPassword(ForgotPasswordReceive obj) {

        //*Save Session And Redirect To Homepage*//*
        view3.onRequestPasswordSuccess(obj.getUserObj());
    }

    @Subscribe
    public void onContactInfoReceive(ContactInfoReceive event) {
        view4.onContactInfo(event);

    }

    @Subscribe
    public void onFlightSummaryReceive(FlightSummaryReceive event) {
        view8.onFlightSummary(event);

    }

    @Subscribe
    public void onSeatReceive(SeatSelectionReveice event) {
        view5.onSeatSelect(event);
    }

    @Subscribe
    public void onPaymentReceive(PaymentReceive event) {
        view7.onPaymentReceive(event);
    }

    @Subscribe
    public void onChangeFlightSelectReceive(ManageChangeContactReceive event) {
        view2.onChangeFlightSuccess(event);
    }

    //@Subscribe
   // public void onItineraryReceive(ContactInfoReceive event) {view6.onItineraryInfo(event);}

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }*/
}
