package io.flutter.plugins.webviewflutter;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import com.tencent.smtt.export.external.TbsCoreSettings;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;
import io.flutter.plugins.webviewflutter.X5WebView;

import java.util.HashMap;

public class X5WebViewHelper {
    public static final String TAG = "X5WebView";
    private static boolean mOnlyWifi = false;
    private static Context mContext;

    //这个key是sp存储的，用来记录上一次是否加载成功
    //因为有的时候需要立刻用到tbs展示文件，但是tbs可能还没加载好
    //因此我们在sp里面做记录，看是否需要等待加载
    //如果上次加载好了，那这次肯定也很快，可以等待
    //如果上次没加载好，那还是提示用户过一会再用吧
    private static String TBS_INIT_KEY = "tbs_init_key";

    private X5WebViewHelper() {
    }

    public void setOnlyWifiDownload(boolean onlyWifi) {
        mOnlyWifi = onlyWifi;
    }

    @SuppressWarnings("unchecked")
    public static void setupTbsEnv(final Context context) {
        if (context == null) {
            throw new NullPointerException("init fail");
        }
        mContext = context;
        setupX5SDK();
        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                //成功时i为100
                //此处存在一种情况，第一次启动app，init不会自动回调，此处额外加一层，判断网络监听器是否为空并作出处理
                Log.d(TAG, "x5WebView 内核下载(progress :" + i + "%)");
                //tbs内核下载完成回调
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onInstallFinish(int i) {
                Log.d(TAG, "x5WebView 内核安装:" + i);
            }

            @Override
            public void onDownloadProgress(int i) {
                //下载进度监听
                Log.d(TAG, "progress" + i);
            }
        });

        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                Log.e(TAG, "加载内核完成");
                //x5内核初始化完成回调接口，此接口回调并表示已经加载起来了x5，有可能特殊情况下x5内核加载失败，切换到系统内核。
            }

            @Override
            public void onViewInitFinished(boolean b) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                //该方法在第一次安装app打开不会回调
                Log.e(TAG, "加载内核是否成功:" + b);
            }
        });

        if (TbsDownloader.needDownload(context, false) && !TbsDownloader.isDownloading()) {
            TbsDownloader.startDownload(mContext);
        }

    }

    private static void setupX5SDK() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        HashMap map = new HashMap();
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER, true);
        map.put(TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE, true);
        QbSdk.initTbsSettings(map);
        QbSdk.setDownloadWithoutWifi(!mOnlyWifi);
        QbSdk.disableAutoCreateX5Webview();
        //强制使用系统内核
        //QbSdk.forceSysWebView();
    }
}
