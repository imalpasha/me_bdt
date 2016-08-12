package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.MobileCheckIn.MobileCheckInFragment3;
import com.metech.tbd.ui.Presenter.MobileCheckInPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MobileCheckInFragment3.class,
        addsTo = AppModule.class,
        complete = false
)
public class MobileCheckInModule3 {

    private final MobileCheckInPresenter.MobileCheckInView3 view;

    public MobileCheckInModule3(MobileCheckInPresenter.MobileCheckInView3 view) {
        this.view = view;
    }

    @Provides
    @Singleton
    MobileCheckInPresenter provideMobileCheckInPresenter1(Bus bus) {
        return new MobileCheckInPresenter(view, bus);
    }
}
