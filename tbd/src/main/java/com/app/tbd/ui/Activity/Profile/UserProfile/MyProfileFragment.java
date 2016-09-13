package com.app.tbd.ui.Activity.Profile.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MyProfileFragment extends BaseFragment implements ProfilePresenter.MyProfileView {

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

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    @InjectView(R.id.txtUserBigID)
    TextView txtUserBigID;


    @InjectView(R.id.edit_btn)
    Button edit_btn;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private String customerNumber;
    private String userInfo;
    private String stateCode;
    String stateName;

    public static MyProfileFragment newInstance(Bundle bundle) {

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_profile, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);
        pref = new SharedPrefManager(getActivity());

        Bundle bundle = getArguments();
        customerNumber = bundle.getString("BIG_ID");
        String userInformation = bundle.getString("USER_INFORMATION");

        Gson gson = new Gson();
        ViewUserReceive obj = gson.fromJson(userInformation, ViewUserReceive.class);
        setShow(obj);

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
    public void onSuccessRequestState(StateReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Gson gson = new Gson();
            String stateList = gson.toJson(obj.getStateList());
            pref.setState(stateList);
            stateName = getStateName(getActivity(), stateCode);
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
        //presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        //presenter.onPause();
    }


    public void setShow(ViewUserReceive returnData) {
        String salutation = returnData.getTitle();
        String givenName = returnData.getFirstName();
        String familyName = returnData.getLastName();
        String dob = returnData.getDOB();
        String nationalityCode = returnData.getNationality();
        String mobile = returnData.getMobilePhone();
        String passport = returnData.getPID();
        String street1 = returnData.getAddressLine1();
        String street2 = returnData.getAddressLine2();
        String city = returnData.getCity();
        String postcode = returnData.getPostalCode();
        stateCode = returnData.getProvinceStateCode();

        String countryCode = returnData.getCountryCode();

        String date = dob;

        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4);

        String newDob = day + "-" + month + "-" + year;

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate;
        try {
            startDate = df.parse(newDob);
            Date myDate = startDate;
            String reportDate = (new SimpleDateFormat("dd MMM yyyy").format(myDate));
            profile_dob.setText(reportDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //call from local
        String country = getCountryName(getActivity(), countryCode);
        String nationality = getCountryName(getActivity(), nationalityCode);

        if (getStateName(getActivity(), stateCode) == null) {

            HashMap<String, String> initAppData = pref.getLanguageCountry();
            String langCountryCode = initAppData.get(SharedPrefManager.LANGUAGE_COUNTRY);

            StateRequest stateRequest = new StateRequest();
            stateRequest.setLanguageCode(langCountryCode);
            stateRequest.setCountryCode(countryCode);
            stateRequest.setPresenterName("ProfilePresenter");

        } else {
            stateName = getStateName(getActivity(), stateCode);
        }

        profile_salutation.setText(salutation);
        profile_given_name.setText(givenName);
        profile_family_name.setText(familyName);
        profile_nationality.setText(nationality);
        profile_mobile.setText(mobile);
        profile_passport.setText(passport);
        profile_street1.setText(street1);
        profile_street2.setText(street2);
        profile_city.setText(city);
        profile_post_code.setText(postcode);
        profile_country.setText(country);
        profile_state.setText(stateName);

        txtUserName.setText(returnData.getFirstName() + " " + returnData.getLastName());
        txtUserBigID.setText("BIG ID : " + customerNumber);

    }
}



