package com.app.tbd.ui.Presenter;

import android.util.Log;

import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.ui.Model.Receive.LoginFacebookReceive;
import com.app.tbd.ui.Model.Receive.LoginReceive;
import com.app.tbd.ui.Model.Request.LoginFacebookRequest;
import com.app.tbd.ui.Model.Request.LoginRequest;
import com.app.tbd.ui.Model.Request.PasswordRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LoginPresenter {

    public interface ForgotPasswordView {
        void onForgotPassword(ForgotPasswordReceive obj);
    }

    public interface LoginView {

        void onLoginSuccess(LoginReceive obj);

        void onCheckFBLoginSuccess(LoginFacebookReceive obj);

        //void onLoginFailed(String dumm);
        void onRequestPasswordSuccess(ForgotPasswordReceive obj);

    }

    private LoginView loginView;
    private ForgotPasswordView forgotPasswordView;

    private final Bus bus;

    public LoginPresenter(LoginView view, Bus bus) {
        this.loginView = view;
        this.bus = bus;
    }

    public LoginPresenter(ForgotPasswordView view, Bus bus) {
        this.forgotPasswordView = view;
        this.bus = bus;
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onLogin(LoginRequest data) {
        bus.post(new LoginRequest(data));
        //Log.e(data.getUsername(), data.getPassword());
    }


    public void onCheckFBLogin(LoginFacebookRequest data) {
        //bus.post(new LoginFacebookRequest(data));
        Log.e(data.getEmail(), data.getToken());
    }


    public void forgotPassword(PasswordRequest data) {
        bus.post(new PasswordRequest(data));
    }

    @Subscribe
    public void onUserSuccessCheckFacebookLogin(LoginFacebookReceive event) {

        /*Save Session And Redirect To Homepage*/
        loginView.onCheckFBLoginSuccess(event);
    }


    @Subscribe
    public void onUserSuccessLogin(LoginReceive event) {

        /*Save Session And Redirect To Homepage*/
        loginView.onLoginSuccess(event.getUserObj());
    }


    @Subscribe
    public void onUserSuccessReqPassword(ForgotPasswordReceive obj) {

        //*Save Session And Redirect To Homepage*//*
        forgotPasswordView.onForgotPassword(obj.getUserObj());

    }

}
