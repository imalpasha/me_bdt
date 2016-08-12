package com.metech.tbd.ui.Activity.DragDrop;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.metech.tbd.R;
import com.metech.tbd.base.BaseFragment;
import com.metech.tbd.ui.Activity.FragmentContainerActivity;
import com.metech.tbd.ui.Presenter.HomePresenter;
import com.metech.tbd.utils.SharedPrefManager;
import com.google.android.gms.analytics.Tracker;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

//import com.estimote.sdk.BeaconManager;

public class DragDropFragment extends BaseFragment {

    private Tracker mTracker;

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.myimage1)
    ImageView myimage1;


    @InjectView(R.id.bottomright)
    LinearLayout bottomright;

    private String facebookUrl, twitterUrl, instagramUrl;
    private int fragmentContainerId;
    private static final String SCREEN_LABEL = "Home";

    private SharedPrefManager pref;
    View view;

    public static DragDropFragment newInstance() {

        DragDropFragment fragment = new DragDropFragment();
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

        View view = inflater.inflate(R.layout.demo, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        aq.recycle(view);

        // Assign the touch listener to your view which you want to move
        //imgToBeMove.setOnTouchListener(new MyTouchListener());
        myimage1.setOnTouchListener(new TouchClass());
        bottomright.setOnDragListener(new DropClass());

        return view;
    }

    class DropClass implements View.OnDragListener  {

       // Drawable enterShape =   ResourcesCompat.getDrawable(getResources(), R.drawable.tbd_icon, null);
       // Drawable normalShape =   ResourcesCompat.getDrawable(getResources(), R.drawable.tbd_icon, null);

        Drawable enterShape = getResources().getDrawable(R.drawable.shape_drop);
        Drawable normalShape = getResources().getDrawable(R.drawable.shape);

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundDrawable(enterShape);
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundDrawable(normalShape);
                    break;
                case DragEvent.ACTION_DROP:
                    // Dropped, reassign View to ViewGroup
                    View view = (View) event.getLocalState();
                    ViewGroup owner = (ViewGroup) view.getParent();
                    owner.removeView(view);
                    LinearLayout container = (LinearLayout) v;
                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    v.setBackgroundDrawable(normalShape);
                default:
                    break;
            }
            return true;
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

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
