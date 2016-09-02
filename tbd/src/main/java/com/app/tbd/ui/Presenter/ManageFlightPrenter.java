package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.ChangeSearchFlightReceive;
import com.app.tbd.ui.Model.Receive.CheckInListReceive;
import com.app.tbd.ui.Model.Receive.ConfirmUpdateReceive;
import com.app.tbd.ui.Model.Receive.ContactInfoReceive;
import com.app.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.ManageChangeContactReceive;
import com.app.tbd.ui.Model.Receive.ManageRequestIntinenary;
import com.app.tbd.ui.Model.Receive.SSRReceive;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Request.ChangeSSR;
import com.app.tbd.ui.Model.Request.ConfirmUpdateRequest;
import com.app.tbd.ui.Model.Request.ContactInfo;
import com.app.tbd.ui.Model.Request.GetChangeFlight;
import com.app.tbd.ui.Model.Request.GetFlightAvailability;
import com.app.tbd.ui.Model.Request.GetSSR;
import com.app.tbd.ui.Model.Request.ManageContactInfo;
import com.app.tbd.ui.Model.Request.ManageFlightObj;
import com.app.tbd.ui.Model.Request.ManageFlightObjV2;
import com.app.tbd.ui.Model.Request.ManageFlightObjV3;
import com.app.tbd.ui.Model.Request.ManagePassengerInfo;
import com.app.tbd.ui.Model.Request.ManageSeatInfo;
import com.app.tbd.ui.Model.Request.SeatAvailabilityRequest;
import com.app.tbd.ui.Model.Request.SendItinenaryObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ManageFlightPrenter {

    public interface ManageFlightView {
        void onGetFlightFromPNR(FlightSummaryReceive event);
        void onUserPnrList(ListBookingReceive event);
        void onCheckUserStatus(CheckInListReceive event);
    }

    public interface ChangeSpecialMealView{
        void onSSRReceive(SSRReceive obj);
        void onChangeSSRReceive(ManageChangeContactReceive obj);
    }

    public interface ChangeContactView {
        void onGetChangeContact(ManageChangeContactReceive obj);
    }

    public interface ConfirmUpdateView {
        void changeConfirm(ConfirmUpdateReceive obj);
    }

    public interface ChangePassengerInfoView {
        void onChangePassengerInfo(ManageChangeContactReceive obj);
    }

    public interface ChangeSeatView {
        void onRequestSeat(ContactInfoReceive obj);
        void onSeatChange(ManageChangeContactReceive obj);

    }

    public interface SendItinenary {
        void onSuccessRequest(ManageRequestIntinenary obj);
    }

    public interface GetFlightView {
        void onGetFlightSuccess(ChangeSearchFlightReceive obj);
        void onNewFlightReceive(SearchFlightReceive obj);
    }



    private ManageFlightView view;
    private ChangeContactView view2;
    private ConfirmUpdateView view3;
    private ChangePassengerInfoView view4;
    private ChangeSeatView view5;
    private SendItinenary view6;
    private GetFlightView view7;
    private ChangeSpecialMealView view8;

    private final Bus bus;

    public ManageFlightPrenter(ManageFlightView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeContactView view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ConfirmUpdateView view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangePassengerInfoView view, Bus bus) {
        this.view4 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeSeatView view, Bus bus) {
        this.view5 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(SendItinenary view, Bus bus) {
        this.view6 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(GetFlightView view, Bus bus) {
        this.view7 = view;
        this.bus = bus;
    }

    public ManageFlightPrenter(ChangeSpecialMealView view, Bus bus) {
        this.view8 = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void getSSRMeal(GetSSR data){
        bus.post(new GetSSR(data));
    }

    public void changeMeal(ChangeSSR data){
        bus.post(new ChangeSSR(data));
    }

    public void onSendPNRV1(ManageFlightObj data) {
        bus.post(new ManageFlightObj(data));
    }

    public void onSendPNRV2(String username,String password,String module,String customerNumber) {
        bus.post(new ManageFlightObjV2(username,password,module,customerNumber));
    }

    public void onSendPNRV3(String username,String password,String module) {
        bus.post(new ManageFlightObjV3(username,password,module));
    }

    public void onSeatAvailability(SeatAvailabilityRequest data) {
        bus.post(new SeatAvailabilityRequest(data));
    }

    public void onChangeContact(ContactInfo data,String pnr , String username,String signature) {
        bus.post(new ManageContactInfo(data,pnr,username,signature));
    }

    public void requestChange(ConfirmUpdateRequest data) {
        bus.post(new ConfirmUpdateRequest(data));
    }

    public void onChangePassengerInfo(ManagePassengerInfo data,String pnr , String username,String signature) {
        bus.post(new ManagePassengerInfo(data, pnr, username, signature));
    }

    public void seatSelect(ManageSeatInfo data,String pnr , String username,String signature) {
        bus.post(new ManageSeatInfo(data, pnr, username, signature));
    }

    public void onSentItinenary(SendItinenaryObj obj) {
        bus.post(new SendItinenaryObj(obj));
    }

    public void onGetFlightAvailability(GetFlightAvailability obj) {
        bus.post(new GetFlightAvailability(obj));
    }

    public void onNewFlightDate(GetChangeFlight data) {
        bus.post(new GetChangeFlight(data));
    }

    @Subscribe
    public void onUserSuccessLogin(FlightSummaryReceive event) {
        try{
            view.onGetFlightFromPNR(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
        try{
            view.onUserPnrList(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onUserPnrList2(CheckInListReceive event) {
        try{
            view.onCheckUserStatus(event);
        }catch (Exception e){

        }
    }


    @Subscribe
    public void onManageChangeContact(ManageChangeContactReceive event) {
        try{
            view2.onGetChangeContact(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onChangeConfirmSuccess(ConfirmUpdateReceive event) {
        view3.changeConfirm(event);
    }

    @Subscribe
    public void onChangeConfirmSuccess(ManageChangeContactReceive event) {
        try{
            view4.onChangePassengerInfo(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onSeatRequestSuccess(ContactInfoReceive event) {
        view5.onRequestSeat(event);
    }

    @Subscribe
    public void onSeatChange(ManageChangeContactReceive event) {
        try{
            view5.onSeatChange(event);
        }catch (Exception e){

        }
    }

    @Subscribe
    public void onSuccessRequestItinenary(ManageRequestIntinenary event) {
        view6.onSuccessRequest(event);
    }

    @Subscribe
    public void onGetFlight(ChangeSearchFlightReceive event) {
        view7.onGetFlightSuccess(event);
    }

    @Subscribe
    public void onGetNewFlight(SearchFlightReceive event) {
        view7.onNewFlightReceive(event);
    }

    @Subscribe
    public void onSSRReceive(SSRReceive event) {
        view8.onSSRReceive(event);
    }

    @Subscribe
    public void onChangeSSRReceive(ManageChangeContactReceive event) {
        try{
            view8.onChangeSSRReceive(event);
        }catch (Exception e){

        }
    }



    //@Subscribe
    //public void onSuccessGetFlightList(SearchFlightReceive event) {
    //    view6.onSuccessRequest(event);
   // }

}
