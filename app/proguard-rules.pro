# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Development\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-dontwarn okhttp3.**
-keep public class [com.lizihanglove.game2048].R$*{
public static final int *;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}

-keep class com.umeng.**

-keep public class com.idea.fifaalarmclock.app.R$*{
    public static final int *;
}

-keep public class com.umeng.fb.ui.ThreadView {
}

-dontwarn com.umeng.**

-dontwarn org.apache.commons.**

-keep public class * extends com.umeng.**

-keep class com.umeng.** {*; }

-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.idea.fifaalarmclock.entity.***
-keep class com.google.gson.stream.** { *; }