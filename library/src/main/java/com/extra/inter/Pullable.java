package com.extra.inter;

/**
 * author：ct on 2017/9/27 16:26
 * email：cnhttt@163.com
 */

public interface Pullable {

    /**
     * 是否可以下拉
     * @return
     */
    boolean canPullDown();


    /**
     * 是否可以上拉
     * @return
     */
    boolean canPullUp();
}
