package com.app.tbd.ui.Activity.Profile.BigPoint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ExpiryDateFragment extends BaseFragment {

    //@Inject
    //ProfilePresenter presenter;

    @InjectView(R.id.txtExpiryDate1)
    TextView txtExpiryDate1;

    @InjectView(R.id.txtExpiryDate2)
    TextView txtExpiryDate2;

    @InjectView(R.id.txtExpiryDate3)
    TextView txtExpiryDate3;

    @InjectView(R.id.txtExpiryDate4)
    TextView txtExpiryDate4;

    @InjectView(R.id.txtExpiryDate5)
    TextView txtExpiryDate5;

    @InjectView(R.id.txtExpiryDate6)
    TextView txtExpiryDate6;

    @InjectView(R.id.txtPoints1)
    TextView txtPoints1;

    @InjectView(R.id.txtPoints2)
    TextView txtPoints2;

    @InjectView(R.id.txtPoints3)
    TextView txtPoints3;

    @InjectView(R.id.txtPoints4)
    TextView txtPoints4;

    @InjectView(R.id.txtPoints5)
    TextView txtPoints5;

    @InjectView(R.id.txtPoints6)
    TextView txtPoints6;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private String customerNumber;

    public static ExpiryDateFragment newInstance(Bundle bundle) {

        ExpiryDateFragment fragment = new ExpiryDateFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.big_point_expiry_date, container, false);
        ButterKnife.inject(this, view);

        Bundle bundle = getArguments();
        String transactionHistory = bundle.getString("TRANSACTION_HISTORY");

        Gson gson = new Gson();
        TransactionHistoryReceive obj = gson.fromJson(transactionHistory, TransactionHistoryReceive.class);

        setData(obj);

        return view;
    }

    public void setData(TransactionHistoryReceive obj) {

        Log.e("obj.getExpiryPts1()",obj.getExpiryPts1());
        txtExpiryDate1.setText(obj.getExpiryPts1());
        txtExpiryDate2.setText(obj.getExpiryPts2());
        txtExpiryDate3.setText(obj.getExpiryPts3());
        txtExpiryDate4.setText(obj.getExpiryPts4());
        txtExpiryDate5.setText(obj.getExpiryPts5());
        txtExpiryDate6.setText(obj.getExpiryPts6());

        txtPoints1.setText(obj.getExpiryMonth1());
        txtPoints2.setText(obj.getExpiryMonth2());
        txtPoints3.setText(obj.getExpiryMonth3());
        txtPoints4.setText(obj.getExpiryMonth4());
        txtPoints5.setText(obj.getExpiryMonth5());
        txtPoints6.setText(obj.getExpiryMonth6());

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
