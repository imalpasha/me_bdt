package com.app.tbd.ui.Activity.Profile.BigPoint;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.app.tbd.ui.Model.Adapter.TransactionHistoryAdapter;
import com.app.tbd.ui.Model.Receive.TBD.BigPointReceive;
import com.app.tbd.ui.Model.Receive.TransactionHistoryReceive;
import com.google.gson.Gson;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Model.JSON.UserInfoJSON;
import com.app.tbd.ui.Model.Receive.TBD.LoginReceive;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;

public class TransactionHistoryFragment extends BaseFragment {

    //@Inject
    //ProfilePresenter presenter;


    @InjectView(R.id.transactionHistoryList)
    ListView transactionHistoryList;

    @InjectView(R.id.transactionHistoryLayout)
    LinearLayout transactionHistoryLayout;

    @InjectView(R.id.noTransactionHistoryLayout)
    LinearLayout noTransactionHistoryLayout;


    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Login";
    private SharedPrefManager pref;
    private String customerNumber;
    private TransactionHistoryAdapter transactionHistoryAdapter;

    public static TransactionHistoryFragment newInstance(Bundle bundle) {

        TransactionHistoryFragment fragment = new TransactionHistoryFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new ProfileModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.big_point_transaction_history, container, false);
        ButterKnife.inject(this, view);
        dataSetup();


        return view;
    }

    public void dataSetup() {

        pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();
        String bigPointTransaction = bundle.getString("TRANSACTION_HISTORY");
        Log.e("bigPointExpiry", bigPointTransaction);

        Gson gson = new Gson();
        TransactionHistoryReceive transactionHistoryReceive = gson.fromJson(bigPointTransaction, TransactionHistoryReceive.class);
        setData(transactionHistoryReceive);
    }

    public void setData(TransactionHistoryReceive obj) {

        /*List<List_TransactionHistory> transactionList = new ArrayList<List_TransactionHistory>();

        //= obj.getList_booking();
        //list_booking.remove(0);

        for (int v = 0; v < obj.getTransaction().size(); v++) {
            test.setDate(obj.getTransaction().get(v));
            test.setPnr(obj.getList_booking().get(v).getPnr());
            transactionList.add(test);
        }*/
        if (obj.getTransaction().size() == 0) {
            transactionHistoryLayout.setVisibility(View.GONE);
            noTransactionHistoryLayout.setVisibility(View.VISIBLE);
        } else {
            transactionHistoryLayout.setVisibility(View.VISIBLE);
            noTransactionHistoryLayout.setVisibility(View.GONE);

            transactionHistoryAdapter = new TransactionHistoryAdapter(getActivity(), obj.getTransaction(), "MF");
            transactionHistoryList.setAdapter(transactionHistoryAdapter);
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
        // presenter.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();
        //presenter.onPause();
    }
}
