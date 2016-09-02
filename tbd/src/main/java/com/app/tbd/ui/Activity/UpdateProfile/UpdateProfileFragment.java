package com.app.tbd.ui.Activity.UpdateProfile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.Picker.SelectFlightFragment;
import com.app.tbd.ui.Model.Receive.UpdateProfileReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Activity.Picker.DatePickerFragment;
import com.app.tbd.ui.Module.UpdateProfileModule;
import com.app.tbd.ui.Model.Request.DatePickerObj;
import com.app.tbd.ui.Model.Request.UpdateProfileRequest;
import com.app.tbd.ui.Presenter.UpdateProfilePresenter;
import com.app.tbd.utils.AESCBC;
import com.app.tbd.utils.App;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class UpdateProfileFragment extends BaseFragment implements
        DatePickerDialog.OnDateSetListener,UpdateProfilePresenter.UpdateProfileView,Validator.ValidationListener  {

    // Validator Attributes
    private Validator mValidator;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> state;
    private DatePickerObj date;
    private ArrayList<DropDownItem> titleList;
    private String selectedState;
    private String selectedCountryCode;
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private int fragmentContainerId;
    public static final String DATEPICKER_TAG = "datepicker";
    private String fullDate;
    private static final String SCREEN_LABEL = "Update Information";
    DropDownItem selectedCountry,selectTitle_code;
    private View view;
    private Boolean validateStatus;

    @Inject UpdateProfilePresenter presenter;

    @InjectView(R.id.txtUsername)
    TextView txtUsername;

    @Order(1)@Optional
    @InjectView(R.id.txtCurrentPassword)
    EditText txtCurrentPassword;

    @Order(2)@Optional
    @InjectView(R.id.txtNewPassword)
    EditText txtNewPassword;

    @Order(3)@Optional
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Order(4)@NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    @Order(5)@NotEmpty
    @InjectView(R.id.txtFirstName)
    EditText txtFirstName;

    @Order(6)
    @InjectView(R.id.txtLastName)
    EditText txtLastName;

    @Order(7)@NotEmpty
    @InjectView(R.id.txtAddressLine1)
    EditText txtAddressLine1;

    @Order(16)@Optional
    @InjectView(R.id.txtAddressLine2)
    EditText txtAddressLine2;

    @Order(17)@Optional
    @InjectView(R.id.txtAddressLine3)
    EditText txtAddressLine3;

    @Order(18) @NotEmpty
    @InjectView(R.id.txtCountry)
    TextView txtCountry;

    @Order(9)@NotEmpty
    @InjectView(R.id.txtCity)
    EditText txtCity;

    @Order(10) @NotEmpty
    @InjectView(R.id.txtState)
    TextView txtState;

    @Order(11)@NotEmpty
    @Length(sequence = 1, min = 5,max = 7, message = "Invalid postcode")
    @InjectView(R.id.txtPostCode)
    EditText txtPostCode;

    @Order(12)@NotEmpty
    @Length(sequence = 1, min = 7,max = 14, message = "Invalid phone number")
    @InjectView(R.id.txtMobilePhone)
    EditText txtMobilePhone;

    @InjectView(R.id.txtAltPhone)
    EditText txtAltPhone;

    @InjectView(R.id.txtFax)
    EditText txtFax;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtDOB)
    TextView txtDOB;

    @Order(16)
    @InjectView(R.id.checkBox2)
    CheckBox checkBox2;

    //@Order(17)
    //@Checked(message = "You must agree with term & condition")
    //@InjectView(R.id.chkTNC)
    //CheckBox chkTNC;

    @InjectView(R.id.txtBonusLink)
    EditText txtBonusLink;

    @InjectView(R.id.updateProfileBtn)
    Button updateProfileBtn;

    private  DatePickerDialog datePickerDialog;
    private String dobAlphabet;
    //datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

    public static UpdateProfileFragment newInstance() {

        UpdateProfileFragment fragment = new UpdateProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new UpdateProfileModule(this)).inject(this);
        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.update_profile, container, false);
        ButterKnife.inject(this, view);

        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        pref = new SharedPrefManager(getActivity());
        countrys = new ArrayList<DropDownItem>();
        state = new ArrayList<DropDownItem>();
        titleList = new ArrayList<DropDownItem>();

        ///password
        txtCurrentPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());

        titleList = getStaticTitle(getActivity());
        countrys = getStaticCountry(getActivity());

        HashMap<String, String> init2 = pref.getNewsletterStatus();
        String newsletter = init2.get(SharedPrefManager.ISNEWSLETTER);
        if (newsletter.equals("Y")) {
            checkBox2.setChecked(true);
        }else{
            checkBox2.setChecked(false);
        }

        /*Display Existing User Data*/
        JSONObject jsonUserInfo = getUserInfo(getActivity());
        String userInfo = getUserInfoCached(getActivity());
        //Gson gson = new Gson();



        /*Select dropdown list*/
        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(),countrys);
            }
        });

        txtState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCountrySelector(getActivity(), state);
            }
        });

        txtDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] splitReturn = txtDOB.getText().toString().split("/");

                datePickerDialog = DatePickerDialog.newInstance(UpdateProfileFragment.this,Integer.parseInt(splitReturn[2]),Integer.parseInt(splitReturn[1])-1,Integer.parseInt(splitReturn[0]));
                datePickerDialog.setYearRange(year-100,year);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Clicked", "Ok");
                popupSelection(titleList, getActivity(),txtTitle,true,view);
            }
        });

        updateProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AnalyticsApplication.sendEvent("Click", "btnUpdateProfile");
                Log.e("Clicked", "Ok");

                //Multiple Manual Validation - Library Problem (failed to validate optional field)
                resetManualValidationStatus();
                manualValidation(txtBonusLink, "bonuslink");
                manualValidation(txtAltPhone, "phoneNumber");
                manualValidation(txtFax,"phoneNumber");
                validateStatus = getManualValidationStatus();

                if(!txtNewPassword.getText().toString().equals("") && !txtConfirmPassword.getText().toString().equals(""))
                {
                    if(!txtNewPassword.getText().toString().equals(txtConfirmPassword.getText().toString())){
                        croutonAlert(getActivity(), "New password & Confirm password not matched");
                        validateStatus = false;
                    }
                }

                mValidator.validate();

            }
        });

        return view;
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(UpdateProfileFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void showCountrySelector(Activity act,ArrayList constParam)
    {
        if(act != null) {
            try {

                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SelectFlightFragment countryListDialogFragment = com.app.tbd.ui.Activity.Picker.SelectFlightFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(UpdateProfileFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (requestCode == 1) {
                selectedCountry = data.getParcelableExtra(SelectFlightFragment.DEPARTURE_FLIGHT);

                if (selectedCountry.getTag() == "Country") {
                    txtCountry.setText(selectedCountry.getText());

                    String toCountryCode =  selectedCountry.getCode();
                    String[] splitCountryCode = toCountryCode.split("/");
                    selectedCountryCode = splitCountryCode[0];
                    setState(selectedCountryCode);

                } else {
                    txtState.setText(selectedCountry.getText());
                    selectedState = selectedCountry.getCode();

                }
            }
        }
    }


    public void setState(String selectedCode){

        /*Each country click - reset state obj*/
        state = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
        JSONArray jsonState = getState(getActivity());
        for(int x = 0 ; x < jsonState.length() ; x++) {

            JSONObject row = (JSONObject) jsonState.opt(x);
            if(selectedCode.equals(row.optString("country_code"))) {
                DropDownItem itemCountry = new DropDownItem();
                itemCountry.setText(row.optString("state_name"));
                itemCountry.setCode(row.optString("state_code"));
                itemCountry.setTag("State");
                state.add(itemCountry);
            }
        }
    }



    public void requestUpdateProfile() {


        initiateLoading(getActivity());

        HashMap<String, String> init = pref.getSignatureFromLocalStorage();
        String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);
        String newsletter;

        JSONObject jsonUserInfo = getUserInfo(getActivity());
        UpdateProfileRequest data = new UpdateProfileRequest();

        String currentPassword= AESCBC.encrypt(App.KEY, App.IV, txtCurrentPassword.getText().toString());
        String newPassword = AESCBC.encrypt(App.KEY, App.IV, txtNewPassword.getText().toString());
        data.setUsername(txtUsername.getText().toString());
        data.setFirst_name(txtFirstName.getText().toString());
        data.setLast_name(txtLastName.getText().toString());
        data.setAddress_1(txtAddressLine1.getText().toString());
        data.setAddress_2(txtAddressLine2.getText().toString());
        data.setAddress_3(txtAddressLine3.getText().toString());
        data.setMobile_phone(txtMobilePhone.getText().toString());
        data.setAlternate_phone(txtAltPhone.getText().toString());
        data.setCity(txtCity.getText().toString());
        data.setPostcode(txtPostCode.getText().toString());
        data.setFax(txtFax.getText().toString());
        data.setSignature(signatureFromLocal);
        data.setBonuslink(txtBonusLink.getText().toString());

        //Post newsletter
        if (checkBox2.isChecked()) {
            pref.setNewsletterStatus("Y");
            newsletter = "Y";
        }else{
            pref.setNewsletterStatus("N");
            newsletter = "N";
        }
        data.setNewsletter(newsletter);

        //currentpassword
        if (txtCurrentPassword.getText().toString().equals("")) {
            data.setPassword("");
        }else{
            data.setPassword(currentPassword);
        }

        //Post new password
        if (txtNewPassword.getText().toString().equals("")) {
            data.setNew_password("");
        }else{
            data.setNew_password(newPassword);
        }


        //Post title
            if (txtTitle.getTag() == null ) {
                data.setTitle(jsonUserInfo.optString("title"));
            }else{
              data.setTitle(txtTitle.getTag().toString());
        }

        //Post country
        if (selectedCountryCode == null) {
            data.setCountry(jsonUserInfo.optString("contact_country"));
        }else {
            data.setCountry(selectedCountryCode);
        }

        //Post state
        if (selectedState ==null) {
            data.setState(jsonUserInfo.optString("contact_state"));
        }else {
            data.setState(selectedState);
        }

        data.setDob(fullDate);

        presenter.onUpdateProfile(data);


    }

    @Override
    public void onSuccessUpdate(UpdateProfileReceive obj) {

       dismissLoading();
       Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
       if (status){

         Gson gsonUserInfo = new Gson();
         String userInfo = gsonUserInfo.toJson(obj.getUserInfo());
         pref.setUserInfo(userInfo);
         pref.setUsername(obj.getUserInfo().getFirst_name());

         setSuccessDialog(getActivity(), "Information Successfully Updated", HomeActivity.class,"Update Information");

       }
    }

    @Override
    public void onValidationSucceeded() {
        if(!txtNewPassword.getText().toString().equals("") && !txtConfirmPassword.getText().toString().equals("")) {
            if(!isValidatePassword(txtNewPassword.getText().toString())){
                txtNewPassword.setError("Password must contain uppercase char,number,symbols and minimum 8 characters.");
            }else{
                if(validateStatus){
                    requestUpdateProfile();
                }
            }
         }else {
            if (validateStatus) {
                requestUpdateProfile();
            }
        }
    }

    private boolean isValidatePassword(String password) {

        String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=-])(?=\\S+$).{8,}$";

        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
        //Agent-17c
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

       for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);

            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

           if (view instanceof EditText) {
               ((EditText) view).setError(splitErrorMsg[0]);
           }
           else if (view instanceof CheckBox){
               ((CheckBox) view).setError(splitErrorMsg[0]);
               croutonAlert(getActivity(), splitErrorMsg[0]);
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
        presenter.onResume();

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthInAlphabet = getMonthAlphabet(month);

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 9){
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }
        //fullDate = varDay+""+day+ "-" + varMonth+""+month + "-" + year;
        //fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;


        //fullDate = day + "/" + varMonth+""+(month+1)+"/"+year;
        fullDate = year +"-"+varMonth+""+(month+1)+"-"+day;
        txtDOB.setText(day + "/" + varMonth+""+(month+1)+ "/" + year);

        //fullDate.setText(day + "/"+varMonth + "" + (month+1)+ "/" + year);

        Log.e("fullDate", fullDate);
    }
}
