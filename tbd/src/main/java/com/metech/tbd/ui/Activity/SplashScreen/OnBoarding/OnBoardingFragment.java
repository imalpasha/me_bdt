package com.metech.tbd.ui.Activity.SplashScreen.OnBoarding;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Activity.Login.LoginActivity;
import com.metech.tbd.ui.Activity.SlidePage.ProductImagesPagerAdapter;
import com.metech.tbd.ui.Model.Receive.RetrieveBoardingPassReceive;
import com.metech.tbd.ui.Model.Request.PagerBoardingPassObj;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.relex.circleindicator.CircleIndicator;

public class OnBoardingFragment extends BaseFragment {

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.indicator)
    CircleIndicator indicator;

    /*@InjectView(R.id.onboardingCtn)
     Button onboardingCtn;

    @InjectView(R.id.onboardingSkip)
     Button onboardingSkip;*/

    private static Button onboardingSkip;
    private static Button onboardingCtn;

    private int fragmentContainerId;

    public static OnBoardingFragment newInstance() {

        OnBoardingFragment fragment = new OnBoardingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.on_boarding, container, false);
        ButterKnife.inject(this, view);

        onboardingSkip = (Button) view.findViewById(R.id.onboardingSkip);
        onboardingCtn = (Button) view.findViewById(R.id.onboardingCtn);

        startPagination();
        return view;
    }

    public void startPagination() {

        int[] myImageList = new int[]{R.drawable.perth_background, R.drawable.bali_background, R.drawable.lombok_background};

        ArrayList<OnBoarding> listProductImages = new ArrayList<OnBoarding>();
        for (int i = 0; i < myImageList.length; i++) {
            OnBoarding onboard = new OnBoarding();
            onboard.setImagePosition(Integer.toString(i));
            onboard.setDrawableResources(myImageList[i]);
            listProductImages.add(onboard);
        }

        OnBoardingAdapter mAdapter = new OnBoardingAdapter(getFragmentManager());
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

    public static void hideSkipButton(final Activity act) {

        onboardingCtn.setVisibility(View.VISIBLE);
        onboardingSkip.setVisibility(View.GONE);

        onboardingCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightDetail = new Intent(act, LoginActivity.class);
                act.startActivity(flightDetail);
                act.finish();
            }
        });
    }

    public static void hideContinueButton(final Activity act) {

        onboardingCtn.setVisibility(View.GONE);
        onboardingSkip.setVisibility(View.VISIBLE);

        onboardingSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightDetail = new Intent(act, LoginActivity.class);
                act.startActivity(flightDetail);
                act.finish();
            }
        });
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
