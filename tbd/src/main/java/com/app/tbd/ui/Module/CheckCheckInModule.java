package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.SlidePage.NearKioskFragment;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = NearKioskFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class CheckCheckInModule {

    private final ManageFlightPrenter.ManageFlightView homeView;

    public CheckCheckInModule(ManageFlightPrenter.ManageFlightView homeView) {
        this.homeView = homeView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideHomePresenter(Bus bus) {
        return new ManageFlightPrenter(homeView, bus);
    }
}
