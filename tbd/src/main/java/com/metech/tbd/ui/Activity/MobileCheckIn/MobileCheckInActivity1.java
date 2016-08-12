package com.metech.tbd.ui.Activity.MobileCheckIn;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class MobileCheckInActivity1 extends MainFragmentActivity implements FragmentContainerActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, MobileCheckInFragment1.newInstance()).commit();

        hideTitle();
        setMenuButton();


    }

    /*@Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            super.onBackPressed();
        }
    }*/


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
