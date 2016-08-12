package com.metech.tbd.ui.Activity.DragDrop;

import android.app.Activity;
import android.content.ClipData;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Dell on 8/2/2016.
 */
public class TouchClass implements View.OnTouchListener {


    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                    view);
            view.startDrag(data, shadowBuilder, view, 0);
            //view.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

}
