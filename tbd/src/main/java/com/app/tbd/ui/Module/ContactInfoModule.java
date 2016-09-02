package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.ContactInfoFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
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
