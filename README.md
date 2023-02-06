# AppUpdater

![Image](app/src/main/ic_launcher-web.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk)
[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314.AppUpdater/app-updater)](https://repo1.maven.org/maven2/com/github/jenly1314/AppUpdater)
[![JCenter](https://img.shields.io/badge/JCenter-1.0.10-46C018.svg)](https://bintray.com/beta/#/jenly/maven/app-updater)
[![JitPack](https://jitpack.io/v/jenly1314/AppUpdater.svg)](https://jitpack.io/#jenly1314/AppUpdater)
[![CI](https://travis-ci.org/jenly1314/AppUpdater.svg?branch=master)](https://travis-ci.org/jenly1314/AppUpdater)
[![CircleCI](https://circleci.com/gh/jenly1314/AppUpdater.svg?style=svg)](https://circleci.com/gh/jenly1314/AppUpdater)
[![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)
[![Blog](https://img.shields.io/badge/blog-Jenly-9933CC.svg)](https://jenly1314.github.io/)
[![QQGroup](https://img.shields.io/badge/QQGroup-20867961-blue.svg)](http://shang.qq.com/wpa/qunwpa?idkey=8fcc6a2f88552ea44b1411582c94fd124f7bb3ec227e2a400dbbfaad3dc2f5ad)

AppUpdater for Android 是一个专注于App更新，一键傻瓜式集成App版本升级的轻量开源库。(无需担心各种细节的处理和适配问题；包括但不仅限于：通知栏适配、重复下载、文件访问授权、App安装等问题；这些AppUpdater都已帮您处理好。)
 
* 核心库主要包括 **app-updater** 和 **app-dialog**。

> **app-updater** 主要负责后台下载更新App，无需担心下载时各种配置相关的细节，一键傻瓜式升级。

> **app-dialog** 主要是提供常用的Dialog和DialogFragment，简化弹框提示，布局样式可随意定制。
 
* 下载更新和弹框提示之所以分开是因为这本来就是两个功能。二者完全独立，可以在解耦同时减少侵入性。

> 如果你只需要单纯的 App下载更新功能，仅依赖 **app-updater** 即可；如果你在需要App下载更新功能的同时，还需要有对话框来进行交互； 那么 **app-updater** + **app-dialog** 二者配合使用，谁用谁知道。


## 功能介绍
- [x] 专注于App更新一键傻瓜式升级
- [x] 够轻量，体积小
- [x] 支持监听下载和自定义下载流程
- [x] 支持下载失败时，可重新下载
- [x] 支持文件MD5校验，避免重复下载
- [x] 支持通知栏提示内容和流程全部可配置
- [x] 支持取消下载
- [x] 支持使用HttpsURLConnection或OkHttpClient进行下载
- [x] 支持Android 10(Q)
- [x] 支持Android 11(R)
- [x] 支持Android 12(S)

## Gif 展示
![Image](GIF.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:

1. 在Project的 **build.gradle** 里面添加远程仓库  
          
```gradle
allprojects {
    repositories {
        //...
        mavenCentral()
    }
}
```

2. 在Module的 **build.gradle** 里面添加引入依赖项

```gradle

    //----------AndroidX 版本
    //app-updater
    implementation 'com.github.jenly1314.AppUpdater:app-updater:1.1.4'
    //app-dialog
    implementation 'com.github.jenly1314.AppUpdater:app-dialog:1.1.4'

```


以前发布至JCenter的版本

```gradle

    //----------AndroidX 版本
    //app-updater
    implementation 'com.king.app:app-updater:1.0.10-androidx'
    //app-dialog
    implementation 'com.king.app:app-dialog:1.0.10-androidx'

    //----------Android Support 版本
    //app-updater
    implementation 'com.king.app:app-updater:1.0.10'
    //app-dialog
    implementation 'com.king.app:app-dialog:1.0.10'
```

## 示例

```Java
    //一句代码，傻瓜式更新
    new AppUpdater(getContext(),url).start();
```
```Java
    //简单弹框升级
    AppDialogConfig config = new AppDialogConfig(context);
    config.setTitle("简单弹框升级")
            .setConfirm("升级") //旧版本使用setOk
            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
            .setOnClickConfirm(new View.OnClickListener() { // 旧版本使用setOnClickOk
                @Override
                public void onClick(View v) {
                    new AppUpdater.Builder(getContext())
                            .setUrl(mUrl)
                            .build()
                            .start();
                    AppDialog.INSTANCE.dismissDialog();
                }
            });
    AppDialog.INSTANCE.showDialog(getContext(),config);
```
```Java
    //简单DialogFragment升级
    AppDialogConfig config = new AppDialogConfig(getContext());
    config.setTitle("简单DialogFragment升级")
            .setConfirm("升级")
            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
            .setOnClickConfirm(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUpdater appUpdater = new AppUpdater.Builder(getContext())
                            .setUrl(mUrl)
                            .build();
                    appUpdater.setHttpManager(OkHttpManager.getInstance()) // 使用OkHttp的实现进行下载
                            .setUpdateCallback(new UpdateCallback() { // 更新回调
                                @Override
                                public void onDownloading(boolean isDownloading) {
                                    // 下载中：isDownloading为true时，表示已经在下载，即之前已经启动了下载；为false时，表示当前未开始下载，即将开始下载
                                }

                                @Override
                                public void onStart(String url) {
                                    // 开始下载
                                }

                                @Override
                                public void onProgress(long progress, long total, boolean isChanged) {
                                    // 下载进度更新：建议在isChanged为true时，才去更新界面的进度；因为实际的进度变化频率很高
                                }

                                @Override
                                public void onFinish(File file) {
                                    // 下载完成
                                }

                                @Override
                                public void onError(Exception e) {
                                    // 下载失败
                                }

                                @Override
                                public void onCancel() {
                                    // 取消下载
                                }
                            }).start();

                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                }
            });
    AppDialog.INSTANCE.showDialogFragment(getSupportFragmentManager(), config);

```

更多使用详情，请查看[app](app)中的源码使用示例或直接查看[API帮助文档](https://javadoc.jitpack.io/com/github/jenly1314/AppUpdater/1.1.0/javadoc/index.html)

## 补充说明

### app-updater

*  不设置 **HttpManager** 时，默认使用 **HttpsURLConnection** 实现的 **HttpManager** 进行下载，如果想要使用 **OkHttpClient** 实现下载，需依赖 **okhttp** 库；（内部默认提供 **HttpManager** 和 **OkHttpManager** 两种实现）
*  支持下载APK时，优先取本地缓存策略，避免多次重复下载相同的APK文件；（校验方式支持 **文件MD5** 或 **VersionCode** 比对）
*  如需自定义更新App时通知栏中的相关文案信息；你只需在 **string.xml** 定义相同的名字进行覆盖即可（**app-updater** 中的资源定义都是以 **app_updater** 开头）。
*  不设置 **Notification** 时，默认使用 **NotificationImpl** 实现的，如果当前的通知栏的布局不满足你的需求，可通过参考 **NotificationImpl** 去自定义实现一个 **INotification**；
*  **AppUpdater** 中的日志统一使用 **LogUtils** 进行管理，通过 **LogUtils.setShowLog** 可以全局设置是否显示日志；需要定位**AppUpdater** 内部日志信息时，只需过滤以 **AppUpdater** 开头的 **Log Tag** 即可。
*  **AppUpdater** 的更多配置说明请查看 **AppUpdater.Builder** 或 **UpdateConfig**；方法上基本都有详细的说明。

### app-dialog

* **AppDialogConfig** 主要提供一些对话框配置，内部提供了一套默认的配置，你也可以通过 **AppDialogConfig** 对外暴露的方法，自定义对话框配置；**AppDialog** 主要负责对话框的显示与消失；通过 **AppDialog** 和 **AppDialogConfig**，你可以很容易的实现一个自定义对话框；
* **AppDialog** 足够通用，其内部实现了一套最常见对话框，并给予一系列的默认配置，让使用者可以尽可能的通过少的配置就能实现功能；**AppDialog** 也足够抽象，对话框布局样式是可随意定制；
* 基于以上几点，这里列个特别的场景说明：如需你想不通过自定义布局的方式定义对话框布局，同时默认的对话框的文字或按钮颜色不太符合你的需求场景，只想修改 **AppDialog** 内置默认对话框提示文字的颜色（包括按钮文字），你可以通过在 **colors.xml** 定义相同的名字进行覆盖即可（**app-dialog** 中的资源定义都是以 **app_dialog** 开头）。

## 混淆

**app-updater** [Proguard rules](app-updater/proguard-rules.pro)

**app-dialog** [Proguard rules](app-dialog/proguard-rules.pro)

## 版本记录

#### v1.1.4：2023-2-5
* 优化注释
* 优化细节

#### v1.1.3：2022-4-25
*  统一日志管理
*  适配Android 12(S)
*  优化细节

#### v1.1.2：2021-11-18
*  AppDialog对外提供更多与WindowManager.LayoutParams相关的配置

#### v1.1.1：2021-9-14
*  对外提供更多可配置参数
*  优化细节

#### v1.1.0：2021-7-2  (从v1.1.0开始不再发布至JCenter)
* 后续版本只支持androidx，版本名称不再带有androidx标识
* 优化细节
* 迁移发布至Maven Central

#### v1.0.10：2021-3-4
*  AppDialogConfig添加构造参数，简化自定义扩展用法
*  优化细节

#### v1.0.9：2020-12-11
*  优化默认Dialog样式的显示细节

#### v1.0.8：2020-1-2
*  支持MD5校验
*  对外提供获取Dialog方法

#### v1.0.7：2019-12-18
*  优化细节

#### v1.0.6：2019-11-27
*  新增OkHttpManager        如果使用了OkHttpManager则必须依赖[okhttp](https://github.com/square/okhttp)
*  优化细节 (progress,total 变更 int -> long)

#### v1.0.5：2019-9-4
*  支持取消下载

#### v1.0.4：2019-6-4      [开始支持AndroidX版本](https://github.com/jenly1314/AppUpdater/tree/androidx)
*  支持添加请求头

#### v1.0.3：2019-5-9
*  新增支持下载APK优先取本地缓存，避免多次下载相同版本的APK文件
*  AppDialog支持隐藏Dialog的标题

#### v1.0.2：2019-3-18
*  新增通知栏是否震动和铃声提示配置
*  AppDialogConfig新增getView(Context context)方法

#### v1.0.1：2019-1-10
*  升级Gradle到4.6

#### v1.0  ：2018-6-29
*  AppUpdater初始版本

## 赞赏
如果您喜欢AppUpdater，或感觉AppUpdater帮助到了您，可以点右上角“Star”支持一下，您的支持就是我的动力，谢谢 :smiley:<p>
您也可以扫描下面的二维码，请作者喝杯咖啡 :coffee:
<div>
<img src="https://jenly1314.github.io/image/pay/sponsor.png" width="98%">
</div>

## 关于我
Name: <a title="关于作者" href="https://jenly1314.github.io" target="_blank">Jenly</a>

Email: <a title="欢迎邮件与我交流" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314#gmail.com</a> / <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314#vip.qq.com</a>

CSDN: <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>

CNBlogs: <a title="博客园" href="https://www.cnblogs.com/jenly" target="_blank">jenly</a>

GitHub: <a title="GitHub开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a>

Gitee: <a title="Gitee开源项目" href="https://gitee.com/jenly1314" target="_blank">jenly1314</a>

加入QQ群: <a title="点击加入QQ群" href="http://shang.qq.com/wpa/qunwpa?idkey=8fcc6a2f88552ea44b1411582c94fd124f7bb3ec227e2a400dbbfaad3dc2f5ad" target="_blank">20867961</a>
   <div>
       <img src="https://jenly1314.github.io/image/jenly666.png">
       <img src="https://jenly1314.github.io/image/qqgourp.png">
   </div>

