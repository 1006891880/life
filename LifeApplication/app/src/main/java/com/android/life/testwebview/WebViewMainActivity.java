package com.android.life.testwebview;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.library.base.Constant;
import com.android.life.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

// 在 29 ，28 加载 x5WebVew 会有问题
public class WebViewMainActivity extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2_1).setOnClickListener(this);
        findViewById(R.id.tv_2_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
        findViewById(R.id.tv_7).setOnClickListener(this);
        findViewById(R.id.tv_8).setOnClickListener(this);
        findViewById(R.id.tv_9).setOnClickListener(this);
        startPermissionsTask();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1:
                startActivity(new Intent(this, WebViewFirstActivity.class));
                break;
            case R.id.tv_2_1:
                startActivity(new Intent(this, WebViewSecondActivity.class));
                break;
            case R.id.tv_2_2:
                startActivity(new Intent(this,WebViewActivity.class));
                break;
            case R.id.tv_3:
                startActivity(new Intent(this, WebViewThreeActivity.class));
                break;
            case R.id.tv_5:
                startActivity(new Intent(this, WebViewFiveActivity.class));
                break;
            case R.id.tv_6:
                startActivity(new Intent(this, WebViewSixActivity.class));
                break;
            case R.id.tv_7:
                startActivity(new Intent(this, WebViewNativeActivity.class));
                break;
            case R.id.tv_8:
                startActivity(new Intent(this, WebViewEightActivity.class));
                break;
            case R.id.tv_9:
                startActivity(new Intent(this, WebViewFileDisplayActivity.class));
                break;
            default:
                break;
        }
    }


    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    private static final String[] LOCATION_AND_CONTACTS = {
            Manifest.permission.CALL_PHONE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.ACCESS_WIFI_STATE

    };

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void startPermissionsTask() {
        //检查是否获取该权限
        if (hasPermissions()) {
            //具备权限 直接进行操作
//            if (SPUtils.getInstance(Constant.SP_NAME).getBoolean(
//                    Constant.KEY_FIRST_SPLASH, true)) {
//                ActivityUtils.startActivity(SplashPagerActivity.class);
//            } else {
//                ActivityUtils.startActivity(GuideActivity.class);
//            }
//            finish();
        } else {
            //权限拒绝 申请权限
            //第二个参数是被拒绝后再次申请该权限的解释
            //第三个参数是请求码
            //第四个参数是要申请的权限

            EasyPermissions.requestPermissions(this,
                    this.getResources().getString(R.string.easy_permissions),
                    RC_LOCATION_CONTACTS_PERM, LOCATION_AND_CONTACTS);
        }
    }


    /**
     * 判断是否添加了权限
     * @return true
     */
    private boolean hasPermissions() {
        return EasyPermissions.hasPermissions(this, LOCATION_AND_CONTACTS);
    }


    /**
     * 将结果转发到EasyPermissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // 将结果转发到EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode,
                permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        //某些权限已被授予
        Log.d("权限", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    /**
     * 某些权限已被拒绝
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        //某些权限已被拒绝
        Log.d("权限", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder builder = new AppSettingsDialog.Builder(this);
            builder.setTitle("允许权限")
                    .setRationale("没有该权限，此应用程序部分功能可能无法正常工作。打开应用设置界面以修改应用权限")
                    .setPositiveButton("去设置")
                    .setNegativeButton("取消")
                    .setRequestCode(124)
                    .build()
                    .show();
        }
    }


}
