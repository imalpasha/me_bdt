package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;

import com.app.tbd.ui.Model.Receive.ResetPasswordReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Request.ResetPasswordRequest;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ProfilePresenter {

    public interface ProfileView {
        void onBigPointReceive(BigPointReceive obj);
    }

    public interface OptionView{
        void onLogoutReceive(LogoutReceive obj);
    }

    public interface ResetPasswordView{
        void onResetPasswordReceive(ResetPasswordReceive obj);
    }

    private ProfileView loginView;
    private OptionView optionView;
    private ResetPasswordView resetPasswordView;

    private final Bus bus;

    public ProfilePresenter(ProfileView view, Bus bus) {
        this.loginView = view;
        this.bus = bus;
    }

    public ProfilePresenter(OptionView view, Bus bus) {
        this.optionView = view;
        this.bus = bus;
    }

    public ProfilePresenter(ResetPasswordView view, Bus bus) {
        this.resetPasswordView = view;
        this.bus = bus;
    }

    public void onRequestResetPassword(ResetPasswordRequest data){
        bus.post(new ResetPasswordRequest(data));
    }

    public void onRequestBigPoint(BigPointRequest data) {
        bus.post(new BigPointRequest(data));
    }

    public void onRequestLogout(LogoutRequest data) {
        bus.post(new LogoutRequest(data));
    }

    @Subscribe
    public void onResetPasswordReceive(ResetPasswordReceive event) {
        resetPasswordView.onResetPasswordReceive(event);
    }

    @Subscribe
    public void onLogoutReceive(LogoutReceive event) {
        optionView.onLogoutReceive(event);
    }

    @Subscribe
    public void onBigPointReceive(BigPointReceive event) {
        loginView.onBigPointReceive(event);
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
