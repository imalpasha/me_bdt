package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.MobileCheckIn.MobileCheckInFragment2;
import com.app.tbd.ui.Presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MobileCheckInFragment2.class,
        addsTo = AppModule.class,
        complete = false
)
public class MobileCheckInModule2 {

    private final MobileCheckInPresenter.MobileCheckInView2 view;

    public MobileCheckInModule2(MobileCheckInPresenter.MobileCheckInView2 view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MobileCheckInPresenter provideMobileCheckInPresenter1(Bus bus) {
        return new MobileCheckInPresenter(view, bus);
    }
}
