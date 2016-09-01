package com.metech.tbd.ui.Activity.Profile;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Model.JSON.UserInfoJSON;
import com.metech.tbd.ui.Model.Receive.LoginReceive;
import com.metech.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.metech.tbd.ui.Model.Request.BoardingPassObj;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.Validator;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProfileFragment extends BaseFragment {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @InjectView(R.id.imgUserDP)
    ImageView imgUserDP;

    @InjectView(R.id.txtUserName)
    TextView txtUserName;

    @InjectView(R.id.txtBigUsername)
    TextView txtBigUsername;

    @InjectView(R.id.txtUserBigID)
    TextView txtUserBigID;


    //@Inject
    //LoginPresenter presenter;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";

    public static ProfileFragment newInstance() {

        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile, container, false);
        ButterKnife.inject(this, view);
        dataSetup();

        //maskUserDP();

        return view;
    }

    public void dataSetup() {

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        txtUserName.setText(obj.getFirstName());
        txtBigUsername.setText(obj.getFirstName() + " " + obj.getLastName());
        txtUserBigID.setText("BIG ID : "+obj.getCustomerNumber());

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
        // presenter.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();
    }
}
