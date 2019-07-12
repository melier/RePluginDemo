package com.veliers.hostapp;

import android.content.Context;

import com.qihoo360.replugin.RePluginApplication;

/**
 * @author WangBaoBao
 * @create 2019/7/9 12:24
 * @Describe : 注意需要继承 RePluginApplication
 */
public class BaseApplication extends RePluginApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
