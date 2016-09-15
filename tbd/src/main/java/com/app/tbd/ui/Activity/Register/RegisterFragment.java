package com.app.tbd.ui.Activity.Register;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.utils.SharedPrefManager;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
