package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.PushNotification.PushNotificationFragment;
import com.app.tbd.ui.Presenter.HomePresenter;
import com.squareup.otto.Bus;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module(
        injects = PushNotificationFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class RegisterNotification {

    private final HomePresenter.PushNotification registerView;

    public RegisterNotification(HomePresenter.PushNotification registerView) {
        this.registerView = registerView;
    }

    @Provides
    @Singleton
    HomePresenter provideLoginPresenter(Bus bus) {
        return new HomePresenter(registerView, bus);
    }
}
