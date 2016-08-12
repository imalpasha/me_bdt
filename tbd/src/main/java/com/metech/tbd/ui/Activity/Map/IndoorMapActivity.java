package com.metech.tbd.ui.Activity.Map;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
//import com.fly.firefly.ui.fragment.Map.IndoorMapFragment;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class IndoorMapActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //@InjectView(R.id.btnLogin) Button btnLogin;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
       ///fragmentManager.beginTransaction().replace(R.id.main_activity_fragment_container, IndoorMapFragment.newInstance()).commit();
///
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
