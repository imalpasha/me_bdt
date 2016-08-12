package com.metech.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class ContactInfoActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, ContactInfoFragment.newInstance(bundle)).commit();

        hideTitle();
        setMenuButton();

    }

    @Override
    public void onResume() {
        super.onResume();
        //mTracker.setScreenName("Personal Detail" + "A");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
