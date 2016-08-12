package com.metech.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class FlightListFragment extends BaseFragment {

    @Inject BookingPresenter presenter;

    //@InjectView(R.id.fareRuleLayout)LinearLayout fareRuleLayout;

    private int fragmentContainerId;

    public static FlightListFragment newInstance(Bundle bundle) {

        FlightListFragment fragment = new FlightListFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new SelectFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
