package com.app.tbd.ui.Activity.Profile.BigPoint;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.app.tbd.ui.Model.Receive.ViewUserReceive;
import com.app.tbd.ui.Model.Request.TransactionHistoryRequest;
import com.app.tbd.ui.Module.BigPointBaseModule;
import com.app.tbd.ui.Presenter.ProfilePresenter;
import com.google.gson.Gson;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class BigPointBaseFragment extends BaseFragment implements ProfilePresenter.BigPointView {

    @Inject
    ProfilePresenter presenter;

    @InjectView(R.id.txtBigShotPoint)
    TextView txtBigShotPoint;

    @InjectView(R.id.txtBigShotId)
    TextView txtBigShotId;

    @InjectView(R.id.bigPointExpiryDateLayout)
    LinearLayout bigPointExpiryDateLayout;

    @InjectView(R.id.transactionHistoryLayout)
    LinearLayout transactionHistoryLayout;

    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private String customerNumber;
    private BigPointReceive bigPointReceive;
    private String bigPointInfo;

    public static BigPointBaseFragment newInstance(Bundle bundle) {

        BigPointBaseFragment fragment = new BigPointBaseFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new BigPointBaseModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.big_point_profile, container, false);
        ButterKnife.inject(this, view);
        dataSetup();

        bigPointExpiryDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent expiryDate = new Intent(getActivity(), ExpiryDateActivity.class);
                expiryDate.putExtra("BIG_POINT_EXPIRY", bigPointInfo);
                getActivity().startActivity(expiryDate);
            }
        });

        transactionHistoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent home = new Intent(getActivity(), TransactionHistoryActivity.class);
                //getActivity().startActivity(home);
                loadTransactionHistory();
            }
        });

        return view;
    }

    public void loadTransactionHistory() {

        initiateLoading(getActivity());
        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        //Log.e(obj.getCustomerNumber(), obj.getHash());
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest();
        transactionHistoryRequest.setUserName(obj.getUserName());

        //modify this later
        transactionHistoryRequest.setStartDate("20160901");
        transactionHistoryRequest.setEndDate("20160914");

        //transactionHistoryRequest.setCustomerNumber(obj.getCustomerNumber());
        transactionHistoryRequest.setTicketId(obj.getTicketId());
        //transactionHistoryRequest.setHash(obj.getHash());
        presenter.onRequestTransactionHistory(transactionHistoryRequest);

    }

    /*public void loadExpiryDate() {

        initiateLoading(getActivity());

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        //Log.e(obj.getCustomerNumber(), obj.getHash());
        TransactionHistoryRequest transactionHistoryRequest = new TransactionHistoryRequest();
        transactionHistoryRequest.setCustomerNumber("2790025233");
        //transactionHistoryRequest.setCustomerNumber(obj.getCustomerNumber());
        transactionHistoryRequest.setHash("4f2767f04b731d6186a8fc2ba06b3eb7");
        //transactionHistoryRequest.setHash(obj.getHash());
        presenter.onRequestTransactionHistory(transactionHistoryRequest);
    }*/

    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());

        Bundle bundle = getArguments();
        bigPointInfo = bundle.getString("BIG_POINT");

        Gson gson = new Gson();
        bigPointReceive = gson.fromJson(bigPointInfo, BigPointReceive.class);

        //convert from realm cache data to basic class
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<UserInfoJSON> result2 = realm.where(UserInfoJSON.class).findAll();
        final LoginReceive obj = (new Gson()).fromJson(result2.get(0).getUserInfo(), LoginReceive.class);

        customerNumber = obj.getCustomerNumber();
        txtBigShotId.setText(customerNumber);
        txtBigShotPoint.setText(bigPointReceive.getAvailablePts());
    }

    @Override
    public void onTransactionHistorySuccess(TransactionHistoryReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {

            String transObj = new Gson().toJson(obj);
            Intent transactionHistory = new Intent(getActivity(), TransactionHistoryActivity.class);
            transactionHistory.putExtra("TRANSACTION_HISTORY", transObj);
            getActivity().startActivity(transactionHistory);

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

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
