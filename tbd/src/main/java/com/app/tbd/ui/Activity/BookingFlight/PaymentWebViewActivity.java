package com.app.tbd.ui.Activity.BookingFlight;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class PaymentWebViewActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, PaymentWebViewFragment.newInstance(bundle),"PaymentWebView").commit();

        //hideTitle();
        BaseFragment.removeLogoHeader(this);

    }

    @Override
    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        PaymentWebViewFragment fragment = (PaymentWebViewFragment) manager.findFragmentByTag("PaymentWebView");
        fragment.paymentBackButton();
    }

    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
        //Log.i("Page Name", "Setting screen name: " + "Payment Flight");
        //mTracker.setScreenName("Payment Flight" + "A");
        //mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_content;
    }
}
