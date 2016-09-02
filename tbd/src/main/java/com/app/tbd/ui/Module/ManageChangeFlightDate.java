package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.ManageFlight.MF_ChangeFlightFragment;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_ChangeFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeFlightDate {

    private final ManageFlightPrenter.GetFlightView loginView;

    public ManageChangeFlightDate(ManageFlightPrenter.GetFlightView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
