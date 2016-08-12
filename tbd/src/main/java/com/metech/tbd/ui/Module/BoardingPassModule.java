package com.metech.tbd.ui.Module;

import com.metech.tbd.AppModule;
import com.metech.tbd.ui.Activity.BoardingPass.BoardingPassFragment;
import com.metech.tbd.ui.Presenter.BoardingPassPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = BoardingPassFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class BoardingPassModule {

    private final BoardingPassPresenter.RetrieveBoardingPassView boardingPassView;

    public BoardingPassModule(BoardingPassPresenter.RetrieveBoardingPassView view) {
        this.boardingPassView = view;
    }

    @Provides
    @Singleton
    BoardingPassPresenter provideChangePasswordPresenter(Bus bus) {
        return new BoardingPassPresenter(boardingPassView, bus);
    }
}
