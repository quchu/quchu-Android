# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Users\Administrator\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
   public *;
}

## -----------------------------------------
################## eventbus混淆  ########
## -----------------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepclassmembers class ** {
    public void setFloating*(**);
}

## -----------------------------------------
################## leakcanary混淆  ########
## -----------------------------------------
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }
## -----------------------------------------
################## butterknife混淆  ########
## -----------------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }


-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
## -----------------------------------------
################## 友盟统计混淆 start ######
## -----------------------------------------
-keepclassmembers class * {
  public <init>(org.json.JSONObject);
}
-keep public class co.quchu.quchu.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 以下类过滤不混淆
-keep public class * extends com.umeng.**
-keep class com.umeng.** { *; }

##反射字段混淆
-keep class android.support.v4.view.ViewPager{*;}
-keep class android.widget.ScrollView{*;}



## ----------------------------------
##   ########## wecha混淆    ########
## ----------------------------------
-keep class com.tencent.mm.sdk.openapi.WXMediaMessage {*;}
-keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}

# # -------------------------------------------
# # ############### volley混淆  ###############
# # -------------------------------------------
-keep class com.android.volley.** {*;}
-keep class com.android.volley.toolbox.** {*;}
-keep class com.android.volley.Response$* { *; }
-keep class com.android.volley.Request$* { *; }
-keep class com.android.volley.RequestQueue$* { *; }
-keep class com.android.volley.toolbox.HurlStack$* { *; }
-keep class com.android.volley.toolbox.ImageLoader$* { *; }
-keep class com.android.volley.*{*; }
-keep class com.android.volley.toolbox.*{*; }
-keepattributes *Annotation*

-dontwarn com.squareup.okhttp.**

-keep class com.squareup.okhttp.** { *;}


## ----------------------------------
##   ########## OkHttp混淆    #######
## ----------------------------------
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
## ----------------------------------
##   ########## Fresco混淆    #######
## ----------------------------------
#-keep class com.facebook.** {*;}
#-keep public class * extends com.facebook.**
#-dontwarn  com.facebook.**
#-keep class com.facebook.** {*;}

# Keep our interfaces so they can be used by other ProGuard rules.
# See http://sourceforge.net/p/proguard/bugs/466/
-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}

# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn com.android.volley.toolbox.**





## ----------------------------------
##   ########## photoView混淆  ######
## ----------------------------------
#-keep class uk.co.senab.**
#-keep class com.qiniu.**

## ----------------------------------
##   ########## 微博混淆    #########
## ----------------------------------
-dontwarn com.weibo.sdk.android.WeiboDialog
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient
-keep public class android.net.http.SslError{
     *;
}
-keep public class android.webkit.WebViewClient{
    *;
}
-keep public class android.webkit.WebChromeClient{
    *;
}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {
    *;
}
-keep public interface android.webkit.ValueCallback {
    *;
}
-keep class * implements android.webkit.WebChromeClient {
    *;
}
## ----------------------------------
##   ##### imageloader混淆  #########
## ----------------------------------
# -dontwarn com.nostra13.universalimageloader.**
#-keep class com.nostra13.universalimageloader.** { *; }
## ----------------------------------
##   ########## 七牛混淆    #########
## ----------------------------------
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings
## ----------------------------------
##   ########## 微信混淆    #########
## ----------------------------------
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}

## ----------------------------------
##   ########## 高德地图混淆    #########
## ----------------------------------
-libraryjars libs/BaiduLBS_Android.jar #替换成自己所用版本的jar包
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
## ----------------------------------
##   ########## nineoldandroids 混淆    #########
## ----------------------------------

-dontwarn com.nineoldandroids.*
-keep class com.nineoldandroids.** { *;}
## ----------------------------------
##   ########## photoView    #########
## ----------------------------------
-dontwarn uk.co.senab.photoview.**
## ----------------------------------
##   ########## QQ混淆    #########
## ----------------------------------
    -dontwarn com.tencent.**
    -keep class com.tencent.** {*;}
## ----------------------------------
##   ########## Gson混淆    #########
## ----------------------------------
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keep class com.google.**{*;}
-keep public class * implements java.io.Serializable{
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
## ----------------------------------
##   ########## 其他混淆    #########
## ----------------------------------

-keep class co.quchu.quchu.model.**{*;}
-keep class co.quchu.quchu.analysis.**{*;}
-keep class m.framework.**{*;}
-dontwarn **R$*
-dontwarn android.support.v4.**
-keepattributes Signature
-dontskipnonpubliclibraryclasses
-dontwarn net.poemcode.**
-ignorewarnings
-keep class co.quchu.quchu.widget.recyclerviewpager.RecyclerViewPager{*;}
## ----------------------------------
##   ########## 其他混淆    #########
## ----------------------------------
-optimizationpasses 5

-dontusemixedcaseclassnames

-dontskipnonpubliclibraryclasses

-dontpreverify

-verbose

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

-keep public class * extends android.app.Activity

-keep public class * extends android.app.Application

-keep public class * extends android.app.Service

-keep public class * extends android.content.BroadcastReceiver

-keep public class * extends android.content.ContentProvider

-keep public class * extends android.app.backup.BackupAgentHelper

-keep public class * extends android.preference.Preference

-keep public class com.android.vending.licensing.ILicensingService

##个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

############## 融云
-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 public *;
}
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

-keepattributes *Annotation*

-keep class com.google.gson.examples.android.model.** { *; }

-keep class **$Properties
-dontwarn org.eclipse.jdt.annotation.**

-libraryjars libs/agora-rtc-sdk.jar
-keep class io.agora.rtc.** {*;}

-keep class io.rong.** {*;}
-keep class io.agora.rtc.** {*; }
-keep class * implements io.rong.imlib.model.MessageContent{*;}

-dontwarn io.rong.push.** 
-dontnote com.xiaomi.** 
-dontnote com.huawei.android.pushagent.** 
-dontnote com.google.android.gms.gcm.** 
-dontnote io.rong.**
 -ignorewarnings
-keep class co.quchu.quchu.im.IMNotificationReceiver {*;}

-dontwarn io.rong.push.**
 -dontnote com.xiaomi.**
 -dontnote com.huawei.android.pushagent.** 
-dontnote com.google.android.gms.gcm.**
 -dontnote io.rong.**
############## 融云 end
