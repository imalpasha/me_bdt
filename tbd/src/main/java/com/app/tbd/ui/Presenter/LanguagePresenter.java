package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LanguagePresenter {

    public interface LanguageView {

        void onSuccessRequestLanguage(LanguageReceive obj);
        //void onSuccessRequestCountry(NewsletterLanguageReceive obj);

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

    /*public void onStateRequest(StateRequest obj) {
        bus.post(new StateRequest(obj));
    }*/


    @Subscribe
    public void onSuccessRequestLanguage(LanguageReceive event) {
        view.onSuccessRequestLanguage(event);
    }

    /*@Subscribe
    public void onSuccessRequestState(StateReceive event) {

        view.onSuccessRequestState(event);
    }*/
}
