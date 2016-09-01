package com.metech.tbd.ui.Activity.SplashScreen.Language;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

public class LanguageActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        //hideMenuButton();
        // hideTitle();
        //lockDrawer();
        // BaseFragment.removeLogoHeader(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, LanguageFragment.newInstance()).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
