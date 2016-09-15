package com.app.tbd.ui.Presenter;

import android.util.Log;

import com.app.tbd.MainFragmentActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.LanguageCountryReceive;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.LanguageCountryRequest;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LanguagePresenter {

    public interface LanguageView {
        void onSuccessRequestLanguage(LanguageReceive obj);
        void onSuccessRequestLanguageCountry(LanguageCountryReceive obj);
        void loadingSuccess(InitialLoadReceive obj);
        void onSuccessRequestState(StateReceive obj);
    }

    private final LanguageView view;
    private final Bus bus;

    public LanguagePresenter(LanguageView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

    public void onLanguageRequest(LanguageRequest obj) {
        bus.post(new LanguageRequest(obj));
    }

    public void onCountryRequest(LanguageCountryRequest obj) {
        bus.post(new LanguageCountryRequest(obj));
    }


    public void onStateRequest(StateRequest obj) {
        bus.post(new StateRequest(obj));
    }

    public void initialLoad(InitialLoadRequest info) {
        bus.post(new InitialLoadRequest(info));
        Log.e("Why","?");
    }

    @Subscribe
    public void onSuccessSendDeviceInformation(InitialLoadReceive event) {
        view.loadingSuccess(event);
    }

    @Subscribe
    public void onSuccessRequestLanguage(LanguageReceive event) {
        view.onSuccessRequestLanguage(event);
    }

    @Subscribe
    public void onSuccessRequestState(StateReceive event) {

        view.onSuccessRequestState(event);
    }

    @Subscribe
    public void onSuccessRequestLanguageCountry(LanguageCountryReceive event) {
        view.onSuccessRequestLanguageCountry(event);
        Log.e("Subscribe", "tRUE");

    }
}
