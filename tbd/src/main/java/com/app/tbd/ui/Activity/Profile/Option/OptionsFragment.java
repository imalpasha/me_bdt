package com.app.tbd.ui.Activity.Profile.Option;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Activity.Picker.SelectLanguageFragment;
import com.app.tbd.ui.Activity.Profile.ProfileActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;
import com.app.tbd.ui.Module.OptionModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OptionsFragment extends BaseFragment implements ProfilePresenter.OptionView {
    private int fragmentContainerId;

    @Inject
    ProfilePresenter presenter;

    @InjectView(R.id.txtLogout)
    LinearLayout txtLogout;

    @InjectView(R.id.resetPasswordLayout)
    LinearLayout resetPasswordLayout;

    @InjectView(R.id.changeLanguageLayout)
    LinearLayout changeLanguageLayout;

    @InjectView(R.id.options_about)
    LinearLayout options_about;

    @InjectView(R.id.options_terms)
    LinearLayout options_terms;

    @InjectView(R.id.options_policy)
    LinearLayout options_policy;

    private SharedPrefManager pref;
    private ProgressDialog progress;
    private String CURRENT_PICKER;
    private Locale myLocale;
    private String languageCode;
    private String countryCode;
    private String cn;
    private String latestCountryCode;

    private ArrayList<DropDownItem> languageList = new ArrayList<DropDownItem>();

    public static OptionsFragment newInstance() {

        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new OptionModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.options, container, false);
        ButterKnife.inject(this, view);
        setupData();

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.clearLoginStatus();
                HashMap<String, String> initTicketId = pref.getToken();
                String token = initTicketId.get(SharedPrefManager.TOKEN);

                HashMap<String, String> initUserName = pref.getUsername();
                String userName = initUserName.get(SharedPrefManager.USERNAME);

                LogoutRequest logoutRequest = new LogoutRequest();
                logoutRequest.setToken(token);
                logoutRequest.setUsername(userName);

                initiateLoading(getActivity());
                presenter.onRequestLogout(logoutRequest);


            }
        });

        resetPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resetPassword = new Intent(getActivity(), ResetPasswordActivity.class);
                getActivity().startActivity(resetPassword);

            }
        });

        changeLanguageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "Change Language");
                if (checkFragmentAdded()) {
                    showCountrySelector(getActivity(), languageList, "LANGUAGE_LIST");
                    CURRENT_PICKER = "LANGUAGE";
                }

            }
        });


        options_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent about = new Intent(getActivity(), AboutActivity.class);
                getActivity().startActivity(about);
            }
        });

        options_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent term = new Intent(getActivity(), TermsActivity.class);
                getActivity().startActivity(term);
            }
        });

        options_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent policy = new Intent(getActivity(), PrivacyPolicyActivity.class);
                getActivity().startActivity(policy);
            }
        });


        return view;
    }

    public void setupData() {
        pref = new SharedPrefManager(getActivity());
        languageList = getLanguage(getActivity());

        //retrieve back all data with selected language
        HashMap<String, String> init = pref.getLanguageCountry();
        String langCountry = init.get(SharedPrefManager.LANGUAGE_COUNTRY);

        String[] parts = langCountry.split("-");
        latestCountryCode = parts[1];
    }

    /*Country selector - > need to move to main activity*/
    public void showCountrySelector(Activity act, ArrayList constParam, String data) {
        if (act != null) {
            try {
                android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
                if (data.equals("LANGUAGE_LIST")) {
                    SelectLanguageFragment languageListFragment = SelectLanguageFragment.newInstance(constParam);
                    languageListFragment.setTargetFragment(OptionsFragment.this, 0);
                    languageListFragment.show(fm, "countryListDialogFragment");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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


            //load state - need to move later on
            StateRequest stateRequest = new StateRequest();
            stateRequest.setLanguageCode(languageCode + "-" + cn);
            stateRequest.setCountryCode(latestCountryCode);
            stateRequest.setPresenterName("LanguagePresenter");
            presenter.onStateRequest(stateRequest);

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {
            if (CURRENT_PICKER.equals("LANGUAGE")) {
                DropDownItem selectedLanguage = data.getParcelableExtra(com.app.tbd.ui.Activity.Picker.SelectLanguageFragment.KEY_LANGUAGE_LIST);
                changeLanguage(selectedLanguage.getCode());
                changeLangContent(selectedLanguage.getCode());

                languageCode = selectedLanguage.getCode();
                Log.e("Change", selectedLanguage.getCode());
            }
        }
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


    public void changeLangContent(String langCode) {
        initiateLoading(getActivity());

        InitialLoadRequest infoData = new InitialLoadRequest();
        infoData.setLanguageCode(langCode + "-" + cn);
        presenter.initialLoad(infoData);

        pref.setLanguageCountry(langCode + "-" + latestCountryCode);

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
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    @Override
    public void onLogoutReceive(LogoutReceive obj) {

        dismissLoading();
        Intent profilePage = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(profilePage);
        getActivity().finish();
        pref.setLoginStatus("N");

        /*Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Intent profilePage = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(profilePage);
            getActivity().finish();

        }*/
    }

    @Override
    public void onSuccessRequestState(StateReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            //save to pref
            Gson gson = new Gson();
            String state = gson.toJson(obj.getStateList());
            pref.setState(state);

            Intent changeLang = new Intent(getActivity(), ProfileActivity.class);
            changeLang.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getActivity().startActivity(changeLang);
            getActivity().finish();
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


