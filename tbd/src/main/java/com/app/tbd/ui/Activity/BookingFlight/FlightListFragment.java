package com.app.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FlightListFragment extends BaseFragment {

    @InjectView(R.id.flightLayout)
    LinearLayout flightLayout;

    @InjectView(R.id.txtDepartureStation)
    TextView txtDepartureStation;

    @InjectView(R.id.txtArrivalStation)
    TextView txtArrivalStation;

    @InjectView(R.id.txtDepartureDate)
    TextView txtDepartureDate;

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


        setData();

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.bottom_up);
        flightLayout.setAnimation(animation);

        return view;
    }

    public void setData() {

        Bundle bundle = getArguments();
        String flightList = bundle.getString("FLIGHT_LIST");

        Gson gson = new Gson();
        SearchFlightReceive flightReceive = gson.fromJson(flightList, SearchFlightReceive.class);

        convertDate(flightReceive.getJourneyDateMarket().get(0).getDepartureDate());
        txtDepartureStation.setText(flightReceive.getJourneyDateMarket().get(0).getDepartureStation());
        txtArrivalStation.setText(flightReceive.getJourneyDateMarket().get(0).getArrivalStation());
        txtDepartureDate.setText(flightReceive.getJourneyDateMarket().get(0).getDepartureDate());


    }

    public void convertDate(String date) {

        // String date = "Sat Dec 01 00:00:00 GMT 2012";
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try {
            Date date2 = format.parse(date);
            Log.e("Date", date2.toString());
        } catch (Exception e) {

        }
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
