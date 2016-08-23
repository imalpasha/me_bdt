package com.metech.tbd.ui.Activity.BookingFlight.ManageFamilyAndFriend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.google.gson.Gson;
import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.Picker.SelectFlightFragment;
import com.metech.tbd.ui.Model.Receive.SelectFlightReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Module.EditFamilyFriendModule;
import com.metech.tbd.ui.Model.Request.DefaultPassengerObj;
import com.metech.tbd.ui.Model.Request.FamilyFriendObj;
import com.metech.tbd.ui.Model.Request.PassengerInfo;
import com.metech.tbd.ui.Presenter.BookingPresenter;
import com.metech.tbd.utils.DropDownItem;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import javax.inject.Inject;
import butterknife.InjectView;

public class EditFamilyFriendsFragment extends BaseFragment implements Validator.ValidationListener,BookingPresenter.EditFamilyFriendView,DatePickerDialog.OnDateSetListener {

    @Inject
    BookingPresenter presenter;

    @Order(1) @NotEmpty
    @InjectView(R.id.txtFFtitle)
    TextView txtFFtitle;

    @Order(2) @NotEmpty
    @InjectView(R.id.txtFFfirstName)
    EditText txtFFfirstName;

    @Order(3) @NotEmpty
    @InjectView(R.id.txtFFLastName)
    EditText txtFFLastName;

    @Order(4) @NotEmpty
    @InjectView(R.id.txtFFDob)
    TextView txtFFDob;

    @Order(5) @NotEmpty
    @InjectView(R.id.txtFFNationality)
    TextView txtFFNationality;

    @InjectView(R.id.txtFFBonuslink)
    EditText txtFFBonuslink;

    @Order(6) @NotEmpty
    @InjectView(R.id.txtFFGender)
    TextView txtFFGender;

    @InjectView(R.id.ffGenderBlock)
    LinearLayout ffGenderBlock;

    @InjectView(R.id.ffTitleBlock)
    LinearLayout ffTitleBlock;

    @InjectView(R.id.ffBonuslinkBlock)
    LinearLayout ffBonuslinkBlock;

    @InjectView(R.id.btnUpdateFF)
    Button btnUpdateFF;

    @InjectView(R.id.personalDetailScrollView)
    ScrollView personalDetailScrollView;

    private Calendar calendar = Calendar.getInstance();
    private int year = calendar.get(Calendar.YEAR);

    private DatePickerDialog datePickerYear1 = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    private ArrayList<DefaultPassengerObj> friendAndFamilyObj = new ArrayList<DefaultPassengerObj>();
    private ArrayList<DropDownItem> titleList;
    private ArrayList<DropDownItem> countrys;
    private ArrayList<DropDownItem> genderList;
    private Validator mValidator;

    private String DATEPICKER_TAG = "DATEPICKER_TAG";
    private int fragmentContainerId;
    private SharedPrefManager pref;
    boolean adult = true;
    private boolean formContinue = true;
    private String add_ff;
    private DefaultPassengerObj obj;
    View view;

