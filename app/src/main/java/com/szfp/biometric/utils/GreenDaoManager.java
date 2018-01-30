package com.szfp.biometric.utils;

import com.szfp.biometric.greendao.DaoMaster;
import com.szfp.biometric.greendao.DaoSession;

import static com.extra.utils.Utils.getContext;

/**
 * Created by 戴尔 on 2018/1/30.
 */

public class GreenDaoManager {

    private static final java.lang.String DB_NAME = "szfp.db";
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;
    private  static volatile GreenDaoManager mInstance=null;
    private GreenDaoManager(){
        if (mInstance == null){
            DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getContext(), DB_NAME);
            mDaoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
            mDaoSession = mDaoMaster.newSession();
        }
    }


    public static  GreenDaoManager getInstance(){
        if (mInstance ==null)
        {
            synchronized (GreenDaoManager.class){
                if (mInstance==null)
                    mInstance = new GreenDaoManager();
            }
        }
        return  mInstance;
    }

    public DaoMaster getMaster() {
        return mDaoMaster;
    }
    public DaoSession getSession() {
        return mDaoSession;
    }
    public DaoSession getNewSession() {
        mDaoSession = mDaoMaster.newSession();
        return mDaoSession;
    }
}
