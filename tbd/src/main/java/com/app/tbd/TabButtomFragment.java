package com.app.tbd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.app.tbd.ui.Activity.BookingFlight.SearchFlightActivity;
import com.app.tbd.ui.Activity.BookingFlight.SearchFlightFragment;
import com.app.tbd.ui.Activity.HolidayShaker.HolidayShakerActivity;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Activity.Homepage.HomeFragment;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Activity.Login.LoginFragment;
import com.app.tbd.ui.Activity.Profile.ProfileActivity;
import com.app.tbd.ui.Activity.Profile.ProfileFragment;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;

import java.util.HashMap;


public class TabButtomFragment extends Fragment {
    public static TabButtomFragment newInstance() {
        TabButtomFragment fragment = new TabButtomFragment();
        return fragment;
    }

    private AQuery aq;
    private SharedPrefManager pref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new AQuery(getActivity());
        pref = new SharedPrefManager(getActivity());

    }

    @SuppressLint("InflateParams")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle) {
        View layout = inflater.inflate(R.layout.layout_tab_container, null);
        aq.recycle(layout);

        aq.id(R.id.tabMyHome2).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance()).addToBackStack(null).commit();
                /*Intent homepage = new Intent(getActivity(), HomeActivity.class);
                homepage.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(homepage);
                getActivity().finish();*/

            }
        });

        aq.id(R.id.tabMyProfile).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {


                //BaseFragmentActivity.setTitle2("MY PROFILE");

                //check if user already login.
                pref = new SharedPrefManager(getActivity());
                HashMap<String, String> init = pref.getLoginStatus();
                String loginStatus = init.get(SharedPrefManager.ISLOGIN);


                //Intent tabProfile = null;
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                if (loginStatus == null || loginStatus.equals("N")) {
                    fragmentManager.beginTransaction().replace(R.id.main_content, LoginFragment.newInstance()).addToBackStack(null).commit();
                } else {
                    fragmentManager.beginTransaction().replace(R.id.main_content, ProfileFragment.newInstance()).addToBackStack(null).commit();
                }
                /*tabProfile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(tabProfile);
                getActivity().finish();*/
            }
        });


        aq.id(R.id.tabSearchFlight).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //Utils.toastNotification(getActivity(), "N/A");

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.main_content, SearchFlightFragment.newInstance()).commit();

                /*Intent searchFlight = new Intent(getActivity(), SearchFlightActivity.class);
                searchFlight.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //need to clear previous activity
                getActivity().startActivity(searchFlight);
                getActivity().finish();*/
            }
        });


        aq.id(R.id.tabMyHome).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //fragmentManager.beginTransaction().replace(R.id.main_content, SelectFlightFrament.newInstance()).commit();
                Utils.toastNotification(getActivity(), "N/A");
                /*Intent searchFlight = new Intent(getActivity(), SearchFlightActivity.class);
                searchFlight.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//need to clear previous activity
				getActivity().startActivity(searchFlight);
				getActivity().finish();*/

            }
        });


        aq.id(R.id.tabMyWishList).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return layout;
    }
}