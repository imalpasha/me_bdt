package com.metech.tbd.ui.Activity.BookingFlight;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.R;
import com.metech.tbd.ui.Model.Receive.SearchFlightReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Module.SearchFlightModule;
import com.metech.tbd.ui.Model.Request.SearchFlightObj;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;
import com.mobsandgeeks.saripaar.Validator;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFlightFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,BookingPresenter.SearchFlightView {

    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.departureDateBlock)
    LinearLayout departureDateBlock;

    @InjectView(R.id.returnDateBlock)
    LinearLayout returnDateBlock;

    @InjectView(R.id.btnSearchFlight)
    Button btnSearchFlight;




    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Search Flight";

    private SharedPrefManager pref;

    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private String DEPARTURE_FLIGHT_DATE = "Please choose your departure date.";
    private String ARRIVAL_FLIGHT_DATE = "Please choose your return date.";
    private String FLIGHT_OBJECT = "FLIGHT_OBJECT";

    private String DEPARTURE_DATE_PICKER = "DEPARTURE_DATE_PICKER";
    private String RETURN_DATE_PICKER = "RETURN_DATE_PICKER";
    private String PICKER;
    public static final String DATEPICKER_TAG = "DATEPICKER_TAG";
    private AlertDialog dialog;
    private Validator mValidator;
    private DatePickerDialog datePickerDialog2;
    private int departureDay,departureMonth,departureYear = 0;
    private SearchFlightObj flightObj;

    /*DatePicker Setup - Failed to make it global*/
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);

    //NEED TO CREATE DIFFERENT INSTANCE FOR EACH CALENDAR TO AVOID DISPLAYING PREVIOUS SELECTED.
    final DatePickerDialog searchFlight_DatePicker = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));


    public static SearchFlightFragment newInstance() {

        SearchFlightFragment fragment = new SearchFlightFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new SearchFlightModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_flight, container, false);
        ButterKnife.inject(this, view);
        searchFlight_DatePicker.setYearRange(year, year + 1);

        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightDetail = new Intent(getActivity(), FlightListActivity.class);
                getActivity().startActivity(flightDetail);
            }
        });

        departureDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "departureBlock");

                if(checkFragmentAdded()){
                    searchFlight_DatePicker.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
                }
                PICKER = DEPARTURE_DATE_PICKER;
            }
        });

                /*Arrival Date Clicked*/
        returnDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkFragmentAdded()){
                    searchFlight_DatePicker.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
                }
                PICKER = RETURN_DATE_PICKER;
            }
        });



        return view;
    }

    @Override
    public void onBookingDataReceive(SearchFlightReceive obj) {


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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);


    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {


    }
}
