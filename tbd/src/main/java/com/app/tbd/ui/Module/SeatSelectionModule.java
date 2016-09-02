package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.SeatSelectionFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SeatSelectionFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SeatSelectionModule {

    private final BookingPresenter.SeatSelectionView seatSelectionView;

    public SeatSelectionModule(BookingPresenter.SeatSelectionView seatSelectionView) {
        this.seatSelectionView = seatSelectionView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(seatSelectionView, bus);
    }
}
