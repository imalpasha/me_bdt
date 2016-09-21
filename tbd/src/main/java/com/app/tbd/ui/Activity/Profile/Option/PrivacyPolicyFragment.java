package com.app.tbd.ui.Activity.Profile.Option;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.base.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PrivacyPolicyFragment extends BaseFragment {

    private static final String SCREEN_LABEL = "PrivacyPolicy";

    @InjectView(R.id.mainWebView2)
    WebView mainWebView;

    String URL = "http://www.airasiabig.com/my/en/assets/pdf/privacypolicy.pdf";

    public static PrivacyPolicyFragment newInstance() {

        PrivacyPolicyFragment fragment = new PrivacyPolicyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.privacy_policy, container, false);
        ButterKnife.inject(this, view);

        initiateLoading(getActivity());

        WebSettings webSettings = mainWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mainWebView.setWebViewClient(new MyCustomWebViewClient());
        mainWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mainWebView.loadUrl(URL);
        mainWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                dismissLoading();
            }

        });
        return view;
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
