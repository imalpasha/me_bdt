package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.ManageFlight.MF_SeatSelectionFragment;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_SeatSelectionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeSeatModule {

    private final ManageFlightPrenter.ChangeSeatView loginView;

    public ManageChangeSeatModule(ManageFlightPrenter.ChangeSeatView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
