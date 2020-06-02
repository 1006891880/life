package com.android.life.testwebview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.life.R;
import com.android.webviewlibrary.DefaultHandler;
import com.android.webviewlibrary.X5WebView;
import com.android.webviewlibrary.interface1.BridgeHandler;
import com.android.webviewlibrary.interface1.CallBackFunction;
import com.android.webviewlibrary.interface1.InterWebListener;


public class WebViewNativeActivity extends AppCompatActivity {

    private X5WebView mWebView;
    private ProgressBar pb;
    private Button btn;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack() && event.getKeyCode() ==
                KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            mWebView.clearHistory();
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
            //mWebView = null;
        }
        super.onDestroy();
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mWebView != null) {
            mWebView.getSettings().setJavaScriptEnabled(false);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_web_view);
        initData();
        initView();
    }


    public void initData() {

    }

    public void initView() {
        mWebView = findViewById(R.id.web_view);
        pb = findViewById(R.id.pb);
        btn = findViewById(R.id.btn);
        mWebView.loadUrl("file:///android_asset/demo.html");
        mWebView.getX5WebChromeClient().setWebListener(interWebListener);
        mWebView.getX5WebViewClient().setWebListener(interWebListener);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.callHandler("functionInJs", "data from Java",
                        new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {
                        Log.i("java调用web----", "reponse data from js " + data);
                    }

                });
                /*具体看demo.html的这段代码
                bridge.registerHandler("functionInJs", function(data, responseCallback) {
                    document.getElementById("show").innerHTML = ("data from Java: = " + data);
                    if (responseCallback) {
                        var responseData = "Javascript Says Right back aka!";
                        responseCallback(responseData);
                    }
                });*/
            }
        });

        //js交互方法
        initWebViewBridge();
    }


    private InterWebListener interWebListener = new InterWebListener() {
        @Override
        public void hindProgressBar() {
            pb.setVisibility(View.GONE);
        }

        @Override
        public void showErrorView() {

        }

        @Override
        public void startProgress(int newProgress) {
            pb.setProgress(newProgress);
        }

        @Override
        public void showTitle(String title) {

        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //这个是处理回调逻辑
        mWebView.getX5WebChromeClient().uploadMessageForAndroid5(data,resultCode);
    }

    @JavascriptInterface
    public void initWebViewBridge() {
        mWebView.setDefaultHandler(new DefaultHandler());
        //js调native
        mWebView.registerHandler("toPhone", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {

            }
        });
        mWebView.registerHandler("submitFromWeb", new BridgeHandler() {
            @Override
            public void handler(String data, CallBackFunction function) {
                Toast.makeText(WebViewNativeActivity.this,data,Toast.LENGTH_LONG).show();
                Log.i("registerHandler", "handler = submitFromWeb, data from web = " + data);
                //这个是回调给web端，比如
                function.onCallBack("submitFromWeb exe, response data 中文 from Java");
            }
        });

        mWebView.callHandler("functionInJs", "小杨逗比", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                Toast.makeText(WebViewNativeActivity.this,data,Toast.LENGTH_LONG).show();
            }
        });
    }

}
