package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.FlightSummaryFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = FlightSummaryFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class FlightSummaryModule {

    private final BookingPresenter.FlightSummaryView loginView;

    public FlightSummaryModule(BookingPresenter.FlightSummaryView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    BookingPresenter provideLoginPresenter(Bus bus) {
        return new BookingPresenter(loginView, bus);
    }
}
