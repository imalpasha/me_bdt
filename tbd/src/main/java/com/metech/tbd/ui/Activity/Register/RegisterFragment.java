package com.metech.tbd.ui.Activity.Register;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.Picker.SelectFlightFragment;
import com.metech.tbd.ui.Model.Receive.RegisterReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Activity.Login.LoginActivity;
import com.metech.tbd.ui.Activity.Picker.DatePickerFragment;
import com.metech.tbd.ui.Activity.Picker.StateListDialogFragment;
import com.metech.tbd.ui.Module.RegisterModule;
import com.metech.tbd.ui.Model.Request.CachedResult;
import com.metech.tbd.ui.Model.Request.DatePickerObj;
import com.metech.tbd.ui.Model.Request.RegisterObj;
import com.metech.tbd.ui.Presenter.RegisterPresenter;
import com.metech.tbd.utils.AESCBC;
import com.metech.tbd.utils.App;
import com.metech.tbd.utils.DropDownItem;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;
import com.metech.tbd.utils.Utils;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Optional;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class RegisterFragment extends BaseFragment implements DatePickerDialog.OnDateSetListener,RegisterPresenter.RegisterView,Validator.ValidationListener {


    // Validator Attributes
    @Inject
    RegisterPresenter presenter;

    @Order(14) @NotEmpty
    @InjectView(R.id.editTextCountry)
    TextView editTextCountry;

    @Order(16) @NotEmpty
    @InjectView(R.id.editTextState)
    TextView editTextState;

    @Order(1)
    @NotEmpty(sequence = 1)
    @Email(sequence = 2 ,message = "Invalid Email")
    @InjectView(R.id.txtUsername)
    EditText txtUsername;

    @Order(2)
    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6, max = 16 , message = "Must be at least 8 and maximum 16 characters")
    @Password(sequence = 3,scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "Password invalid , please refer to the password hint") // Password validator
    @InjectView(R.id.txtPassword)
    EditText txtPassword;

    @Order(3)
    @NotEmpty(sequence = 1)
    @ConfirmPassword(sequence = 2)
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @Order(4)
    @NotEmpty
    @InjectView(R.id.txtFirstName)
    EditText txtFirstName;

    @Order(5)
    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtRegisterDatePicker)
    TextView txtRegisterDatePicker;

    @Order(6) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtLastName)
    EditText txtLastName;

    @Order(7) @NotEmpty(sequence = 1)
    @InjectView(R.id.txtAddressLine1)
    EditText txtAddressLine1;

    @Order(8) @Optional
    @InjectView(R.id.txtAddressLine2)
    EditText txtAddressLine2;

    @Order(9) @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 5,max = 7, message = "Invalid postcode")
    @InjectView(R.id.editTextPostcode)
    EditText editTextPostcode;

    @InjectView(R.id.editTextMobilePhone)
    EditText editTextMobilePhone;

    @InjectView(R.id.editTextAlternatePhone)
    EditText txtAlternatePhoneNumber;

    @InjectView(R.id.editTextFax)
    EditText txtFaqNumber;

    @Order(13)@NotEmpty
    @InjectView(R.id.txtCity)
    EditText txtCity;

    @Order(15)@NotEmpty
    @InjectView(R.id.txtTitle)
    TextView txtTitle;

    //@Order(16)
    //@Checked(message = "You must agree with terms & conditions")
    @InjectView(R.id.chkTNC)
    CheckBox chkTNC;

    @Order(17)
    @InjectView(R.id.checkBox2)
    CheckBox checkBox2;

    @InjectView(R.id.registerContinueButton)
    Button registerContinueButton;

    @InjectView(R.id.txtBonusLink)
    EditText txtBonusLink;

    @InjectView(R.id.txtPasswordHint)
    LinearLayout txtPasswordHint;

    private Validator mValidator;
    private int currentPage;
    private int day;
    private int month;
    private int year;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Register";
    private SharedPrefManager pref;
    private int month_number;
    private DatePickerObj date;
    private String selectedTitle;
    private String[] state_val;
    private String selectedState;
    private String selectedCountryCode;
    private String dialingCode;
    public static final String DATEPICKER_TAG = "datepicker";
    private String fullDate;
    private Boolean validateStatus = true;
    private Boolean limitAge;
    /*DropDown Variable*/
    private ArrayList<DropDownItem> titleList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> countrys  = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> state = new ArrayList<DropDownItem>();
    private Calendar calendar;
    private int age;

    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

        // new SearchFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new RegisterModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register, container, false);
        ButterKnife.inject(this, view);

        //set textview focusable for error
        //txtTitle.setFocusable(true);
        //txtTitle.setFocusableInTouchMode(true);
       // txtRegisterDatePicker
       //         editTextCountry
       //         editTextState

        //set only capital
        txtFirstName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtLastName.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        pref = new SharedPrefManager(getActivity());

        txtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
        txtPassword.setTransformationMethod(new PasswordTransformationMethod());

        /*Get Data From BaseFragment*/
        countrys = getStaticCountry(getActivity());
        titleList = getStaticTitle(getActivity());

          /*Display Password Hint*/
        txtPasswordHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNormalDialog(getActivity(), getActivity().getResources().getString(R.string.register_password_hint), getActivity().getResources().getString(R.string.register_password_policy));
            }
        });

         /*Switch register info block*/
        editTextCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Country");
                showCountrySelector(getActivity(), countrys,"country");
            }
        });

        editTextState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "State");
                showCountrySelector(getActivity(), state,"state");
            }
        });

        txtRegisterDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Date");
                datePickerDialog.setYearRange(year - 80, year);
                datePickerDialog.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        txtTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Title");
                popupSelection(titleList, getActivity(), txtTitle, true, view);
            }
        });

        registerContinueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "ContinueButton");
                Utils.hideKeyboard(getActivity(), v);

                //Multiple Manual Validation - Library Problem (failed to validate optional field)
                resetManualValidationStatus();
                manualValidation(txtBonusLink, "bonuslink");
                if(editTextMobilePhone.getText().toString().length() > 2){
                    manualValidation(editTextMobilePhone, "phoneNumber");
                }else{
                    editTextMobilePhone.setText("");
                }
                if(txtAlternatePhoneNumber.getText().toString().length() > 2){
                    manualValidation(txtAlternatePhoneNumber, "phoneNumber");
                }else{
                    txtAlternatePhoneNumber.setText("");
                }
                if(txtFaqNumber.getText().toString().length() > 2){
                    manualValidation(txtFaqNumber,"faxNumber");
                }else{
                    txtFaqNumber.setText("");
                }

                manualValidation(editTextMobilePhone, "phoneNumber");
                validateStatus = getManualValidationStatus();

                //get mobile phone dialing code
                //if(dialingCode != null || dialingCode != ""){
                    if(!editTextMobilePhone.getText().toString().equals("")){
                        if(validateDialingCode(dialingCode,editTextMobilePhone.getText().toString())){
                            editTextMobilePhone.setError("Mobile phone must start with country code.");
                            validateStatus = false;
                            setShake(editTextMobilePhone);

                        }
                    }

                    if(!txtAlternatePhoneNumber.getText().toString().equals("")){
                        if(validateDialingCode(dialingCode,txtAlternatePhoneNumber.getText().toString())){
                            txtAlternatePhoneNumber.setError("Mobile phone must start with country code.");
                            validateStatus = false;
                            setShake(txtAlternatePhoneNumber);
                        }
                    }

                    if(!txtFaqNumber.getText().toString().equals("")){
                        if(validateDialingCode(dialingCode,txtFaqNumber.getText().toString())){
                            txtFaqNumber.setError("Mobile phone must start with country code.");
                            validateStatus = false;
                            setShake(txtFaqNumber);

                        }
                    }

                //}
                mValidator.validate();
            }
        });

        return view;
    }



    /*Date Picker -> need to move to main activity*/
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.setTargetFragment(RegisterFragment.this, 0);
        newFragment.show(getFragmentManager(), "datePicker");
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam,String data)
    {
        if(act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                if(data.equals("state")){
                    StateListDialogFragment countryListDialogFragment = StateListDialogFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(RegisterFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }else{
                    SelectFlightFragment countryListDialogFragment = SelectFlightFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(RegisterFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

   @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (resultCode != Activity.RESULT_OK) {
           return;
        } else {
           if (requestCode == 1) {
               DropDownItem selectedCountry = data.getParcelableExtra(com.metech.tbd.ui.Activity.Picker.SelectFlightFragment.DEPARTURE_FLIGHT);

               if (selectedCountry.getTag() == "Country") {
                   editTextCountry.setText(selectedCountry.getText());

                   //split country code with dialing code
                   String toCountryCode =  selectedCountry.getCode();
                   String[] splitCountryCode = toCountryCode.split("/");
                   selectedCountryCode = splitCountryCode[0];
                   dialingCode = splitCountryCode[1];
                   //editTextMobilePhone.setText(dialingCode);
                   //txtAlternatePhoneNumber.setText(dialingCode);
                   //txtFaqNumber.setText(dialingCode);

                   /*Each country click - reset state obj*/
                   state = new ArrayList<DropDownItem>();

                    /* Set state from selected Country Code*/
                   JSONArray jsonState = getState(getActivity());
                   for(int x = 0 ; x < jsonState.length() ; x++) {

                       JSONObject row = (JSONObject) jsonState.opt(x);
                       if (selectedCountryCode.equals(row.optString("country_code"))) {
                           DropDownItem itemCountry = new DropDownItem();
                           itemCountry.setText(row.optString("state_name"));
                           itemCountry.setCode(row.optString("state_code"));
                           itemCountry.setTag("State");
                           state.add(itemCountry);
                       }
                   }

               } else {
                   editTextState.setText(selectedCountry.getText());
                   selectedState = selectedCountry.getCode();
               }

           }
       }
    }

    public void requestRegister(){

            initiateLoading(getActivity());

            HashMap<String, String> init = pref.getSignatureFromLocalStorage();
            String signatureFromLocal = init.get(SharedPrefManager.SIGNATURE);

            HashMap<String, String> initi = pref.getNewsletterStatus();
            String newsletter = initi.get(SharedPrefManager.ISNEWSLETTER);

            RegisterObj regObj = new RegisterObj();

            regObj.setUsername(txtUsername.getText().toString());
            regObj.setFirst_name(txtFirstName.getText().toString());
            regObj.setLast_name(txtLastName.getText().toString());
            regObj.setPassword(AESCBC.encrypt(App.KEY, App.IV, txtPassword.getText().toString()));
            regObj.setTitle(txtTitle.getTag().toString());
            regObj.setDob(fullDate);
            regObj.setAddress_1(txtAddressLine1.getText().toString());
            regObj.setAddress_2(txtAddressLine2.getText().toString());
            regObj.setAddress_3(txtAddressLine2.getText().toString());
            regObj.setAlternate_phone(txtAlternatePhoneNumber.getText().toString());
            regObj.setMobile_phone(editTextMobilePhone.getText().toString());
            regObj.setCountry(selectedCountryCode);
            regObj.setState(selectedState);
            regObj.setCity(txtCity.getText().toString());
            regObj.setPostcode(editTextPostcode.getText().toString());
            regObj.setFax(txtFaqNumber.getText().toString());
            regObj.setBonuslink(txtBonusLink.getText().toString());
            regObj.setSignature("");

            //regObj.setNewsletter(newsletter);

            if (checkBox2.isChecked()) {
                pref.setNewsletterStatus("Y");
                regObj.setNewsletter("Y");
            }else{
                pref.setNewsletterStatus("N");
                regObj.setNewsletter("N");
            }

            presenter.onRequestRegister(regObj);
    }

    @Override
    public void onSuccessRegister(RegisterReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            Intent loginPage = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(loginPage);
            getActivity().finish();
        }

    }

    @Override
    public void onValidationSucceeded() {

        //need to do optional validation
        if(limitAge){
            if(chkTNC.isChecked()){
                if(validateStatus){
                    requestRegister();
                }
            }else{
                croutonAlert(getActivity(), "You must agree with terms & conditions");
            }
        }else{
            croutonAlert(getActivity(), "You must be at least 18 years old to register.");
        }

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        boolean fieldError = false;
        String errorMessage = null;
        boolean firstView = true;

        for (ValidationError error : errors) {


            View view = error.getView();
            view.setFocusable(true);

            setShake(view);
             /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }else if(view instanceof TextView)
            {
                ((TextView) view).setError(splitErrorMsg[0]);
            }

            if(firstView){

                view.requestFocus();
            }
            firstView = false;
        }



        //if(fieldError){
        //    croutonAlert(getActivity(), errorMessage);
        //}


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
        if(result.size() > 0){
            Gson gson = new Gson();
            RegisterReceive obj = gson.fromJson(result.get(0).getCachedResult(), RegisterReceive.class);
            onSuccessRegister(obj);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        String monthInAlphabet = getMonthAlphabet(month);
        txtRegisterDatePicker.setText(day + " " + monthInAlphabet + " " + year);

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 10){
            varMonth = "0";
        }
        if(day < 10){
            varDay = "0";
        }

        fullDate = year + "-" + varMonth+""+(month+1)+"-"+varDay+""+day;
        int currentYear = calendar.get(Calendar.YEAR);
        age = currentYear - year;
        limitAge = age >= 18;

        /*fullDate = varDay+""+day+ "-" + varMonth+""+month + "-" + year;
        Log.e("fullDate", fullDate);*/
    }

}
