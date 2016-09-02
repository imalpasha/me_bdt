package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.UpdateProfile.UpdateProfileFragment;
import com.app.tbd.ui.Presenter.UpdateProfilePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = UpdateProfileFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class UpdateProfileModule {

    private final UpdateProfilePresenter.UpdateProfileView UpdateProfileview;

    public UpdateProfileModule(UpdateProfilePresenter.UpdateProfileView UpdateProfileview) {
        this.UpdateProfileview = UpdateProfileview;
    }

    @Provides
    @Singleton
    UpdateProfilePresenter provideUpdateProfilePresenter(Bus bus) {
        return new UpdateProfilePresenter(UpdateProfileview, bus);
    }
}
