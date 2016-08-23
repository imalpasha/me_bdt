package com.metech.tbd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.metech.tbd.ui.Activity.BookingFlight.SearchFlightActivity;
import com.metech.tbd.ui.Activity.HolidayShaker.HolidayShakerActivity;
import com.metech.tbd.ui.Activity.Profile.ProfileActivity;


public class TabButtomFragment extends Fragment
{
	public static TabButtomFragment newInstance()
	{
		TabButtomFragment fragment = new TabButtomFragment();
		return fragment;
	}

	private AQuery aq;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		aq = new AQuery(getActivity());

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle icicle)
	{
		View layout = inflater.inflate(R.layout.layout_tab_container, null);
		aq.recycle(layout);


		aq.id(R.id.tabMyProfile).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				//FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				//fragmentManager.beginTransaction().replace(R.id.main_content, ProfileFragment.newInstance()).commit();

				//BaseFragmentActivity.setTitle2("MY PROFILE");

				Intent tabProfile = new Intent(getActivity(), ProfileActivity.class);
				tabProfile.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//need to clear previous activity
				getActivity().startActivity(tabProfile);
				getActivity().finish();

			}
		});


		aq.id(R.id.tabSearchFlight).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v) {

				//FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				//fragmentManager.beginTransaction().replace(R.id.main_content, BaggageFragment.newInstance()).commit();


				Intent searchFlight = new Intent(getActivity(), HolidayShakerActivity.class);
				searchFlight.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//need to clear previous activity
				getActivity().startActivity(searchFlight);
				getActivity().finish();
			}
		});


		aq.id(R.id.tabMyHome).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

				//FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				//fragmentManager.beginTransaction().replace(R.id.main_content, SelectFlightFrament.newInstance()).commit();


				Intent searchFlight = new Intent(getActivity(), SearchFlightActivity.class);
				searchFlight.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//need to clear previous activity
				getActivity().startActivity(searchFlight);
				getActivity().finish();

			}
		});


		aq.id(R.id.tabMyWishList).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});

		return layout;
	}
}