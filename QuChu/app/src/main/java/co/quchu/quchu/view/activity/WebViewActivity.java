package co.quchu.quchu.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import co.quchu.quchu.R;
import co.quchu.quchu.base.BaseActivity;
import co.quchu.quchu.dialog.DialogUtil;
import co.quchu.quchu.utils.StringUtils;

/**
 * Created by admin on 2016/3/22.
 */
public class WebViewActivity extends BaseActivity {

    @Bind(R.id.title_content_tv)
    TextView mTvTitle;
    @Bind(R.id.webView)
    WebView mWebView;

    public static final String BUNDLE_KEY_WEBVIEW_URL = "BUNDLE_KEY_WEB_URL";
    public static final String BUNDLE_KEY_WEBVIEW_TITLE = "BUNDLE_KEY_WEBVIEW_TITLE";
    private String mUrl;
    private String mPageTitle;
    private WebChromeClient mWebChromeClient;
    private SimpleWebViewClient mWebViewClient;
    public static final String TAG = "WebViewActivity";


    public static void enterActivity(Activity from,String url,String title){
        Intent intent = new Intent(from,WebViewActivity.class);
        intent.putExtra(BUNDLE_KEY_WEBVIEW_URL,url);
        intent.putExtra(BUNDLE_KEY_WEBVIEW_TITLE,title);
        from.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);
        initTitleBar();
        mTvTitle.setText(getTitle());
        mWebChromeClient = new WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                if (null==mPageTitle&&!StringUtils.isEmpty(title)){
                    mTvTitle.setText(title);
                }
            }
        };
        mWebViewClient = new SimpleWebViewClient();
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(mWebChromeClient);
        mWebView.setWebViewClient(mWebViewClient);

        mUrl = getIntent().getStringExtra(BUNDLE_KEY_WEBVIEW_URL);
        mPageTitle = getIntent().getStringExtra(BUNDLE_KEY_WEBVIEW_TITLE);
        mTvTitle.setText(!StringUtils.isEmpty(mPageTitle)?mPageTitle:"");

        if (!StringUtils.isEmpty(mUrl) && URLUtil.isValidUrl(mUrl)){
            Log.d(TAG,mUrl);
            mWebView.loadUrl(mUrl);
            DialogUtil.showProgess(WebViewActivity.this, R.string.loading_dialog_text);
        }else{
            showErrorToast();
        }


    }

    private void showErrorToast() {
        if (DialogUtil.isDialogShowing()){
            DialogUtil.dismissProgess();
        }
        Toast.makeText(WebViewActivity.this,R.string.error_invalid_arguments,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        }else{
            super.onBackPressed();
        }
    }

    private class SimpleWebViewClient extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
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
            Log.d(TAG,"onReceivedSslError");
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            showErrorToast();
            Log.d(TAG,"onReceivedError");
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request , errorResponse);
            showErrorToast();
            Log.d(TAG,"onReceivedHttpError");
        }
    }
}
