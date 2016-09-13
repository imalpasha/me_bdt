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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Profile.BigPoint.BigPointBaseActivity;
import com.app.tbd.ui.Activity.Profile.Option.OptionsActivity;
import com.app.tbd.ui.Activity.Profile.UserProfile.MyProfileActivity;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.app.tbd.ui.Module.ProfileModule;
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


    @InjectView(R.id.profile_options)
    LinearLayout profile_options;

    @InjectView(R.id.txtBigPoint)
    TextView txtBigPoint;

    @InjectView(R.id.profileBigPointClickLayout)
    LinearLayout profileBigPointClickLayout;
    @InjectView(R.id.profile_myProfile)
    LinearLayout profile_myProfile;

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private ProgressDialog progress;
    private String customerNumber;
    private String userInfo;
    private String bigPoint;

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

        getActivity().setTitle("Profile");

        maskUserDP();

        profile_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionsPage = new Intent(getActivity(), OptionsActivity.class);
                getActivity().startActivity(optionsPage);
            }
        });

        profileBigPointClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionsPage = new Intent(getActivity(), BigPointBaseActivity.class);
                optionsPage.putExtra("BIG_POINT", bigPoint);
                getActivity().startActivity(optionsPage);
            }
        });

        profile_myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                initiateLoading(getActivity());
                //presenter
                loadProfileInfo();


            }
        });

        return view;
    }

    public void loadProfileInfo() {

        initiateLoading(getActivity());
        HashMap<String, String> initAuth = pref.getUsername();
        final String username = initAuth.get(SharedPrefManager.USERNAME);

        HashMap<String, String> initPassword = pref.getUserPassword();
        String password = initPassword.get(SharedPrefManager.PASSWORD);

        HashMap<String, String> initTicketId = pref.getTicketId();
        String ticketId = initTicketId.get(SharedPrefManager.TICKET_ID);

        ViewUserRequest data = new ViewUserRequest();
        data.setUserName(username);
        data.setPassword(password);
        data.setTicketId(ticketId);

        presenter.showFunction(data);

    }

    @Override
    public void onViewUserSuccess(ViewUserReceive obj) {
        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), "", getActivity());
        if (status) {

            userInfo = new Gson().toJson(obj);

            Intent myProfilePage = new Intent(getActivity(), MyProfileActivity.class);
            myProfilePage.putExtra("BIG_ID", customerNumber);
            myProfilePage.putExtra("USER_INFORMATION", userInfo);
            getActivity().startActivity(myProfilePage);

        }
    }

    @Override
    public void onBigPointReceive(BigPointReceive obj) {

        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            profileBigPointClickLayout.setClickable(true);
            txtBigPoint.setText(obj.getPtsBal());
            bigPoint = obj.getPtsBal();

        }
    }


    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        txtUserName.setText(obj.getFirstName() + " " + obj.getLastName());
        txtBigUsername.setText(obj.getFirstName() + " " + obj.getLastName());
        txtUserBigID.setText("BIG ID : " + obj.getCustomerNumber());
        customerNumber = obj.getCustomerNumber();

        loadBigPointData(obj);
    }

    public void loadBigPointData(LoginReceive obj) {

        txtBigPoint.setText(getResources().getString(R.string.register_general_loading));
        BigPointRequest bigPointRequest = new BigPointRequest();
        bigPointRequest.setHash(obj.getHash());
        bigPointRequest.setCustomerNumber(obj.getCustomerNumber());
        presenter.onRequestBigPoint(bigPointRequest);

    }

    public void maskUserDP() {

        //mask dp profile
        Bitmap original = BitmapFactory.decodeResource(getResources(), R.drawable.no_profile_image);
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
        //imgUserDP.setBackgroundResource(R.drawable.image_border);

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
