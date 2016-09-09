package com.app.tbd.ui.Presenter;

import com.app.tbd.ui.Model.Receive.EditProfileReceive;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class EditProfilePresenter {

    public interface EditProfileView {
        void onUpdateUserSuccess(EditProfileReceive obj);
    }

    private EditProfileView editProfileView;
    private final Bus bus;

    public EditProfilePresenter(EditProfileView view, Bus bus) {
        this.editProfileView = view;
        this.bus = bus;
    }


    @Subscribe
    public void onEditProfileSuccess(EditProfileReceive event) {
        editProfileView.onUpdateUserSuccess(event);
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}


