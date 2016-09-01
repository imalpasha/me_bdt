package com.metech.tbd.ui.Activity.SplashScreen.Language;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LanguageFragment extends BaseFragment implements View.OnClickListener {
    @InjectView(R.id.txt_hello)
    TextView txt_hello;

    @InjectView(R.id.btn_ms)
    Button btn_ms;

    @InjectView(R.id.btn_en)
    Button btn_en;

    private int fragmentContainerId;
    private Locale myLocale;

    public static LanguageFragment newInstance() {

        LanguageFragment fragment = new LanguageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.language, container, false);
        ButterKnife.inject(this, view);


        this.btn_en.setOnClickListener(this);
        this.btn_ms.setOnClickListener(this);

        loadLocale();

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    public void loadLocale()
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        String language = prefs.getString(langPref, "");
        changeLang(language);
    }

    public void saveLocale(String lang)
    {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    public void changeLang(String lang)
    {
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

    private void updateTexts()
    {
        txt_hello.setText(R.string.hello_world);
        btn_en.setText(R.string.btn_en);
        btn_ms.setText(R.string.btn_ms);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View view) {
        String lang = "en";
        switch (view.getId()) {
            case R.id.btn_en:
                lang = "en";
                break;
            case R.id.btn_ms:
                lang = "ms";
                break;
            default:
                break;
        }
        changeLang(lang);

    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null){
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getActivity().getBaseContext().getResources().updateConfiguration(newConfig, getActivity().getBaseContext().getResources().getDisplayMetrics());
        }
    }
}

