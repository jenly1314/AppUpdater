
## 待发布版本

> 判断是否有待发布版本，只需查看 [README的版本日志](https://github.com/jenly1314/AppUpdater?tab=readme-ov-file#%E7%89%88%E6%9C%AC%E6%97%A5%E5%BF%97) 的最新记录是否是 **待发布版本** 。

待发布版本暂使用 **Jit Pack** 仓库；待收集的一些问题测试稳定后，再统一发布正式版本至 **Maven Central** 仓库。

## 引入

### Gradle:

1. 在Project的 **build.gradle** 或 **setting.gradle** 中添加远程仓库

    ```gradle
    repositories {
        //...
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
    ```

2. 在Module的 **build.gradle** 里面添加引入依赖项

    ```gradle

    //app-updater
    implementation 'com.github.jenly1314.AppUpdater:app-updater:-SNAPSHOT'
    //app-dialog
    implementation 'com.github.jenly1314.AppUpdater:app-dialog:-SNAPSHOT'
    ```
