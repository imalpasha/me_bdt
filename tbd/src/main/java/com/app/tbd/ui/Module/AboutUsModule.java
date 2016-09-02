package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.Aboutus.AboutUsFragment;
import com.app.tbd.ui.Presenter.AboutUsPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = AboutUsFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class AboutUsModule {

    private final AboutUsPresenter.AboutUsView aboutUsView;

    public AboutUsModule(AboutUsPresenter.AboutUsView aboutUsView) {
        this.aboutUsView = aboutUsView;
    }

    @Provides
    @Singleton
    AboutUsPresenter provideAboutUsPresenter(Bus bus) {
        return new AboutUsPresenter(aboutUsView, bus);
    }
}
