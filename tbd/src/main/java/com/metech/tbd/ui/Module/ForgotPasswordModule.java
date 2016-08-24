package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.ForgotPassword.ForgotPasswordFragment;
import com.metech.tbd.ui.Activity.Login.LoginFragment;
import com.metech.tbd.ui.Presenter.LoginPresenter;
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
