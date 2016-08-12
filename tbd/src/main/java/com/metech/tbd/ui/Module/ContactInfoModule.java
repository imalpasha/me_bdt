package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.ContactInfoFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ContactInfoFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class ContactInfoModule {

    private final BookingPresenter.ContactInfoView contactInfoView;

    public ContactInfoModule(BookingPresenter.ContactInfoView contactInfoView) {
        this.contactInfoView = contactInfoView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(contactInfoView, bus);
    }
}
