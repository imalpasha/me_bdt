package com.app.tbd.ui.Activity.BoardingPass;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class BoardingPassDisplayActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, BoardingPassDisplayFragment.newInstance(bundle)).commit();

        //hideTitle();

    }


    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        //Log.i("Page Name", "Setting screen name: " + "Boarding Pass");
        //mTracker.setScreenName("Boarding Pass" + "Main");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
