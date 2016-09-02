package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.ForgotPassword.ForgotPasswordFragment;
import com.app.tbd.ui.Presenter.LoginPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ForgotPasswordFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class ForgotPasswordModule {

    private final LoginPresenter.ForgotPasswordView loginView;

    public ForgotPasswordModule(LoginPresenter.ForgotPasswordView loginView) {
        this.loginView = loginView;
    }

    @Provides
    @Singleton
    LoginPresenter provideLoginPresenter(Bus bus) {
        return new LoginPresenter(loginView, bus);
    }
}
