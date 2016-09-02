package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.ManageFlight.CommitChangeFragment;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = CommitChangeFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightConfirmUpdate {

    private final ManageFlightPrenter.ConfirmUpdateView loginView;

    public ManageFlightConfirmUpdate(ManageFlightPrenter.ConfirmUpdateView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
