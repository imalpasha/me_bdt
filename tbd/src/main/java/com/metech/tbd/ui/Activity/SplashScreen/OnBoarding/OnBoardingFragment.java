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

    @InjectView(R.id.onboardingCtn)
    Button onboardingCtn;

    @InjectView(R.id.onboardingSkip)
    Button onboardingSkip;

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

        onboardingCtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightDetail = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(flightDetail);
                getActivity().finish();
            }
        });

        onboardingSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent flightDetail = new Intent(getActivity(), LoginActivity.class);
                getActivity().startActivity(flightDetail);
                getActivity().finish();
            }
        });

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

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {

                if (position == 2) {
                    onboardingCtn.setVisibility(View.VISIBLE);
                    onboardingSkip.setVisibility(View.GONE);
                } else {
                    onboardingCtn.setVisibility(View.GONE);
                    onboardingSkip.setVisibility(View.VISIBLE);
                }

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
