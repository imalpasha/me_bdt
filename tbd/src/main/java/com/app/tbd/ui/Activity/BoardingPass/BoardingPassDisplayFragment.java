package com.app.tbd.ui.Activity.BoardingPass;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.androidquery.util.BitmapCache;
//import com.commit451.inkpageindicator.InkPageIndicator;
import com.app.tbd.application.AnalyticsApplication;
import com.app.tbd.MainController;
import com.app.tbd.application.MainApplication;
import com.app.tbd.MainFragmentActivity;
import com.app.tbd.R;
import com.app.tbd.ui.Model.Receive.ListBookingReceive;
import com.app.tbd.ui.Model.Receive.MobileConfirmCheckInPassengerReceive;
import com.app.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Activity.SlidePage.ProductImagesPagerAdapter;
import com.app.tbd.ui.Module.BoardingPassDisplayModule;
import com.app.tbd.ui.Model.Request.PagerBoardingPassObj;
import com.app.tbd.ui.Model.Request.RetrieveBoardingPassObj;
import com.app.tbd.ui.Presenter.BoardingPassPresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.app.tbd.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;

public class BoardingPassDisplayFragment extends BaseFragment implements BoardingPassPresenter.RetrieveBoardingPassView {

    @Inject
    BoardingPassPresenter presenter;

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.indicator)
    CircleIndicator indicator;

    @InjectView(R.id.horizontalProgressBar)
    ProgressBar horizontalProgressBar;

    private BitmapCache mMemoryCache;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Boarding Pass Detail";
    private SharedPrefManager pref;
    private String boardingPassList, selectedBoardingPassList;
    private RetrieveBoardingPassReceive obj;
    private boolean retrieveBoardingPass = false;
    private boolean boardingPassPagePause = false;
    private boolean offline = false;
    private Bundle bundle;

    public static BoardingPassDisplayFragment newInstance(Bundle bundle) {

        BoardingPassDisplayFragment fragment = new BoardingPassDisplayFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmObjectController.clearCachedResult(getActivity());
        MainApplication.get(getActivity()).createScopedGraph(new BoardingPassDisplayModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.boarding_pass_list, container, false);
        ButterKnife.inject(this, view);

        bundle = getArguments();
        if (bundle.containsKey("BOARDING_PASS_OBJ")) {
            offline = false;
            boardingPassList = bundle.getString("BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            obj = gson.fromJson(boardingPassList, RetrieveBoardingPassReceive.class);

            startPagination(obj);
            RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());

            //put some condition here later
            if (bundle.getString("LOAD_BACKGROUND").equals("Y")) {
                retrieveBoardingPass(bundle);
            }

        } else if (bundle.containsKey("OFFLINE_BOARDING_PASS_OBJ")) {
            offline = true;
            boardingPassList = bundle.getString("OFFLINE_BOARDING_PASS_OBJ");
            Gson gson = new Gson();
            MobileConfirmCheckInPassengerReceive oXXbXXj = gson.fromJson(boardingPassList, MobileConfirmCheckInPassengerReceive.class);
            offLineStartPagination(oXXbXXj);
            RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());

        }


        return view;
    }


    public void startPagination(RetrieveBoardingPassReceive passObj) {

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < passObj.getBoarding_pass().size(); i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();

            boardingPass.setQRCodeURL(passObj.getBoarding_pass().get(i).getQRCodeURL());
            boardingPass.setQRCode(passObj.getBoarding_pass().get(i).getQRCode());
            boardingPass.setSSR(passObj.getBoarding_pass().get(i).getSSR());
            boardingPass.setName(passObj.getBoarding_pass().get(i).getName());
            boardingPass.setFlightNumber(passObj.getBoarding_pass().get(i).getFlightNumber());
            boardingPass.setSeat(passObj.getBoarding_pass().get(i).getSeat());
            boardingPass.setFare(passObj.getBoarding_pass().get(i).getFare());
            boardingPass.setArrivalDateTime(passObj.getBoarding_pass().get(i).getArrivalDateTime());
            boardingPass.setArrivalStation(passObj.getBoarding_pass().get(i).getArrivalStation());
            boardingPass.setArrivalStationCode(passObj.getBoarding_pass().get(i).getArrivalStationCode());
            boardingPass.setArrivalTime(passObj.getBoarding_pass().get(i).getArrivalTime());
            boardingPass.setBarCodeData(passObj.getBoarding_pass().get(i).getBarCodeData());
            boardingPass.setBarCodeURL(passObj.getBoarding_pass().get(i).getBarCodeURL());
            boardingPass.setBoardingSequence(passObj.getBoarding_pass().get(i).getBoardingSequence());
            boardingPass.setBoardingTime(passObj.getBoarding_pass().get(i).getBoardingTime());
            boardingPass.setDepartureDate(passObj.getBoarding_pass().get(i).getDepartureDate());
            boardingPass.setDepartureDateTime(passObj.getBoarding_pass().get(i).getDepartureDateTime());
            boardingPass.setDepartureGate(passObj.getBoarding_pass().get(i).getDepartureGate());
            boardingPass.setDepartureStation(passObj.getBoarding_pass().get(i).getDepartureStation());
            boardingPass.setDepartureStationCode(passObj.getBoarding_pass().get(i).getDepartureStationCode());
            boardingPass.setDepartureTime(passObj.getBoarding_pass().get(i).getDepartureTime());
            boardingPass.setRecordLocator(passObj.getBoarding_pass().get(i).getRecordLocator());
            boardingPass.setDepartureDayDate(passObj.getBoarding_pass().get(i).getDepartureDayDate());

            boardingPass.setBoardingPosition((i + 1) + " / " + (passObj.getBoarding_pass().size()));
            listProductImages.add(boardingPass);
        }

        ProductImagesPagerAdapter mAdapter = new ProductImagesPagerAdapter(getFragmentManager());
        mAdapter.addAll(listProductImages);
        pager.setAdapter(mAdapter);

        indicator.setViewPager(pager);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //CirclePageIndicator mIndicator = (CirclePageIndicator) aq.id(R.id.indicator).getView();
        //mIndicator.setViewPager(mPager);

    }

    public void offLineStartPagination(MobileConfirmCheckInPassengerReceive passObj) {

        ArrayList<PagerBoardingPassObj> listProductImages = new ArrayList<PagerBoardingPassObj>();
        for (int i = 0; i < passObj.getObj().getBoarding_pass().size(); i++) {
            PagerBoardingPassObj boardingPass = new PagerBoardingPassObj();

            boardingPass.setQRCodeURL(passObj.getObj().getBoarding_pass().get(i).getQRCodeURL());
            boardingPass.setQRCode(passObj.getObj().getBoarding_pass().get(i).getQRCode());
            boardingPass.setSSR(passObj.getObj().getBoarding_pass().get(i).getSSR());
            boardingPass.setName(passObj.getObj().getBoarding_pass().get(i).getName());
            boardingPass.setFlightNumber(passObj.getObj().getBoarding_pass().get(i).getFlightNumber());
            boardingPass.setSeat(passObj.getObj().getBoarding_pass().get(i).getSeat());
            boardingPass.setFare(passObj.getObj().getBoarding_pass().get(i).getFare());
            boardingPass.setArrivalDateTime(passObj.getObj().getBoarding_pass().get(i).getArrivalDateTime());
            boardingPass.setArrivalStation(passObj.getObj().getBoarding_pass().get(i).getArrivalStation());
            boardingPass.setArrivalStationCode(passObj.getObj().getBoarding_pass().get(i).getArrivalStationCode());
            boardingPass.setArrivalTime(passObj.getObj().getBoarding_pass().get(i).getArrivalTime());
            boardingPass.setBarCodeData(passObj.getObj().getBoarding_pass().get(i).getBarCodeData());
            boardingPass.setBarCodeURL(passObj.getObj().getBoarding_pass().get(i).getBarCodeURL());
            boardingPass.setBoardingSequence(passObj.getObj().getBoarding_pass().get(i).getBoardingSequence());
            boardingPass.setBoardingTime(passObj.getObj().getBoarding_pass().get(i).getBoardingTime());
            boardingPass.setDepartureDate(passObj.getObj().getBoarding_pass().get(i).getDepartureDate());
            boardingPass.setDepartureDayDate(passObj.getObj().getBoarding_pass().get(i).getDepartureDayDate());
            boardingPass.setDepartureDateTime(passObj.getObj().getBoarding_pass().get(i).getDepartureDateTime());
            boardingPass.setDepartureGate(passObj.getObj().getBoarding_pass().get(i).getDepartureGate());
            boardingPass.setDepartureStation(passObj.getObj().getBoarding_pass().get(i).getDepartureStation());
            boardingPass.setDepartureStationCode(passObj.getObj().getBoarding_pass().get(i).getDepartureStationCode());
            boardingPass.setDepartureTime(passObj.getObj().getBoarding_pass().get(i).getDepartureTime());
            boardingPass.setRecordLocator(passObj.getObj().getBoarding_pass().get(i).getRecordLocator());

            boardingPass.setBoardingPosition((i + 1) + " / " + (passObj.getObj().getBoarding_pass().size()));
            listProductImages.add(boardingPass);
        }

        ProductImagesPagerAdapter mAdapter = new ProductImagesPagerAdapter(getFragmentManager());
        mAdapter.addAll(listProductImages);
        pager.setAdapter(mAdapter);

        indicator.setViewPager(pager);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });


        //CirclePageIndicator mIndicator = (CirclePageIndicator) aq.id(R.id.indicator).getView();
        //mIndicator.setViewPager(mPager);

    }

    //update current boarding pass list if available (cached list)
    public void retrieveBoardingPass(Bundle bundle) {

        RetrieveBoardingPassObj flightObj = new RetrieveBoardingPassObj();
        flightObj.setUser_id(bundle.getString("USER_ID"));
        flightObj.setPnr(bundle.getString("PNR"));
        flightObj.setDeparture_station(bundle.getString("DEPARTURE_STATION_CODE"));
        flightObj.setArrival_station(bundle.getString("ARRIVAL_STATION_CODE"));
        flightObj.setSignature(bundle.getString("SIGNATURE"));
        //retrieveBoardingPass = true;
        horizontalProgressBar.setVisibility(View.VISIBLE);

        retrieveBoardingPass = true;

        presenter.retrieveBoardingPass(flightObj);

    }

    @Override
    public void onBoardingPassReceive(RetrieveBoardingPassReceive obj) {

        dismissLoading();
        Boolean status = MainController.getRequestStatus(obj.getStatus(), obj.getMessage(), getActivity());
        RealmObjectController.clearCachedResult(MainFragmentActivity.getContext());
        if (status) {
            startPagination(obj);
            horizontalProgressBar.setVisibility(View.GONE);
        }

    }


    @Override
    public void onUserPnrList(final ListBookingReceive obj) {

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

        if(boardingPassPagePause){
            //reload after pause
            if(!offline){
                boardingPassPagePause = false;
                retrieveBoardingPass(bundle);
            }else{
                //do nothing
            }
        }else{
            //offline boarding pass
        }
        //onresume reload page
        /*RealmResults<CachedResult> result = RealmObjectController.getCachedResult(MainFragmentActivity.getContext());
        if (result.size() > 0) {
            Gson gson = new Gson();
            RetrieveBoardingPassReceive obj = gson.fromJson(result.get(0).getCachedResult(), RetrieveBoardingPassReceive.class);
            onBoardingPassReceive(obj);
        }*/

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
        boardingPassPagePause = true;

    }


}
