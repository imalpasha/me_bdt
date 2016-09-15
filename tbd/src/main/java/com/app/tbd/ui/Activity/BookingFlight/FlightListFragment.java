package com.app.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Realm.RealmObjectController;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightListFragment extends BaseFragment {

    @InjectView(R.id.flightLayout)
    LinearLayout flightLayout;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.flight_detail, container, false);
        ButterKnife.inject(this, view);

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        flightLayout.setAnimation(animation);

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
