package com.extra.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by 戴尔 on 2017/11/17.
 */

public class InnerScrollerListView  extends ListView

{
    public InnerScrollerListView(Context context) {
        super(context);
    }

    public InnerScrollerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InnerScrollerListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}