package com.metech.tbd.ui.Activity.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.ForgotPassword.ForgotPasswordActivity;
import com.metech.tbd.ui.Activity.Profile.ProfileActivity;
import com.metech.tbd.ui.Activity.Register.RegisterActivity;
import com.metech.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.metech.tbd.ui.Model.Receive.LoginReceive;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Activity.Homepage.HomeActivity;
import com.metech.tbd.ui.Module.LoginModule;
import com.metech.tbd.ui.Model.Request.CachedResult;
import com.metech.tbd.ui.Model.Request.LoginRequest;
import com.metech.tbd.ui.Model.Request.PasswordRequest;
import com.metech.tbd.ui.Presenter.LoginPresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.metech.tbd.utils.SharedPrefManager;
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
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;

public class LoginFragment extends BaseFragment implements LoginPresenter.LoginView, Validator.ValidationListener {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.txtLoginBtn)
    TextView txtLoginBtn;

    @NotEmpty(sequence = 1)
    @Email
    @Order(1)
    @InjectView(R.id.txtLoginEmail)
    EditText txtLoginEmail;

    @NotEmpty(sequence = 1)
    @Order(2)
    @InjectView(R.id.txtLoginPassword)
    EditText txtLoginPassword;

    @InjectView(R.id.txtForgotPassword)
    TextView txtForgotPassword;

    @InjectView(R.id.dummyFBButton)
    Button dummyFBButton;

    @InjectView(R.id.login_button)
    LoginButton login_button;

    @InjectView(R.id.btnLogin)
    Button btnLogin;

    private AlertDialog dialog;
    private SharedPrefManager pref;
    private String storePassword, storeUsername;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private boolean resetPassword = false;
    private ProgressDialog progress;
    private CallbackManager callbackManager;


    public static LoginFragment newInstance() {

        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());

        callbackManager = CallbackManager.Factory.create();


        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.inject(this, view);

        progress = new ProgressDialog(getActivity());
        pref = new SharedPrefManager(getActivity());

        txtLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        dummyFBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fb login
                login_button.performClick();
                Log.e("?", "?");
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilePage = new Intent(getActivity(), RegisterActivity.class);
                getActivity().startActivity(profilePage);
            }
        });


        LoginManager.getInstance().logOut();

        login_button.setReadPermissions("email");
        login_button.setFragment(this);

        // Callback registration
        login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //friendProfilePicture.setProfileId(loginResult.getAccessToken().getUserId());

                /*try{
                    Profile profile = Profile.getCurrentProfile();
                    shakeDevice.setText(profile.getName());
                }catch (Exception e){

                }*/
                Log.e("?", "1");

                Log.e("FB ID", loginResult.getAccessToken().getUserId());
                Log.e("FB TOKEN", loginResult.getAccessToken().getToken());
                Log.e("Another ID", loginResult.getAccessToken().getApplicationId());
            }

            @Override
            public void onCancel() {
                // App code
                Log.e("?", "2");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("Exception", exception.getMessage());
                Log.e("?", "3");
            }
        });


        return view;
    }

    public void forgotPassword() {
        Intent profilePage = new Intent(getActivity(), ForgotPasswordActivity.class);
        getActivity().startActivity(profilePage);
    }

    public void loginFromFragment(String username, String password) {
        /*Start Loading*/
        //initiateLoading(getActivity());
        initiateDefaultLoading(progress, getActivity());

        LoginRequest loginData = new LoginRequest();
        loginData.setUsername(username);
        loginData.setPassword(password);

        presenter.onLogin(loginData);


    }

    /*Public-Inner Func*/
    public void profile() {
        Intent profilePage = new Intent(getActivity(), ProfileActivity.class);
        getActivity().startActivity(profilePage);
        getActivity().finish();

    }

    public void homepage() {
        Intent loginPage = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(loginPage);
        getActivity().finish();

    }

    @Override
    public void onLoginSuccess(LoginReceive obj) {

        dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            RealmObjectController.clearCachedResult(getActivity());
            pref.setLoginStatus("Y");
            pref.setUsername(obj.getUserName());

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj);
            RealmObjectController.saveUserInformation(getActivity(), userInfo);

            //success login -> homepage*/
            profile();
        }
    }

    @Override
    public void onRequestPasswordSuccess(ForgotPasswordReceive obj) {

        dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            RealmObjectController.clearCachedResult(getActivity());
            setSuccessDialog(getActivity(), obj.getMessage(), null, "Success!");
        }

    }

    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {

        loginFromFragment(txtLoginEmail.getText().toString(), txtLoginPassword.getText().toString());

    }

    /* Validation Failed - Toast Error */
    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        boolean firstView = true;

        for (ValidationError error : errors) {
            View view = error.getView();
            setShake(view);
            view.setFocusable(true);
            view.requestFocus();

            /* Split Error Message. Display first sequence only */
            String message = error.getCollatedErrorMessage(getActivity());
            String splitErrorMsg[] = message.split("\\r?\\n");

            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(splitErrorMsg[0]);
            }

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

        if (resetPassword) {
            if (result.size() > 0) {
                Gson gson = new Gson();
                ForgotPasswordReceive obj = gson.fromJson(result.get(0).getCachedResult(), ForgotPasswordReceive.class);
                onRequestPasswordSuccess(obj);
            }
        } else {
            if (result.size() > 0) {
                Gson gson = new Gson();
                LoginReceive obj = gson.fromJson(result.get(0).getCachedResult(), LoginReceive.class);
                onLoginSuccess(obj);
            }
        }


        //check for reset password

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
