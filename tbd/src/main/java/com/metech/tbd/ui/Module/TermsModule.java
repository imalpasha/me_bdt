package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.Terms.TermsFragment;
import com.metech.tbd.ui.Presenter.TermsPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = TermsFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class TermsModule {

    private final TermsPresenter.TermsView Termsview;

    public TermsModule(TermsPresenter.TermsView Termsview) {
        this.Termsview = Termsview;
    }

    @Provides
    @Singleton
    TermsPresenter provideTermsPresenter(Bus bus) {
        return new TermsPresenter(Termsview, bus);
    }
}
