package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.app.tbd.ui.Model.Request.ManageFlightObjV2;
import com.app.tbd.ui.Model.Request.RetrieveBoardingPassObj;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class BoardingPassPresenter {

    public interface RetrieveBoardingPassView {
        void onBoardingPassReceive(RetrieveBoardingPassReceive obj);
        void onUserPnrList(ListBookingReceive event);
    }

    private RetrieveBoardingPassView view;

    private final Bus bus;

    public BoardingPassPresenter(RetrieveBoardingPassView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    /*Retrieve Boarding Pass By PNR*/
    public void retrieveBoardingPass(RetrieveBoardingPassObj obj){
        bus.post(new RetrieveBoardingPassObj(obj));
    }


    /*Retrieve Boarding Pass By PNR*/
    public void retriveListOfBoardingPass(String username,String password,String module,String customerNumber) {
        bus.post(new ManageFlightObjV2(username,password,module,customerNumber));
    }


    @Subscribe
    public void onItineraryReceive(RetrieveBoardingPassReceive event)
    {
        view.onBoardingPassReceive(event);
    }


    @Subscribe
    public void onUserPnrList(ListBookingReceive event) {
            view.onUserPnrList(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }
}
