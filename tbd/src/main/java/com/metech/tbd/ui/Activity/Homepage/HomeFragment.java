package com.metech.tbd.ui.Activity.Homepage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.R;
import com.metech.tbd.ui.Model.Receive.DeviceInfoSuccess;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.BoardingPass.BoardingPassActivity;
import com.metech.tbd.ui.Activity.BookingFlight.SearchFlightActivity;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Activity.ManageFlight.MF_Activity;
import com.metech.tbd.ui.Activity.MobileCheckIn.MobileCheckInActivity1;
import com.metech.tbd.ui.Module.HomeModule;
import com.metech.tbd.ui.Presenter.HomePresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;

//import com.estimote.sdk.BeaconManager;

public class HomeFragment extends BaseFragment implements HomePresenter.HomeView{

    // --------------------------------------------------------------------------------//



    // --------------------------------------------------------------------------------//
    private Tracker mTracker;

    @Inject
    HomePresenter presenter;

    //@InjectView(R.id.homeBeacon)
    //LinearLayout homeBeacon;

    @InjectView(R.id.bannerImg)
    ImageView bannerImg;

    private String facebookUrl,twitterUrl,instagramUrl;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Home";

    private SharedPrefManager pref;
    View view;

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        aq.recycle(view);

        HashMap<String, String> initPromoBanner = pref.getPromoBanner();
        String banner = initPromoBanner.get(SharedPrefManager.PROMO_BANNER);

        if(banner == null || banner == ""){
            HashMap<String, String> initDefaultBanner = pref.getDefaultBanner();
            banner = initDefaultBanner.get(SharedPrefManager.DEFAULT_BANNER);
        }

        //put in try catch
        try {
            aq.id(R.id.bannerImg).image(banner);
        }catch (Exception e){
        }

        HashMap<String, String> initBannerModule = pref.getBannerModule();
        HashMap<String, String> initBannerURL = pref.getBannerRedirectURL();

        final String bannerModule = initBannerModule.get(SharedPrefManager.BANNER_MODULE);
        final String bannerRedirectURL = initBannerURL.get(SharedPrefManager.BANNER_REDIRECT_URL);

        bannerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bannerRedirectURL.equals("")){
                    MainController.clickableBannerWithURL(getActivity(),bannerRedirectURL);
                }else if(!bannerModule.equals("")){
                    MainController.clickableBanner(getActivity(),bannerModule);
                }else{
                    //No Action
                }
            }
        });

        HashMap<String, String> initSocialMedia = pref.getSocialMedia();
        String socialMedia = initSocialMedia.get(SharedPrefManager.SOCIAL_MEDIA);

        Gson gson = new Gson();
        DeviceInfoSuccess.SocialMedia socialMediaObj = gson.fromJson(socialMedia, DeviceInfoSuccess.SocialMedia.class);

        try {
            facebookUrl = socialMediaObj.getFacebook();
            twitterUrl = socialMediaObj.getTwitter();
            instagramUrl = socialMediaObj.getInstagram();
        }catch (Exception e){

        }

        // [START shared_tracker]
        // Obtain the shared Tracker instance.
        AnalyticsApplication application = (AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        // [END shared_tracker]

        /*mobileFaq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBeacon();
            }
        });*/

        //homeBeacon.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View v) {
        //        AnalyticsApplication.sendEvent("Click", "homeBeacon");
        //        goToBeacon();
        //    }
        //});



        //setUpMap();
        //trySetAlarm();
        //LocalNotification.convert(getActivity());

       // screenSize();


       // forceCrash(view);
        return view;
    }

    public void forceCrash(View view) {
        throw new RuntimeException("This is a crash");
    }


    public void screenSize(){

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

    }



    public void getScreenSize(){




        int screenSize = getResources().getConfiguration().screenLayout &  Configuration.SCREENLAYOUT_SIZE_MASK;

        String toastMsg;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                toastMsg = "Large screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                toastMsg = "Normal screen";
                break;
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                toastMsg = "Small screen";
                break;
            default:
                toastMsg = "Screen size is neither large, normal or small";
        }
    }

    // ------------------------------------------------------------------------------------------- //

    public void goToManageFlight() {
        Intent loginPage = new Intent(getActivity(), MF_Activity.class);
        getActivity().startActivity(loginPage);

    }

    /*Public-Inner Func*/
    public void goToMobileCheckIn() {
        Intent mcheckin = new Intent(getActivity(), MobileCheckInActivity1.class);
        getActivity().startActivity(mcheckin);
    }

    public void goToBoardingPass() {
        //Intent loginPage = new Intent(getActivity(), BeaconRanging.class);
        Intent loginPage = new Intent(getActivity(), BoardingPassActivity.class);
        //Intent loginPage = new Intent(getActivity(), MainActivity.class);
        //Intent loginPage = new Intent(getActivity(), GenFencingActivity.class);
        //Intent loginPage = new Intent(getActivity(), AutoCamera.class);

        getActivity().startActivity(loginPage);



    }


    public void goBookingPage() {
        Intent loginPage = new Intent(getActivity(), SearchFlightActivity.class);
        getActivity().startActivity(loginPage);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void registerBackFunction() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT TBD")
                .setContentText("Confirm exit?")
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setConfirmText("Close")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        getActivity().finish();
                        System.exit(0);

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();


    }





























}
