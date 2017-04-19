package com.cicinnus.jsbridgesample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.kitco.utils.JavaCallBack;
import com.kitco.utils.JsCallBack;
import com.kitco.utils.WebViewSettingUtil;
import com.kitco.utils.WebViewTools;
import com.webview.tools.jsbridge.BridgeWebView;
import com.webview.tools.jsbridge.CallBackFunction;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "JsBridgeTest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BridgeWebView webView = (BridgeWebView) findViewById(R.id.webView);
        WebViewSettingUtil.initWebViewSetting(this, webView);
        webView.loadUrl("file:///android_asset/demo.html");


        final WebViewTools webViewTools = new WebViewTools(webView);
        webViewTools.registerWebViewFunction("callNative", new JsCallBack() {
            @Override
            public void notifyNativeMethod() {
                Toast.makeText(MainActivity.this, "唤起本地某方法", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void JsData(String data) {
                Log.d(TAG, "Js传递过来的数据" + data);
            }

            @Override
            public void CallBack(CallBackFunction function) {
                function.onCallBack("传递给Js的数据");
            }
        });
        findViewById(R.id.btn_callJs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webViewTools.callWebFunction("callJs", "data from Java", new JavaCallBack() {
                    @Override
                    public void onJsResponse(String data) {
                        Toast.makeText(MainActivity.this, "Js的回调内容：" + data, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
