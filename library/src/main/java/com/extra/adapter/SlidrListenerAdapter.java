package com.extra.adapter;

import com.extra.inter.SlidrListener;

/**
 * author：ct on 2017/8/30 15:11
 * email：cnhttt@163.com
 */

public class SlidrListenerAdapter implements SlidrListener {
    @Override
    public void onSlideStateChanged(int state) {}
    @Override
    public void onSlideChange(float percent) {}
    @Override
    public void onSlideOpened() {}
    @Override
    public void onSlideClosed() {}
}