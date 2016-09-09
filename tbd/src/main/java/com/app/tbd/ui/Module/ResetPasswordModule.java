package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.Profile.Option.ResetPasswordFragment;
import com.app.tbd.ui.Activity.Register.RegisterFragment;
import com.app.tbd.ui.Activity.Register.RegisterFragmentPending;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.ui.Presenter.RegisterPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ResetPasswordFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ResetPasswordModule {

    private final ProfilePresenter.ResetPasswordView resetPasswordView;

    public ResetPasswordModule(ProfilePresenter.ResetPasswordView resetPasswordView) {
        this.resetPasswordView = resetPasswordView;
    }

    @Provides
    @Singleton
    ProfilePresenter provideLoginPresenter(Bus bus) {
        return new ProfilePresenter(resetPasswordView, bus);
    }
}
