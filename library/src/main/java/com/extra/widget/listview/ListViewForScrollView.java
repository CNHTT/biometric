package com.extra.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by 戴尔 on 2017/12/4.
 */

public class ListViewForScrollView  extends ListView {
    int mLastMotionY;
    boolean bottomFlag;
    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        // TODO Auto-generated method stub
        getParent().requestDisallowInterceptTouchEvent(true);

        return  super.dispatchTouchEvent(ev);
    }

}