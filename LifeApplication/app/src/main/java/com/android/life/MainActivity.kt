package com.android.life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.life.testbannerlib.BannerActivity
import java.sql.Array
import java.util.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun  onBannerBar(view : View){
        startActivity(Intent(this, BannerActivity::class.java))
    }

}
