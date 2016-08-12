package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.ManageFlight.MF_SpecialServiceRequestFragment;
import com.metech.tbd.ui.Presenter.ManageFlightPrenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MF_SpecialServiceRequestFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ManageFlightSpecialMeal {

    private final ManageFlightPrenter.ChangeSpecialMealView view;

    public ManageFlightSpecialMeal(ManageFlightPrenter.ChangeSpecialMealView view) {
        this.view = view;
    }

    @Provides
    @Singleton
    ManageFlightPrenter provideMobileCheckInPresenter1(Bus bus) {
        return new ManageFlightPrenter(view, bus);
    }
}
