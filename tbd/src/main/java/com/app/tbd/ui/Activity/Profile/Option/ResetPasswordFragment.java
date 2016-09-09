package com.app.tbd.ui.Activity.Profile.Option;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Profile.Option.OptionsActivity;
import com.app.tbd.ui.Model.Receive.ResetPasswordReceive;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Request.RegisterRequest;
import com.app.tbd.ui.Model.Request.ResetPasswordRequest;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Module.ProfileModule;
import com.app.tbd.ui.Module.ResetPasswordModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.google.gson.Gson;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.app.tbd.utils.SharedPrefManager;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ResetPasswordFragment extends BaseFragment implements ProfilePresenter.ResetPasswordView, Validator.ValidationListener {

    @Inject
    ProfilePresenter presenter;

    @NotEmpty(sequence = 1)
    @InjectView(R.id.txtOldPassword)
    EditText txtOldPassword;

    @NotEmpty(sequence = 1)
    @Password(sequence = 2, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE, message = "Password invalid.")
    @Length(sequence = 3, min = 8, max = 15, message = "Must be at least 8 and maximum 16 characters")
    @InjectView(R.id.txtNewPassword)
    EditText txtNewPassword;

    @NotEmpty(sequence = 1)
    @ConfirmPassword(sequence = 2)
    @InjectView(R.id.txtConfirmPassword)
    EditText txtConfirmPassword;

    @InjectView(R.id.btnResetPasswordCont)
    Button btnResetPasswordCont;


    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private ProgressDialog progress;

    private String userName;
    private String ticketID;

    public static ResetPasswordFragment newInstance() {

        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new ResetPasswordModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reset_password, container, false);
        ButterKnife.inject(this, view);
        dataSetup();

        btnResetPasswordCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mValidator.validate();
            }
        });
        return view;
    }

    @Override
    public void onValidationSucceeded() {

        initiateLoading(getActivity());

        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest();
        resetPasswordRequest.setOldPassword("Password123");
        resetPasswordRequest.setNewPassword(txtNewPassword.getText().toString());
        resetPasswordRequest.setUserName(userName);
        resetPasswordRequest.setTicketId(ticketID);

        presenter.onRequestResetPassword(resetPasswordRequest);

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {

        for (ValidationError error : errors) {

            View view = error.getView();

            setShake(view);
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            } else if (view instanceof TextView) {
                ((TextView) view).setError(splitErrorMsg[0]);
            }
        }
        croutonAlert(getActivity(), getResources().getString(R.string.fill_emtpy_field));

    }


    @Override
    public void onResetPasswordReceive(ResetPasswordReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            setSuccessDialogNoFinish(getActivity(), obj.getMessage(), null, getResources().getString(R.string.success));
        }
    }


    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        userName = obj.getUserName();
        ticketID = obj.getTicketId();

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
