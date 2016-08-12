package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.Homepage.HomeFragment;
import com.metech.tbd.ui.Presenter.HomePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = HomeFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class HomeModule {

    private final HomePresenter.HomeView homeView;

    public HomeModule(HomePresenter.HomeView homeView) {
        this.homeView = homeView;
    }

    @Provides
    @Singleton
    HomePresenter provideHomePresenter(Bus bus) {
        return new HomePresenter(homeView, bus);
    }
}
