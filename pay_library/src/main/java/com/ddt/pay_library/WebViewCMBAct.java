package com.ddt.pay_library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import cmb.pb.util.CMBKeyboardFunc;

/**
 * 招商银行----一网通h5支付
 * @author Administrator
 * 
 */
public class WebViewCMBAct extends Activity {
	String url;
	private ProgressBar pb; // webview加载进度条
	static private WebView webview;

	String from = "";//标识从哪个页面跳转至当前页面 
	                 //from=pay(购物车页面); from = recharge(充值页面)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		from = getIntent().getStringExtra("from");

		setContentView(R.layout.webview_video);
		initBarView();
		init();
	}

	private void initBarView() {
		Button btn_left = (Button) findViewById(R.id.actionbar_btn_left);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WebViewCMBAct.this.finish();
			}
		});
		btn_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_btn_left));
		btn_left.setVisibility(View.VISIBLE);
		TextView tv_barname = (TextView) findViewById(R.id.actionbar_tv_name);
		tv_barname.setText("支付");
	}

	@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
	@SuppressWarnings("deprecation")
	public void init() {

		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);

		webview = (WebView) findViewById(R.id.video_webview);

		// 对WebView进行设置
		WebSettings set = webview.getSettings();
		// 支持JS
		set.setJavaScriptEnabled(true);
		set.setSaveFormData(false);
		set.setSavePassword(false);
		set.setSupportZoom(false);
		set.setCacheMode(WebSettings.LOAD_NO_CACHE); // 禁用缓存

		// webview.loadUrl(DevUrl);
		LoadUrl();
		webview.setWebViewClient(new WebViewClient() {
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				if (url.contains("data/phone/recharge/recharge_callback.htm")) {
					WebViewCMBAct.this.finish();
				}
				// 使用当前的WebView加载页面
				CMBKeyboardFunc kbFunc = new CMBKeyboardFunc(WebViewCMBAct.this);
				if (kbFunc.HandleUrlCall(webview, url) == false) {
					return super.shouldOverrideUrlLoading(view, url);
				} else {
					return true;
				}

			}
		});

	}

	private void LoadUrl() {
		try {

			CookieSyncManager.createInstance(WebViewCMBAct.this.getApplicationContext());
			CookieManager.getInstance().removeAllCookie();
			CookieSyncManager.getInstance().sync();
		} catch (Exception e) {

		}
		webview.loadUrl(url);
	}
}
