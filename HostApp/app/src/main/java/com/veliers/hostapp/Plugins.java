package com.veliers.hostapp;

import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author WangBaoBao
 * @create 2019/7/10 16:12
 * @Describe :
 */
public class Plugins {

    //private static final String BASE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + "speech" + File.separator;

    private static final String BASE_PATH = "/sdcard/speech/";


    public static final String PLUGIN1_NAME = "VideoCall";
//    public static final String PLUGIN1_NAME = "plugin1";
    public static final String PLUGIN2_NAME = "plugin2";

    public static final Map<String, PluginExtra> PLUGINS = new HashMap<>();

    static {
        PLUGINS.put(PLUGIN1_NAME, new PluginExtra(PLUGIN1_NAME, "VideoCall.apk", new String[]{
                "com.veliers.video.MainActivity"}, new String[]{}));

//        PLUGINS.put(PLUGIN1_NAME, new PluginExtra(PLUGIN1_NAME, "plugin1.apk", new String[] {
//                "com.test.android.plugin1.MainActivity",
//                "com.test.android.plugin1.activity.InnerActivity",
//                "com.test.android.plugin1.activity.ForResultActivity",
//                "com.test.android.plugin1.provider.FileProviderActivity",
//        }, new String[] {"com.test.android.plugin1.service.Plugin1Service1"}));

        PLUGINS.put(PLUGIN2_NAME, new PluginExtra(PLUGIN2_NAME, "plugin2.apk", new String[]{}, new String[]{}));

    }


    public static class PluginExtra {
        public String pluginName;
        public String apkPath;
        public String[] activitys;
        public String[] services;

        public PluginExtra(String pluginName, String apkName, String[] activitys, String[] services) {
            this.pluginName = pluginName;
            this.apkPath = BASE_PATH + apkName;
            this.activitys = activitys;
            this.services = services;
        }

    }


}
