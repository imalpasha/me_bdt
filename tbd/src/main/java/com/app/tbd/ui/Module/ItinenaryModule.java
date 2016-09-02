package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.ItinenaryFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ItinenaryFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ItinenaryModule {

    private final BookingPresenter.ItinenaryView loginView;

    public ItinenaryModule(BookingPresenter.ItinenaryView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    BookingPresenter provideLoginPresenter(Bus bus) {
        return new BookingPresenter(loginView, bus);
    }
}
