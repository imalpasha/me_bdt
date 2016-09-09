package com.app.tbd.ui.Activity.MyProfile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.EditProfile.EditProfileActivity;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.app.tbd.ui.Module.MyProfileModule;
import com.app.tbd.ui.Presenter.MyProfilePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyProfileFragment extends BaseFragment implements MyProfilePresenter.MyProfileView{

    @Inject
    MyProfilePresenter presenter;

    @InjectView(R.id.profile_salutation)
    TextView profile_salutation;

    @InjectView(R.id.profile_given_name)
    TextView profile_given_name;

    @InjectView(R.id.profile_family_name)
    TextView profile_family_name;

    @InjectView(R.id.profile_dob)
    TextView profile_dob;

    @InjectView(R.id.profile_nationality)
    TextView profile_nationality;

    @InjectView(R.id.profile_mobile)
    TextView profile_mobile;

    @InjectView(R.id.profile_passport)
    TextView profile_passport;

    @InjectView(R.id.profile_street1)
    TextView profile_street1;

    @InjectView(R.id.profile_street2)
    TextView profile_street2;

    @InjectView(R.id.profile_city)
    TextView profile_city;

    @InjectView(R.id.profile_post_code)
    TextView profile_post_code;

    @InjectView(R.id.profile_state)
    TextView profile_state;

    @InjectView(R.id.profile_country)
    TextView profile_country;

    @InjectView(R.id.edit_btn)
    Button edit_btn;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    String userInfo;

    public static MyProfileFragment newInstance() {

        MyProfileFragment fragment = new MyProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new MyProfileModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initAuth = pref.getUsername();
        final String username = initAuth.get(SharedPrefManager.USERNAME);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String password = initPassword.get(SharedPrefManager.PASSWORD);

        HashMap<String, String> initTicketId = pref.getTicketId();
        String ticketId = initTicketId.get(SharedPrefManager.TICKET_ID);

        ViewUserRequest data = new ViewUserRequest();
        data.setUserName(username);
        data.setPassword(password);
        data.setTicketId(ticketId);
        Log.e("Username from pref", username);
        Log.e("Password from pref", password);
        Log.e("Ticket ID from pref", ticketId);
        presenter.showFunction(data);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editPage = new Intent(getActivity(), EditProfileActivity.class);
                editPage.putExtra("USER_INFORMATION", userInfo);
                getActivity().startActivity(editPage);
                getActivity().finish();

            }
        });
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

    @Override
    public void onViewUserSuccess(ViewUserReceive obj) {
        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), "", getActivity());
        if (status){
            Log.e("status", obj.getStatus());
            setShow(obj);
            userInfo = new Gson().toJson(obj);

        }
    }

    @Override
    public void onSuccessRequestState(StateReceive obj) {
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            HashMap<String, String> initStateCode = pref.getStateCode();
            String stateCode = initStateCode.get(SharedPrefManager.STATECODE);

            for (int x = 0; x < obj.getStateList().size(); x++) {
                if (stateCode.equals(obj.getStateList().get(x).getProvinceStateCode())) {
                    String z = obj.getStateList().get(x).getProvinceStateName();
                    profile_state.setText(z);
                    pref.setEditStateName(z);
                }
            }
        }
    }

    public void setShow(ViewUserReceive returnData) {
        String salutation = returnData.getTitle();
        String givenName = returnData.getFirstName();
        String familyName = returnData.getLastName();
        String dob = returnData.getDOB();
        String nationality = returnData.getNationality();
        String mobile = returnData.getMobilePhone();
        String passport = returnData.getPID();
        String street1 = returnData.getAddressLine1();
        String street2 = returnData.getAddressLine2();
        String city = returnData.getCity();
        String postcode = returnData.getPostalCode();
        pref.setStateCode(returnData.getProvinceStateCode());
        String country = returnData.getCountryCode();

        String date = dob;

        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4);

        Log.e("Day", day);
        Log.e("Month", month);
        Log.e("year", year);

        String newDob = day + "-" + month + "-" + year;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate;
        try {
            startDate = df.parse(newDob);

            Date myDate = startDate;
            System.out.println(myDate);
            System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(myDate));
            System.out.println(new SimpleDateFormat("dd MMM yyyy").format(myDate));
            System.out.println(myDate);
            String reportDate = (new SimpleDateFormat("dd MMM yyyy").format(myDate));
            profile_dob.setText(reportDate);
            pref.setEditDOB(reportDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        //State API request
        StateRequest stateRequest = new StateRequest();
        stateRequest.setLanguageCode("en-GB");
        stateRequest.setCountryCode(country);

        presenter.onStateRequest(stateRequest);

        //call from local
        String c = getCountryName(getActivity(), country);
        String n = getCountryName(getActivity(), nationality);

        Log.e("CountryName", c);
        Log.e("Nationality", n);

        profile_salutation.setText(salutation);
        profile_given_name.setText(givenName);
        profile_family_name.setText(familyName);
        profile_nationality.setText(n);
        profile_mobile.setText(mobile);
        profile_passport.setText(passport);
        profile_street1.setText(street1);
        profile_street2.setText(street2);
        profile_city.setText(city);
        profile_post_code.setText(postcode);
        profile_country.setText(c);



    }
}



