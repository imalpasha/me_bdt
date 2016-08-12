package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.UpdateProfile.UpdateProfileFragment;
import com.metech.tbd.ui.Presenter.UpdateProfilePresenter;
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
