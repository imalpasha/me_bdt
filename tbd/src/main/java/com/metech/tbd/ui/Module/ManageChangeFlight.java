package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.ManageFlight.MF_ChangeFlightFragment;
import com.metech.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_ChangeFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeFlight {

    private final ManageFlightPrenter.GetFlightView loginView;

    public ManageChangeFlight(ManageFlightPrenter.GetFlightView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
