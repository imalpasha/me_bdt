package com.metech.tbd.ui.Activity.Login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.metech.tbd.application.AnalyticsApplication;
import com.metech.tbd.MainController;
import com.metech.tbd.application.MainApplication;
import com.metech.tbd.MainFragmentActivity;
import com.metech.tbd.R;
import com.metech.tbd.ui.Activity.Profile.ProfileActivity;
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

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import io.realm.RealmResults;

public class LoginFragment extends BaseFragment implements LoginPresenter.LoginView,Validator.ValidationListener {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;


    @InjectView(R.id.txtLoginBtn) TextView txtLoginBtn;
    //LoginButton loginButton;

    /*@InjectView(R.id.registerBtn) Button registerButton;


    @InjectView(R.id.txtForgotPassword)
    TextView txtForgotPassword;

    @NotEmpty(sequence = 1)
    @Order(1)
    @InjectView(R.id.txtLoginEmail) EditText txtLoginEmail;


    @NotEmpty(sequence = 1)
    @Length(sequence = 2, min = 6, max = 16 , message = "Must be at least 8 and maximum 16 characters")
    @Order(2)
    @InjectView(R.id.txtLoginPassword) EditText txtLoginPassword;
*/
    private AlertDialog dialog;
    private SharedPrefManager pref;
    private String storePassword,storeUsername;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private boolean resetPassword = false;

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

        // Validator
        mValidator = new Validator(this);
        mValidator.setValidationListener(this);
        mValidator.setValidationMode(Validator.Mode.BURST);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());

        txtLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profile();
            }
        });

        //set edittext password input type
       /* txtLoginPassword.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "btnLogin");
                mValidator.validate();
                Utils.hideKeyboard(getActivity(), v);
            }
        });

        txtForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnalyticsApplication.sendEvent("Click", "forget password");
                forgotPassword();
            }
        });
*/
        return view;
    }

    public void loginFromFragment(String username,String password){
        /*Start Loading*/
        initiateLoading(getActivity());
        LoginRequest data = new LoginRequest();
        data.setUsername(username);
        data.setPassword(password);

        storeUsername = username;
        storePassword = password;
        resetPassword = false;
        presenter.loginFunction(data);
    }


    /*Public-Inner Func*/
    public void profile()
    {
        Intent profilePage = new Intent(getActivity(), ProfileActivity.class);
        getActivity().startActivity(profilePage);

    }

    public void homepage()
    {
        Intent loginPage = new Intent(getActivity(), HomeActivity.class);
        getActivity().startActivity(loginPage);
        getActivity().finish();

    }


    @Override
    public void onLoginSuccess(LoginReceive obj) {

        //Log.e("STATUS",obj.getStatus());
        /*Dismiss Loading*/
        dismissLoading();
        pref.setUserEmail(storeUsername);
        pref.setUserPassword(storePassword);
        RealmObjectController.clearCachedResult(getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            RealmObjectController.deleteRealmFile(getActivity());
            if(obj.getUser_info().getCustomer_number() != null){
                pref.setCustomerNumber(obj.getUser_info().getCustomer_number());
            }
            if(obj.getUser_info().getPersonID() != null){
                pref.setPersonID(obj.getUser_info().getPersonID());
            }

            pref.setLoginStatus("Y");
            pref.setNewsletterStatus(obj.getUser_info().getNewsletter());
            pref.setSignatureToLocalStorage(obj.getUser_info().getSignature());
            pref.setUsername(obj.getUser_info().getFirst_name());

            Gson gsonUserInfo = new Gson();
            String userInfo = gsonUserInfo.toJson(obj.getUser_info());
            pref.setUserInfo(userInfo);
            //setSuccessDialog(getActivity(), obj.getMessage(), SearchFlightActivity.class);
            homepage();
        }
    }

    /*IF Login Failed*/
    @Override
    public void onLoginFailed(String obj) {
        Crouton.makeText(getActivity(), obj, Style.ALERT).show();
        setAlertDialog(getActivity(),obj,"Login Error");

    }

    @Override
    public void onRequestPasswordSuccess(ForgotPasswordReceive obj) {
        dismissLoading();

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            RealmObjectController.clearCachedResult(getActivity());
            setSuccessDialog(getActivity(), obj.getMessage(),null,"Success!");
        }

    }

    /* Validation Success - Start send data to server */
    @Override
    public void onValidationSucceeded() {
       // loginFromFragment(txtLoginEmail.getText().toString(),
       //         AESCBC.encrypt(App.KEY, App.IV, txtLoginPassword.getText().toString()));

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
            } else {
                croutonAlert(getActivity(), splitErrorMsg[0]);
            }
        }
    }

    /*PopupActivity Forgot Password*/
    public void forgotPassword(){

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.forgot_password_screen, null);
        Button cont = (Button)myView.findViewById(R.id.btncontinue);

        final EditText editEmail = (EditText)myView.findViewById(R.id.editTextemail_login);
        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(editEmail.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "Email is required", Toast.LENGTH_LONG).show();

            }
            //else if (!editEmail.getText().toString().matches(emailPattern)) {
             //   Toast.makeText(getActivity(), "Invalid Email", Toast.LENGTH_LONG).show();
            //}
            else{
                    requestForgotPassword(editEmail.getText().toString(),"");
                    dialog.dismiss();
                }

            }

        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

    }


    public void requestForgotPassword(String username,String signature){
        initiateLoading(getActivity());
        PasswordRequest data = new PasswordRequest();
        data.setEmail(username);
        data.setSignature(signature);

        resetPassword = true;
        presenter.forgotPassword(data);
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

        if(resetPassword){
            if(result.size() > 0){
                Gson gson = new Gson();
                ForgotPasswordReceive obj = gson.fromJson(result.get(0).getCachedResult(), ForgotPasswordReceive.class);
                onRequestPasswordSuccess(obj);
            }
        }else{
            if(result.size() > 0){
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
