package com.ddt.pay_library;

//import com.alipay.sdk.app.PayTask;
//import com.alipay.sdk.util.H5PayResultModel;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

/**
 * 支付宝网页支付界面
 * @author Administrator
 *
 */
public class H5PayActivity extends Activity {

	private WebView mWebView;
	String from = "";//标识从哪个页面跳转至当前页面  //from=pay(购物车页面); from = recharge(充值页面)

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = null;
		try {
			extras = getIntent().getExtras();
		} catch (Exception e) {
			finish();
			return;
		}
		if (extras == null) {
			finish();
			return;
		}
		String url = null;
		try {
			url = extras.getString("url");
			from = getIntent().getStringExtra("from");
		} catch (Exception e) {
			finish();
			return;
		}
		if (TextUtils.isEmpty(url)) {
			// 测试H5支付，必须设置要打开的url网站
			/*new AlertDialog.Builder(H5PayActivity.this).setTitle("警告")
					.setMessage("必须配置需要打开的url 站点，请在PayDemoActivity类的h5Pay中配置")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							finish();
						}
					}).show();*/
			this.finish();
		}
		super.requestWindowFeature(Window.FEATURE_NO_TITLE);
		LinearLayout layout = new LinearLayout(getApplicationContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layout.setOrientation(LinearLayout.VERTICAL);
		setContentView(layout, params);

		mWebView = new WebView(getApplicationContext());
		params.weight = 1;
		mWebView.setVisibility(View.VISIBLE);
		layout.addView(mWebView, params);

		WebSettings settings = mWebView.getSettings();
		settings.setRenderPriority(RenderPriority.HIGH);
		settings.setSupportMultipleWindows(true);
		settings.setJavaScriptEnabled(true);
		settings.setSavePassword(false);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setMinimumFontSize(settings.getMinimumFontSize() + 8);
		settings.setAllowFileAccess(false);
		settings.setTextSize(WebSettings.TextSize.NORMAL);
		mWebView.setVerticalScrollbarOverlay(true);
		mWebView.setWebViewClient(new MyWebViewClient());
		mWebView.loadUrl(url);
	}

	@Override
	public void onBackPressed() {
//		if (mWebView.canGoBack()) {
//			mWebView.goBack();
//		} else {
			finish();
//		}
	}

	@Override
	public void finish() {
		super.finish();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {
			if (!(url.startsWith("http") || url.startsWith("https"))) {
				return true;
			}
			if(url.startsWith("http://app.ygqq.com/data/phone/recharge/recharge_callback.htm")||url.startsWith("http://apk.che6che5.com/data/phone/recharge/recharge_callback.htm")){
				setResult(RESULT_OK);
				H5PayActivity.this.finish();
				return true;
			}
			/*final PayTask task = new PayTask(H5PayActivity.this);
			final String ex = task.fetchOrderInfoFromH5PayUrl(url);
			if (!TextUtils.isEmpty(ex)) {
				System.out.println("paytask:::::" + url);
				new Thread(new Runnable() {
					public void run() {
						System.out.println("payTask:::" + ex);
						final H5PayResultModel result = task.h5Pay(ex, true);
						if (!TextUtils.isEmpty(result.getReturnUrl())) {
							H5PayActivity.this.runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									view.loadUrl(result.getReturnUrl());
								}
							});
						}
					}
				}).start();
			} else {
				view.loadUrl(url);
			}*/
			return true;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mWebView != null) {
			mWebView.removeAllViews();
			try {
				mWebView.destroy();
			} catch (Throwable t) {
			}
			mWebView = null;
		}
	}
}
