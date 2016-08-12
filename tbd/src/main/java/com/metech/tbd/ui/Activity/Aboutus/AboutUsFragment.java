package com.metech.tbd.ui.Activity.Aboutus;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.R;
import com.metech.tbd.ui.Model.Receive.AboutUsReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Module.AboutUsModule;
import com.metech.tbd.ui.Model.Request.AboutUs;
import com.metech.tbd.ui.Presenter.AboutUsPresenter;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AboutUsFragment extends BaseFragment implements AboutUsPresenter.AboutUsView {

    @Inject
    AboutUsPresenter presenter;

    @InjectView(R.id.txtAboutUs)
    TextView txtAboutUs;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "About Us";

    public static AboutUsFragment newInstance() {

        AboutUsFragment fragment = new AboutUsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new AboutUsModule(this)).inject(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.aboutus, container, false);
        ButterKnife.inject(this, view);

        AboutUs obj = new AboutUs();
        initiateLoading(getActivity());
        presenter.requestAboutUsInfo(obj);
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();

        AnalyticsApplication.sendScreenView(SCREEN_LABEL);
        Log.e("Tracker", SCREEN_LABEL);

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


    @Override
    public void onRequestSuccess(AboutUsReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if(status) {
            txtAboutUs.setText(Html.fromHtml(obj.getData().replaceAll("</br>", "<p>")));
        }
    }
}
