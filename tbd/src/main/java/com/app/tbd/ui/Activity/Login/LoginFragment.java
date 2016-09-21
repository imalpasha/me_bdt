package com.app.tbd.ui.Activity.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.tbd.ui.Model.JSON.UserFacebookInfo;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.TBD.LoginFacebookReceive;
import com.app.tbd.ui.Model.Receive.UploadPhotoReceive;
import com.app.tbd.ui.Model.Receive.UserPhotoReceive;
import com.app.tbd.ui.Model.Request.TBD.LoginFacebookRequest;
import com.app.tbd.ui.Model.Request.UserPhotoRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Activity.ForgotPassword.ForgotPasswordActivity;
import com.app.tbd.ui.Activity.Profile.ProfileActivity;
import com.app.tbd.ui.Activity.Register.RegisterActivity;
import com.app.tbd.ui.Model.Receive.ForgotPasswordReceive;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Module.LoginModule;
import com.app.tbd.ui.Realm.Cached.CachedResult;
import com.app.tbd.ui.Model.Request.TBD.LoginRequest;
import com.app.tbd.ui.Presenter.LoginPresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;
import com.google.gson.Gson;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class LoginFragment extends BaseFragment implements LoginPresenter.LoginView, Validator.ValidationListener {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.txtLoginBtn)
    Button txtLoginBtn;

    @NotEmpty(sequence = 1)
    @Email(sequence = 2)
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
    TextView btnLogin;

    private String facebookInfoGSON;

    private AlertDialog dialog;
    private SharedPrefManager pref;
    private String storePassword, storeUsername;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private boolean resetPassword = false;
    private ProgressDialog progress;
    CallbackManager callbackManager;


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

        getActivity().setTitle("Member Login");

        progress = new ProgressDialog(getActivity());
        pref = new SharedPrefManager(getActivity());

        txtLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mValidator.validate();
                //hideKeyboard();
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotPassword();
            }
        });

        login_button.setReadPermissions(Arrays.asList("email"));
        login_button.setFragment(this);

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

        //LoginManager.getInstance().logOut();

        // Callback registration
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //friendProfilePicture.setProfileId(loginResult.getAccessToken().getUserId());

                /*try {
                    Profile profile = Profile.getCurrentProfile();
                    profile.ge
                    //shakeDevice.setText(profile.getName());
                } catch (Exception e) {

                }*/
                final String userToken = loginResult.getAccessToken().getToken();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        Log.i("LoginActivity", object.toString());
                        try {
                            String fbEmail = response.getJSONObject().get("email").toString();
                            String fbFirstName = response.getJSONObject().get("first_name").toString();
                            String fbLastName = response.getJSONObject().get("last_name").toString();
                            String fbGender = response.getJSONObject().get("gender").toString();
                            String fbBirthday = response.getJSONObject().get("birthday").toString();

                            UserFacebookInfo userFacebookInfo = new UserFacebookInfo();
                            userFacebookInfo.setUserEmail(fbEmail);
                            userFacebookInfo.setUserFirstName(fbFirstName);
                            userFacebookInfo.setUserLastName(fbLastName);
                            userFacebookInfo.setUserGender(fbGender);
                            userFacebookInfo.setUserBirthday(fbBirthday);

                            //convert information to gson
                            Gson gsonUserInfo = new Gson();
                            facebookInfoGSON = gsonUserInfo.toJson(userFacebookInfo);


                            LoginFacebookRequest loginFacebookRequest = new LoginFacebookRequest();
                            loginFacebookRequest.setEmail(fbEmail);
                            loginFacebookRequest.setToken(userToken);
                            presenter.onCheckFBLogin(loginFacebookRequest);

                        } catch (Exception e) {

                        }


                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();


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
        initiateLoading(getActivity());

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

        //dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            RealmObjectController.clearCachedResult(getActivity());
            pref.setLoginStatus("Y");
            pref.setUsername(obj.getUserName());
            pref.setToken(obj.getToken());
            pref.setUserPassword(txtLoginPassword.getText().toString());

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj);
            RealmObjectController.saveUserInformation(getActivity(), userInfo);

            UserPhotoRequest userPhotoRequest = new UserPhotoRequest();
            userPhotoRequest.setToken(obj.getToken());
            userPhotoRequest.setUserName(obj.getUserName());
            presenter.onRequestUserPhoto(userPhotoRequest);
            //success login -> homepage*/
            //homepage();
        }else{
            dismissLoading();
        }
    }

    @Override
    public void onRequestUserPhotoSuccess(UserPhotoReceive obj) {

        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            //add URL to REALM User info
            Realm realm = RealmObjectController.getRealmInstance(getActivity());
            final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
            LoginReceive loginReceive = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

            loginReceive.setProfile_URL(obj.getURL());

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(loginReceive);
            RealmObjectController.saveUserInformation(getActivity(), userInfo);

            homepage();
        } else {

            homepage();
        }
    }


    @Override
    public void onCheckFBLoginSuccess(LoginFacebookReceive obj) {

        dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            if (true) {
                // redirect to homepage
                Intent homepage = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(homepage);
                getActivity().finish();
            } else {
                //register
                Intent registerPage = new Intent(getActivity(), RegisterActivity.class);
                registerPage.putExtra("FACEBOOK_USER_INFORMATION", facebookInfoGSON);
                getActivity().startActivity(registerPage);
            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("resultCode", Integer.toString(resultCode));
        callbackManager.onActivityResult(requestCode, resultCode, data);
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
