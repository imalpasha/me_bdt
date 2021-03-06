package com.app.tbd.base;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.BookingFlight.Checkout.CheckoutActivity;
import com.app.tbd.ui.Activity.Profile.UserProfile.EditProfileActivity;
import com.app.tbd.ui.Activity.Profile.UserProfile.MyProfileFragment;
import com.app.tbd.ui.Model.Request.NotificationMessage;
import com.app.tbd.utils.App;
import com.app.tbd.ui.Realm.RealmObjectController;

import io.realm.RealmResults;

//import com.actionbarsherlock.app.ActionBar;
//import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.actionbarsherlock.internal.widget.ScrollingTabContainerView.TabView;

public class BaseFragmentActivity extends FragmentActivity {

    public static String appStatus;
    public com.app.tbd.base.AQuery aq;
    private static Activity instance;

    //TabView tabsView;

    public static String getAppStatus() {
        return appStatus;
    }

   /* public void tabSearch(View v)
    {


    }

    public void tabWish(View v)
    {
        Intent intent = new Intent(BaseFragmentActivity.this, MyWishListList.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseFragmentActivity.this.startActivity(intent);
    }

    public void tabCart(View v)
    {
        Intent intent = new Intent(BaseFragmentActivity.this, CartsView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        BaseFragmentActivity.this.startActivity(intent);
    }*/

    public static void setAppStatus(String status) {
        appStatus = status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        aq = new com.app.tbd.base.AQuery(this);
        Log.e("test2", "test2");

        //SET TO POTRAIT ONLY
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


      /* if ((getApplicationContext().getResources().getConfiguration().screenLayout &
                android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE){
            //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
           setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
           getApplicationContext().getResources().getConfiguration().orientation = 2;

        }else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            getApplicationContext().getResources().getConfiguration().orientation = 1;
        }*/


        try {
            ActionBar actionBar = getActionBar();
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            // actionBar.setElevation(0);
            //actionBar.setBackgroundDrawable(null);
            // tabsView = new ScrollingTabContainerView(actionBar.getThemedContext());
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            actionBar.setCustomView(R.layout.actionbar);
            View actionBarView = actionBar.getCustomView();
            aq.recycle(actionBarView);
            aq.id(R.id.title).typeface(Typeface.createFromAsset(getAssets(), App.FONT_HELVETICA_NEUE)).textSize(22).textColor(Color.WHITE);
            // if(Utils.getDeviceType(this) == "1")
            //{
            //   aq.id(R.id.tabContainerTablet).visible();
            //display tab here
            // }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.title).text(title);
    }


    public void hideTitle() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.title).visibility(View.GONE);
    }

    public void setCancelButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtCancel).visible();
        aq.id(R.id.txtCancel).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment.myProfileNotEditable();
                hideBackButton();
                hideDoneButton();
                hideCancelButton();
                setEditButton();
                setBackButton();
            }
        });
    }

    public void hideCancelButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtCancel).gone();
    }

    public void hideDoneButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtDone).gone();
    }

    public void hideBackButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.backbutton).gone();
    }

    public void hideEditButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtEdit).gone();
    }

    public void setDoneButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtDone).visible();
        aq.id(R.id.txtDone).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyProfileFragment.editProfileRequest();
                BaseFragment.initiateLoading(BaseFragmentActivity.this);
            }
        });
    }

    public void setEditButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.txtEdit).visible();
        aq.id(R.id.txtEdit).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent(BaseFragmentActivity.this, EditProfileActivity.class);
                String userInfo = MyProfileFragment.returnUserInfo();
                intent.putExtra("USER_INFORMATION", userInfo);
                BaseFragmentActivity.this.startActivity(intent);*/
                setCancelButton();
                setDoneButton();
                hideEditButton();
                hideBackButton();

                BaseFragment.initiateLoading(BaseFragmentActivity.this);
                MyProfileFragment.myProfileEditable();
            }
        });
    }

    public void setLogOutButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.btnLogOut).visible();
    }

    public void setARButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.btnAR).visible();
    }

    /*public void setEditButton()
    {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.btn).visible();
    }*/

    public void setBackButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.backbutton).visible();
        aq.id(R.id.backbutton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseFragmentActivity.this.finish();
            }
        });
    }

    public void setCheckOutButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.checkOut).visible();
        aq.id(R.id.checkOut).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BaseFragmentActivity.this, CheckoutActivity.class);
                BaseFragmentActivity.this.startActivity(intent);
            }
        });
    }

    public void setGlobalSearchButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.tabMySearch1).gone();
        aq.id(R.id.centerPart).gone();

        /*RelativeLayout leftPart = (RelativeLayout) actionBarView.findViewById(R.id.leftPart);
        RelativeLayout.LayoutParams lp = new RelativeLayout().LayoutParams(0,1);
        lp.weight = 0.05f;
        leftPart.setLayoutParams(lp);*/

        /*aq.id(R.id.tabBackButton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });*/
    }

    public void setTabBackButton() {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.tabBackButton).visible();
        aq.id(R.id.tabBackButton).clicked(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void setTitle(int titleId) {
        super.setTitle(titleId);
    }

    public void setTitleImage(String imageUrl) {
        View actionBarView = getActionBar().getCustomView();

        //aq.recycle(actionBarView);
        //aq.id(R.id.icon).image(imageUrl);
    }

    public void setTitleImage(int imageId) {
        View actionBarView = getActionBar().getCustomView();
        aq.recycle(actionBarView);
        aq.id(R.id.icon).image(imageId);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        //RealmObjectController.clearCachedResult(this);

    }

    @Override
    public void finish() {
        super.finish();
        RealmObjectController.clearCachedResult(this);
        System.gc();

        //overridePendingTransition(R.anim.fadeout,R.anim.fadein);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.fade_out);
        // RealmObjectController.clearCachedResult(this);
        //setResult(RESULT_CANCELED);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onResume() {

        super.onResume();
        MainFragmentActivity.setContext(this);

        //push notification alert
        try {
            if (!this.getClass().getSimpleName().equals("SplashScreenActivity")) {
                if (appStatus != null && appStatus.equals("ready_for_notification")) {
                    if (appStatus.equals("ready_for_notification")) {
                        RealmResults<NotificationMessage> result = RealmObjectController.getNotificationMessage(this);
                        if (result.size() > 0) {
                            BaseFragment.setPushNotificationAlert(this, result.get(0).getMessage(), result.get(0).getTitle());
                            MainFragmentActivity.setAppStatus("not_ready_for_notification");
                            RealmObjectController.clearNotificationMessage(this);
                        }
                    } else {
                    }
                } else {
                    appStatus = "not_for_notification";
                }
            }
        } catch (Exception e) {

        }
    }
}
