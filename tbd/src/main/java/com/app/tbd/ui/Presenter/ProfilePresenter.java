package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.EditProfileReceive;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;

import com.app.tbd.ui.Model.Receive.ResetPasswordReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.EditProfileRequest;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.ResetPasswordRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;

import com.app.tbd.ui.Model.Request.TransactionHistoryRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class ProfilePresenter {

    public interface ProfileView {
        void onBigPointReceive(BigPointReceive obj);

        void onViewUserSuccess(ViewUserReceive obj);
    }

    public interface MyProfileView {
        void onSuccessRequestState(StateReceive obj);
        void onUpdateUserSuccess(EditProfileReceive obj);
    }

    public interface OptionView {
        void onLogoutReceive(LogoutReceive obj);

        void loadingSuccess(InitialLoadReceive obj);

        void onSuccessRequestState(StateReceive obj);
    }

    public interface ResetPasswordView {
        void onResetPasswordReceive(ResetPasswordReceive obj);
    }


    public interface BigPointView {
        void onTransactionHistorySuccess(TransactionHistoryReceive obj);
    }


    private ProfileView loginView;
    private OptionView optionView;
    private BigPointView bigPointView;
    private MyProfileView myProfileView;
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

    public ProfilePresenter(BigPointView view, Bus bus) {
        this.bigPointView = view;
        this.bus = bus;
    }

    public ProfilePresenter(ResetPasswordView view, Bus bus) {
        this.resetPasswordView = view;
        this.bus = bus;
    }

    public ProfilePresenter(MyProfileView view, Bus bus) {
        this.myProfileView = view;
        this.bus = bus;
    }

    public void onRequestResetPassword(ResetPasswordRequest data) {
        bus.post(new ResetPasswordRequest(data));
    }

    public void onRequestBigPoint(BigPointRequest data) {
        bus.post(new BigPointRequest(data));
    }

    public void onRequestLogout(LogoutRequest data) {
        bus.post(new LogoutRequest(data));
    }

    public void onRequestTransactionHistory(TransactionHistoryRequest data) {
        bus.post(new TransactionHistoryRequest(data));
    }

    public void showFunction(ViewUserRequest data) {
        bus.post(new ViewUserRequest(data));
    }

    public void onStateRequest(StateRequest obj) {
        bus.post(new StateRequest(obj));
    }

    public void initialLoad(InitialLoadRequest info) {
        bus.post(new InitialLoadRequest(info));
    }

    public void updateFunction(EditProfileRequest data) {
        bus.post(new EditProfileRequest(data));
    }

    @Subscribe
    public void onEditProfileSuccess(EditProfileReceive event) {
        myProfileView.onUpdateUserSuccess(event);
    }

    @Subscribe
    public void onSuccessSendDeviceInformation(InitialLoadReceive event) {
        optionView.loadingSuccess(event);
    }

    @Subscribe
    public void onTransactionHistoryReceive(TransactionHistoryReceive event) {
        bigPointView.onTransactionHistorySuccess(event);
    }

    @Subscribe
    public void onProfileShowSuccess(ViewUserReceive event) {
        loginView.onViewUserSuccess(event);
    }

    @Subscribe
    public void onSuccessRequestState(StateReceive event) {
        if (optionView != null) {
            optionView.onSuccessRequestState(event);
        }
    }

    @Subscribe
    public void onSuccessRequestState2(StateReceive event) {
        if (myProfileView != null) {
            myProfileView.onSuccessRequestState(event);
        }
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
        if(loginView != null){
            loginView.onBigPointReceive(event);
        }
    }


    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
