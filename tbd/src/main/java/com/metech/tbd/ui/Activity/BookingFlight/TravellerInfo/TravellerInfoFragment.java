package com.metech.tbd.ui.Activity.BookingFlight.TravellerInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.BookingFlight.SSR.BaggageActivity;
import com.metech.tbd.ui.Activity.BookingFlight.SSR.MealActivity;
import com.metech.tbd.ui.Activity.Profile.ProfileActivity;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Presenter.LoginPresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.Validator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class TravellerInfoFragment extends BaseFragment{

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.btnContinueToSSR)
    Button btnContinueToSSR;

    private int fragmentContainerId;

    public static TravellerInfoFragment newInstance() {

        TravellerInfoFragment fragment = new TravellerInfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.traveller_info, container, false);
        ButterKnife.inject(this, view);

        btnContinueToSSR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baggage();
            }
        });


        return view;
    }


    /*Public-Inner Func*/
    public void baggage()
    {
        Intent profilePage = new Intent(getActivity(), BaggageActivity.class);
        getActivity().startActivity(profilePage);

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
