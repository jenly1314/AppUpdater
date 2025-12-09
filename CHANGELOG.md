## 版本日志

#### v2.1.0：2025-12-9
* 优化细节 (progress,total 变更 int -> long)
* 优化进度显示（[#44](https://github.com/jenly1314/AppUpdater/issues/44)）
* 更新Gradle至v8.6

#### v2.0.1：2025-8-31
* 优化AppUpdater中属性的访问权限

#### v2.0.0：2025-8-30
* 统一采用Kotlin进行了重构
* 更新minSdk至21
* 更新compileSdk至34
* 更新Gradle至v8.5
* 使用[LogX](https://github.com/jenly1314/LogX) 统一管理日志

#### v1.2.0：2023-7-9
* 更新Gradle至v7.3.3
* 优化lint检测

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
*  新增OkHttpManager，如果使用了OkHttpManager则必须依赖[okhttp](https://github.com/square/okhttp)
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

#### v1.0 ：2018-6-29
*  AppUpdater初始版本
