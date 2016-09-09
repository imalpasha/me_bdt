package com.app.tbd.ui.Activity.Register;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Activity.Picker.BasicPicker;
import com.app.tbd.ui.Activity.Picker.SelectCountryFragment;
import com.app.tbd.ui.Activity.Picker.SelectDefaultFragment;
import com.app.tbd.ui.Activity.Picker.SelectStateFragment;
import com.app.tbd.ui.Model.JSON.UserFacebookInfo;
import com.app.tbd.ui.Model.Receive.ContactInfoReceive;
import com.app.tbd.ui.Model.Receive.NewsletterLanguageReceive;
import com.app.tbd.ui.Model.Receive.RegisterReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Picker.DatePickerFragment;
import com.app.tbd.ui.Model.Receive.StateReceive;
import com.app.tbd.ui.Model.Request.NewsletterLanguageRequest;
import com.app.tbd.ui.Model.Request.RegisterRequest;
import com.app.tbd.ui.Model.Request.StateRequest;
import com.app.tbd.ui.Module.RegisterModule;
import com.app.tbd.ui.Model.Request.CachedResult;
import com.app.tbd.ui.Presenter.RegisterPresenter;
import com.app.tbd.utils.DropDownItem;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.app.tbd.utils.Utils;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import io.realm.RealmResults;

public class RegisterFragment extends BaseFragment {

    @InjectView(R.id.mainWebView)
    WebView mainWebView;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Register";
    private ProgressDialog progress;
    boolean loadingFinished = true;
    boolean redirect = false;
    private SharedPrefManager pref;

    public static RegisterFragment newInstance(Bundle bundle) {
        RegisterFragment fragment = new RegisterFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.register_webview, container, false);
        ButterKnife.inject(this, view);
        progress = new ProgressDialog(getActivity());
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initLanguageCountry = pref.getLanguageCountry();
        String lc = initLanguageCountry.get(SharedPrefManager.LANGUAGE_COUNTRY);

        String URL =  "https://member.airasia.com/register.aspx?culture="+lc;

        initiateLoading(getActivity());

        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        /*mainWebView.setWebViewClient(new MyCustomWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mainWebView.loadUrl(URL);
        mainWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissDefaultLoading(progress, getActivity());
            }

        });*/
        loadURL(URL);
        return view;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void focus() {
        croutonAlert(getActivity(),"ERROR");
    }

    public void loadURL(String url){

        Boolean override = false;
        mainWebView.getSettings().setJavaScriptEnabled(true);
        mainWebView.setWebChromeClient(new WebChromeClient());
        mainWebView.addJavascriptInterface(new RegisterFragment(), "Android");
        mainWebView.loadUrl(url);

        mainWebView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (!loadingFinished) {
                        redirect = true;
                    }

                    loadingFinished = false;

                    if (url.contains("member-reg-submit")) {
                        setSuccessDialog(getActivity(), "Thank You! Please check your email to complete your registration", LoginActivity.class, "Success!");
                        Log.e("A","A");

                    }else{
                        view.loadUrl(url);
                        Log.e("B","B");
                    }
                    Log.e("URL",url);

                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                    //SHOW LOADING IF IT ISNT ALREADY VISIBLE
                    loadingFinished = false;
                    initiateLoading(getActivity());
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    if (!redirect) {
                        loadingFinished = true;
                    }

                    if (loadingFinished && !redirect) {
                        dismissLoading();
                    } else {
                        redirect = false;
                    }
                }

            });
    }

    private class MyCustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsApplication.sendScreenView(SCREEN_LABEL);

    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
