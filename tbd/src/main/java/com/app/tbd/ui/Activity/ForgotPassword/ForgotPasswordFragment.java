package com.app.tbd.ui.Activity.ForgotPassword;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Module.ForgotPasswordModule;
import com.app.tbd.ui.Realm.Cached.CachedResult;
import com.app.tbd.ui.Model.Request.PasswordRequest;
import com.app.tbd.ui.Presenter.LoginPresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class ForgotPasswordFragment extends BaseFragment implements LoginPresenter.ForgotPasswordView, Validator.ValidationListener {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
    @InjectView(R.id.txtResetThisEmail)
    EditText txtResetThisEmail;

    @Order(1)
    @InjectView(R.id.btnResetPassword)
    Button btnResetPassword;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private ProgressDialog progress;

    public static ForgotPasswordFragment newInstance() {

        ForgotPasswordFragment fragment = new ForgotPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new ForgotPasswordModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.forgot_password_screen, container, false);
        ButterKnife.inject(this, view);

        progress = new ProgressDialog(getActivity());

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });

        return view;
    }


    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {

        requestForgotPassword(txtResetThisEmail.getText().toString());

    }

    /* Validation Failed - Toast Error */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);

            /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }
        }
    }


    public void requestForgotPassword(String username) {

        initiateDefaultLoading(progress, getActivity());
        PasswordRequest data = new PasswordRequest();
        data.setUserName(username);

        presenter.forgotPassword(data);
    }

    @Override
    public void onForgotPassword(ForgotPasswordReceive obj) {

        dismissDefaultLoading(progress, getActivity());
        RealmObjectController.clearCachedResult(getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            setSuccessDialog(getActivity(), getResources().getString(R.string.reset_password_success_send), LoginActivity.class, "Success!");

        }

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
        RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());

        if (result.size() > 0) {
            Gson gson = new Gson();
            ForgotPasswordReceive obj = gson.fromJson(result.get(0).getCachedResult(), ForgotPasswordReceive.class);
            onForgotPassword(obj);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
