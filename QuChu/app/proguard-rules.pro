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
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

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
-keepattributes Signature
-keepattributes *Annotation*

## ----------------------------------
##   ########## OkHttp混淆    #######
## ----------------------------------
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**
## ----------------------------------
##   ########## Fresco混淆    #######
## ----------------------------------
#-keep class com.facebook.** {*;}
#-keep public class * extends com.facebook.**
-keep class com.facebook.** {*;}
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
##   ########## 微信混淆    #########
## ----------------------------------
-dontwarn com.tencent.mm.**
-keep class com.tencent.mm.**{*;}

## ----------------------------------
##   ########## 高德地图混淆    #########
## ----------------------------------
-keep class com.amap.api.location.**{*;}

-keep class com.amap.api.fence.**{*;}

-keep class com.autonavi.aps.amapapi.model.**{*;}
## ----------------------------------
##   ########## QQ混淆    #########
## ----------------------------------
    -dontwarn com.tencent.**
    -keep class com.tencent.** {*;}
## ----------------------------------
##   ########## Gson混淆    #########
## ----------------------------------
#-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

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

-keep public class co.quchu.quchu.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class co.quchu.quchu.model.**{*;}
-keep class co.quchu.quchu.analysis.**{*;}

-dontwarn **R$*
-dontwarn android.support.v4.**
-keepattributes Signature
-dontskipnonpubliclibraryclasses
-dontwarn net.poemcode.**
-ignorewarnings