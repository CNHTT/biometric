package com.szfp.biometric.utils;

import com.szfp.biometric.greendao.BiometricBeanDao;
import com.szfp.biometric.model.BiometricBean;

/**
 * Created by 戴尔 on 2018/1/30.
 */

public class DbHelper {
    private static final Object PAH ="%" ;
    public static final String FINGERPRINT ="FINGERPRINT=";
    public static final String FINGERPRINT_END ="END";

    public static boolean insertBean(BiometricBean bean) {
        try {
            GreenDaoManager.getInstance().getSession().getBiometricBeanDao().insert(bean);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    public static BiometricBean getBiometricBean(Integer id) {
        try {
           return GreenDaoManager.getInstance().getSession().getBiometricBeanDao().queryBuilder()
                    .where(BiometricBeanDao.Properties.FingerId.like(PAH+FINGERPRINT+id+FINGERPRINT_END+PAH)).build().unique();
        }catch (Exception e){
            return null;
        }
    }

    public static void clear() {
        try {
            GreenDaoManager.getInstance().getSession().getBiometricBeanDao().deleteAll();
        }catch (Exception e){
        }
    }
}
