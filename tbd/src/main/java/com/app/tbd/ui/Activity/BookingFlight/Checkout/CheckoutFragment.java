package com.app.tbd.ui.Activity.BookingFlight.Checkout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.tbd.R;
import com.app.tbd.base.BaseFragment;
import com.app.tbd.ui.Activity.FragmentContainerActivity;
import com.app.tbd.ui.Presenter.LoginPresenter;
import com.app.tbd.ui.Realm.RealmObjectController;
import com.google.android.gms.analytics.Tracker;
import com.mobsandgeeks.saripaar.Validator;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CheckoutFragment extends BaseFragment {

    // Validator Attributes
    private Validator mValidator;
    private Tracker mTracker;

    @Inject
    LoginPresenter presenter;

    @InjectView(R.id.btnPay)
    TextView btnPay;

    @InjectView(R.id.txtBigPoint)
    TextView txtBigPoint;

    @InjectView(R.id.txtDiscount)
    TextView txtDiscount;

    @InjectView(R.id.customSeekBar)
    SeekBar customSeekBar;

    private int fragmentContainerId;


    public static CheckoutFragment newInstance() {

        CheckoutFragment fragment = new CheckoutFragment();
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

        View view = inflater.inflate(R.layout.check_out, container, false);
        ButterKnife.inject(this, view);

        customSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            int bigPoint = 8500;
            int bigPointCash = 250;

            int progress = 0;

            @Override

            public void onProgressChanged(SeekBar seekBar, int progresValue, boolean fromUser) {

                progress = progresValue;
                //txtBigPoint.setText(Integer.toString(bigPoint * (100 / progress)) + " pts");
                //txtDiscount.setText(Integer.toString(bigPointCash * (progress)) + " MYR");

                Toast.makeText(getActivity(), "Changing seekbar's progress", Toast.LENGTH_SHORT).show();

            }

            @Override

            public void onStartTrackingTouch(SeekBar seekBar) {


            }

            @Override

            public void onStopTrackingTouch(SeekBar seekBar) {

                //txtBigPoint.setText(Integer.toString(bigPoint * (100 / progress)) + " pts");
                //txtDiscount.setText(Integer.toString(bigPointCash * (progress)) + " MYR");

            }

        });


        btnPay.setOnClickListener(new View.OnClickListener()

                                  {
                                      @Override
                                      public void onClick(View v) {
                                          Intent profilePage = new Intent(getActivity(), PaymentActivity.class);
                                          getActivity().startActivity(profilePage);
                                      }
                                  }

        );

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
