# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.king.app.updater.**
-keep class com.king.app.updater.**{ *;}
-keep class * extends com.king.app.updater.**{ *;}
-keep class * implements com.king.app.updater.**{ *;}
-keepattributes InnerClasses

-keep public class * extends android.app.Service

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
