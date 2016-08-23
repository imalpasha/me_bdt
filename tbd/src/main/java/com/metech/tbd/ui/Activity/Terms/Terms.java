package com.metech.tbd.ui.Activity.Terms;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

public class Terms extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;
    private Tracker mTracker;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, TermsFragment.newInstance()).commit();



    }


    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();

    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}