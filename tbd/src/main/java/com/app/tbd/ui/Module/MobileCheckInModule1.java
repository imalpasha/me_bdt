package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.MobileCheckIn.MobileCheckInFragment1;
import com.app.tbd.ui.Presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MobileCheckInFragment1.class,
        addsTo = AppModule.class,
        complete = false
)
public class MobileCheckInModule1 {

    private final MobileCheckInPresenter.MobileCheckInView view;

    public MobileCheckInModule1(MobileCheckInPresenter.MobileCheckInView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MobileCheckInPresenter provideMobileCheckInPresenter1(Bus bus) {
        return new MobileCheckInPresenter(view, bus);
    }
}
