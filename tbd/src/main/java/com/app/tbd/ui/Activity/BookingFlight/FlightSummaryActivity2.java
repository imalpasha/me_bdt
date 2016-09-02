package com.app.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class FlightSummaryActivity2 extends MainFragmentActivity implements FragmentContainerActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, FlightSummaryFragment.newInstance(),"FlightSummary").commit();


    }


    @Override
    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        FlightSummaryFragment fragment = (FlightSummaryFragment) manager.findFragmentByTag("FlightSummary");
        fragment.backButton();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_content;
    }
}
