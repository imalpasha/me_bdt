package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.PersonalDetailFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PersonalDetailFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class PersonalDetailModule {

    private final BookingPresenter.PassengerInfoView personalDetailView;

    public PersonalDetailModule(BookingPresenter.PassengerInfoView personalDetailView) {
        this.personalDetailView = personalDetailView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(personalDetailView, bus);
    }
}
