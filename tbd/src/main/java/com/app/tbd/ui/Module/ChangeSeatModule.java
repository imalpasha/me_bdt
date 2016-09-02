package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.ManageFlight.MF_EditPassengerFragment;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_EditPassengerFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class ChangeSeatModule {

    private final ManageFlightPrenter.ChangePassengerInfoView contactInfoView;

    public ChangeSeatModule(ManageFlightPrenter.ChangePassengerInfoView contactInfoView) {
        this.contactInfoView = contactInfoView;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideFlightDetailPresenter(Bus bus) {
        return new ManageFlightPrenter(contactInfoView, bus);
    }
}
