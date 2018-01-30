package com.extra.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.extra.inter.Pullable;

/**
 * author：ct on 2017/9/27 18:35
 * email：cnhttt@163.com
 */

public class PullLinearLayout extends LinearLayout implements Pullable {
    public PullLinearLayout(Context context) {
        super(context);
    }

    public PullLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean canPullDown() {
        return true;
    }

    @Override
    public boolean canPullUp() {
        return true;
    }
}