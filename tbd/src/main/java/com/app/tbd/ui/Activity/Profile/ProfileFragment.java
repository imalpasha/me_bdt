package com.app.tbd.ui.Activity.Profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Activity.Profile.BigPoint.BigPointBaseActivity;
import com.app.tbd.ui.Activity.Profile.Option.OptionsActivity;
import com.app.tbd.ui.Activity.Profile.UserProfile.MyProfileActivity;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.TBD.BigPointRequest;
import com.app.tbd.ui.Realm.Cached.CachedBigPointResult;
import com.app.tbd.ui.Model.Request.ViewUserRequest;
import com.app.tbd.ui.Module.ProfileModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.app.tbd.utils.Utils;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
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
    private LoginReceive loginReceive;
    private String gsonBigPoint;
    private Boolean bigPointClickable = false;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private AlertDialog dialog;
    private Uri mImageCaptureUri;
    private final int PICK_FROM_CAMERA = 1;
    private final int CROP_FROM_CAMERA = 2;
    private final int PICK_FROM_FILE = 3;

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

        getActivity().setTitle(getResources().getString(R.string.tbd_my_profile));

        maskUserDP();

        imgUserDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });

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
                if (bigPointClickable) {
                    Intent optionsPage = new Intent(getActivity(), BigPointBaseActivity.class);
                    optionsPage.putExtra("BIG_POINT", gsonBigPoint);
                    getActivity().startActivity(optionsPage);
                }
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
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
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

        //Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (obj.getStatus().equals("OK")) {

            bigPointClickable = true;
            profileBigPointClickLayout.setClickable(true);
            txtBigPoint.setText(obj.getAvailablePts());
            bigPoint = obj.getAvailablePts();

            //convert big point to string -> sent to big point detail
            Gson gsonUserInfo = new Gson();
            gsonBigPoint = gsonUserInfo.toJson(obj);

        } else {
            txtBigPoint.setText(getResources().getString(R.string.failed_load));
        }
    }


    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());
        progress = new ProgressDialog(getActivity());
        captureImageInitialization();

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        loginReceive = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        txtUserName.setText(loginReceive.getFirstName() + " " + loginReceive.getLastName());
        txtBigUsername.setText(loginReceive.getFirstName() + " " + loginReceive.getLastName());
        txtUserBigID.setText("BIG ID : " + loginReceive.getCustomerNumber());
        customerNumber = loginReceive.getCustomerNumber();

        loadBigPointData(loginReceive);

    }

    public void loadBigPointData(LoginReceive obj) {

        txtBigPoint.setText(getResources().getString(R.string.register_general_loading));
        pref.setBigPointRequestStatus("Y");

        BigPointRequest bigPointRequest = new BigPointRequest();
        bigPointRequest.setTicketId(obj.getTicketId());
        bigPointRequest.setUserName(obj.getUserName());
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


    //* ----------------- SELECT IMAGE - ADD TO DIFFERENT CLASS LATER ------------------*//
    @SuppressWarnings("null")
    private void captureImageInitialization() {
        /**
         * a selector dialog to display two image source options, from camera
         * �Take from camera� and from existing files �Select from gallery�
         */

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.add_profile_image, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        Button btnGallery = (Button) myView.findViewById(R.id.btn_select_gallery);
        Button btnCamera = (Button) myView.findViewById(R.id.btn_take_camera);
        Button btnRemove = (Button) myView.findViewById(R.id.btn_remove_img);

        dialog = builder.create();

        btnRemove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // Intent intent = new Intent();
                // intent.setType("image/*");
                // intent.setAction(Intent.ACTION_GET_CONTENT);
                // startActivityForResult(Intent.createChooser(intent,
                // "Complete action using"), PICK_FROM_FILE);
                // dialog.dismiss();
                //editImg.setProfileImg("");
                //MyProfileSoap.editUserProfiletwo(MyProfileView.this, onTaskStartEditImg, onTaskCompleteImg, sessionId, deviceId, editImg);
                //profilePic.setImageDrawable(MyProfileView.this.getResources().getDrawable(R.drawable.add_image3));
                dialog.dismiss();

            }
        });

        btnGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.tbd_big_point_expiry_date)), PICK_FROM_FILE);
                dialog.dismiss();

            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);

                try {
                    intent.putExtra("return-data", true);

                    startActivityForResult(intent, PICK_FROM_CAMERA);
                    dialog.dismiss();

                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = 1070;
        dialog.getWindow().setAttributes(lp);

    }

    /* __________Crop Adapter______________*/
    public class CropOptionAdapter extends ArrayAdapter<CropOption> {
        private ArrayList<CropOption> mOptions;
        private LayoutInflater mInflater;

        public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
            super(context, R.layout.crop_selector, options);
            mOptions = options;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup group) {
            if (convertView == null)
                convertView = mInflater.inflate(R.layout.crop_selector, null);

            CropOption item = mOptions.get(position);

            if (item != null) {
                ((ImageView) convertView.findViewById(R.id.iv_icon)).setImageDrawable(item.icon);
                ((TextView) convertView.findViewById(R.id.tv_name)).setText(item.title);

                return convertView;
            }

            return null;
        }
    }

    public class CropOption {
        public CharSequence title;
        public Drawable icon;
        public Intent appIntent;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if (resultCode != 0)
        //    return;

        switch (requestCode) {
            case PICK_FROM_CAMERA:
                /**
                 * After taking a picture, do the crop
                 */
                doCrop();

                break;

            case PICK_FROM_FILE:
                /**
                 * After selecting image from files, save the selected path
                 */
                mImageCaptureUri = data.getData();

                doCrop();

                break;

            case CROP_FROM_CAMERA:
                Bundle extras = data.getExtras();
                /**
                 * After cropping the image, get the bitmap of the cropped image
                 * and
                 * display it on imageview.
                 */
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    //imgUserDP.setImageBitmap(photo);

                    imgUserDP.setImageDrawable(new BitmapDrawable(getActivity().getResources(), photo));
                    imgUserDP.setScaleType(ImageView.ScaleType.FIT_XY);

                    //profileImgBitmap = photo;
                    //editImg.setProfileImg(Utils.bitmapToBase64(photo));

                }

                File f = new File(mImageCaptureUri.getPath());
                // Log.e("Path",mImageCaptureUri.getPath());

                if (f.exists())
                    f.delete();

                break;

        }
    }

    private void doCrop() {
        final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
        /**
         * Open image crop app by starting an intent
         * �com.android.camera.action.CROP�.
         */
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setType("image/*");

        /**
         * Check if there is image cropper app installed.
         */
        List<ResolveInfo> list = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        /**
         * Specify the image path, crop dimension and scale
         */
        intent.setData(mImageCaptureUri);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", true);

        Intent i = new Intent(intent);
        ResolveInfo res = list.get(0);

        i.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

        startActivityForResult(i, CROP_FROM_CAMERA);
        /**
         * There is posibility when more than one image cropper app exist, so we
         * have to check for it first. If there is only one app, open then app.
         */

        CropOptionAdapter adapter = new CropOptionAdapter(getActivity(), cropOptions);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

                if (mImageCaptureUri != null) {
                    mImageCaptureUri = null;
                }
            }
        });

    }


    /* END */

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
        checkBigPointResult();

    }

    public void checkBigPointResult() {
        RealmResults<CachedBigPointResult> result = RealmObjectController.getCachedBigPointResult(MainFragmentActivity.getContext());
        if (result.size() > 0) {
            Gson gson = new Gson();
            BigPointReceive obj = gson.fromJson(result.get(0).getCachedResult(), BigPointReceive.class);
            RealmObjectController.clearBigPointCached(getActivity());

            onBigPointReceive(obj);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

    public void exitApp() {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText("EXIT")
                .setContentText("Confirm exit?")
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setConfirmText("Close")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        getActivity().finish();
                        System.exit(0);

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

    }
}
