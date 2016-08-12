package com.metech.tbd.ui.Activity.Homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;


public class HomeActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance(),"Home").commit();

        setMenuButton();
        setTitle(R.string.TBD_app_name);
        unlockDrawer();
    }

   /* private void goToSearchFragment() {
        fragmentManager.beginTransaction()
                .add(R.id.main_activity_fragment_container, SearchFragment.newInstance())
                .commit();
    }*/

    @Override
    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        HomeFragment fragment = (HomeFragment) manager.findFragmentByTag("Home");
        fragment.registerBackFunction();
    }

    /*@Override
    public ProgressBar getProgressIndicator() {
        return progressIndicator;
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }*/
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
