package com.metech.tbd.ui.Activity.Register;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class RegisterActivity extends MainFragmentActivity implements FragmentContainerActivity {

    //implements ToolbarActivity, ProgressIndicatorActivity, FragmentContainerActivity {
    //@InjectView(R.id.main_activity_toolbar) Toolbar toolbar;
    //@InjectView(R.id.main_activity_progress_indicator) ProgressBar progressIndicator;

    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, RegisterFragment.newInstance(),"xx").commit();


       /* Fragment fragmentA = new FragmentA();
        getFragmentManager().beginTransaction()
                .replace(R.id.MainFrameLayout,fragmentA,"YOUR_TARGET_FRAGMENT_TAG")
                .addToBackStack("YOUR_SOURCE_FRAGMENT_TAG").commit();*/
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

/*    public void onBackPressed(){

        final FragmentManager manager = getSupportFragmentManager();
        RegisterFragment fragment = (RegisterFragment) manager.findFragmentByTag("xx");
        fragment.registerBackFunction();

    }*/
}
