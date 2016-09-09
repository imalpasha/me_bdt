package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.LogoutReceive;
import com.app.tbd.ui.Model.Request.LogoutRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ProfilePresenter {

    public interface ProfileView {
        void onLogoutReceive(LogoutReceive obj);
    }

    private ProfileView loginView;
    private final Bus bus;

    public ProfilePresenter(ProfileView view, Bus bus) {
        this.loginView = view;
        this.bus = bus;
    }

    public void onRequestLogout(LogoutRequest data) {
        bus.post(new LogoutRequest(data));
    }

    @Subscribe
    public void onLogoutReceive(LogoutReceive event) {
        loginView.onLogoutReceive(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
