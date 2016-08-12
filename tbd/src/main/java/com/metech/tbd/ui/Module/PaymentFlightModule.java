package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.PaymentFlightFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PaymentFlightFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class PaymentFlightModule {

    private final BookingPresenter.PaymentFlightView paymentFlightView;

    public PaymentFlightModule(BookingPresenter.PaymentFlightView paymentFlightView) {
        this.paymentFlightView = paymentFlightView;
    }

    @Provides
    @Singleton
    BookingPresenter provideSearchFlightPresenter(Bus bus) {
        return new BookingPresenter(paymentFlightView, bus);
    }
}
