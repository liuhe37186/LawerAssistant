package com.he.lawerassistant.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.he.lawerassistant.R;
import com.he.lawerassistant.http.Constant;
import com.he.lawerassistant.utils.LogUtil;
import com.he.lawerassistant.utils.SharedPreferencesUtil;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnlineLawsActivity extends BaseCommonActivity {

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.btnRight)
    ImageView btnRight;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_result_num)
    TextView tvResultNum;
    @BindView(R.id.iv_up)
    ImageView ivUp;
    @BindView(R.id.iv_down)
    ImageView ivDown;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.search_bar)
    LinearLayout searchBar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.pd)
    ProgressBar pd;
    @BindView(R.id.rl_pd)
    RelativeLayout rlPd;
    String url = "";
    boolean isNight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_laws);
        ButterKnife.bind(this);
//        url = getIntent().getStringExtra("url");
        url = "https://www.chinacourt.org/law";
//        toolbarTitle.setText(parserUrl(url));
        isNight = SharedPreferencesUtil.getBoolean(OnlineLawsActivity.this, Constant.ISNIGHT, false);

        initSearchEvent();

        this.setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setDisplayShowTitleEnabled(false);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setBuiltInZoomControls(true); // 设置显示缩放按钮
        settings.setSupportZoom(true); // 支持缩放
        settings.setUseWideViewPort(true);
        setWebViewClient();

        DisplayMetrics metrics = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int mDensity = metrics.densityDpi;

        Log.d("maomao", "densityDpi = " + mDensity);

        if (mDensity == 240) {

            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        } else if (mDensity == 160) {

            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        } else if(mDensity == 120) {

            settings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);

        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){

            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        }else if (mDensity == DisplayMetrics.DENSITY_TV){

            settings.setDefaultZoom(WebSettings.ZoomDensity.FAR);

        }else{

            settings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);

        }

        rlPd.setVisibility(View.VISIBLE);

        if (isNight) {
            webView.setBackgroundColor(0x424242);
            rlPd.setBackgroundColor(0xcc424242);
        }else {
            webView.setBackgroundColor(0xffffff);
            rlPd.setBackgroundColor(0xccffffff);
        }

        webView.loadUrl(url);
    }


    private void setWebViewClient() {

        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (isNight) {
                    //使用夜间模式
                    webView.loadUrl("javascript:function getSub(){" +
                            "document.getElementsByTagName('body')[0].style.background='#424242'" +
                            "};getSub();");
                } else {
                    //不使用夜间模式
                }
            }
        });
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url != null && url.endsWith(".html")) {
                    Intent intent = new Intent(OnlineLawsActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    Log.e("xxxx", "shouldOverrideUrlLoading" + url);
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url != null && url.endsWith(".html")) {
                    Intent intent = new Intent(OnlineLawsActivity.this, WebActivity.class);
                    intent.putExtra("url", url);
                    Log.e("xxxx", "shouldOverrideUrlLoading" + url);
                    startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                rlPd.setVisibility(View.GONE);
                LogUtil.d("isNight=" + isNight);
                if (isNight) {
                    //使用夜间模式
                    webView.loadUrl("javascript:function getSub(){" +
                            "document.getElementsByTagName('body')[0].style.background='#424242'" +
                            "};getSub();");
                } else {
                    //不使用夜间模式
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (isNight) {
                    //使用夜间模式
                    webView.loadUrl("javascript:function getSub(){" +
                            "document.getElementsByTagName('body')[0].style.background='#424242'" +
                            "};getSub();");
                } else {
                    //不使用夜间模式
                }
            }
        });
    }

    private void initSearchEvent() {


        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.VISIBLE);
                etContent.setFocusable(true);
                etContent.setFocusableInTouchMode(true);
                etContent.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etContent, 0);
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable key) {
                //搜索点击
                webView.findAllAsync(key.toString());
                webView.setFindListener(new WebView.FindListener() {
                    @Override
                    public void onFindResultReceived(int position, int all, boolean b) {
                        if (all > 0) {
                            tvResultNum.setText("(" + (position + 1) + "/" + all + ")");
                        } else {
                            tvResultNum.setText("(" + 0 + "/" + all + ")");
                        }
                    }
                });

            }
        });

        ivUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.findNext(false);
            }
        });

        ivDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.findNext(true);
            }
        });

        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBar.setVisibility(View.GONE);
                etContent.setText("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(ivDelete.getWindowToken(), 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private String parserUrl(String url) {
        String title = "";
        List<String> strings = Arrays.asList(url.split("/"));
        String t = strings.get(strings.size() - 1);
        List<String> ts = Arrays.asList(t.split("\\."));
        if (ts.size() > 0) {
            title = ts.get(0);
        }

        return Uri.decode(title);

    }
}
