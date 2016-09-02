package com.app.tbd.ui.Activity.SplashScreen.OnBoarding;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Model.Request.ProductImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class OnBoardingImage extends BaseFragment {

    @InjectView(R.id.onboarding_image)
    ImageView onboarding_image;

    private static final String KEY_PRODUCTIMAGE = OnBoardingImage.class.getSimpleName() + ":KEY_PRODUCTIMAGE";

    public static OnBoardingImage newInstance(ProductImage productImage) {
        OnBoardingImage fragment = new OnBoardingImage();
        Bundle args = new Bundle();
        args.putParcelable(KEY_PRODUCTIMAGE, productImage);
        fragment.setArguments(args);
        return fragment;
    }

    public static OnBoardingImage newInstance(OnBoarding productImage, List<OnBoarding> listProductImages) {
        OnBoardingImage fragment = new OnBoardingImage();
        ArrayList<String> imgUrls = new ArrayList<String>();
        Bundle args = new Bundle();
        args.putString(KEY_PRODUCTIMAGE, (new Gson()).toJson(productImage));
        //flight.putExtra("FLIGHT_OBJ", (new Gson()).toJson(obj));
        //for (PagerBoardingPassObj pI : listProductImages) {
        //    imgUrls.add(pI.getQRCodeURL());
        // }
        // args.putStringArrayList("imgUrls", imgUrls);
        fragment.setArguments(args);
        return fragment;
    }

    private OnBoarding boardingObj = null;
    private String gsonBoardingPassObj;
    private ArrayList<String> listProductImages;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        gsonBoardingPassObj = args.getString(KEY_PRODUCTIMAGE);

        Gson gson = new Gson();
        boardingObj = gson.fromJson(gsonBoardingPassObj, OnBoarding.class);

        //listProductImages = args.getStringArrayList("imgUrls");

        //if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_PRODUCTIMAGE)) {
        //    boardingObj = savedInstanceState.getString(KEY_PRODUCTIMAGE);
       // }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.on_boarding_image, container, false);
        aq.recycle(rootView);
        ButterKnife.inject(this, rootView);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        aq.recycle(view);

        onboarding_image.setImageResource(boardingObj.getDrawableResources());




    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_PRODUCTIMAGE, (new Gson()).toJson(boardingObj));
    }
}
