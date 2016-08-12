package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.ManageFlight.CommitChangeFragment;
import com.metech.tbd.ui.Presenter.ManageFlightPrenter;
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
