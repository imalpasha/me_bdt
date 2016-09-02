package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.FlightSummaryFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
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
