package com.app.tbd.ui.Activity.SplashScreen;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Picker.SelectLanguageCountryFragment;
import com.app.tbd.ui.Activity.Picker.SelectLanguageFragment;
import com.app.tbd.ui.Activity.SplashScreen.OnBoarding.OnBoardingActivity;
import com.app.tbd.ui.Model.Receive.LanguageReceive;
import com.app.tbd.ui.Model.Request.LanguageRequest;
import com.app.tbd.ui.Module.LanguageModule;
import com.app.tbd.ui.Presenter.LanguagePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

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

    @InjectView(R.id.btn_nxt)
    Button btn_nxt;

    ProgressDialog progress;
    private Validator mValidator;
    private int fragmentContainerId;
    private Locale myLocale;
    private ArrayList<DropDownItem> language = new ArrayList<DropDownItem>();
    private ArrayList<DropDownItem> country = new ArrayList<DropDownItem>();
    private String CURRENT_PICKER;

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

        //language = getLanguage(getActivity());
        country = getLangCountry(getActivity());

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
                //if (txtLangCountry.getText().toString() != "") {
                AnalyticsApplication.sendEvent("Click", "Select language");
                if (checkFragmentAdded()) {
                    showCountrySelector(getActivity(), language, "LANGUAGE");
                    CURRENT_PICKER = "LANGUAGE";
                }
            } //else {
            // Utils.toastNotification(getActivity(), "Please select country");
            //}
            //}
        });

        return view;
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

            } else if (CURRENT_PICKER.equals("COUNTRY")) {
                DropDownItem selectedCountry = data.getParcelableExtra(com.app.tbd.ui.Activity.Picker.SelectLanguageCountryFragment.KEY_LANGUAGE_COUNTRY_LIST);
                txtLangCountry.setText(selectedCountry.getText());
                txtLangCountry.setTag(selectedCountry.getCode());
                retrieveLanguage("ms");
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

                } else
                if (data.equals("LANGUAGE")) {
                    SelectLanguageFragment countryListDialogFragment = SelectLanguageFragment.newInstance(constParam);
                    countryListDialogFragment.setTargetFragment(LanguageFragment.this, 0);
                    countryListDialogFragment.show(fm, "countryListDialogFragment");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<DropDownItem> getLangCountry(Activity act) {

		/*Travelling Purpose*/
        ArrayList<DropDownItem> langCountryList = new ArrayList<DropDownItem>();

		/*Travel Doc*/
        final String[] country = act.getResources().getStringArray(R.array.lang_country);
        for (int i = 0; i < country.length; i++) {
            String countryDoc = country[i];
            String[] splitCountryDoc = countryDoc.split("-");

            DropDownItem itemCountryDoc = new DropDownItem();
            itemCountryDoc.setText(splitCountryDoc[0]);
            itemCountryDoc.setCode(splitCountryDoc[1]);
            langCountryList.add(itemCountryDoc);
        }
        return langCountryList;
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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void changeLanguage(String selectedLanguage) {
        String lang = "en";
        if (selectedLanguage.equals("en")) {
            lang = "en";
        } else if (selectedLanguage.equals("ms")) {
            lang = "ms";
        }else if (selectedLanguage.equals("th")) {
            lang = "th";
        }
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
        ArrayList<DropDownItem> languageList = new ArrayList<DropDownItem>();

		/*Travel Doc*/
        for (int i = 0; i < obj.getLanguageList().size(); i++) {

            DropDownItem itemDoc = new DropDownItem();
            itemDoc.setText(obj.getLanguageList().get(i).getLanguageName());
            itemDoc.setCode(obj.getLanguageList().get(i).getLanguageCode());
            languageList.add(itemDoc);
        }
    }

    @Override
    public void onValidationSucceeded() {
        initiateDefaultLoading(progress, getActivity());
        home();

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

    public void home(){
        Intent home = new Intent(getActivity(), OnBoardingActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    public void retrieveLanguage(String countryCode) {

        txtLangLanguage.setHint(getResources().getString(R.string.register_general_loading));

        LanguageRequest languageRequest = new LanguageRequest();
        languageRequest.setCountryCode(countryCode);

        presenter.onLanguageRequest(languageRequest);
    }
}

