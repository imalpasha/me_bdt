package com.metech.tbd.ui.Presenter;

import com.squareup.otto.Bus;

public class SeatSelectionPresenter {

    public interface SeatSelectionView {

    }

    private final SeatSelectionView view;
    private final Bus bus;

    public SeatSelectionPresenter(SeatSelectionView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }


}
