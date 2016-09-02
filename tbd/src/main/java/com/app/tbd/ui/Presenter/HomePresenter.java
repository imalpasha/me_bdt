package com.app.tbd.ui.Presenter;

import com.app.tbd.MainFragmentActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.SplashFailedConnect;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.PushNotificationObj;
import com.app.tbd.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class HomePresenter {

    private SharedPrefManager pref;

    public interface PushNotification {

    }

    public interface HomeView {

    }

    public interface SplashScreen {
        void loadingSuccess(InitialLoadReceive obj);
        void onConnectionFailed();

    }

    private HomeView view;
    private SplashScreen view2;
    private PushNotification view3;


    private final Bus bus;

    public HomePresenter(HomeView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public HomePresenter(SplashScreen view, Bus bus) {
        this.view2 = view;
        this.bus = bus;
    }

    public HomePresenter(PushNotification view, Bus bus) {
        this.view3 = view;
        this.bus = bus;
    }



    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void initialLoad(InitialLoadRequest info) {
        bus.post(new InitialLoadRequest(info));
    }

    public void onRegisterNotification(PushNotificationObj info) {
        bus.post(new PushNotificationObj(info));
    }



    @Subscribe
    public void onSuccessSendDeviceInformation(InitialLoadReceive event) {
        pref = new SharedPrefManager(MainFragmentActivity.getContext());
        if (view2!=null){
            view2.loadingSuccess(event);
        }
    }

    @Subscribe
    public void onFailedConnect(SplashFailedConnect event) {
        view2.onConnectionFailed();
    }
}
