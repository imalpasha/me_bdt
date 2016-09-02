package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.MobileCheckInPassengerReceive;
import com.app.tbd.ui.Model.Receive.MobileCheckinReceive;
import com.app.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.app.tbd.ui.Model.Request.ManageFlightObjV2;
import com.app.tbd.ui.Model.Request.MobileCheckInPassenger;
import com.app.tbd.ui.Model.Request.MobileCheckinObj;
import com.app.tbd.ui.Model.Request.MobileConfirmCheckInPassenger;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MobileCheckInPresenter {

    public interface MobileCheckInView {
        void onCheckindataReceive(MobileCheckinReceive obj);
        void onUserPnrList(ListBookingReceive event);

    }

    public interface MobileCheckInView2 {
        void onCheckInPassenger(MobileCheckInPassengerReceive obj);
    }

    public interface MobileCheckInView3 {
        void onConfirmCheckInPassenger(MobileConfirmCheckInPassengerReceive obj);
    }


    private MobileCheckInView view;
    private MobileCheckInView2 view2;
    private MobileCheckInView3 view3;

    private final Bus bus;

    public MobileCheckInPresenter(MobileCheckInView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public MobileCheckInPresenter(MobileCheckInView2 view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public MobileCheckInPresenter(MobileCheckInView3 view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }

    public void getUserPNR(String username,String password,String module,String customerNumber) {
        bus.post(new ManageFlightObjV2(username,password,module,customerNumber));

    }

    public void checkinFlight(MobileCheckinObj flightObj) {
        bus.post(new MobileCheckinObj(flightObj));
    }

    public void checkInPassenger(MobileCheckInPassenger flightObj) {
        bus.post(new MobileCheckInPassenger(flightObj));
    }

    public void confirmCheckInPassenger(MobileConfirmCheckInPassenger flightObj) {
        bus.post(new MobileConfirmCheckInPassenger(flightObj));
    }


    @Subscribe
    public void onRequestSuccess(MobileCheckinReceive event) {
        /*Save Session And Redirect To Homepage*/
        view.onCheckindataReceive(event);
    }

    @Subscribe
    public void onCheckInPassenger(MobileCheckInPassengerReceive event) {
        /*Save Session And Redirect To Homepage*/
        view2.onCheckInPassenger(event);
    }


    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
        if(event != null){
            view.onUserPnrList(event);
        }
    }

    @Subscribe
    public void onConfirmCheckInPassenger(MobileConfirmCheckInPassengerReceive event) {
        /*Save Session And Redirect To Homepage*/
        view3.onConfirmCheckInPassenger(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
