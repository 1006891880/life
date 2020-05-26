/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.android.webviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.webviewlibrary.interface1.InterWebListener;

/**
 * 自定义带进度条的webView
 */
public class ProgressWebView extends FrameLayout {

    private X5WebView webView;

    public ProgressWebView(@NonNull Context context) {
        super(context);
        init();
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProgressWebView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        Context context = getContext();
        if (context!=null){
            View view = LayoutInflater.from(context).inflate(
                    R.layout.view_progress_web_view, this, false);
            webView = view.findViewById(R.id.web_view);
            final ProgressBar pbProgress = view.findViewById(R.id.pb_progress);
            pbProgress.setVisibility(VISIBLE);
            webView.getX5WebChromeClient().setWebListener(new InterWebListener() {
                @Override
                public void hindProgressBar() {
                    pbProgress.setVisibility(GONE);
                }

                @Override
                public void showErrorView() {

                }

                @Override
                public void startProgress(int newProgress) {
                    pbProgress.setProgress(newProgress);
                }

                @Override
                public void showTitle(String title) {

                }
            });
        }
    }

    /**
     * 获取X5WebView对象
     * @return                                  获取X5WebView对象
     */
    public X5WebView getWebView(){
        return webView;
    }

}
