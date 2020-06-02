package com.android.life;

import android.app.Application;

import com.android.dialoglibrary.toast.ToastUtils;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
    }
}
