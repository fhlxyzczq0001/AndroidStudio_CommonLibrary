package com.ddt.pay_library;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebSettings.ZoomDensity;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.ddt.pay_library.utils.ToolsUtils;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class WebView_RechargeAct extends Activity {

	private ProgressBar pb; // webview加载进度条
	private WebView webview;
	private xWebChromeClient xwebchromeclient;
	private String url;
	private String bar_name;
	private TextView tv_barname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		 requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉应用标题
		 super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_video);
		initwidget();
		url = getIntent().getStringExtra("url");
		bar_name = getIntent().getStringExtra("barname");

		initBarView();

		webview.getSettings().setDisplayZoomControls(false);
		webview.getSettings().setLoadsImagesAutomatically(true); // 支持自动加载图片

		webview.addJavascriptInterface(new WebInvivate(), "android");
		webview.loadUrl(url);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initBarView() {
		Button btn_left = (Button) findViewById(R.id.actionbar_btn_left);
		btn_left.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_btn_left));
		btn_left.setVisibility(View.VISIBLE);
		btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				webview.loadUrl("about:blank");
				WebView_RechargeAct.this.finish();
			}
		});
		tv_barname = (TextView) findViewById(R.id.actionbar_tv_name);
	}

	private void initwidget() {
		// TODO Auto-generated method stub
		pb = (ProgressBar) findViewById(R.id.pb);
		pb.setMax(100);

		webview = (WebView) findViewById(R.id.video_webview);

		WebSettings ws = webview.getSettings();
		ws.setBuiltInZoomControls(true);// 隐藏缩放按钮
		ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);// 排版适应屏幕
		ws.setUseWideViewPort(true);// 可任意比例缩放
		ws.setLoadWithOverviewMode(true);// setUseWideViewPort方法设置webview推荐使用的窗口。setLoadWithOverviewMode方法是设置webview加载的页面的模式。
		ws.setSavePassword(true);
		ws.setSaveFormData(true);// 保存表单数据
		ws.setJavaScriptEnabled(true);
		ws.setGeolocationEnabled(true);// 启用地理定位
		ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");// 设置定位的数据库路径
		ws.setDomStorageEnabled(true);

		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setDefaultZoom(ZoomDensity.FAR);

		xwebchromeclient = new xWebChromeClient();
		webview.setWebChromeClient(xwebchromeclient);
		webview.setWebViewClient(new xWebViewClientent());
		webview.setDownloadListener(new MyWebViewDownLoadListener());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (webview.canGoBack()) {
				webview.goBack();
				return true;
			} else {
				webview.loadUrl("about:blank");
				// mTestWebView.loadData("", "text/html; charset=UTF-8", null);
				this.finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 处理Javascript的对话框、网站图标、网站标题以及网页加载进度等
	 * 
	 * @author
	 */
	public class xWebChromeClient extends WebChromeClient {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			pb.setProgress(newProgress);
			if (newProgress == 100) {
				pb.setVisibility(View.GONE);
			}
			super.onProgressChanged(view, newProgress);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	/**
	 * 处理各种通知、请求等事件
	 * 
	 * @author
	 */
	public class xWebViewClientent extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			Log.i("webviewtest", "shouldOverrideUrlLoading: " + url);
			//界面内跳转的监听处理，注意判断顺序

			//跳转本地应用
			if (url.startsWith("intent://")) {
				Intent intent;
				try {
					intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
					intent.addCategory("android.intent.category.BROWSABLE");
					intent.setComponent(null);
//					intent.setSelector(null);
					startActivity(intent);
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(WebView_RechargeAct.this, "应用未安装或未找到", Toast.LENGTH_LONG).show();
				}
				return true;
			}
			if (!(url.startsWith("http") || url.startsWith("https"))) {
				return true;
			}
			//网页支付完成回调
			if(url.startsWith("http://app.ygqq.com/data/phone/recharge/recharge_callback.htm")||url.startsWith("http://apk.che6che5.com/data/phone/recharge/recharge_callback.htm")){
				setResult(RESULT_OK);
				WebView_RechargeAct.this.finish();
				return true;
			}
			//支付宝H5支付
			final PayTask task = new PayTask(WebView_RechargeAct.this);
			final String ex = task.fetchOrderInfoFromH5PayUrl(url);
			if (!TextUtils.isEmpty(ex)) {
				new Thread(new Runnable() {
					public void run() {
						final H5PayResultModel result = task.h5Pay(ex, true);
						if (!TextUtils.isEmpty(result.getReturnUrl())) {
							WebView_RechargeAct.this.runOnUiThread(new Runnable() {
								@Override
								public void run() {
									webview.loadUrl(result.getReturnUrl());
								}
							});
						}
					}
				}).start();
			} else {
				view.loadUrl(url);
			}
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			String t = view.getTitle();
			if (null == t || t.equals("")) {
				return;
			}
			if (t.length() > 10) {
				t = t.substring(0, 10) + "...";
			}
			if (null != bar_name && !"".equals(bar_name)) {
				tv_barname.setText(bar_name);
			} else {
				tv_barname.setText(t);
			}
		}
	}

	private class MyWebViewDownLoadListener implements DownloadListener {
		@Override
		public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
									long contentLength) {
			if (url.contains(".apk")){
				Uri uri = Uri.parse(url);
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		}
	}

	/**
	 * 网页中的js方法处理
	 */
	private class WebInvivate{

		/**
		 * 保存当前界面到内存中，截屏
		 */
		@JavascriptInterface
		public void savePicture(){
			final String imagePath = ToolsUtils.saveCurrentImage(WebView_RechargeAct.this);
			new Thread(new Runnable() {
				@Override
				public void run() {
					WebView_RechargeAct.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							String saveSucess = "";
							if(imagePath == null){
								saveSucess = "文件保存发生错误";
							}
							webview.loadUrl("javascript:saveImgSucess(" + saveSucess + ")");
						}
					});
				}
			}).start();
		}

		/**
		 * 根据包名调起指定应用
		 * @param packagename zfb or wx
         */
		@JavascriptInterface
		public void toThirdParty(String packagename){
			//"com.tencent.mm" 微信
			//com.eg.android.AlipayGphone 支付宝
			if(packagename.toLowerCase().equals("zfb")){
				packagename = "com.eg.android.AlipayGphone";
			}else if(packagename.toLowerCase().equals("wx")){
				packagename = "com.tencent.mm";
			}
			ToolsUtils.doStartApplicationWithPackageName(WebView_RechargeAct.this, packagename);
		}
	}

}
