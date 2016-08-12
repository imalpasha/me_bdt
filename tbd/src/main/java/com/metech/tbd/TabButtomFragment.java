package com.metech.tbd;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import com.androidquery.AQuery;
import com.metech.tbd.ui.Activity.BookingFlight.ManageFamilyAndFriend.ManageFriendAndFamilyActivity;
import com.metech.tbd.ui.Activity.BookingFlight.SearchFlightActivity;
import com.metech.tbd.ui.Activity.BookingFlight.SearchFlightFragment;
import com.metech.tbd.ui.Activity.Login.LoginFragment;
import com.metech.tbd.ui.Activity.Profile.ProfileActivity;
import com.metech.tbd.ui.Activity.Terms.TermsFragment;


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

		/* My Profile */
		aq.id(R.id.tabMyProfile).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent searchFlight = new Intent(getActivity(), ProfileActivity.class);
				//need to clear previous activity
				getActivity().startActivity(searchFlight);
			}
		});

		/* My Cart */
		aq.id(R.id.tabSearchFlight).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v) {

				Intent searchFlight = new Intent(getActivity(), SearchFlightActivity.class);
				//need to clear previous activity
				getActivity().startActivity(searchFlight);


		}
		});

		/* Homepage */
		aq.id(R.id.tabMyHome).clicked(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{

			}
		});

		/* My Wish List */
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