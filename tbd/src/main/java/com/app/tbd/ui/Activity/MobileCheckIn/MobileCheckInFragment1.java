package com.app.tbd.ui.Activity.MobileCheckIn;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.MobileCheckinReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Adapter.BookingListAdapter;
import com.app.tbd.ui.Module.MobileCheckInModule1;
import com.app.tbd.ui.Model.Request.MobileCheckInList;
import com.app.tbd.ui.Model.Request.CachedResult;
import com.app.tbd.ui.Model.Request.MobileCheckinObj;
import com.app.tbd.ui.Presenter.MobileCheckInPresenter;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class MobileCheckInFragment1 extends BaseFragment implements MobileCheckInPresenter.MobileCheckInView,Validator.ValidationListener {

    @Inject
    MobileCheckInPresenter presenter;

    @InjectView(R.id.mobileCheckInNext1) Button mobileCheckInNext1;

    @NotEmpty
    @InjectView(R.id.editPnr) EditText editPnr;

    @NotEmpty
    @InjectView(R.id.txtDeparture) TextView txtDeparture;

    @NotEmpty
    @InjectView(R.id.txtArrive) TextView txtArrive;

    @InjectView(R.id.mobileCheckInPNRLayout)
    LinearLayout mobileCheckInPNRLayout;

    @InjectView(R.id.listView)
    ListView listView;

    @InjectView(R.id.mobileCheckInNA)
    LinearLayout mobileCheckInNA;

    @InjectView(R.id.listviewLayout)
    LinearLayout listviewLayout;

    @InjectView(R.id.horizontalProgressBar)
    ProgressBar horizontalProgressBar;

    @InjectView(R.id.mobileCheckInNoInternet)
    LinearLayout mobileCheckInNoInternet;

    private ArrayList<DropDownItem> dataFlightDeparture;
    private static ArrayList<DropDownItem> dataFlightArrival;
    private SharedPrefManager pref;
    private String DEPARTURE_FLIGHT = "Please choose your departure airport";
    private String ARRIVAL_FLIGHT = "Please choose your arrival airport";
    private Validator mValidator;
    private String loginStatus;
    private String storeUsername;
    private int fragmentContainerId;
    public String SCREEN_LABEL = "Login Mobile Check-In";
    private BookingListAdapter adapter;
    private String signatureFromLocal;
    private boolean cache_login = false;
    private boolean retrieveCheckIn = false;
    private boolean cachedDisplay = false;
    private String customerNumber;

    public static MobileCheckInFragment1 newInstance() {

        MobileCheckInFragment1 fragment = new MobileCheckInFragment1();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new MobileCheckInModule1(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.mobile_checkin_1, container, false);
        ButterKnife.inject(this, view);

        /*Preference Manager*/
        pref = new SharedPrefManager(MainFragmentActivity.getContext());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

        HashMap<String, String> initLogin = pref.getLoginStatus();
        loginStatus = initLogin.get(SharedPrefManager.ISLOGIN);

        HashMap<String, String> initUsername = pref.getUserEmail();
        storeUsername = initUsername.get(SharedPrefManager.USER_EMAIL);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String storePassword = initPassword.get(SharedPrefManager.PASSWORD);

        HashMap<String, String> initCustomerNumber = pref.getCustomerNumber();
        customerNumber = initCustomerNumber.get(SharedPrefManager.CUSTOMER_NUMBER);


        if(loginStatus != null && loginStatus.equals("Y")) {

            RealmResults<MobileCheckInList> cachedListResult = RealmObjectController.getMobileCheckInList(MainFragmentActivity.getContext());
            if(cachedListResult.size() > 0){
                Gson gson = new Gson();
                ListBookingReceive obj = gson.fromJson(cachedListResult.get(0).getCachedList(), ListBookingReceive.class);
                onUserPnrList(obj);
                cachedDisplay = true;
                horizontalProgressBar.setVisibility(View.VISIBLE);
                //update the list on background
                presenter.getUserPNR(storeUsername,storePassword,"check_in",customerNumber);

            }else{
                if(MainController.connectionAvailable(getActivity())){
                    initiateLoading(getActivity());
                    presenter.getUserPNR(storeUsername,storePassword,"check_in",customerNumber);
                }else{
                    mobileCheckInNoInternet.setVisibility(View.VISIBLE);
                }
            }

            mobileCheckInNext1.setVisibility(View.GONE);

        }else{
            SCREEN_LABEL =  "Mobile Check-In";
            mobileCheckInPNRLayout.setVisibility(View.VISIBLE);
            listviewLayout.setVisibility(View.GONE);
            txtDeparture.setTag(DEPARTURE_FLIGHT);
            txtArrive.setTag(ARRIVAL_FLIGHT);

            /*Retrieve All Flight Data From Preference Manager - Display Flight Data*/
            JSONArray jsonFlight = getFlight(getActivity());
            dataFlightDeparture = new ArrayList<>();

            ArrayList<String> tempFlight = new ArrayList<>();

            /*Get All Airport - remove redundant*/
            List<String> al = new ArrayList<>();
            Set<String> hs = new LinkedHashSet<>();
            for (int i = 0; i < jsonFlight.length(); i++) {
                JSONObject row = (JSONObject) jsonFlight.opt(i);
                if(!row.optString("status").equals("N") && !row.optString("mobile_check_in").equals("N")){
                    al.add(row.optString("location")+"/-"+row.optString("location_code"));
                }
            }
            hs.addAll(al);
            al.clear();
            al.addAll(hs);

            /*Display Airport*/
            for (int i = 0; i < al.size(); i++)
            {
                String flightSplit = al.get(i).toString();
                String[] str1 = flightSplit.split("/-");
                String p1 = str1[0];
                String p2 = str1[1];

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(p1+ " ("+p2+")");
                itemFlight.setCode(p2);
                itemFlight.setTag("FLIGHT");
                dataFlightDeparture.add(itemFlight);

            }
        }

        /*Set PNR auto caps*/
        InputFilter[] FilterArray = new InputFilter[2];
        FilterArray[0] = new InputFilter.LengthFilter(6);
        FilterArray[1] = new InputFilter.AllCaps();
        editPnr.setFilters(FilterArray);

        //editPnr.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        //editPnr.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});

       /*Departure Flight Clicked*/
        txtDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "txtDeparture");
                popupSelection(dataFlightDeparture, getActivity(), txtDeparture,true,view);
                txtArrive.setText("");
                //txtDeparture.setText("ARRIVAL AIRPORT");
            }
        });

         /*Arrival Flight Clicked*/
        txtArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnArrivalFlight");
                if(txtDeparture.getTag().toString().equals("NOT SELECTED"))
                {
                    popupAlert("Select Departure Airport First");
                }else{
                    popupSelection(dataFlightArrival, getActivity(), txtArrive,true,view);
                }
            }
        });

        mobileCheckInNext1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();

            }
        });
        return view;
    }

    @Override
    public void onUserPnrList(final ListBookingReceive obj){

        if(!cachedDisplay){
            dismissLoading();
        }else{
            horizontalProgressBar.setVisibility(View.INVISIBLE);
        }

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            if (obj.getList_booking().size() == 0) {
                mobileCheckInNA.setVisibility(View.VISIBLE);
                listviewLayout.setVisibility(View.GONE);
            } else {
                mobileCheckInNA.setVisibility(View.GONE);
                listviewLayout.setVisibility(View.VISIBLE);
                adapter = new BookingListAdapter(getActivity(), obj.getList_booking(),"MC");
                listView.setAdapter(adapter);
                pref.setSignatureToLocalStorage(obj.getSignature());
                //pnrLayout.setVisibility(View.GONE);
            }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng) {
                ListBookingReceive.ListBooking selectedFromList = (ListBookingReceive.ListBooking) (listView.getItemAtPosition(myItemInt));
                initiateLoading(getActivity());

                // ManageFlightObj manageFlightObj = new ManageFlightObj();
                // manageFlightObj.setPnr(selectedFromList.getPnr());
                // manageFlightObj.setUsername(storeUsername);

                // presenter.checkinFlight(flightObj);

                // presenter.onSendPNRV1(manageFlightObj);

                horizontalProgressBar.setVisibility(View.INVISIBLE);

                MobileCheckinObj flightObj = new MobileCheckinObj();
                flightObj.setPnr(selectedFromList.getPnr());
                flightObj.setUser_id(obj.getUser_id());
                flightObj.setDeparture_station(selectedFromList.getDeparture_station_code());
                flightObj.setArrival_station(selectedFromList.getArrival_station_code());
                flightObj.setSignature(obj.getSignature());

                searchFlightFragment(flightObj);

            }
        });

        }

    }





    @Override
    public void onValidationSucceeded() {
        checkinFlight();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);
             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof TextView) {
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof EditText){
                ((EditText) view).setError(splitErrorMsg[0]);
            }

        }

    }

    public void checkinFlight(){

        initiateLoading(getActivity());

        MobileCheckinObj flightObj = new MobileCheckinObj();
        flightObj.setPnr(editPnr.getText().toString());
        flightObj.setDeparture_station(txtDeparture.getTag().toString());
        flightObj.setArrival_station(txtArrive.getTag().toString());
        //flightObj.setSignature(signatureFromLocal);
        searchFlightFragment(flightObj);
                    /*FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.main_activity_fragment_container, BF_FlightDetailFragment.newInstance(), "FLIGHT_DETAIL");
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/

    }

    public void searchFlightFragment(MobileCheckinObj flightObj){
        retrieveCheckIn = true;
        presenter.checkinFlight(flightObj);
    }

    /*Filter Arrival Airport*/
    public static void filterArrivalAirport(String code) {

        JSONArray jsonFlight = getFlight(MainFragmentActivity.getContext());
        dataFlightArrival = new ArrayList<>();

            /*Display Arrival*/
        for (int i = 0; i < jsonFlight.length(); i++)
        {
            JSONObject row = (JSONObject) jsonFlight.opt(i);
            if(code.equals(row.optString("location_code")) && row.optString("status").equals("Y") && row.optString("mobile_check_in").equals("Y") ) {

                DropDownItem itemFlight = new DropDownItem();
                itemFlight.setText(row.optString("travel_location")+" ("+row.optString("travel_location_code")+")");
                itemFlight.setCode(row.optString("travel_location_code" + ""));
                itemFlight.setTag("FLIGHT_DEPARTURE");
                dataFlightArrival.add(itemFlight);

            }
        }

}

    @Override
    public void onCheckindataReceive(MobileCheckinReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent flight = new Intent(getActivity(), MobileCheckInActivity2.class);
            flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
            //flight.putExtra("PNR", editPnr.getText().toString());
            //flight.putExtra("DEPARTURE_CODE", txtDeparture.getTag().toString());
            //flight.putExtra("ARRIVAL_CODE", txtArrive.getTag().toString());

            getActivity().startActivity(flight);
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
        presenter.onResume();

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);

        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if (!retrieveCheckIn) {
            if (result.size() > 0) {
                Gson gson = new Gson();
                ListBookingReceive obj = gson.fromJson(result.get(0).getCachedResult(), ListBookingReceive.class);
                onUserPnrList(obj);
            }
        }else {
                if (result.size() > 0) {
                    Gson gson = new Gson();
                    MobileCheckinReceive obj = gson.fromJson(result.get(0).getCachedResult(), MobileCheckinReceive.class);
                    onCheckindataReceive(obj);
                }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
