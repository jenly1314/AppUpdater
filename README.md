# AppUpdater

![Image](app/src/main/ic_launcher-web.png)

[![Download](https://img.shields.io/badge/download-App-blue.svg)](https://raw.githubusercontent.com/jenly1314/AppUpdater/master/app/release/app-release.apk)
[![](https://jitpack.io/v/jenly1314/AppUpdater.svg)](https://jitpack.io/#jenly1314/AppUpdater)
[![CI](https://travis-ci.org/jenly1314/AppUpdater.svg?branch=master)](https://travis-ci.org/jenly1314/AppUpdater)
[![API](https://img.shields.io/badge/API-15%2B-blue.svg?style=flat)](https://android-arsenal.com/api?level=15)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](https://opensource.org/licenses/mit-license.php)
[![Blog](https://img.shields.io/badge/blog-Jenly-9933CC.svg)](http://blog.csdn.net/jenly121)

AppUpdater for Android 是一个专注于App更新一键傻瓜式集成的开源库，主要包括app-updater和app-dialog。
> 下载更新和弹框提示分开，是因为这本来就是两个逻辑。完全独立开来能有效的解耦。
* app-updater 主要负责后台下载更新App，无需担心下载时各种配置相关的细节，一键傻瓜式升级。
* app-dialog 主要是提供常用的Dialog和DialogFragment，简化弹框提示，样式支持高度自定义。
> app-updater + app-dialog 配合使用，谁用谁知道。


## 功能介绍
- [x] 专注于App更新一键傻瓜式升级
- [x] 支持下载监听
- [x] 支持下载失败，重新下载
- [x] 支持通知栏提示内容和过程全部可配置
- [x] 支持Android O



## Gif 展示
![Image](GIF.gif)

## 引入

### Maven：
```maven
    //app-updater
    <dependency>
      <groupId>com.king.app</groupId>
      <artifactId>app-updater</artifactId>
      <version>1.0</version>
      <type>pom</type>
    </dependency>
    
    //app-dialog
    <dependency>
      <groupId>com.king.app</groupId>
      <artifactId>app-dialog</artifactId>
      <version>1.0</version>
      <type>pom</type>
    </dependency>
```
### Gradle:
```gradle
    //app-updater
    compile 'com.king.app:app-updater:1.0'
    
    //app-dialog
    compile 'com.king.app:app-dialog:1.0'
```
### Lvy:
```lvy
    //app-updater
    <dependency org='com.king.app' name='app-dialog' rev='1.0'>
      <artifact name='$AID' ext='pom'></artifact>
    </dependency>
    
    //app-dialog
    <dependency org='com.king.app' name='app-dialog' rev='1.0'>
      <artifact name='$AID' ext='pom'></artifact>
    </dependency>
```

###### 如果Gradle出现compile失败的情况，可以在Project的build.gradle里面添加如下：（也可以使用上面的GitPack来complie）
```gradle
    allprojects {
        repositories {
            //...
            maven { url 'https://dl.bintray.com/jenly/maven' }
        }
    }
```

## 示例

```Java
    //一句代码，傻瓜式更新
    new AppUpdater(getContext(),url).start();
```
```Java
    //简单弹框升级
    AppDialogConfig config = new AppDialogConfig();
    config.setTitle("简单弹框升级")
            .setOk("升级")
            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
            .setOnClickOk(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AppUpdater.Builder()
                            .serUrl(mUrl)
                            .setFilename("AppUpdater.apk")
                            .build(getContext())
                            .start();
                    AppDialog.INSTANCE.dismissDialog();
                }
            });
    AppDialog.INSTANCE.showDialog(getContext(),config);
```
```Java
    //简单DialogFragment升级
    AppDialogConfig config = new AppDialogConfig();
    config.setTitle("简单DialogFragment升级")
            .setOk("升级")
            .setContent("1、新增某某功能、\n2、修改某某问题、\n3、优化某某BUG、")
            .setOnClickOk(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AppUpdater.Builder()
                            .serUrl(mUrl)
                            .setFilename("AppUpdater.apk")
                            .build(getContext())
                            .start();
                    AppDialog.INSTANCE.dismissDialogFragment(getSupportFragmentManager());
                }
            });
    AppDialog.INSTANCE.showDialogFragment(getSupportFragmentManager(),config);

```

更多使用示例请查看[App](app)。


## 关于我
   Name: <a title="关于作者" href="https://about.me/jenly1314" target="_blank">Jenly</a>

   Email: <a title="欢迎邮件与我交流" href="mailto:jenly1314@gmail.com" target="_blank">jenly1314#gmail.com</a> / <a title="给我发邮件" href="mailto:jenly1314@vip.qq.com" target="_blank">jenly1314#vip.qq.com</a>

   CSDN: <a title="CSDN博客" href="http://blog.csdn.net/jenly121" target="_blank">jenly121</a>

   Github: <a title="Github开源项目" href="https://github.com/jenly1314" target="_blank">jenly1314</a>

   微信公众号:

   ![公众号](http://olambmg9j.bkt.clouddn.com/jenly666.jpg)

   加入QQ群: <a title="点击加入QQ群" href="http://shang.qq.com/wpa/qunwpa?idkey=8fcc6a2f88552ea44b1411582c94fd124f7bb3ec227e2a400dbbfaad3dc2f5ad" target="_blank">20867961</a>