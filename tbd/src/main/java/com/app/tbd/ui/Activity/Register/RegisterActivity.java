package com.app.tbd.ui.Activity.Register;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.FragmentContainerActivity;

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


        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, RegisterFragment.newInstance(bundle), "xx").commit();

        setBackButton();
        setTitle(getResources().getString(R.string.register_title));



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
