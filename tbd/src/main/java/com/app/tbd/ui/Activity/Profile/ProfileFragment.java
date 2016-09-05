package com.app.tbd.ui.Activity.Profile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Homepage.HomeActivity;
import com.app.tbd.ui.Activity.Register.RegisterActivity;
import com.app.tbd.ui.Model.Receive.LoginFacebookReceive;
import com.app.tbd.ui.Model.Receive.LogoutReceive;
import com.app.tbd.ui.Model.Request.LogoutRequest;
import com.app.tbd.ui.Module.ProfileModule;
import com.app.tbd.ui.Presenter.LoginPresenter;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.google.gson.Gson;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.Login.LoginActivity;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.LoginReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.app.tbd.utils.SharedPrefManager;
import com.mobsandgeeks.saripaar.Validator;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProfileFragment extends BaseFragment implements ProfilePresenter.ProfileView {

    @Inject
    ProfilePresenter presenter;

    @InjectView(R.id.imgUserDP)
    ImageView imgUserDP;

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    @InjectView(R.id.txtBigUsername)
    TextView txtBigUsername;

    @InjectView(R.id.txtUserBigID)
    TextView txtUserBigID;

    @InjectView(R.id.txtLogout)
    TextView txtLogout;

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private ProgressDialog progress;

    public static ProfileFragment newInstance() {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new ProfileModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.inject(this, view);
        dataSetup();


        //maskUserDP();
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

                initiateDefaultLoading(progress,getActivity());
                presenter.onRequestLogout(logoutRequest);


            }
        });
        return view;
    }


    @Override
    public void onLogoutReceive(LogoutReceive obj) {

        dismissDefaultLoading(progress, getActivity());

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            Intent profilePage = new Intent(getActivity(), LoginActivity.class);
            getActivity().startActivity(profilePage);
            getActivity().finish();

        }
    }


    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        txtUserName.setText(obj.getFirstName());
        txtBigUsername.setText(obj.getFirstName() + " " + obj.getLastName());
        txtUserBigID.setText("BIG ID : " + obj.getCustomerNumber());

    }

    public void maskUserDP() {

        //mask dp profile
        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.tbd_dp2);
        Bitmap mask = BitmapFactory.decodeResource(getResources(), R.drawable.dp_mask);
        Bitmap result = Bitmap.createBitmap(mask.getWidth(), mask.getHeight(), Bitmap.Config.ARGB_8888);

        Bitmap resized = Bitmap.createScaledBitmap(original, mask.getWidth(), mask.getHeight(), true);


        Canvas mCanvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mCanvas.drawBitmap(resized, 0, 0, null);
        mCanvas.drawBitmap(mask, 0, 0, paint);
        paint.setXfermode(null);
        imgUserDP.setImageBitmap(result);
        imgUserDP.setScaleType(ImageView.ScaleType.CENTER);
        //imgUserDP.setBackgroundResource(R.drawable.tbd_image_border);

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
