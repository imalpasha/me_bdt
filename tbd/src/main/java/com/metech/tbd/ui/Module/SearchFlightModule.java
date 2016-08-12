package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.SearchFlightFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SearchFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SearchFlightModule {

    private final BookingPresenter.SearchFlightView searchFlightView;

    public SearchFlightModule(BookingPresenter.SearchFlightView searchFlightView) {
        this.searchFlightView = searchFlightView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(searchFlightView, bus);
    }
}
