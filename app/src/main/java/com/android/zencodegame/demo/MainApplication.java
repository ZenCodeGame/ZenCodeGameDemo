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
//        GameIAPSDK.getInstance().init(this,true);
        boolean debug = false; //开启debug的话，会答应更多有关IAP SDK的日志
        boolean enableLogin = false; //true表示接入账号登录 false表示不接入账号登录
        GameIAPSDK.getInstance().init(this,debug,enableLogin);
        GameIAPSDK.getInstance().setTestModel(true);
    }
}
