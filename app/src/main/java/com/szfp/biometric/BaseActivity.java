package com.szfp.biometric;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.extra.utils.ToastUtils;
import com.extra.widget.dialog.LoadingAlertDialog;
import com.szfp.asynctask.AsyncFingerprint;

import android_serialport_api.SerialPortManager;

/**
 * Created by 戴尔 on 2018/1/30.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private LoadingAlertDialog loadingAlertDialog;
    protected App application;
    protected HandlerThread handlerThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        super.onCreate(savedInstanceState);

        application = (App) getApplicationContext();
    }



    @Override
    protected void onResume() {
        super.onResume();

        if (!SerialPortManager.getInstance().isOpen()){
            SerialPortManager.getInstance().openSerialPort();
        }
        Log.i("whw", "onResume=" + SerialPortManager.getInstance().isOpen());
        handlerThread = application.getHandlerThread();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SerialPortManager.getInstance().closeSerialPort();
        handlerThread = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void  showToastError(String str){
        ToastUtils.error(this,str).show();
    }
    void  showToastError(){
        showErrorToast("Please Input");
    }


    public   void showLoadingDialog(){
        if (loadingAlertDialog==null)
            loadingAlertDialog = new LoadingAlertDialog(this);
        loadingAlertDialog.show("loading...");
    }

    public void dismissLoadingDialog(){
        if (loadingAlertDialog!=null)loadingAlertDialog.dismiss();
    }
    public void stopAsy(AsyncFingerprint asyncFingerprint){
        asyncFingerprint.setStop(true);
    };

    public void showErrorToast(String s){
        ToastUtils.error(s);
    }
}
