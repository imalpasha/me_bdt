package com.app.tbd.ui.Activity.SplashScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Activity.Picker.SelectLanguageCountryFragment;
import com.app.tbd.ui.Activity.Picker.SelectLanguageFragment;
import com.app.tbd.ui.Activity.Picker.SelectionListFragment;
import com.app.tbd.ui.Activity.SplashScreen.OnBoarding.OnBoardingActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.LanguageCountryReceive;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.LanguageCountryRequest;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Module.LanguageModule;
import com.app.tbd.ui.Presenter.LanguagePresenter;
import com.app.tbd.ui.Realm.Cached.CachedLanguageCountry;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class LanguageFragment extends BaseFragment implements LanguagePresenter.LanguageView, Validator.ValidationListener {

    @Inject
    LanguagePresenter presenter;

    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtLangCountry)
    TextView txtLangCountry;

    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtLangLanguage)
    TextView txtLangLanguage;

    @InjectView(R.id.selectionLayout)
    RelativeLayout selectionLayout;

    @InjectView(R.id.btn_nxt)
    Button btn_nxt;

    @InjectView(R.id.chooseLayout)
    LinearLayout chooseLayout;

    @InjectView(R.id.imageLayout)
    LinearLayout imageLayout;

    ProgressDialog progress;
    private Validator mValidator;
    private int fragmentContainerId;
    private Locale myLocale;
    private ArrayList<DropDownItem> languageList = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> country = new ArrayList<DropDownItem>();
    private String CURRENT_PICKER;
    private SharedPrefManager pref;
    private String languageCode, countryCode;
    private Boolean languageClickable = false;
    private String cn;

    public static LanguageFragment newInstance() {

        LanguageFragment fragment = new LanguageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new LanguageModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.language, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        //set animation to choose country & language layout
        //Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.abc_popup_enter);
        //selectionLayout.setAnimation(animation);

        //load country

        HashMap<String, String> initAppData = pref.getFirstTimeUser();
        String firstTime = initAppData.get(SharedPrefManager.FIRST_TIME_USER);

        if (firstTime != null && firstTime.equals("N")) {
            Intent language = new Intent(getActivity(), HomeActivity.class);
            startActivity(language);
            getActivity().finish();
        } else {
            LanguageCountryRequest languageCountryRequest = new LanguageCountryRequest();
            presenter.onCountryRequest(languageCountryRequest);
            txtLangCountry.setClickable(false);

            loadLocale();
        }

        btn_nxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();
            }
        });

        //country selection
        txtLangCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "Select country");
                if (checkFragmentAdded()) {
                    showCountrySelector(getActivity(), country, "LANGUAGE_COUNTRY");
                    CURRENT_PICKER = "LANGUAGE_COUNTRY";
                }
            }
        });

        //language selection
        txtLangLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtLangCountry.getText().toString() != "") {
                    if (languageClickable) {
                        AnalyticsApplication.sendEvent("Click", "Select language");
                        if (checkFragmentAdded()) {
                            showCountrySelector(getActivity(), languageList, "LANGUAGE");
                            CURRENT_PICKER = "LANGUAGE";
                        }
                    } else {
                        Utils.toastNotification(getActivity(), "Loading language...");
                    }
                } else {
                    Utils.toastNotification(getActivity(), "Please select country");
                }
            }
        });

        return view;
    }

    @Override
    public void loadingSuccess(InitialLoadReceive obj) {


        Boolean status = MainController.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {


            Gson gson = new Gson();

            String title = gson.toJson(obj.getObj().getData_title());
            pref.setUserTitle(title);

            String country = gson.toJson(obj.getObj().getData_country());
            pref.setCountry(country);

            String route = gson.toJson(obj.getObj().getRouteList());
            pref.setRoute(route);

            //load state - need to move later on
            StateRequest stateRequest = new StateRequest();
            stateRequest.setLanguageCode(languageCode + "-" + cn);
            stateRequest.setCountryCode(countryCode);
            stateRequest.setPresenterName("LanguagePresenter");
            presenter.onStateRequest(stateRequest);

        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            DropDownItem selectedLanguage = data.getParcelableExtra(CURRENT_PICKER);
            if (CURRENT_PICKER.equals("LANGUAGE")) {
                txtLangLanguage.setText(selectedLanguage.getText());
                txtLangLanguage.setTag(selectedLanguage.getCode());
                changeLanguage(selectedLanguage.getCode());
                languageCode = selectedLanguage.getCode();
            } else if (CURRENT_PICKER.equals("LANGUAGE_COUNTRY")) {
                DropDownItem selectedCountry = data.getParcelableExtra(CURRENT_PICKER);
                txtLangCountry.setText(selectedCountry.getText());
                txtLangCountry.setTag(selectedCountry.getCode());
                countryCode = selectedCountry.getCode();
                retrieveLanguage(selectedCountry.getCode());
            }
        }
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act, ArrayList constParam, String data) {
        if (act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                /*if (data.equals("COUNTRY")) {
                    SelectionListFragment countryListDialogFragment = SelectionListFragment.newInstance(constParam,data);
                    countryListDialogFragment.setTargetFragment(LanguageFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");

                } else if (data.equals("LANGUAGE")) {*/
                SelectionListFragment countryListDialogFragment = SelectionListFragment.newInstance(constParam, data);
                countryListDialogFragment.setTargetFragment(LanguageFragment.this, 0);
                countryListDialogFragment.show(fm, "countryListDialogFragment");
                //}
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void loadLocale() {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        updateTexts();
    }

    private void updateTexts() {
        btn_nxt.setText(R.string.btn_nxt);
    }

    public void changeLanguage(String selectedLanguage) {
        String lang = "en";

        if (selectedLanguage.equals("en")) {
            cn = "GB";
            lang = "en";
        } else if (selectedLanguage.equals("ms")) {
            lang = "ms";
            cn = "MY";

        } else if (selectedLanguage.equals("zh")) {
            lang = "zh";
            cn = "CN";
        } else {
            lang = "en";
            cn = "GB";

        } //else if (selectedLanguage.equals("th")) {
        //lang = "th";
        //}
        changeLang(lang);
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getActivity().getBaseContext().getResources().updateConfiguration(newConfig, getActivity().getBaseContext().getResources().getDisplayMetrics());
        }
    }

    @Override
    public void onSuccessRequestLanguage(LanguageReceive obj) {

        languageList = new ArrayList<DropDownItem>();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Gson gson = new Gson();
            String gsonLanguageList = gson.toJson(obj.getLanguageList());
            pref.setLanguageList(gsonLanguageList);

            languageClickable = true;
            txtLangLanguage.setClickable(true);
            txtLangLanguage.setHint(getResources().getString(R.string.register_newsletter_language_hint));

		    /*Travel Doc*/
            for (int i = 0; i < obj.getLanguageList().size(); i++) {

                DropDownItem itemDoc = new DropDownItem();
                itemDoc.setText(obj.getLanguageList().get(i).getLanguageName());
                itemDoc.setCode(obj.getLanguageList().get(i).getLanguageCode());
                languageList.add(itemDoc);
            }
            //String languageCountry = gson.toJson(obj.getCountryList());
            //pref.setLanguageList(languageCountry);
        }

    }

    @Override
    public void onSuccessRequestState(StateReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            //save to pref

            pref.setFirstTimeUser("N");

            Gson gson = new Gson();
            String state = gson.toJson(obj.getStateList());
            pref.setState(state);

            home();

        }

    }

    @Override
    public void onSuccessRequestLanguageCountry(LanguageCountryReceive obj) {

        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.center_top);
        animation.setFillAfter(true);
        imageLayout.setAnimation(animation);

        country = new ArrayList<DropDownItem>();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    selectionLayout.setVisibility(View.VISIBLE);
                    chooseLayout.animate().alpha(1.0f);
                    imageLayout.setVisibility(View.GONE);

                }
            }, 2000);


            txtLangCountry.setClickable(true);
            txtLangCountry.setHint(getResources().getString(R.string.register_select_country));

		    /*Travel Doc*/
            for (int i = 0; i < obj.getCountryList().size(); i++) {

                DropDownItem itemDoc = new DropDownItem();
                itemDoc.setText(obj.getCountryList().get(i).getCountryName());
                itemDoc.setCode(obj.getCountryList().get(i).getCountryCode());
                country.add(itemDoc);
            }

            Gson gson = new Gson();
            String languageCountry = gson.toJson(obj.getCountryList());
            pref.setLanguageCountry(languageCountry);

        }

    }

    @Override
    public void onValidationSucceeded() {
        initiateLoading(getActivity());
        //retrieve back all data with selected language

        InitialLoadRequest infoData = new InitialLoadRequest();
        infoData.setLanguageCode(languageCode + "-" + cn);
        presenter.initialLoad(infoData);

        pref.setLanguageCountry(languageCode + "-" + countryCode);
        Log.e("CountryCode", languageCode + "-" + countryCode);
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {

            View view = error.getView();

            setShake(view);
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            } else if (view instanceof TextView) {
                ((TextView) view).setError(splitErrorMsg[0]);
            }
        }
        croutonAlert(getActivity(), getResources().getString(R.string.fill_emtpy_field));


    }

    public void home() {
        Intent home = new Intent(getActivity(), OnBoardingActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    public void retrieveLanguage(String countryCode) {

        txtLangLanguage.setText("");
        txtLangLanguage.setHint(getResources().getString(R.string.register_general_loading));

        LanguageRequest languageRequest = new LanguageRequest();
        languageRequest.setCountryCode(countryCode);

        presenter.onLanguageRequest(languageRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        checkLanguageCountryResult();
        Log.e("OnREsume", "true");
    }

    public void checkLanguageCountryResult() {
        RealmResults<CachedLanguageCountry> result = RealmObjectController.getCachedLanguageCountry(MainFragmentActivity.getContext());
        if (result.size() > 0) {
            Log.e("OnREsume", "1");

            Gson gson = new Gson();
            LanguageCountryReceive obj = gson.fromJson(result.get(0).getCachedResult(), LanguageCountryReceive.class);
            onSuccessRequestLanguageCountry(obj);
        }
        Log.e("OnREsume", "2");

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();

    }
}

