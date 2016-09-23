package com.app.tbd.ui.Activity.BookingFlight;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.R;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Picker.SelectFlightFragment;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Picker.SelectionListFragment;
import com.app.tbd.ui.Activity.Picker.SelectionListFragmentV2;
import com.app.tbd.ui.Model.Receive.SearchFlightReceive;
import com.app.tbd.ui.Model.Receive.SignatureReceive;
import com.app.tbd.ui.Model.Request.SearchFlightRequest;
import com.app.tbd.ui.Model.Request.SignatureRequest;
import com.app.tbd.ui.Module.SearchFlightModule;
import com.app.tbd.ui.Presenter.BookingPresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchFlightFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener, BookingPresenter.SearchFlightView {


    @Inject
    BookingPresenter presenter;

    @InjectView(R.id.departureDateBlock)
    LinearLayout departureDateBlock;

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

    @InjectView(R.id.testImage)
    ImageView testImage;

    @InjectView(R.id.btnReturn)
    LinearLayout btnReturn;

    @InjectView(R.id.btnOneWay)
    LinearLayout btnOneWay;

    @InjectView(R.id.returnDateBlock)
    LinearLayout returnDateBlock;


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
    final Calendar calendar = Calendar.getInstance();
    static ArrayList<DropDownItem> arrivalFlight = new ArrayList<DropDownItem>();
    static String stationCode;
    String currency;
    static Boolean arrivalClickable = false;
    Boolean oneWay = false;

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
        //initiateFlightStation();


        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                returnDateBlock.setVisibility(View.VISIBLE);
                oneWay = false;

            }
        });

        btnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                returnDateBlock.setVisibility(View.GONE);
                oneWay = true;

            }
        });

        testImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.pop_in);
                animation.setFillAfter(true);
                testImage.setAnimation(animation);

            }
        });

        btnDepartureFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CURRENT_PICKER = "DEPARTURE_FLIGHT";
                showCountrySelector(getActivity(), initiateFlightStation(getActivity()), CURRENT_PICKER);
            }
        });

        btnArrivalFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (arrivalClickable) {
                    CURRENT_PICKER = "ARRIVAL_FLIGHT";
                    showCountrySelector(getActivity(), arrivalFlight, CURRENT_PICKER);
                } else {
                    Utils.toastNotification(getActivity(), DEPARTURE_FLIGHT);
                }
            }
        });


        btnSearchFlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent flightDetail = new Intent(getActivity(), FlightListActivity.class);
                //getActivity().startActivity(flightDetail);

                getSignature();

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

        SearchFlightRequest searchFlightRequest = new SearchFlightRequest();
        searchFlightRequest.setAdult("1");
        searchFlightRequest.setChild("1");
        searchFlightRequest.setInfant("1");
        searchFlightRequest.setArrivalStation0(txtArrivalFlight.getTag().toString());
        searchFlightRequest.setDepartureStation0(txtDepartureFlight.getTag().toString());
        Boolean twoWay = false;
        if (!oneWay) {
            searchFlightRequest.setArrivalStation1(txtDepartureFlight.getTag().toString());
            searchFlightRequest.setDepartureStation1(txtArrivalFlight.getTag().toString());
            searchFlightRequest.setDepartureDate1(bookFlightReturnDate.getTag().toString());
        }
        searchFlightRequest.setCurrencyCode(currency);
        searchFlightRequest.setDepartureDate0(bookFlightDepartureDate.getTag().toString());
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

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Log.e("Status", obj.getStatus());

            Gson gsonUserInfo = new Gson();
            String flightList = gsonUserInfo.toJson(obj);

            Intent flightDetail = new Intent(getActivity(), FlightListActivity.class);
            flightDetail.putExtra("FLIGHT_LIST", flightList);
            getActivity().startActivity(flightDetail);

        }

    }

    @Override
    public void onSignatureReceive(SignatureReceive obj) {

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            signature = obj.getSignature();
            searchFlight();
        }

    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act, ArrayList constParam, String data) {
        if (act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SelectionListFragment routeListDialogFragment = SelectionListFragment.newInstance(constParam, data);
                routeListDialogFragment.setTargetFragment(SearchFlightFragment.this, 0);
                routeListDialogFragment.show(fm, "countryListDialogFragment");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<DropDownItem> initiateFlightStation(Activity act) {

        ArrayList<DropDownItem> flightMarket = new ArrayList<DropDownItem>();
        JSONArray jsonFlight = getFlight(act);


        //sort here
        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonFlight.length(); i++) {
            try {
                jsonList.add(jsonFlight.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get("DepartureStationName");
                    valB = (String) b.get("DepartureStationName");
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonFlight.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }

        //Get All Airport - remove redundant
        List<String> al = new ArrayList<>();
        Set<String> hs = new LinkedHashSet<>();
        for (int i = 0; i < sortedJsonArray.length(); i++) {
            JSONObject row = (JSONObject) sortedJsonArray.opt(i);
            if (!row.optString("DepartureStationName").equals("") && !row.optString("ArrivalStationName").equals("")) {
                al.add(row.optString("DepartureStationName") + "/-" + row.optString("DepartureStation") + "/-" + row.optString("DepartureCountryName") + "/-" + row.optString("DepartureStationCurrencyCode"));
            }
        }

        hs.addAll(al);
        al.clear();
        al.addAll(hs);

        for (int i = 0; i < al.size(); i++) {
            String flightSplit = al.get(i).toString();
            String[] str1 = flightSplit.split("/-");
            String p1 = str1[0];
            String p2 = str1[1];
            String p3 = str1[2];
            String p4 = str1[3];

            DropDownItem itemFlight = new DropDownItem();
            itemFlight.setText(p1 + " (" + p2 + ")");
            itemFlight.setCode(p2 + "/" + p3 + "/" + p4);
            itemFlight.setTag("FLIGHT");
            flightMarket.add(itemFlight);

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
                DropDownItem selectedFlight = data.getParcelableExtra(CURRENT_PICKER);

                if (CURRENT_PICKER.equals("DEPARTURE_FLIGHT")) {
                    txtDepartureFlight.setText(selectedFlight.getText());

                    String codeSplit = selectedFlight.getCode();
                    String[] str1 = codeSplit.split("/");
                    String p1 = str1[0];
                    String p3 = str1[2];
                    stationCode = p1;
                    currency = p3;

                    txtDepartureFlight.setTag(p1);

                    arrivalFlight = new ArrayList<DropDownItem>();
                    arrivalFlight = initiateArrivalStation(getActivity());

                } else {

                    String codeSplit = selectedFlight.getCode();
                    String[] str1 = codeSplit.split("/");
                    String p1 = str1[0];
                    stationCode = p1;

                    txtArrivalFlight.setText(selectedFlight.getText());
                    txtArrivalFlight.setTag(stationCode);
                }

            }
        }
    }

    public static ArrayList<DropDownItem> initiateArrivalStation(Activity act) {

        JSONArray jsonFlight = getFlight(act);

        JSONArray sortedJsonArray = new JSONArray();
        List<JSONObject> jsonList = new ArrayList<JSONObject>();
        for (int i = 0; i < jsonFlight.length(); i++) {
            try {
                jsonList.add(jsonFlight.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Collections.sort(jsonList, new Comparator<JSONObject>() {

            public int compare(JSONObject a, JSONObject b) {
                String valA = new String();
                String valB = new String();

                try {
                    valA = (String) a.get("ArrivalStationName");
                    valB = (String) b.get("ArrivalStationName");
                } catch (JSONException e) {
                    //do something
                }

                return valA.compareTo(valB);
            }
        });

        for (int i = 0; i < jsonFlight.length(); i++) {
            sortedJsonArray.put(jsonList.get(i));
        }

        for (int i = 0; i < sortedJsonArray.length(); i++) {
            JSONObject row = (JSONObject) sortedJsonArray.opt(i);

            DropDownItem item = new DropDownItem();
            if (stationCode.equals(row.optString("DepartureStation"))) {
                item.setText(row.optString("ArrivalStationName") + " (" + row.optString("ArrivalStation") + ")");
                item.setCode(row.optString("ArrivalStation") + "/" + row.optString("ArrivalCountryName"));
                item.setTag("ARRIVAL_STATION");
                arrivalFlight.add(item);
            }

        }
        arrivalClickable = true;
        return arrivalFlight;
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
