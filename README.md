# JsBridgeSample
JsBridgeDemo
- Android与Js通信的文章已经太多，而且都写的非常详细，这里只针对使用JsBridge的库进行二次封装使用进行介绍，使用简单。
- 为什么使用JsBridge?
  - Android 调用 Js 的方法 - Android 没法拿到返回值
  - Android调用Js方法时，需要手写完整的Js脚本和参数，出错几率大
  - Js无法知道调用Android本地方法成功与否，需要Android再次调用相关的Js方法，增加前端和Android开发的交互难度。
- JsBridge是'大头鬼'为Android与Js交互写的一个通用库，解决了Android无法通过回调与Js交互的问题，并且封装了Android与Js双向的调用方法。
JsBridge 开源地址：https://github.com/lzyzsd/JsBridge



----
### 使用步骤
- 1引入library依赖/导入aar并依赖、（由于只是对开源库进行的封装，所以不再发布到jCenter和mavenCentral）
- 2 将布局文件中的WebView控件替换为BridgeWebView

![图2-1](http://upload-images.jianshu.io/upload_images/2786935-84e757b57560a926.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
- 3   初始化工具类，将bridgeWebView传入
```
WebViewTools webViewTools = new WebViewTools(webView);
```
- 4-1 Js调用Android方法
####  注意：Js和Android方法的第一个参数需要两端保持一致，并且Js代码必须使用window.WebViewJavascriptBridge.callHandler(x,y,function(data))这种类型的方法，因为在Android端进行的Js文件的注入，如果不用该方法，Android端则与Js无法与正确交互
```
//Js代码
//Js方法第一个参数为两端约定好的方法名
//第二个参数为传递给Android的数据内容，String类型
//第三个参数为function回调
function testClick1() {
            //唤起本地方法
            window.WebViewJavascriptBridge.callHandler(
                'callNative'
                , {'data': 'data com from Js'}
                , function(responseData) {
                  //responseData是Android回调给Js的数据
                  //注意responseData只能是String类型
                }
            );
        }
```
----

```
//Android端代码
//第一个参数为两端约定好的方法名
//第二个参数为方法回调
   webViewTools.registerWebViewFunction("callNative", new JsCallBack() {
            @Override
            public void notifyNativeMethod() {
                //唤起本地功能
            }
            @Override
            public void JsData(String data) {
                Log.d("WebViewTools", "Js传递过来的数据"+data);
            }
            @Override
            public void CallBack(CallBackFunction function) {
                function.onCallBack("传递给Js的数据内容");
            }
        });
```
- 4-2Android调用Js方法
#### 注意：Js和Android方法的第一个参数需要两端保持一致，并且Js代码必须使用 bridge.registerHandler(x, function(data, responseCallback){}因为在Android端进行的Js文件的注入，如果不用该方法，Android端则与Js无法与正确交互
```
//Js代码
bridge.registerHandler("callJs", function(data, responseCallback) {
                //data为Android端传递过来的数据
                responseCallback("回调给Android的数据");
            });
```
---

```
//Android端代码
webViewTools.callWebFunction("callJs", "data from Java", new JavaCallBack() {
            @Override
            public void onJsResponse(String data) {
                Log.d("webViewTools", "Js的回调数据" + data);
            }
        });
```
----
## 这样就完成了Android和Js的双向调用，并且双向都能通过回调进行相应的数据交互。
