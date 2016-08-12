package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.FlightListFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = FlightListFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SelectFlightModule {

    private final BookingPresenter.ListFlightView flightDetailViewView;

    public SelectFlightModule(BookingPresenter.ListFlightView flightDetailViewView) {
        this.flightDetailViewView = flightDetailViewView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(flightDetailViewView, bus);
    }
}
