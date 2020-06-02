package com.android.life;

import android.app.Application;

import com.android.dialoglibrary.toast.ToastUtils;
import com.android.webviewlibrary.utils.X5WebUtils;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        X5WebUtils.init(this);
    }
}
