package com.metech.tbd;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.metech.tbd.base.AQuery;
import com.metech.tbd.base.BaseFragmentActivity;
import com.metech.tbd.drawer.DrawerItem;
import com.metech.tbd.drawer.NavigationDrawerFragment;
import com.metech.tbd.ui.Activity.Aboutus.AboutUsActivity;
import com.metech.tbd.ui.Activity.Homepage.HomeActivity;
import com.metech.tbd.ui.Activity.Login.LoginActivity;
import com.metech.tbd.ui.Activity.Register.RegisterActivity;
import com.metech.tbd.ui.Activity.Terms.Terms;
import com.metech.tbd.ui.Activity.UpdateProfile.UpdateProfileActivity;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;

import butterknife.ButterKnife;

//import com.actionbarsherlock.view.Menu;
//import com.actionbarsherlock.view.MenuItem;


public class MainFragmentActivity extends BaseFragmentActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static Activity instance;
    /**
     * Fragment managing the behaviors, interactions and presentation of the
     * navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private SharedPrefManager pref;

    public static Activity setContext(Activity act) {
        instance = act;
        return instance;

    }

    public static Activity getContext() {
        //return instance.getApplicationContext();
        //return ActivityName.this;
        return instance;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aq = new AQuery(this);
        ButterKnife.inject(this);
        instance = this;

        /*Testing*/
        moveDrawerToTop();
        pref = new SharedPrefManager(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.tab_container, TabButtomFragment.newInstance()).commit();

    }

    private void initActionBar() {

    }

    private void moveDrawerToTop() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        DrawerLayout drawer = (DrawerLayout) inflater.inflate(R.layout.decor3, null); // "null" is important.

        // HACK: "steal" the first child of decor view
        ViewGroup decor = (ViewGroup) getWindow().getDecorView();
        View child = decor.getChildAt(0);
        decor.removeView(child);
        LinearLayout container = (LinearLayout) drawer.findViewById(R.id.main_activity_fragment_container); // This is the container we defined just now.
        container.addView(child, 0);
        drawer.findViewById(R.id.navigation_drawer).setPadding(0, getStatusBarHeight(), 0, 0);

        // Make the drawer replace the first child
        decor.addView(drawer);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isTaskRoot()) {
            // clean the file cache with advance option
            long triggerSize = 3000000; // starts cleaning when cache size is
            // larger than 3M
            long targetSize = 2000000; // remove the least recently used files
            // until cache size is less than 2M
            //AQUtility.cleanCacheAsync(this, triggerSize, targetSize);
        }
    }

    public void hideTabButton() {
        aq.id(R.id.tab_container).gone();
    }

    public void setMenuButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.menubutton).visible();
        aq.id(R.id.menubutton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerFragment.openDrawer();
            }
        });
    }

    public void lockDrawer() {
        mNavigationDrawerFragment.lockDrawer();

    }

    public void unlockDrawer() {
        mNavigationDrawerFragment.unlockDrawer();
    }


    public void hideMenuButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.menubutton).gone();
        aq.id(R.id.menubutton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mNavigationDrawerFragment.openDrawer();
            }
        });
    }

    @Override
    public void onNavigationDrawerItemSelected(int position, DrawerItem item) {
        // update the main content by replacing fragments
        try {
            if (item.getTag().equals("Home")) {
                item.setBackgroundColor(getResources().getColor(R.color.white));
                Intent homepage = new Intent(this, HomeActivity.class);
                homepage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(homepage);

            } else if (item.getTag().equals("Login")) {

                Intent login = new Intent(this, LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);

            } else if (item.getTag().equals("Logout")) {
                pref.setLoginStatus("N");
                MainController.clearAll(this);
                RealmObjectController.deleteRealmFile(MainFragmentActivity.getContext());

                Intent login = new Intent(this, HomeActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(login);

            } else if (item.getTag().equals("Register")) {
                item.setBackgroundColor(getResources().getColor(R.color.white));
                Intent register = new Intent(this, RegisterActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(register);

            } else if (item.getTag().equals("Information_Update")) {
                Intent register = new Intent(this, UpdateProfileActivity.class);
                register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(register);

            } else if (item.getTag().equals("About")) {
                Intent about = new Intent(this, AboutUsActivity.class);
                about.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(about);
                Log.e("xx", item.getTag().toString());

            } else if (item.getTag().equals("FAQ")) {
                Intent terms = new Intent(this, Terms.class);
                terms.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(terms);

            } else if (item.getTag().equals("HEADER")) {

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        // actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        // actionBar.setDisplayShowTitleEnabled(true);
        // actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        /*int id = item.getItemId();
        switch (id)
        {
            case R.id.action_settings:
                break;

            default:
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }


}
