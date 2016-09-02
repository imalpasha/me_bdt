package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.ManageFamilyAndFriend.ManageFriendAndFamilyFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = ManageFriendAndFamilyFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class DeleteFFModule {

    private final BookingPresenter.DeleteFamilyView editFamilyFriendView;

    public DeleteFFModule(BookingPresenter.DeleteFamilyView editFamilyFriendView) {
        this.editFamilyFriendView = editFamilyFriendView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(editFamilyFriendView, bus);
    }
}
