package com.extra.widget.listview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.extra.inter.Pullable;

/**
 * author：ct on 2017/9/27 17:24
 * email：cnhttt@163.com
 */

public class PullListView  extends ListView implements Pullable {

    private boolean pullDownEnable = true;  //下拉

    private boolean pullUpEnable = true;    //上啦



    public PullListView(Context context) {
        super(context);
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public boolean canPullDown() {
        if(!pullDownEnable){
            return false;
        }
        //没有数据时刷新
        if (getCount()==0){
            return  false;
        }else if (getFirstVisiblePosition()==0&&getChildAt(0).getTop()>=0){
            //滑到顶部
            return  true;
        }
        return false;
    }

    @Override
    public boolean canPullUp() {
        if (!pullUpEnable){
            return false;
        }
        //没有数据时刷新
        if (getCount()==0){
            return  false;
        }else if (getLastVisiblePosition() == getCount() -1 ){
            //滑到底部
            if (getChildAt(getLastVisiblePosition() - getFirstVisiblePosition())!=null&&getChildAt(getLastVisiblePosition()-getFirstVisiblePosition()).getBaseline()<=getMeasuredHeight()){
                return true;
            }
        }
        return false;
    }


    public boolean isPullDownEnable() {
        return pullDownEnable;
    }

    public void setPullDownEnable(boolean pullDownEnable) {
        this.pullDownEnable = pullDownEnable;
    }

    public boolean isPullUpEnable() {
        return pullUpEnable;
    }

    public void setPullUpEnable(boolean pullUpEnable) {
        this.pullUpEnable = pullUpEnable;
    }
}
