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
-keep class com.google.android.material.** { *; }
-keep interface com.google.android.material.** { *; }

#im
-dontwarn com.netease.nim.**
-keep class com.netease.nim.** {*;}

-dontwarn com.netease.nimlib.**
-keep class com.netease.nimlib.** {*;}

-dontwarn com.netease.share.**
-keep class com.netease.share.** {*;}

-dontwarn com.netease.mobsec.**
-keep class com.netease.mobsec.** {*;}

#全文检索插件需要添加
-dontwarn org.apache.lucene.**
-keep class org.apache.lucene.** {*;}

#数据库功能需要添加
-keep class net.sqlcipher.** {*;}


#小米推送
-keep class com.xiaomi.** {*;}


-keep class com.luck.picture.lib.** { *; }

#use Camerax
-keep class com.luck.lib.camerax.** { *; }

# use uCrop
-dontwarn com.yalantis.ucrop**
-keep class com.yalantis.ucrop** { *; }
-keep interface com.yalantis.ucrop** { *; }

# Gson 反射使用
-keep class com.yourpackage.model.** { *; }
-keepattributes Signature
-keepattributes *Annotation*

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** { *; }
-keep class jp.wasabeef.glide.transformations.** { *; }

# 保留 ZXing 核心库
-keep class com.google.zxing.** { *; }
-dontwarn com.google.zxing.**

# 保留 zxing-android-embedded
-keep class com.journeyapps.barcodescanner.** { *; }
-dontwarn com.journeyapps.barcodescanner.**

# 如果使用了反射（一般不会），可以加上：
-keepclassmembers class * {
    @com.journeyapps.barcodescanner.* <fields>;
}

# 保留 OkHttp 内部平台类，防止 R8 删除
-keep class okhttp3.internal.platform.** { *; }
-dontwarn okhttp3.internal.platform.**

# 保留 BouncyCastle 的 TLS 支持
-keep class org.bouncycastle.** { *; }
-dontwarn org.bouncycastle.**

-keep class * {
    public <init>(...);
}
-keepclassmembers class * {
    public <init>(...);
}

# 保留 Activity 和 Fragment
-keep class * extends android.app.Activity
-keep class * extends androidx.fragment.app.Fragment

# 保留所有序列化类
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object readResolve();
    java.lang.Object writeReplace();
}


# DataBinding
-keep class androidx.databinding.** { *; }

# 删除 Log.d / Log.v
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
}


# 保留 DataBinding 自动生成的类
-keep class **BR { *; }
-keep class *BindingImpl { *; }

# 保留带有 @Bindable 注解的字段
-keepclassmembers class * {
    @androidx.databinding.Bindable <fields>;
}

# 防止 R8 删除 setVariable 方法
-keepclassmembers class * extends androidx.databinding.ViewDataBinding {
    public boolean setVariable(int, java.lang.Object);
}

-keep class com.core.commonsdk.** { *; }