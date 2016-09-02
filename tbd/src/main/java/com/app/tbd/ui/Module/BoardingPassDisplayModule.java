package com.app.tbd.ui.Module;

import com.app.tbd.AppModule;
import com.app.tbd.ui.Activity.BoardingPass.BoardingPassDisplayFragment;
import com.app.tbd.ui.Presenter.BoardingPassPresenter;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = BoardingPassDisplayFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class BoardingPassDisplayModule {

    private final BoardingPassPresenter.RetrieveBoardingPassView boardingPassView;

    public BoardingPassDisplayModule(BoardingPassPresenter.RetrieveBoardingPassView view) {
        this.boardingPassView = view;
    }

    @Provides
    @Singleton
    BoardingPassPresenter provideChangePasswordPresenter(Bus bus) {
        return new BoardingPassPresenter(boardingPassView, bus);
    }
}
