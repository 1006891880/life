package com.android.life

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.library.base.Constant
import com.android.life.testbannerlib.BannerActivity
import com.android.life.testdialog.DialogMainActivity
import com.android.life.testwebview.WebViewMainActivity
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.EasyPermissions

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    fun  onBannerBar(view : View){
        startActivity(Intent(this, BannerActivity::class.java))
    }
    fun  onDialogClick(view : View){
        startActivity(Intent(this, DialogMainActivity::class.java))
    }
    fun onWebViewClick(view: View){
        startActivity(Intent(this, WebViewMainActivity::class.java))
    }




}
