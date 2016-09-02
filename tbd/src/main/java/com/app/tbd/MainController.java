package com.app.tbd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.BookingFlight.SearchFlightActivity;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Activity.PasswordExpired.ChangePasswordActivity;
import com.app.tbd.ui.Activity.SplashScreen.SplashScreenActivity;
import com.app.tbd.ui.Activity.Terms.Terms;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Dell on 1/5/2016.
 */
public class MainController extends BaseFragment {

    private static boolean homeStatus;

    public static boolean getHomeStatus(){
        return homeStatus;
    }

    public static void setHomeStatus(){
        homeStatus = true;
    }

    public static void clearAll(Activity act){
        SharedPrefManager  pref = new SharedPrefManager(act);
        pref.clearSignatureFromLocalStorage();
        pref.clearPassword();
        pref.clearUserEmail();
        pref.clearBookingID();
        pref.clearCustomerNumber();
        pref.clearPersonID();
        pref.clearFlightType();
        pref.clearNewsletterStatus();
        pref.clearPNR();
        pref.setLoginStatus("N");
        Log.e("SUCCESS","ok");
    }


    private static SweetAlertDialog pDialog;

    public static void clickableBanner(Activity act,String page){

        Intent bannerIntent;
        if(page.equals("booking")){
            bannerIntent = new Intent(act,SearchFlightActivity.class);
            act.startActivity(bannerIntent);
        }else if(page.equals("faq")){
            bannerIntent = new Intent(act,Terms.class);
            act.startActivity(bannerIntent);
        }
    }

    public static void clickableBannerWithURL(Activity act,String url){

        if (!url.startsWith("http://") && !url.startsWith("https://")){
            url = "http://" + url;
        }

        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        act.startActivity(i);

        Log.e("url",url);

    }


    public static boolean connectionAvailable(Activity act){

        Boolean internet;
        internet = Utils.isNetworkAvailable(act);

        return internet;
    }

    public static boolean getRequestStatus(String objStatus,String message,Activity act) {

        SharedPrefManager pref;
        pref = new SharedPrefManager(act);

        Boolean status = false;
        if (objStatus.equals("OK") || objStatus.equals("Redirect")) {
            status = true;

        } else if (objStatus.equals("Error") || objStatus.equals("error_validation")) {
            status = false;
            setAlertDialog(act, message,"Error");

        } else if (objStatus.equals("401")) {
            status = false;
            setSignatureInvalid(act,message);
            pref.clearSignatureFromLocalStorage();

        }else if(objStatus.equals("force_logout")){
            MainController.clearAll(act);
            resetPage(act);
        }else if(objStatus.equals("503")){
            //MainController.clearAll(act);
            //resetPage(act);
            setAlertMaintance(act,message,SplashScreenActivity.class,"Sorry!");

        }
        else if (objStatus.equals("change_password")) {
            goChangePasswordPage(act);
        }else if(objStatus.equals("error")) {
            //croutonAlert(getActivity(),obj.getMessage());
            //setSuccessDialog(getActivity(), obj.getMessage(), getActivity(), SearchFlightActivity.class);
            setAlertDialog(act,message,"Validation Error");
        }
        return status;

    }

    public void retry(){

    }

    //Redirect
    public static void goChangePasswordPage(Activity act){
        Intent loginPage = new Intent(act, ChangePasswordActivity.class);
        act.startActivity(loginPage);
        act.finish();
    }

    //Redirect
    public static void resetPage(Activity act){
        Intent loginPage = new Intent(act, HomeActivity.class);
        act.startActivity(loginPage);
        act.finish();
    }

}
