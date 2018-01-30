package com.szfp.biometric;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.extra.utils.DataUtils;
import com.extra.utils.ToastUtils;
import com.player.util.L;
import com.szfp.asynctask.AsyncFingerprint;
import com.szfp.biometric.model.BiometricBean;
import com.szfp.biometric.utils.DbHelper;

import android_serialport_api.FingerprintAPI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.szfp.biometric.App.FINGERPRINT;

public class MainActivity extends BaseActivity {


    public static final String FINGERPRINT ="FINGERPRINT=";
    public static final String FINGERPRINT_END ="END";

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.iv_finger)
    ImageView ivFinger;
    @BindView(R.id.bt_save)
    Button btSave;
    @BindView(R.id.bt_register)
    Button btRegister;
    @BindView(R.id.bt_clear_the)
    Button btClearThe;
    @BindView(R.id.bt_find)
    Button btFind;
    @BindView(R.id.bt_clear_all)
    Button btClearAll;

    private BiometricBean bean;
    private AsyncFingerprint asyncFingerprint;
    private ProgressDialog progressDialog;
    private byte[] modelByte;

    private String name;
    private String fingerId;

    private  Integer id;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case AsyncFingerprint.SHOW_PROGRESSDIALOG:
                    cancleProgressDialog();
                    showProgressDialog((Integer) msg.obj);
                    break;
                case AsyncFingerprint.SHOW_FINGER_IMAGE:
                    modelByte = (byte[]) msg.obj;
                    showFingerImage(modelByte);
                    break;
                case AsyncFingerprint.SHOW_FINGER_MODEL:

                    cancleProgressDialog();
                    break;
                case AsyncFingerprint.REGISTER_SUCCESS:
                    cancleProgressDialog();
                    if (msg.obj != null) {
                      id = (Integer) msg.obj;
                        fingerId = FINGERPRINT + String.valueOf(id) + FINGERPRINT_END;

//                        if (DataUtils.isNullString(fingerPrintFileUrl))
//                            fingerPrintFileUrl = FINGERPRINT + String.valueOf(id) + FINGERPRINT_END;
//
//                        else {
//                            //please user StringBuffer
//                            fingerPrintFileUrl = fingerPrintFileUrl + "_" + FINGERPRINT + String.valueOf(id) + FINGERPRINT_END;
//                        }

                        L.d(fingerId);
                        ToastUtils.showToast(getString(R.string.register_success));
                    } else {
                        ToastUtils.showToast(
                                R.string.register_success);
                    }

                    break;
                case AsyncFingerprint.REGISTER_FAIL:
                    cancleProgressDialog();
                    ToastUtils.showToast(
                            R.string.register_fail);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT1:
                    cancleProgressDialog();
                    showValidateResult((Boolean) msg.obj);
                    break;
                case AsyncFingerprint.VALIDATE_RESULT2:
                    cancleProgressDialog();
                    Integer r = (Integer) msg.obj;
                    if (r != -1) {
                        showBiometricBean(r);
                    } else {
                        showValidateResult(false);
                    }
                    break;
                case AsyncFingerprint.UP_IMAGE_RESULT:
                    cancleProgressDialog();
                    break;
                default:
                    break;
            }
        }

    };

    private void showBiometricBean(Integer r) {
        bean = DbHelper.getBiometricBean(r);
        if (DataUtils.isEmpty(bean))
            ToastUtils.error("no information");
        else {
            etName.setText(bean.getName());
            showFingerImage(bean.getModel());
        }
    }

    private void showFingerImage( byte[] obj) {
        Bitmap image = BitmapFactory.decodeByteArray(obj, 0, obj.length);
        // saveImage(data);
        ivFinger.setBackgroundDrawable(new BitmapDrawable(image));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancleProgressDialog();
        asyncFingerprint.setStop(true);
    }

    @Override
    protected void onDestroy() {
        cancleProgressDialog();
        super.onDestroy();
    }
    private void initData() {

        asyncFingerprint = new AsyncFingerprint(handlerThread.getLooper(), mHandler);

        asyncFingerprint.setOnEmptyListener(new AsyncFingerprint.OnEmptyListener() {
            @Override
            public void onEmptySuccess() {
                ToastUtils.showToast(R.string.clear_flash_success);
            }

            @Override
            public void onEmptyFail() {
                ToastUtils.showToast(R.string.clear_flash_fail);
            }
        });

        asyncFingerprint.setFingerprintType(FingerprintAPI.BIG_FINGERPRINT_SIZE);
    }

    @OnClick({R.id.bt_save, R.id.bt_register, R.id.bt_clear_the, R.id.bt_find, R.id.bt_clear_all})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_save:
                save();
                break;
            case R.id.bt_register:
                if (id!=null){
                    ToastUtils.showToast("Please save the fingerprint information or clear the fingerprint ");
                    return;
                }
                asyncFingerprint.register2();
                break;
            case R.id.bt_clear_the:
               asyncFingerprint.PS_DeleteChar(id,1); id=null;
                break;
            case R.id.bt_find:
                asyncFingerprint.validate2();
                break;
            case R.id.bt_clear_all:
                asyncFingerprint.PS_Empty();
                DbHelper.clear();
                break;
        }
    }

    private void save() {
        name = etName.getText().toString();
        if (DataUtils.isNullString(name)){
            ToastUtils.showToast("please input name");return;
        }

        bean = new BiometricBean();
        bean.setName(name);
        bean.setFingerId(fingerId);
        bean.setModel(modelByte);
        if (DbHelper.insertBean(bean)) {
            id=null;
            etName.setText("");
            ToastUtils.success("SUCCESS");
        } else {
            ToastUtils.error("ERROR");
        }

    }



    private void showProgressDialog(int resId) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(resId));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener((dialog, keyCode, event) -> {
            if (KeyEvent.KEYCODE_BACK == keyCode) {
                asyncFingerprint.setStop(true);
            }
            return false;
        });
        progressDialog.show();
    }


    private void cancleProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }
    private void showValidateResult(boolean matchResult) {
        if (matchResult) {
            ToastUtils.showToast(
                    R.string.verifying_through);
        } else {
            ToastUtils.showToast(
                    R.string.verifying_fail);
        }
    }
}
