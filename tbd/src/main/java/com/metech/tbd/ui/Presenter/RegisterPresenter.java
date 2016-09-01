package com.metech.tbd.ui.Presenter;

import com.metech.tbd.ui.Model.Receive.RegisterReceive;
import com.metech.tbd.ui.Model.Receive.StateReceive;
import com.metech.tbd.ui.Model.Request.RegisterObj;
import com.metech.tbd.ui.Model.Request.RegisterRequest;
import com.metech.tbd.ui.Model.Request.StateRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class RegisterPresenter {

    public interface RegisterView {
        void onSuccessRegister(RegisterReceive obj);
        void onSuccessRequestState(StateReceive obj);
    }

    private final RegisterView view;
    private final Bus bus;

    public RegisterPresenter(RegisterView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onRequestRegister(RegisterRequest obj) {
        bus.post(new RegisterRequest(obj));
    }

    public void onStateRequest(StateRequest obj) {
        bus.post(new StateRequest(obj));
    }

    @Subscribe
    public void onUserSuccessRegister(RegisterReceive event) {
        view.onSuccessRegister(event.getUserObj());
    }

    @Subscribe
    public void onSuccessRequestState(StateReceive event) {

        view.onSuccessRequestState(event);
    }
}
