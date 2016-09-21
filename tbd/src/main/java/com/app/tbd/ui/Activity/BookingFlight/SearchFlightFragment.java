package com.app.tbd.ui.Activity.BookingFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.R;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Picker.SelectFlightFragment;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.SignatureReceive;
import com.app.tbd.ui.Model.Request.SearchFlightRequest;
import com.app.tbd.ui.Model.Request.SignatureRequest;
import com.app.tbd.ui.Module.SearchFlightModule;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.app.tbd.ui.Presenter.LanguagePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFlightFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, BookingPresenter.SearchFlightView {


    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.departureDateBlock)
    LinearLayout departureDateBlock;

    @InjectView(R.id.returnDateBlock)
    LinearLayout returnDateBlock;

    @InjectView(R.id.btnSearchFlight)
    Button btnSearchFlight;

    @InjectView(R.id.btnDepartureFlight)
    LinearLayout btnDepartureFlight;

    @InjectView(R.id.btnArrivalFlight)
    LinearLayout btnArrivalFlight;

    @InjectView(R.id.bookFlightDepartureDate)
    TextView bookFlightDepartureDate;

    @InjectView(R.id.bookFlightReturnDate)
    TextView bookFlightReturnDate;

    @InjectView(R.id.txtDepartureFlight)
    TextView txtDepartureFlight;

    @InjectView(R.id.txtArrivalFlight)
    TextView txtArrivalFlight;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Book Flight: Search Flight";
    private String signature;
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
    private String CURRENT_PICKER;
    /*DatePicker Setup - Failed to make it global*/
    final Calendar calendar = Calendar.getInstance();

    //NEED TO CREATE DIFFERENT INSTANCE FOR EACH CALENDAR TO AVOID DISPLAYING PREVIOUS SELECTED.
    DatePickerDialog searchFlight_DatePicker;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.search_flight, container, false);
        ButterKnife.inject(this, view);

        getActivity().setTitle(R.string.search_flight_page);

        datePickerSetting();
        //initiatePageData();

        btnDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), initiatePageData(getActivity()));
                CURRENT_PICKER = "DEPARTURE";
            }
        });

        btnArrivalFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), initiatePageData(getActivity()));
                CURRENT_PICKER = "ARRIVAL";
            }
        });


        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent flightDetail = new Intent(getActivity(), FlightListActivity.class);
                //getActivity().startActivity(flightDetail);

                getSignature();
                searchFlight();

            }
        });

        departureDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "departureBlock");
                if (checkFragmentAdded()) {
                    searchFlight_DatePicker.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
                }
                PICKER = DEPARTURE_DATE_PICKER;
            }
        });

                /*Arrival Date Clicked*/
        returnDateBlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    searchFlight_DatePicker.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
                }
                PICKER = RETURN_DATE_PICKER;
            }
        });


        return view;
    }

    public void searchFlight() {

        initiateLoading(getActivity());

        SearchFlightRequest searchFlightRequest = new SearchFlightRequest();
        searchFlightRequest.setAdult("1");
        searchFlightRequest.setChild("1");
        searchFlightRequest.setInfant("1");
        searchFlightRequest.setArrivalStation0("MEL");
        searchFlightRequest.setDepartureStation0("KUL");
        searchFlightRequest.setCurrencyCode("MYR");
        searchFlightRequest.setDepartureDate0("2016-09-21");
        searchFlightRequest.setSignature(signature);

        presenter.onSearchFlight(searchFlightRequest);

    }

    public void getSignature() {

        initiateLoading(getActivity());

        SignatureRequest signatureRequest = new SignatureRequest();
        presenter.onRequestSignature(signatureRequest);

    }

    @Override
    public void onSearchFlightReceive(SearchFlightReceive obj) {

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Log.e("Status", obj.getStatus());
        }

    }

    @Override
    public void onSignatureReceive(SignatureReceive obj) {

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Log.e("Status",signature);
            signature = obj.getSignature();
        }

    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act, ArrayList constParam) {
        if (act != null) {
            try {

                Log.e("at search flight", Integer.toString(constParam.size()));

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SelectFlightFragment countryListDialogFragment = com.app.tbd.ui.Activity.Picker.SelectFlightFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(SearchFlightFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<DropDownItem> initiatePageData(Activity act) {

        ArrayList<DropDownItem> flightMarket = new ArrayList<DropDownItem>();
        /*ArrayList<DropDownItem> flightMarket = new ArrayList<DropDownItem>();
        JSONArray jsonFlight = getFlight(act);

        //Get All Airport - remove redundant
        List<String> al = new ArrayList<>();
        Set<String> hs = new LinkedHashSet<>();
        for (int i = 0; i < jsonFlight.length(); i++) {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            if (!row.optString("status").equals("N")) {
                al.add(row.optString("location") + "/-" + row.optString("location_code"));
            }
        }
        hs.addAll(al);
        al.clear();
        al.addAll(hs);
        */

        /*
        for (int i = 0; i < al.size(); i++) {
            String flightSplit = al.get(i).toString();
            String[] str1 = flightSplit.split("/-");
            String p1 = str1[0];
            String p2 = str1[1];

            DropDownItem itemFlight = new DropDownItem();
            itemFlight.setText(p1 + " (" + p2 + ")");
            itemFlight.setCode(p2);
            itemFlight.setTag("FLIGHT");
            flightMarket.add(itemFlight);

        }*/

        	/*Travel Doc*/
        final String[] language = act.getResources().getStringArray(R.array.dummy_flight);
        for (int i = 0; i < language.length; i++) {
            String flight = language[i];
            String[] splitDoc = flight
                    .split("-");

            DropDownItem itemDoc = new DropDownItem();
            itemDoc.setText(splitDoc[0]);
            itemDoc.setCode(splitDoc[1]);
            flightMarket.add(itemDoc);
        }

        return flightMarket;
    }

    public void datePickerSetting() {
        //datePicker setting
        searchFlight_DatePicker = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        searchFlight_DatePicker.setYearRange(calendar.get(Calendar.YEAR), calendar.get(Calendar.YEAR) + 1);
        searchFlight_DatePicker.setAccentColor(ContextCompat.getColor(getActivity(), R.color.default_theme_colour));
        Calendar output = Calendar.getInstance();
        output.set(Calendar.YEAR, output.get(Calendar.YEAR));
        output.set(Calendar.DAY_OF_MONTH, output.get(Calendar.DAY_OF_MONTH) + 1);
        output.set(Calendar.MONTH, output.get(Calendar.MONTH));
        searchFlight_DatePicker.setMinDate(output);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                DropDownItem selectedFlight = data.getParcelableExtra(SelectFlightFragment.FLIGHT_SELECTION);

                if (CURRENT_PICKER.equals("DEPARTURE")) {
                    txtDepartureFlight.setText(selectedFlight.getText());
                } else {
                    txtArrivalFlight.setText(selectedFlight.getText());
                }

            }
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

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);


    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if (month < 9) {
            varMonth = "0";
        }
        if (day < 10) {
            varDay = "0";
        }
        if (PICKER.equals(DEPARTURE_DATE_PICKER)) {

            // departureDay = day;
            // departureMonth = month;
            //departureYear = year;

            bookFlightDepartureDate.setText(day + "/" + varMonth + "" + (month + 1) + "/" + year);
            bookFlightDepartureDate.setTag(year + "-" + varMonth + "" + (month + 1) + "-" + varDay + "" + day);
        } else if (PICKER.equals(RETURN_DATE_PICKER)) {
            bookFlightReturnDate.setText(day + "/" + varMonth + "" + (month + 1) + "/" + year);
            bookFlightReturnDate.setTag(year + "-" + varMonth + "" + (month + 1) + "-" + varDay + "" + day);
        } else {
            //DeadBlock
        }

    }
}
