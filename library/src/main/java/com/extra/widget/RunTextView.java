package com.extra.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.ViewDebug;

/**
 * Created by 戴尔 on 2017/11/30.
 */

public class RunTextView extends AppCompatTextView {
    public RunTextView(Context context) {
        super(context);
    }

    public RunTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RunTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 当前并没有焦点，我只是欺骗了Android系统
     */
    @Override
    @ViewDebug.ExportedProperty(category = "focus")
    public boolean isFocused() {
        return true;
    }
}
