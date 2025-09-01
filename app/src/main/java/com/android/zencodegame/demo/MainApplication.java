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
        GameIAPSDK.getInstance().init(this,true);
        GameIAPSDK.getInstance().setTestModel(true);
    }
}
