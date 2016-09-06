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
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Picker.SelectLanguageCountryFragment;
import com.app.tbd.ui.Activity.Picker.SelectLanguageFragment;
import com.app.tbd.ui.Activity.SplashScreen.OnBoarding.OnBoardingActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.LanguageCountryReceive;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.LanguageCountryRequest;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Module.LanguageModule;
import com.app.tbd.ui.Presenter.HomePresenter;
import com.app.tbd.ui.Presenter.LanguagePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
    LinearLayout selectionLayout;

    @InjectView(R.id.btn_nxt)
    Button btn_nxt;


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
    private String languageCode,countryCode;

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

        //Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.center_top);
        //imageLayout.setAnimation(animation);

        //load country
        LanguageCountryRequest languageCountryRequest = new LanguageCountryRequest();
        presenter.onCountryRequest(languageCountryRequest);
        txtLangCountry.setClickable(false);

        loadLocale();

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
                    showCountrySelector(getActivity(), country, "COUNTRY");
                    CURRENT_PICKER = "COUNTRY";
                }
            }
        });

        //language selection
        txtLangLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtLangCountry.getText().toString() != "") {
                    AnalyticsApplication.sendEvent("Click", "Select language");
                    if (checkFragmentAdded()) {
                        showCountrySelector(getActivity(), languageList, "LANGUAGE");
                        CURRENT_PICKER = "LANGUAGE";
                    }
                } else if (txtLangLanguage.getHint().toString().equals("Loading...")) {
                    Utils.toastNotification(getActivity(), "Loading language...");
                } else {
                    Utils.toastNotification(getActivity(), "Please select country");
                }
            }
        });

        return view;
    }

    @Override
    public void loadingSuccess(InitialLoadReceive obj) {

        dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            Gson gson = new Gson();

            String title = gson.toJson(obj.getObj().getData_title());
            pref.setUserTitle(title);

            String country = gson.toJson(obj.getObj().getData_country());
            pref.setCountry(country);

            home();

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
            if (CURRENT_PICKER.equals("LANGUAGE")) {
                DropDownItem selectedLanguage = data.getParcelableExtra(com.app.tbd.ui.Activity.Picker.SelectLanguageFragment.KEY_LANGUAGE_LIST);
                txtLangLanguage.setText(selectedLanguage.getText());
                txtLangLanguage.setTag(selectedLanguage.getCode());
                changeLanguage(selectedLanguage.getCode());
                languageCode = selectedLanguage.getCode();

            } else if (CURRENT_PICKER.equals("COUNTRY")) {
                DropDownItem selectedCountry = data.getParcelableExtra(com.app.tbd.ui.Activity.Picker.SelectLanguageCountryFragment.KEY_LANGUAGE_COUNTRY_LIST);
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
                if (data.equals("COUNTRY")) {
                    SelectLanguageCountryFragment countryListDialogFragment = SelectLanguageCountryFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(LanguageFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");

                } else if (data.equals("LANGUAGE")) {
                    SelectLanguageFragment countryListDialogFragment = SelectLanguageFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(LanguageFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }
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
        //register_language.setText(R.string.register_language);
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

    public void changeLanguage(String selectedLanguage) {
        String lang = "en";
        if (selectedLanguage.equals("en")) {
            lang = "en";
        } else if (selectedLanguage.equals("ms")) {
            lang = "ms";
        } else {
            lang = "en";
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

            txtLangLanguage.setClickable(true);
            txtLangLanguage.setHint(getResources().getString(R.string.register_newsletter_language_hint));

		    /*Travel Doc*/
            for (int i = 0; i < obj.getLanguageList().size(); i++) {

                DropDownItem itemDoc = new DropDownItem();
                itemDoc.setText(obj.getLanguageList().get(i).getLanguageName());
                itemDoc.setCode(obj.getLanguageList().get(i).getLanguageCode());
                languageList.add(itemDoc);
            }

        }


    }

    @Override
    public void onSuccessRequestLanguageCountry(LanguageCountryReceive obj) {

        country = new ArrayList<DropDownItem>();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    selectionLayout.setVisibility(View.VISIBLE);
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

        }

    }


    @Override
    public void onValidationSucceeded() {
        initiateDefaultLoading(progress, getActivity());
        //retrieve back all data with selected language

        InitialLoadRequest infoData = new InitialLoadRequest();
        infoData = new InitialLoadRequest();
        infoData.setLanguageCode(languageCode+"-"+countryCode);
        presenter.initialLoad(infoData);
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
}