    public static EditFamilyFriendsFragment newInstance(Bundle bundle) {

        EditFamilyFriendsFragment fragment = new EditFamilyFriendsFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new EditFamilyFriendModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.edit_family_friend, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        Bundle bundle = getArguments();

        add_ff = bundle.getString("ADD_FF");
        if(add_ff != null){
                //adda
            if(add_ff.equals("Adult")){
                ffGenderBlock.setVisibility(View.GONE);
                adult = true;

            }else{
                ffTitleBlock.setVisibility(View.GONE);
                ffBonuslinkBlock.setVisibility(View.GONE);
                adult = false;
            }

            btnUpdateFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    formContinue = true;
                    mValidator.validate();
                    if(txtFFBonuslink.getText().toString() != null){
                        checkBonuslink(txtFFBonuslink);
                    }
                }
            });

        }else{
            //set value ff

            String familyFriend = bundle.getString("FRIEND_AND_FAMILY_");

            final Gson gson = new Gson();
            obj = gson.fromJson(familyFriend, DefaultPassengerObj.class);

            txtFFfirstName.setText(obj.getFirst_name());
            txtFFLastName.setText(obj.getLast_name());
            txtFFDob.setText(reformatDOB3(obj.getDob()));
            txtFFNationality.setText(getCountryName(getActivity(),obj.getNationality()));
            txtFFNationality.setTag(obj.getNationality());

            if(obj.getPassenger_type().equals("Adult")){
                ffGenderBlock.setVisibility(View.GONE);
                txtFFtitle.setText(getTitleCode(getActivity(),obj.getTitle(),"name"));
                txtFFtitle.setTag(obj.getTitle());
                txtFFBonuslink.setText(obj.getBonuslink_card());
                adult = true;
            }else{
                ffTitleBlock.setVisibility(View.GONE);
                ffBonuslinkBlock.setVisibility(View.GONE);
                txtFFGender.setText(obj.getGender());
                adult = false;
            }

            btnUpdateFF.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    formContinue = true;
                    mValidator.validate();
                    if(txtFFBonuslink.getText().toString() != null){
                        checkBonuslink(txtFFBonuslink);
                    }
                }
            });
        }


        titleList = new ArrayList<DropDownItem>();
        countrys = new ArrayList<DropDownItem>();
        genderList = new ArrayList<DropDownItem>();

        /*Display Title Data*/
        titleList = getStaticTitle(getActivity());
        countrys = getStaticCountry(getActivity());
        genderList = getGender(getActivity());




        txtFFtitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(titleList, getActivity(), txtFFtitle, true ,view);
            }
        });


        txtFFGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSelection(genderList, getActivity(), txtFFGender, true ,view);
            }
        });


         /*Switch register info block*/
        txtFFNationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Edit", "Country");
                showCountrySelector(getActivity(), countrys,"country");
            }
        });

        txtFFDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String currentDOB = txtFFDob.getText().toString();
                if(!currentDOB.equals("")){
                    String[] splitReturn = currentDOB.split("/");
                    createDateObj(Integer.parseInt(splitReturn[2]), Integer.parseInt(splitReturn[1])-1, Integer.parseInt(splitReturn[0]));
                }else{
                    createDateObj(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                }
            }
        });

        personalDetailScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                view.requestFocus();
            }
        });

        return view;
    }

    public void sendFF(){


        HashMap<String, String> initEmail = pref.getUserEmail();
        String email = initEmail.get(SharedPrefManager.USER_EMAIL);

        //do some validation here.
        PassengerInfo passengerInfo = new PassengerInfo();
        passengerInfo.setFirst_name(txtFFfirstName.getText().toString());
        passengerInfo.setLast_name(txtFFLastName.getText().toString());
        passengerInfo.setDob(reformatDOB4(txtFFDob.getText().toString()));
        passengerInfo.setIssuing_country(txtFFNationality.getTag().toString());
        passengerInfo.setUser_email(email);

        if(add_ff != null){
            passengerInfo.setPassenger_type(add_ff);
        }else{
            passengerInfo.setPassenger_type(obj.getPassenger_type());
            passengerInfo.setFriend_and_family_id(obj.getId());
        }

        if(adult){
            passengerInfo.setTitle(txtFFtitle.getTag().toString());
            passengerInfo.setBonusLink(txtFFBonuslink.getText().toString());

        }else{
            passengerInfo.setGender(txtFFGender.getText().toString());
        }

        if(formContinue){
            initiateLoading(getActivity());
            presenter.onRequestEditFF(passengerInfo);
        }


    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act,ArrayList constParam,String data)
    {
        if(act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                SelectFlightFragment countryListDialogFragment = SelectFlightFragment.newInstance(constParam);
                countryListDialogFragment.setTargetFragment(EditFamilyFriendsFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");

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
                    txtFFNationality.setText(selectedCountry.getText());

                    String toCountryCode =  selectedCountry.getCode();
                    String[] splitCountryCode = toCountryCode.split("/");
                    txtFFNationality.setTag(splitCountryCode[0]);

                }

            }
        }
    }

    @Override
    public void onEditFF(SelectFlightReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //save into realm object
            FamilyFriendObj thisObj = new FamilyFriendObj();
            thisObj.setFamily_and_friend(obj.getFamily_and_friend());

            RealmObjectController.saveFamilyFriends(getActivity(),thisObj);
            setSuccessDialogNoClear(getActivity(), obj.getMessage(),null,"Updated!!");

        }

    }

    public void createDateObj(Integer year , Integer month , Integer day){

        datePickerYear1 = DatePickerDialog.newInstance(this,year,month,day);
        datePickerYear1.setYearRange(calendar.get(Calendar.YEAR) - 80, calendar.get(Calendar.YEAR));

        if(checkFragmentAdded()){
            datePickerYear1.show(getActivity().getSupportFragmentManager(), DATEPICKER_TAG);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {

        //Reconstruct DOB
        String varMonth = "";
        String varDay = "";

        if(month < 9) {
            varMonth = "0";

        }
        if(day < 10){
            varDay = "0";
        }

        txtFFDob.setText(varDay+""+day + "/"+varMonth + "" + (month+1)+ "/" + year);

    }

    public void checkTextViewNull(TextView txtView){
        if(txtView.getText().toString() == "") {
            txtView.setError("Field Required");
            setShake(txtView);
            txtView.requestFocus();
            formContinue = false;
        }
    }

    public void checkBonuslink(EditText bonuslink){

        if (!bonuslink.getText().toString().matches("")) {
            if(bonuslink.length() < 16 || bonuslink.length() > 16){
                bonuslink.setError("Invalid bonuslink card number");
                setShake(bonuslink);
                bonuslink.requestFocus();
            }
            formContinue = false;
        }

    }

    public void checkEditTextNull(EditText editText){

        if (editText.getText().toString().matches("")) {
            editText.setError("Field Required");
            setShake(editText);
            editText.requestFocus();
            formContinue = false;
        }

    }

    @Override
    public void onValidationSucceeded() {

        sendFF();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

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

            }else if(view instanceof TextView){
                ((TextView) view).setError(splitErrorMsg[0]);
            }
            else if (view instanceof CheckBox){
                ((CheckBox) view).setError(splitErrorMsg[0]);
                // croutonAlert(getActivity(), splitErrorMsg[0]);
            }

            if(firstView){
                view.requestFocus();
            }
            firstView = false;
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
}
