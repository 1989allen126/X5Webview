package io.flutter.plugins.webviewflutter;

import io.flutter.app.FlutterApplication;

import android.content.Context;
import android.util.Log;
import com.tencent.smtt.sdk.QbSdk;

public class X5WebViewAplication extends FlutterApplication {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 初始化
		X5WebViewHelper.setupTbsEnv(getApplicationContext());
	}
}
