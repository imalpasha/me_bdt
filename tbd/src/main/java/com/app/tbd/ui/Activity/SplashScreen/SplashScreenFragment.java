package com.app.tbd.ui.Activity.SplashScreen;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainController;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Model.Receive.InitialLoadReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Request.InitialLoadRequest;
import com.app.tbd.ui.Module.SplashScreenModule;
import com.app.tbd.ui.Presenter.HomePresenter;
import com.app.tbd.utils.SharedPrefManager;


import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashScreenFragment extends BaseFragment implements HomePresenter.SplashScreen {

    @Inject
    HomePresenter presenter;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private InitialLoadRequest info;
    private ProgressDialog progress;

    public static SplashScreenFragment newInstance() {

        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new SplashScreenModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        info = new InitialLoadRequest();
        info.setLanguageCode("en");
        sendDeviceInformationToServer(info);

        return view;
    }

    public static void splash(Context act, String regId) {
        Intent home = new Intent(act, SplashScreenActivity.class);
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        act.startActivity(home);
    }

    public void sendDeviceInformationToServer(InitialLoadRequest info) {

        //initiateDefaultLoading(progress, getActivity());

        if (MainController.connectionAvailable(getActivity())) {
            presenter.initialLoad(info);

        } else {
            //connectionRetry("No Internet Connection");
        }
    }

    /*public void connectionRetry(String msg) {

        pDialog.setTitleText("Connection Error");
        pDialog.setCancelable(false);
        pDialog.setContentText(msg);
        pDialog.setConfirmText("Retry");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sendDeviceInformationToServer(info);
            }
        })
                .show();

    }*/

    @Override
    public void loadingSuccess(InitialLoadReceive obj) {

        //dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getObj().getStatus(), obj.getObj().getMessage(), getActivity());
        if (status) {

            Gson gson = new Gson();

            String title = gson.toJson(obj.getObj().getData_title());
            pref.setUserTitle(title);

            String country = gson.toJson(obj.getObj().getData_country());
            pref.setCountry(country);

            goHomepage();

        }

    }

   /*public void update(InitialLoadReceive obj) {

        String signature = obj.getObj().getSignature();
        String bannerUrl = obj.getObj().getBanner_default();
        String promoBannerUrl = obj.getObj().getBanner_promo();
        String bannerModule = obj.getObj().getBanner_module();
        String dataVersion = obj.getObj().getData_version();
        String appVersion = obj.getObj().getData_version_mobile().getVersion();
        String bannerRedirectURL = obj.getObj().getBanner_redirect_url();

        InitialLoadReceive.SocialMedia socialMediaObj = obj.getObj().getSocial_media();

        Gson gson = new Gson();
        String title = gson.toJson(obj.getObj().getData_title());
        pref.setUserTitle(title);

        String country = gson.toJson(obj.getObj().getData_country());
        pref.setCountry(country);

        String state = gson.toJson(obj.getObj().getData_state());
        pref.setState(state);

        String flight = gson.toJson(obj.getObj().getData_market());
        pref.setFlight(flight);

        String socialMedia = gson.toJson(socialMediaObj);
        pref.setSocialMedia(socialMedia);

        pref.setSignatureToLocalStorage(signature);
        pref.setBannerUrl(bannerUrl);
        pref.setPromoBannerUrl(promoBannerUrl);
        pref.setBannerModule(bannerModule);
        pref.setBannerRedirectURL(bannerRedirectURL);
        pref.setDataVersion(dataVersion);
        pref.setAppVersion(appVersion);

        //thru homepage
        goHomepage();

    }*/

    public void goHomepage() {
        //go to on boarding first

        //first time user
        MainController.setHomeStatus();
        HashMap<String, String> initAppData = pref.getFirstTimeUser();
        String firstTime = initAppData.get(SharedPrefManager.FIRST_TIME_USER);

        if (firstTime != null && firstTime.equals("N")) {
            Intent home = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(home);
            getActivity().finish();
        } else {
            Intent language = new Intent(getActivity(), LanguageActivity.class);
            startActivity(language);
            getActivity().finish();
        }
    }

    public void forceUpdate() {
        Intent home = new Intent(getActivity(), ForceUpdateActivity.class);
        getActivity().startActivity(home);
        getActivity().finish();
    }

    @Override
    public void onConnectionFailed() {
        // connectionRetry("Unable to connect to server");
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
