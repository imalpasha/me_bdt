package com.app.tbd.ui.Presenter;


import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class MyProfilePresenter {

    public interface MyProfileView {
        void onViewUserSuccess(ViewUserReceive obj);
        void onSuccessRequestState(StateReceive obj);
    }

    private MyProfileView myProfileView;
    private final Bus bus;

    public MyProfilePresenter(MyProfileView view, Bus bus) {
        this.myProfileView = view;
        this.bus = bus;
    }

    public void showFunction(ViewUserRequest data) {
        bus.post(new ViewUserRequest(data));
    }

    public void onStateRequest(StateRequest obj) {
        bus.post(new StateRequest(obj));
    }

    @Subscribe
    public void onProfileShowSuccess(ViewUserReceive event) {
        myProfileView.onViewUserSuccess(event);
    }

    @Subscribe
    public void onSuccessRequestState(StateReceive event) {

        myProfileView.onSuccessRequestState(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}

