package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BookingFlight.ManageFamilyAndFriend.EditFamilyFriendsFragment;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = EditFamilyFriendsFragment.class,
        addsTo = AppModule.class,
        complete = false
)

public class EditFamilyFriendModule {

    private final BookingPresenter.EditFamilyFriendView editFamilyFriendView;

    public EditFamilyFriendModule(BookingPresenter.EditFamilyFriendView editFamilyFriendView) {
        this.editFamilyFriendView = editFamilyFriendView;
    }

    @Provides
    @Singleton
    BookingPresenter provideFlightDetailPresenter(Bus bus) {
        return new BookingPresenter(editFamilyFriendView, bus);
    }
}
