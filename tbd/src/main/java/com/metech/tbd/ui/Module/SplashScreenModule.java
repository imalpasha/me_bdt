package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.SplashScreen.SplashScreenFragment;
import com.metech.tbd.ui.Presenter.HomePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = SplashScreenFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class SplashScreenModule {

    private final HomePresenter.SplashScreen loginView;

    public SplashScreenModule(HomePresenter.SplashScreen loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    HomePresenter provideLoginPresenter(Bus bus) {
        return new HomePresenter(loginView, bus);
    }
}
