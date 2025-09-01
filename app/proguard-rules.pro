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

-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keepclassmembers class okhttp3.** {
    public *;
}

-keep class com.android.game.xiaomi.** { *; }
-keep interface com.android.game.xiaomi.** { *; }
-keepclassmembers class com.android.game.xiaomi.** {
    public *;
}
-keep class com.android.game.oppo.** { *; }
-keep interface com.android.game.oppo.** { *; }
-keepclassmembers class com.android.game.oppo.** {
    public *;
}
-keep class com.android.game.iap.** { *; }
-keep interface com.android.game.iap.** { *; }
-keepclassmembers class com.android.game.iap.** {
    public *;
}
-keep class com.nearme.** { *; }
-keep interface com.nearme.** { *; }
-keepclassmembers class com.nearme.** {
    public *;
}

-ignorewarnings
-keepattributes *Annotation*
-keepattributes Exceptions
-keepattributes InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
-keep class com.huawei.hianalytics.**{*;}
-keep class com.huawei.updatesdk.**{*;}
-keep class com.huawei.hms.**{*;}