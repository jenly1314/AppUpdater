# AppUpdater

![Image](app/src/main/ic_launcher-web.png)

[![MavenCentral](https://img.shields.io/maven-central/v/com.github.jenly1314.AppUpdater/app-updater?logo=sonatype)](https://repo1.maven.org/maven2/com/github/jenly1314/AppUpdater)
[![JitPack](https://img.shields.io/jitpack/v/github/jenly1314/AppUpdater?logo=jitpack)](https://jitpack.io/#jenly1314/AppUpdater)
[![CI](https://img.shields.io/github/actions/workflow/status/jenly1314/AppUpdater/build.yml?logo=github)](https://github.com/jenly1314/AppUpdater/actions/workflows/build.yml)
[![Download](https://img.shields.io/badge/download-APK-brightgreen?logo=github)](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk)
[![API](https://img.shields.io/badge/API-15%2B-brightgreen?logo=android)](https://developer.android.com/guide/topics/manifest/uses-sdk-element#ApiLevels)
[![License](https://img.shields.io/github/license/jenly1314/AppUpdater?logo=open-source-initiative)](https://opensource.org/licenses/mit)

AppUpdater 是一个轻量级开源库，专注于实现 App 版本更新功能。它提供一键式集成方案，极大简化了 App 的升级流程，无需复杂配置，开箱即用，助你快速构建可靠的 App 更新体验。

* **AppUpdater** 核心库主要包括 **app-updater** 和 **app-dialog** 。

> **app-updater** 主要负责后台下载更新App，封装了所有下载配置细节，提供真正的一键式傻瓜升级体验。

> **app-dialog** 主要是提供常用的Dialog和DialogFragment，简化弹框提示的实现，支持灵活的布局样式定制。

* 下载更新和弹框提示之所以分开是因为这本来就是两个功能。二者完全独立，可以在解耦的同时减少侵入性。

> 如果你仅需要纯粹的App下载更新功能，单独依赖 **app-updater** 即可满足需求；

> 如果你需要在下载更新时配合对话框进行用户交互，那么 **app-updater** + **app-dialog** 的组合将是完美搭配；

> 当然，你也可以单独使用 **app-dialog**，它适用于任何需要对话框交互的场景。

## 功能介绍
- ✅ 专注于App更新一键傻瓜式升级
- ✅ 够轻量，体积小
- ✅ 支持监听下载和自定义下载流程
- ✅ 支持下载失败时，可重新下载
- ✅ 支持文件MD5校验，避免重复下载
- ✅ 支持通知栏提示内容和流程全部可配置
- ✅ 支持取消下载
- ✅ 支持使用HttpsURLConnection或OkHttpClient进行下载
- ✅ 支持Android 10(Q)
- ✅ 支持Android 11(R)
- ✅ 支持Android 12(S)

## 效果展示
![Image](GIF.gif)

> 你也可以直接下载 [演示App](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk) 体验效果

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle

    //app-updater
    implementation 'com.github.jenly1314.AppUpdater:app-updater:1.2.0'
    //app-dialog
    implementation 'com.github.jenly1314.AppUpdater:app-dialog:1.2.0'

    ```

## 使用

### 使用说明

**app-dialog** 和 **app-updater** 的使用方式简单直观，主要针对常见场景提供了丰富的预设配置。在充分保证灵活性的同时，尽可能的简化
使用流程：你可以直接采用默认配置，也可以根据实际需求进行个性化调整，只需关注需要修改的配置即可。

### 代码示例

```Java
    //一句代码，傻瓜式更新
    new AppUpdater(context,url).start();
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
                    new AppUpdater.Builder(context)
                            .setUrl(mUrl)
                            .build()
                            .start();
                    AppDialog.INSTANCE.dismissDialog();
                }
            });
    AppDialog.INSTANCE.showDialog(context,config);
```
```Java
    //简单DialogFragment升级
    AppDialogConfig config = new AppDialogConfig(context);
    config.setTitle("简单DialogFragment升级")
            .setConfirm("升级")
            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
            .setOnClickConfirm(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppUpdater appUpdater = new AppUpdater.Builder(context)
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

更多使用详情，请查看[app](app)中的源码使用示例或直接查看[API帮助文档](https://jenly1314.github.io/AppUpdater/api/)

### 补充说明

#### app-updater

*  不设置 **HttpManager** 时，默认使用 **HttpsURLConnection** 实现的 **HttpManager** 进行下载，如果想要使用 **OkHttpClient** 实现下载，需依赖 **okhttp** 库；（内部默认提供 **HttpManager** 和 **OkHttpManager** 两种实现）
*  支持下载APK时，优先取本地缓存策略，避免多次重复下载相同的APK文件；（校验方式支持文件的 **MD5** 或 **VersionCode** ）
*  如需自定义更新App时通知栏中的相关文案信息；你只需在 **string.xml** 定义相同的名字进行覆盖即可（ **app-updater** 中的资源定义都是以 **app_updater** 开头）。
*  不设置 **Notification** 时，默认使用 **NotificationImpl** 实现的，如果当前的通知栏的布局不满足你的需求，可通过参考 **NotificationImpl** 去自定义实现一个 **INotification** ；
*  **AppUpdater** 中的日志统一使用 **LogUtils** 进行管理，通过 **LogUtils.setShowLog** 可以全局设置是否显示日志；需要定位 **AppUpdater** 内部日志信息时，只需过滤以 **AppUpdater** 开头的 **Log Tag** 即可。
*  **AppUpdater** 的更多配置说明请查看 **AppUpdater.Builder** 或 **UpdateConfig**；方法上基本都有详细的说明。

#### app-dialog

* **AppDialogConfig** 主要提供一些对话框配置，内部提供了一套默认的配置，你也可以通过 **AppDialogConfig** 对外暴露的方法，自定义对话框配置；**AppDialog** 主要负责对话框的显示与消失；通过 **AppDialog** 和 **AppDialogConfig**，你可以很容易的实现一个自定义对话框；
* **AppDialog** 足够通用，其内部实现了一套最常见对话框，并给予一系列的默认配置，让使用者可以尽可能的通过少的配置就能实现功能；**AppDialog** 也足够抽象，对话框布局样式是可随意定制；
* 基于以上几点，这里说个特别的需求：如需你想不通过自定义布局的方式定义对话框布局，同时默认的对话框的文字或按钮颜色不太符合你的需求场景，只想修改 **AppDialog** 内置默认对话框提示文字的颜色（包括按钮文字），你可以通过在 **colors.xml** 定义相同的名字进行覆盖即可（ **app-dialog** 中的资源定义都是以 **app_dialog** 开头）。

## 混淆

**app-updater** [Proguard rules](app-updater/proguard-rules.pro)

**app-dialog** [Proguard rules](app-dialog/proguard-rules.pro)

## 相关推荐

- [RetrofitHelper](https://github.com/jenly1314/RetrofitHelper) 是一个支持配置多个BaseUrl，支持动态改变BaseUrl，动态配置超时时长的Retrofit帮助类。
- [BaseUrlManager](https://github.com/jenly1314/BaseUrlManager) BaseUrl管理器，主要打测试包时，一个App可动态切换到不同的开发环境或测试环境。
- [MVVMFrame](https://github.com/jenly1314/MVVMFrame) 一个基于Google官方推出的JetPack构建的MVVM快速开发框架。
- [LogX](https://github.com/jenly1314/LogX) 一个轻量而强大的日志框架；好用不解释。

<!-- end -->

## 版本日志

#### v1.2.0：2023-7-9
* 更新Gradle至v7.3.3
* 优化lint检测

#### [查看更多版本日志](CHANGELOG.md)

---

![footer](https://jenly1314.github.io/page/footer.svg)
