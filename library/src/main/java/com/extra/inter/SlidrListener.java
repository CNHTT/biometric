package com.extra.inter;

/**
 * author：ct on 2017/8/30 15:10
 * email：cnhttt@163.com
 */

public interface SlidrListener {
    /**
     * This is called when the {@link android.support.v4.widget.ViewDragHelper} calls it's
     * state change callback.
     *
     * @see android.support.v4.widget.ViewDragHelper#STATE_IDLE
     * @see android.support.v4.widget.ViewDragHelper#STATE_DRAGGING
     * @see android.support.v4.widget.ViewDragHelper#STATE_SETTLING
     *
     * @param state     the {@link android.support.v4.widget.ViewDragHelper} state
     */
    void onSlideStateChanged(int state);

    void onSlideChange(float percent);

    void onSlideOpened();

    void onSlideClosed();
}
