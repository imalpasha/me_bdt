package com.app.tbd.ui.Activity.Profile.UserProfile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
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

public class EditProfileFragment extends BaseFragment {

    @InjectView(R.id.edit_salutation)
    TextView edit_salutation;

    @InjectView(R.id.edit_given_name)
    EditText edit_given_name;

    @InjectView(R.id.edit_family_name)
    EditText edit_family_name;

    @InjectView(R.id.edit_dob)
    TextView edit_dob;

    @InjectView(R.id.edit_nationality)
    TextView edit_nationality;

    @InjectView(R.id.edit_mobile)
    EditText edit_mobile;

    @InjectView(R.id.edit_passport)
    EditText edit_passport;

    @InjectView(R.id.edit_street1)
    EditText edit_street1;

    @InjectView(R.id.edit_street2)
    EditText edit_street2;

    @InjectView(R.id.edit_city)
    EditText edit_city;

    @InjectView(R.id.edit_post_code)
    EditText edit_post_code;

    @InjectView(R.id.edit_state)
    TextView edit_state;

    @InjectView(R.id.edit_country)
    TextView edit_country;

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    private int fragmentContainerId;
    private SharedPrefManager pref;

    public static EditProfileFragment newInstance(Bundle bundle) {

        EditProfileFragment fragment = new EditProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new MyProfileModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        Bundle bundle = getArguments();
        String insurance = bundle.getString("USER_INFORMATION");

        Gson gson = new Gson();
        ViewUserReceive obj = gson.fromJson(insurance, ViewUserReceive.class);

        edit_salutation.setText(obj.getTitle());
        edit_given_name.setText(obj.getFirstName());
        edit_family_name.setText(obj.getEmergencyFamilyName());
        edit_mobile.setText(obj.getMobilePhone());
        edit_passport.setText(obj.getPID());
        edit_street1.setText(obj.getAddressLine1());
        edit_street2.setText(obj.getAddressLine2());
        edit_city.setText(obj.getCity());
        edit_post_code.setText(obj.getPostalCode());

        txtUserName.setText(obj.getFirstName() + obj.getLastName());
        txtUserName.setText(obj.getFirstName() + obj.getLastName());

        String date = obj.getDOB();

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
            edit_dob.setText(reportDate);
            pref.setEditDOB(reportDate);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        String c = getCountryName(getActivity(), obj.getCountryCode());
        String n = getCountryName(getActivity(), obj.getNationality());

        edit_country.setText(c);
        edit_nationality.setText(n);

        HashMap<String, String> initAuth = pref.getEditStateName();
        final String state = initAuth.get(SharedPrefManager.EDIT_STATE_NAME);

        edit_state.setText(state);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

}



