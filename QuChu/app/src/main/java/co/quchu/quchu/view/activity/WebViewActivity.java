package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.toolsfinal.DeviceUtils;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.base.EnhancedToolbar;
import co.quchu.quchu.dialog.ConfirmDialogFg;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by admin on 2016/3/22.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.webView)
    WebView mWebView;

    public static final String BUNDLE_KEY_WEBVIEW_URL = "BUNDLE_KEY_WEB_URL";
    public static final String BUNDLE_KEY_WEBVIEW_TITLE = "BUNDLE_KEY_WEBVIEW_TITLE";
    private String mPageTitle;
    public static final String TAG = "WebViewActivity";


    public static void enterActivity(Activity from, String url, String title) {
        Intent intent = new Intent(from, WebViewActivity.class);
        intent.putExtra(BUNDLE_KEY_WEBVIEW_URL, url);
        intent.putExtra(BUNDLE_KEY_WEBVIEW_TITLE, title);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        EnhancedToolbar toolbar = getEnhancedToolbar();
        toolbar.getLeftIv().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        WebChromeClient webChromeClient = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (null == mPageTitle && !StringUtils.isEmpty(title)) {
                    getEnhancedToolbar().getTitleTv().setText(title);
                }
            }
        };
        SimpleWebViewClient webViewClient = new SimpleWebViewClient();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        //mWebView.getSettings().setUserAgentString("Mozilla/5.0 (iPhone; CPU iPhone OS 5_0 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A334 Safari/7534.48.3");

        String url = getIntent().getStringExtra(BUNDLE_KEY_WEBVIEW_URL);
        mPageTitle = getIntent().getStringExtra(BUNDLE_KEY_WEBVIEW_TITLE);
        System.out.println(url + "|" + mPageTitle);
        getEnhancedToolbar().getTitleTv().setText(!StringUtils.isEmpty(mPageTitle) ? mPageTitle : "");

        if (!StringUtils.isEmpty(url) && URLUtil.isValidUrl(url)) {
            Log.d(TAG, url);
            mWebView.loadUrl(url);
            DialogUtil.showProgess(WebViewActivity.this, R.string.loading_dialog_text);
        } else {
            showErrorToast();
        }


    }

    @Override
    protected int activitySetup() {
        return 0;
    }

    private void showErrorToast() {
        if (DialogUtil.isDialogShowing()) {
            DialogUtil.dismissProgess();
        }
        Toast.makeText(WebViewActivity.this, R.string.error_invalid_arguments, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class SimpleWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (null!=url&&url.startsWith("intent")){
//                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                if (intent.resolveActivity(getPackageManager())==null){
//                    Toast.makeText(WebViewActivity.this,"你还没有安装大众点评",Toast.LENGTH_SHORT).show();
//                }else{
//                    ConfirmDialogFg confirmDialogFg = ConfirmDialogFg.newInstance("提示","是否跳转至大众点评");
//                    confirmDialogFg.show(getFragmentManager(),"confirm");
//                    confirmDialogFg.setActionListener(new ConfirmDialogFg.OnActionListener() {
//                        @Override
//                        public void onClick(int index) {
//                            if (index==ConfirmDialogFg.INDEX_OK){
//                                startActivity(intent);
//                            }else{
//
//                            }
//                        }
//                    });
//                }
            }else{
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            DialogUtil.dismissProgess();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            showErrorToast();
            Log.d(TAG, "onReceivedSslError");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            showErrorToast();
            Log.d(TAG, "onReceivedError");
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            showErrorToast();
            Log.d(TAG, "onReceivedHttpError");
        }
    }
}
