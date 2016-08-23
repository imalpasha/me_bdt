package com.metech.tbd.ui.Activity.MobileCheckIn;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

public class MobileCheckInActivity3 extends MainFragmentActivity implements FragmentContainerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, MobileCheckInFragment3.newInstance(bundle)).commit();


    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
