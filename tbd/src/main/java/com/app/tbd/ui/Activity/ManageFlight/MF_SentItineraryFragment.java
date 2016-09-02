package com.app.tbd.ui.Activity.ManageFlight;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainController;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Model.Receive.FlightSummaryReceive;
import com.app.tbd.ui.Model.Receive.ManageRequestIntinenary;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Module.ManageFlightItinenary;
import com.app.tbd.ui.Model.Request.CachedResult;
import com.app.tbd.ui.Model.Request.SendItinenaryObj;
import com.app.tbd.ui.Presenter.ManageFlightPrenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;
import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.RealmResults;

public class MF_SentItineraryFragment extends BaseFragment implements ManageFlightPrenter.SendItinenary {

    @Inject
    ManageFlightPrenter presenter;
    private SharedPrefManager pref;

    @InjectView(R.id.txtEmail)
    TextView txtEmail;

    @InjectView(R.id.messageLayout)
    LinearLayout messageLayout;

    @InjectView(R.id.btnClose)
    Button btnClose;


    //private ProgressBar progressIndicator;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Send Itinerary";
    private String pnr,username,bookingId,signature;

    public static MF_SentItineraryFragment newInstance(Bundle bundle) {

        MF_SentItineraryFragment fragment = new MF_SentItineraryFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new ManageFlightItinenary(this)).inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.manage_flight_sent_itenenary, container, false);
        ButterKnife.inject(this, view);

        /*Retrieve bundle data*/
        Bundle bundle = getArguments();
        String flightSummary = bundle.getString("ITINENARY_INFORMATION");
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initSignature = pref.getSignatureFromLocalStorage();
        signature = initSignature.get(SharedPrefManager.SIGNATURE);

        Gson gson = new Gson();
        final FlightSummaryReceive obj = gson.fromJson(flightSummary, FlightSummaryReceive.class);

        pnr = obj.getItenerary_information().getPnr();
        bookingId = obj.getBooking_id();
        username = obj.getContact_information().getEmail();

        SendItinenaryObj requestObj = new SendItinenaryObj();
        requestObj.setPnr(pnr);
        requestObj.setSignature(signature);
        requestObj.setBookind_Id(bookingId);

        initiateLoading(getActivity());
        presenter.onSentItinenary(requestObj);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), MF_ActionActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                //intent.putExtra("AlertDialog", "Y");
                //getActivity().startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    @Override
    public void onSuccessRequest(ManageRequestIntinenary obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        if (status) {
            txtEmail.setText(username);
            messageLayout.setVisibility(View.VISIBLE);
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
        if(result.size() > 0){
            Gson gson = new Gson();
            ManageRequestIntinenary obj = gson.fromJson(result.get(0).getCachedResult(), ManageRequestIntinenary.class);
            onSuccessRequest(obj);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }


}
