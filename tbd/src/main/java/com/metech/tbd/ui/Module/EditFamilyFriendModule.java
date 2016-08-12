package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BookingFlight.ManageFamilyAndFriend.EditFamilyFriendsFragment;
import com.metech.tbd.ui.Presenter.BookingPresenter;
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
