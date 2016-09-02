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

public class BoardingPassActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //implements ToolbarActivity, ProgressIndicatorActivity, FragmentContainerActivity {
    //@InjectView(R.id.main_activity_toolbar) Toolbar toolbar;
    //@InjectView(R.id.main_activity_progress_indicator) ProgressBar progressIndicator;
    private Tracker mTracker;
    //private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, BoardingPassFragment.newInstance(),"Boarding").commit();

        //hideTitle();

    }

   // @Override
   // public void onBackPressed(){

     //   final FragmentManager manager = getSupportFragmentManager();
     //   BoardingPassFragment fragment = (BoardingPassFragment) manager.findFragmentByTag("Boarding");
     //   fragment.onBackPressed();
   // }

    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        /*Log.i("Page Name", "Setting screen name: " + "Boarding Pass");
        mTracker.setScreenName("Boarding Pass" + "Main");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());*/
    }


    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
