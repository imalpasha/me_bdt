package com.app.tbd.ui.Activity.Profile.Option;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.tbd.R;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.base.BaseFragment;

import butterknife.ButterKnife;

public class PrivacyPolicyFragment extends BaseFragment {

    private static final String SCREEN_LABEL = "PrivacyPolicy";

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

        return view;
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
