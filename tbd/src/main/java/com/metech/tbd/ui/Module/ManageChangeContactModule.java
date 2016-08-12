package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.ManageFlight.MF_ChangeContactFragment;
import com.metech.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_ChangeContactFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageChangeContactModule {

    private final ManageFlightPrenter.ChangeContactView loginView;

    public ManageChangeContactModule(ManageFlightPrenter.ChangeContactView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideLoginPresenter(Bus bus) {
        return new ManageFlightPrenter(loginView, bus);
    }
}
