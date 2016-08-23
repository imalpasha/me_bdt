package com.metech.tbd.ui.Activity.BookingFlight.Checkout;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Presenter.LoginPresenter;
import com.metech.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.Validator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PaymentFragment extends BaseFragment {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.btnProceedMSOS)
    TextView btnProceedMSOS;

    private int fragmentContainerId;


    public static PaymentFragment newInstance() {

        PaymentFragment fragment = new PaymentFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new LoginModule(this)).inject(this);
        RealmObjectController.clearCachedResult(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.payment, container, false);
        ButterKnife.inject(this, view);

        btnProceedMSOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profilePage = new Intent(getActivity(), PaymentMSOSActivity.class);
                getActivity().startActivity(profilePage);
            }
        });
        return view;
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
