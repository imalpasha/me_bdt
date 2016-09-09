package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.MyProfile.MyProfileFragment;
import com.app.tbd.ui.Presenter.MyProfilePresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = MyProfileFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class MyProfileModule {

    private final MyProfilePresenter.MyProfileView myProfileView;

    public MyProfileModule(MyProfilePresenter.MyProfileView myProfileView) {
        this.myProfileView = myProfileView;
    }

    @Provides
    @Singleton
    MyProfilePresenter provideMyProfilePresenter(Bus bus) {
        return new MyProfilePresenter(myProfileView, bus);
    }
}
