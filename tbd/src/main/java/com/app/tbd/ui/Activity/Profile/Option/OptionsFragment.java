package com.app.tbd.ui.Activity.Profile.Option;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.tbd.R;
import com.app.tbd.application.MainApplication;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Model.Receive.TBD.LogoutReceive;
import com.app.tbd.ui.Model.Request.TBD.LogoutRequest;
import com.app.tbd.ui.Module.OptionModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OptionsFragment extends BaseFragment implements ProfilePresenter.OptionView {
    private int fragmentContainerId;

    @Inject
    ProfilePresenter presenter;

    @InjectView(R.id.txtLogout)
    LinearLayout txtLogout;

    @InjectView(R.id.resetPasswordLayout)
    LinearLayout resetPasswordLayout;

    private SharedPrefManager pref;
    private ProgressDialog progress;

    public static OptionsFragment newInstance() {

        OptionsFragment fragment = new OptionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new OptionModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.options, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pref.clearLoginStatus();
                HashMap<String, String> initTicketId = pref.getTicketId();
                String ticketId = initTicketId.get(SharedPrefManager.TICKET_ID);

                HashMap<String, String> initUserName = pref.getUsername();
                String userName = initUserName.get(SharedPrefManager.USERNAME);

                LogoutRequest logoutRequest = new LogoutRequest();
                logoutRequest.setTicketId(ticketId);
                logoutRequest.setUsername(userName);

                initiateLoading(getActivity());
                presenter.onRequestLogout(logoutRequest);


            }
        });

        resetPasswordLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent resetPassword = new Intent(getActivity(), ResetPasswordActivity.class);
                getActivity().startActivity(resetPassword);

            }
        });


        return view;
    }


    @Override
    public void onLogoutReceive(LogoutReceive obj) {

        dismissLoading();
        Intent profilePage = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(profilePage);
        getActivity().finish();
        pref.setLoginStatus("N");

        /*Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Intent profilePage = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(profilePage);
            getActivity().finish();

        }*/
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

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}


