package com.metech.tbd.ui.Activity.SplashScreen.OnBoarding;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.metech.tbd.base.BaseFragmentPagerAdapter;
import com.metech.tbd.ui.Model.Request.PagerBoardingPassObj;

import java.util.List;

public class OnBoardingAdapter extends BaseFragmentPagerAdapter
{

    private List<OnBoarding> listProductImages;

    public OnBoardingAdapter(FragmentManager fm)
    {
        super(fm);
    }

    public void addAll(List<OnBoarding> listProductImages)
    {
        this.listProductImages = listProductImages;
        Log.e("ProductImagesFragment", "listProductImages");

        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position)
    {
        Log.e("ProductImagesFragment", "ProductImagesFragment");
        return OnBoardingImage.newInstance(listProductImages.get(position), listProductImages);

    }

    @Override
    public int getCount()
    {
        return this.listProductImages.size();
        //return 4;

    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        return super.getPageTitle(position);
    }

}
