package com.app.tbd.ui.Activity.Profile.UserProfile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Picker.SelectionListFragment;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.EditProfileReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.EditProfileRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Module.MyProfileModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class MyProfileFragment extends BaseFragment implements ProfilePresenter.MyProfileView, DatePickerDialog.OnDateSetListener {

    @Inject
    ProfilePresenter presenter;

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    @InjectView(R.id.txtUserBigID)
    TextView txtUserBigID;

    @InjectView(R.id.edit_btn)
    Button edit_btn;

    @InjectView(R.id.myImgUserDP)
    ImageView myImgUserDP;


    static EditText profile_given_name;
    static EditText profile_family_name;
    static EditText profile_mobile;
    static EditText profile_passport;
    static EditText profile_street1;
    static EditText profile_street2;
    static EditText profile_post_code;
    static EditText profile_city;
    static TextView profile_salutation;
    static TextView profile_dob;
    static TextView profile_nationality;
    static TextView profile_state;
    static TextView profile_country;
    static Boolean selectable = false;
    static String token;
    static ProfilePresenter presenter2;
    static Drawable drawableRight, drawableRightDob;
    static EditProfileRequest editRequest;
    static String username;
    static LinearLayout userParentInformationBlock;
    static int travellerAge;
    static Boolean parentInfo;
    static TextView parentTitle;
    static EditText txtParentFullName;
    static EditText txtParentID;
    static EditText txtParentEmail;
    static String gender;
    static String cid;
    DatePickerDialog dob_picker;

    Calendar calendar = Calendar.getInstance();
    ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    ArrayList<DropDownItem> parentTitleList = new ArrayList<DropDownItem>();
    ArrayList<DropDownItem> countryList = new ArrayList<DropDownItem>();
    ArrayList<DropDownItem> stateList = new ArrayList<DropDownItem>();

    String DATEPICKER_TAG = "DATEPICKER_TAG";
    String CURRENT_PICKER;
    String customerNumber;
    String userInfo;
    String stateCode;
    String stateName;
    String date;

    SharedPrefManager pref;
    int fragmentContainerId;

    private static String userInformation;
    private ViewUserReceive viewUserReceive;
    private View view;

    public static MyProfileFragment newInstance(Bundle bundle) {

        MyProfileFragment fragment = new MyProfileFragment();
        fragment.setArguments(bundle);
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

        view = inflater.inflate(R.layout.my_profile, container, false);
        ButterKnife.inject(this, view);
        aq.recycle(view);

        pref = new SharedPrefManager(getActivity());

        dataSetup();

        Bundle bundle = getArguments();
        customerNumber = bundle.getString("BIG_ID");
        userInformation = bundle.getString("USER_INFORMATION");

        Gson gson = new Gson();
        viewUserReceive = gson.fromJson(userInformation, ViewUserReceive.class);
        setShow(viewUserReceive);

        HashMap<String, String> initTicket = pref.getToken();
        token = initTicket.get(SharedPrefManager.TOKEN);
        Log.e("token", token);

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //reconvert
                Intent editPage = new Intent(getActivity(), EditProfileActivity.class);
                editPage.putExtra("USER_INFORMATION", userInformation);
                getActivity().startActivity(editPage);

            }
        });

        profile_salutation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        showCountrySelector(getActivity(), titleList, "TITLE");
                        CURRENT_PICKER = "TITLE";
                    }
                }
            }
        });

        profile_nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        showCountrySelector(getActivity(), countryList, "NATIONALITY");
                        CURRENT_PICKER = "NATIONALITY";
                    }
                }
            }
        });

        profile_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        dob_picker.show(getActivity().getFragmentManager(), DATEPICKER_TAG);
                    }
                }
            }
        });

        profile_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        showCountrySelector(getActivity(), stateList, "STATE");
                        CURRENT_PICKER = "STATE";
                    }
                }
            }
        });

        profile_country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        showCountrySelector(getActivity(), countryList, "COUNTRY");
                        CURRENT_PICKER = "COUNTRY";
                    }
                }
            }
        });

        parentTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFragmentAdded()) {
                    if (selectable) {
                        showCountrySelector(getActivity(), countryList, "PARENT_GUARDIAN_TITLE");
                        CURRENT_PICKER = "PARENT_GUARDIAN_TITLE";
                    }
                }
            }
        });


        myProfileNotEditable();

        return view;
    }

    public void showCountrySelector(Activity act, ArrayList constParam, String data) {
        if (act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SelectionListFragment countryListDialogFragment = SelectionListFragment.newInstance(constParam, data);
                countryListDialogFragment.setTargetFragment(MyProfileFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void dataSetup() {

        presenter2 = presenter;
        profile_given_name = (EditText) view.findViewById(R.id.profile_given_name);
        profile_given_name.setTag(profile_given_name.getKeyListener());

        profile_family_name = (EditText) view.findViewById(R.id.profile_family_name);
        profile_family_name.setTag(profile_family_name.getKeyListener());

        profile_mobile = (EditText) view.findViewById(R.id.profile_mobile);
        profile_mobile.setTag(profile_mobile.getKeyListener());

        profile_passport = (EditText) view.findViewById(R.id.profile_passport);
        profile_passport.setTag(profile_passport.getKeyListener());

        profile_street1 = (EditText) view.findViewById(R.id.profile_street1);
        profile_street1.setTag(profile_street1.getKeyListener());

        profile_street2 = (EditText) view.findViewById(R.id.profile_street2);
        profile_street2.setTag(profile_street2.getKeyListener());

        profile_post_code = (EditText) view.findViewById(R.id.profile_post_code);
        profile_post_code.setTag(profile_post_code.getKeyListener());

        profile_city = (EditText) view.findViewById(R.id.profile_city);
        profile_city.setTag(profile_city.getKeyListener());

        txtParentFullName = (EditText) view.findViewById(R.id.txtParentFullName);
        txtParentFullName.setTag(txtParentFullName.getKeyListener());

        txtParentID = (EditText) view.findViewById(R.id.txtParentID);
        txtParentID.setTag(txtParentID.getKeyListener());

        txtParentEmail = (EditText) view.findViewById(R.id.txtParentEmail);
        txtParentEmail.setTag(txtParentEmail.getKeyListener());

        profile_salutation = (TextView) view.findViewById(R.id.profile_salutation);
        profile_nationality = (TextView) view.findViewById(R.id.profile_nationality);
        profile_state = (TextView) view.findViewById(R.id.profile_state);
        profile_dob = (TextView) view.findViewById(R.id.profile_dob);
        profile_country = (TextView) view.findViewById(R.id.profile_country);
        userParentInformationBlock = (LinearLayout) view.findViewById(R.id.userParentInformationBlock);
        parentTitle = (TextView) view.findViewById(R.id.parentTitle);

        titleList = getTitle(getActivity());
        countryList = getCountry(getActivity());
        stateList = getState(getActivity());
        //parentTitleList = getParentTitle(getActivity());
        drawableRight = ContextCompat.getDrawable(getActivity(), R.drawable.down_arrow_green);
        drawableRightDob = ContextCompat.getDrawable(getActivity(), R.drawable.down_arrow);

        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        username = obj.getUserName();
        if (obj.getProfile_URL() != null) {
            displayImage(getActivity(), myImgUserDP, obj.getProfile_URL());
        }
    }

    public static void myProfileEditable() {

        profile_given_name.setKeyListener((KeyListener) profile_given_name.getTag());
        profile_family_name.setKeyListener((KeyListener) profile_family_name.getTag());
        profile_mobile.setKeyListener((KeyListener) profile_mobile.getTag());
        profile_passport.setKeyListener((KeyListener) profile_passport.getTag());
        profile_street1.setKeyListener((KeyListener) profile_street1.getTag());
        profile_street2.setKeyListener((KeyListener) profile_street2.getTag());
        profile_post_code.setKeyListener((KeyListener) profile_post_code.getTag());
        profile_city.setKeyListener((KeyListener) profile_city.getTag());
        txtParentFullName.setKeyListener((KeyListener) txtParentFullName.getTag());
        txtParentID.setKeyListener((KeyListener) txtParentID.getTag());
        txtParentEmail.setKeyListener((KeyListener) txtParentEmail.getTag());


        parentTitle.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        profile_salutation.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        profile_nationality.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        profile_state.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        profile_country.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRight, null);
        profile_dob.setCompoundDrawablesWithIntrinsicBounds(null, null, drawableRightDob, null);


        //profile_salutation.setCompoundDrawables(null, null, drawableRight, null);
        selectable = true;

        dismissLoading();
    }

    public static void myProfileNotEditable() {

        profile_given_name.setKeyListener(null);
        profile_family_name.setKeyListener(null);
        profile_mobile.setKeyListener(null);
        profile_passport.setKeyListener(null);
        profile_street1.setKeyListener(null);
        profile_street2.setKeyListener(null);
        profile_post_code.setKeyListener(null);
        profile_city.setKeyListener(null);
        txtParentFullName.setKeyListener(null);
        txtParentID.setKeyListener(null);
        txtParentEmail.setKeyListener(null);

        parentTitle.setCompoundDrawables(null, null, null, null);
        profile_salutation.setCompoundDrawables(null, null, null, null);
        profile_nationality.setCompoundDrawables(null, null, null, null);
        profile_state.setCompoundDrawables(null, null, null, null);
        profile_country.setCompoundDrawables(null, null, null, null);
        profile_dob.setCompoundDrawables(null, null, null, null);

        selectable = false;
    }

    public static void editProfileRequest() {


        editRequest = new EditProfileRequest();
        editRequest.setToken(token);
        editRequest.setTitle(profile_salutation.getText().toString());
        editRequest.setFirstName(profile_given_name.getText().toString());
        editRequest.setLastName(profile_family_name.getText().toString());
        editRequest.setAddressLine1(profile_street1.getText().toString());
        editRequest.setAddressLine2(profile_street2.getText().toString());
        //editRequest.setAddressLine3("-");
        editRequest.setGender(gender);
        editRequest.setMobilePhone(profile_mobile.getText().toString());
        editRequest.setNationality(profile_nationality.getTag().toString());
        editRequest.setCountry(profile_country.getTag().toString());
        editRequest.setState(profile_state.getTag().toString());
        editRequest.setPostalCode(profile_post_code.getText().toString());
        editRequest.setCity(profile_city.getText().toString());
        editRequest.setDateOfBirth(profile_dob.getTag().toString());
        editRequest.setQuestionAns1("0");
        editRequest.setQuestionAns2("en-GB");
        editRequest.setPID(profile_passport.getText().toString());
        editRequest.setUserName(username);

        if (parentInfo) {
            editRequest.setParentGuardianFullName(txtParentFullName.getText().toString());
            editRequest.setParentGuardian(parentTitle.getText().toString());
            editRequest.setParentGuardianDocNumber(txtParentID.getText().toString());
            editRequest.setParentGuardianEmail(txtParentEmail.getText().toString());
        }

        editRequest.setCID(cid);
        //editRequest.setBusinessEmail("nurfatin93.jamaludin@gmail.com");
        /*
        editRequest.setEmergencyDialingCode("");
        editRequest.setEmergencyFamilyName("");
        editRequest.setEmergencyGivenName("");
        editRequest.setEmergencyPhoneNumber("");
        editRequest.setEmergencyRelationship("");*/


        presenter2.updateFunction(editRequest);
    }

    @Override
    public void onUpdateUserSuccess(EditProfileReceive obj) {
        //successUpdate
        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            setSuccessDialogNoClear(getActivity(), "Thank You! Update Successful", null, "Success!");

        }

    }

    public void datePickerSetting(int year, int month, int day) {
        //datePicker setting
        dob_picker = DatePickerDialog.newInstance(this, year, month, day);
        dob_picker.setYearRange(calendar.get(Calendar.YEAR) - 80, calendar.get(Calendar.YEAR));
        dob_picker.setAccentColor(ContextCompat.getColor(getActivity(), R.color.default_theme_colour));

        Calendar output = Calendar.getInstance();
        output.set(Calendar.YEAR, output.get(Calendar.YEAR));
        output.set(Calendar.DAY_OF_MONTH, output.get(Calendar.DAY_OF_MONTH));
        output.set(Calendar.MONTH, output.get(Calendar.MONTH));
        dob_picker.setMaxDate(output);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (CURRENT_PICKER.equals("NATIONALITY")) {
                DropDownItem selectedCountry = data.getParcelableExtra(CURRENT_PICKER);
                profile_nationality.setText(selectedCountry.getText());

                //split
                String splitCountryCode = splitCountryDialingCode("CountryCode", selectedCountry.getCode());
                profile_nationality.setTag(splitCountryCode);
                //retrieveState(splitCountryCode);

            } else if (CURRENT_PICKER.equals("STATE")) {
                DropDownItem selectedState = data.getParcelableExtra(CURRENT_PICKER);
                profile_state.setText(selectedState.getText());
                profile_state.setTag(selectedState.getCode());

            } else if (CURRENT_PICKER.equals("COUNTRY")) {
                DropDownItem selectedCountry = data.getParcelableExtra(CURRENT_PICKER);
                profile_country.setText(selectedCountry.getText());

                String splitCountryCode = splitCountryDialingCode("CountryCode", selectedCountry.getCode());
                profile_country.setTag(splitCountryCode);

                retrieveState(splitCountryCode);

            } else if (CURRENT_PICKER.equals("TITLE")) {
                DropDownItem selectedCountry = data.getParcelableExtra(CURRENT_PICKER);
                profile_salutation.setText(selectedCountry.getText());
                gender = selectedCountry.getCode();
                /*if(selectedCountry.getText().equals("MR")){
                    gender = "1";
                }else{
                    gender = "2";
                }*/
            } else if (CURRENT_PICKER.equals("PARENT_GUARDIAN_TITLE")) {
                DropDownItem selectedCountry = data.getParcelableExtra(CURRENT_PICKER);
                parentTitle.setText(selectedCountry.getText());

            }
        }
    }

    public void retrieveState(String countryCode) {

        profile_state.setText("");
        profile_state.setHint(getResources().getString(R.string.register_general_loading));

        HashMap<String, String> initAppData = pref.getLanguageCountry();
        String langCountryCode = initAppData.get(SharedPrefManager.LANGUAGE_COUNTRY);

        StateRequest stateRequest = new StateRequest();
        stateRequest.setLanguageCode(langCountryCode);
        stateRequest.setCountryCode(countryCode);

        presenter.onStateRequest(stateRequest);

    }

    public static String returnUserInfo() {
        return userInformation;
    }

    @Override
    public void onSuccessRequestState(StateReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Gson gson = new Gson();
            String stateList = gson.toJson(obj.getStateList());
            pref.setState(stateList);
            profile_state.setHint(getResources().getString(R.string.register_select_state));
            stateName = getStateName(getActivity(), stateCode);
            setState(obj);
        }
    }

    public void
    setState(StateReceive stateList2) {

        /*Each country click - reset state obj*/
        stateList = new ArrayList<DropDownItem>();

        for (int x = 0; x < stateList2.getStateList().size(); x++) {

            DropDownItem itemCountry = new DropDownItem();
            itemCountry.setText(stateList2.getStateList().get(x).getProvinceStateName());
            itemCountry.setCode(stateList2.getStateList().get(x).getProvinceStateCode());
            itemCountry.setTag("State");

            stateList.add(itemCountry);
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

        travellerAge = getAge(year, month + 1, day);
        if (travellerAge < 18) {
            userParentInformationBlock.setVisibility(View.VISIBLE);
            parentInfo = true;
        } else {
            userParentInformationBlock.setVisibility(View.GONE);
            parentInfo = false;
        }

        profile_dob.setText(day + " " + getMonthAlphabet(month) + " " + year);
        date = varDay + "" + day + "" + varMonth + "" + (month + 1) + "" + year;

    }

    public int getAge(int _year, int _month, int _day) {

        GregorianCalendar cal = new GregorianCalendar();
        int y, m, d, a;

        y = cal.get(Calendar.YEAR);
        m = cal.get(Calendar.MONTH);
        d = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(_year, _month, _day);
        a = y - cal.get(Calendar.YEAR);
        if ((m < cal.get(Calendar.MONTH))
                || ((m == cal.get(Calendar.MONTH)) && (d < cal
                .get(Calendar.DAY_OF_MONTH)))) {
            --a;
        }
        if (a < 0)
            throw new IllegalArgumentException("Age < 0");
        return a;
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
        cid = returnData.getCID();
        date = dob;

        String day = date.substring(0, 2);
        String month = date.substring(2, 4);
        String year = date.substring(4);

        travellerAge = getAge(Integer.parseInt(day), Integer.parseInt(month), Integer.parseInt(year));
        if (travellerAge < 18) {
            userParentInformationBlock.setVisibility(View.VISIBLE);
            parentInfo = true;

            parentTitle.setText(returnData.getParentGuardian());
            txtParentEmail.setText(returnData.getParentGuardianEmail());
            txtParentFullName.setText(returnData.getParentGuardianFullName());
            txtParentID.setText(returnData.getParentGuardianDocNumber());
        } else {
            userParentInformationBlock.setVisibility(View.GONE);
            parentInfo = false;
        }

        String newDob = day + "-" + month + "-" + year;

        datePickerSetting(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day));

        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate;
        try {
            startDate = df.parse(newDob);
            Date myDate = startDate;
            String reportDate = (new SimpleDateFormat("dd MMM yyyy").format(myDate));
            profile_dob.setText(reportDate);
            profile_dob.setTag(date);


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
        profile_nationality.setTag(nationalityCode);

        profile_mobile.setText(mobile);
        profile_passport.setText(passport);
        profile_street1.setText(street1);
        profile_street2.setText(street2);
        profile_city.setText(city);
        profile_post_code.setText(postcode);
        profile_country.setText(country);
        profile_country.setTag(countryCode);
        profile_state.setText(stateName);
        profile_state.setTag(stateCode);
        gender = returnData.getGender();

        txtUserName.setText(returnData.getFirstName() + " " + returnData.getLastName());
        txtUserBigID.setText("BIG ID : " + customerNumber);

    }
}



