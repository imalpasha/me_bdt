package com.metech.tbd.ui.Activity.PushNotification;

import android.app.PendingIntent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.metech.tbd.application.MainApplication;
import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import com.metech.tbd.ui.Module.RegisterNotification;
import com.metech.tbd.ui.Model.Request.PushNotificationObj;
import com.metech.tbd.ui.Presenter.HomePresenter;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;

//import com.estimote.sdk.BeaconManager;

public class PushNotificationFragment extends BaseFragment implements HomePresenter.PushNotification {

    // --------------------------------------------------------------------------------//

    private static final long LOCATION_ITERATION_PAUSE_TIME = 1000;
    private static final int NUMBER_OF_LOCATION_ITERATIONS = 10;
    private GoogleMap map; // Might be null if Google Play services APK is not available.

    private List<Geofence> myFences = new ArrayList<>();
    private GoogleApiClient googleApiClient;
    private PendingIntent geofencePendingIntent;
    //private UpdateLocationRunnable updateLocationRunnable;
    private LocationManager locationManager;
    private int marker = 0;
    private Location lastLocation;
    //FragmentManager fm;

    // --------------------------------------------------------------------------------//
    private Tracker mTracker;

    @Inject
    HomePresenter presenter;

    private int fragmentContainerId;


    public static PushNotificationFragment newInstance() {

        PushNotificationFragment fragment = new PushNotificationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new RegisterNotification(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);
        PushNotificationObj obj = new PushNotificationObj();
        obj.setUser_id("imalpasha@gmail.com");
        obj.setName("ImalPasha");
        obj.setToken("1234567890123456789012345678901234567890123456789012345678901234");
        obj.setCode("test");
        obj.setCmd("join");
        presenter.onRegisterNotification(obj);

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
        presenter.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


























}
