package com.android.zencodegame.demo;

import android.app.Application;
import android.util.Log;

import com.android.game.iap.GameIAPSDK;


public class MainApplication extends Application {
    private static final String TAG = "MainApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, getClass().getSimpleName() + ": onCreate = ");
        onMainCreate();

    }

    //主进程
    private final void onMainCreate() {
        Log.d(TAG, getClass().getSimpleName() + ": onMainCreate = ");
        String appSecret = "5f50f0930c0e48f7aedf7147606e3c65";//oppo渠道接入需要的参数
        String appId = "10031";
        GameIAPSDK.getInstance().init(this, appId,appSecret,false);
    }
}
