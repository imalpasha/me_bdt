package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.Aboutus.AboutUsFragment;
import com.metech.tbd.ui.Presenter.AboutUsPresenter;
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
