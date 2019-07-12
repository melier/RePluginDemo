package com.veliers.hostapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.qihoo360.replugin.RePlugin;
import com.qihoo360.replugin.model.PluginInfo;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = "MainActivity";
    public static final String advFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Advertisement.apk";
    public static final String videoFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "VideoCall.apk";

    public static final int TYPE_ADV = 1; //广告类型
    public static final int TYPE_VIDEO = 2; //音视频类型

    private Button btn_adv_install, btn_video_install;

    public static final int REQUEST_PERMISSION_SETTING = 10000;
    public static final int REQUEST_CODE = 1; //授权标识码

    public static final String[] permissions = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
            } else {
                Log.i(TAG, "该程序已经授予读写存储卡的权限。");
            }
        }

        findViewById(R.id.btn_Advertisement).setOnClickListener(this);
        findViewById(R.id.btn_VideoCall).setOnClickListener(this);
        btn_adv_install = (Button) findViewById(R.id.btn_adv_install);
        btn_adv_install.setOnClickListener(this);

        btn_video_install = (Button) findViewById(R.id.btn_video_install);
        btn_video_install.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Advertisement:
                Log.i(TAG, "--- onClick() 点击了广告插件。");
                Intent intent = RePlugin.createIntent("com.veliers.advert", "com.veliers.advert.MainActivity");
                boolean startAdvertResult = RePlugin.startActivity(MainActivity.this, intent);
                if (!startAdvertResult) {
                    Toast.makeText(getApplicationContext(), "未安装广告插件，是否下载？", Toast.LENGTH_LONG).show();
                    File advFile = new File(advFilePath);
                    if (advFile.exists()) {
                        if (advFile.isFile()) {
                            btn_adv_install.setVisibility(View.VISIBLE);
                            btn_adv_install.setEnabled(true);
                        }
                    }
                }
                break;
            case R.id.btn_VideoCall:
                Log.i(TAG, "--- onClick() 点击了音视频插件。");
                Intent videoIntent = RePlugin.createIntent("com.veliers.video", "com.veliers.video.MainActivity");
                boolean startVideoResult = RePlugin.startActivity(MainActivity.this, videoIntent);
                if (!startVideoResult) {
                    Toast.makeText(getApplicationContext(), "未安装音视频插件，是否下载？", Toast.LENGTH_LONG).show();
                    File advFile = new File(videoFilePath);
                    if (advFile.exists()) {
                        if (advFile.isFile()) {
                            btn_video_install.setVisibility(View.VISIBLE);
                            btn_video_install.setEnabled(true);
                        }
                    }
                }
                break;
            case R.id.btn_adv_install:
                Log.i(TAG, "--- onClick() 点击了安装/升级广告插件！");
                install(TYPE_ADV, advFilePath);
                break;
            case R.id.btn_video_install:
                Log.i(TAG, "--- onClick() 点击了安装/升级视频插件！");
                install(TYPE_VIDEO, videoFilePath);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isRationale = false;
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问 false 表示已勾选
                    boolean showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permissions[i]);
                    if (!showRequestPermission) {
                        isRationale = true;
                    }
                }
            }

            //用户勾选禁止后不再询问，解释原因并且引导用户至设置页手动授权
            if (isRationale) {
                new AlertDialog.Builder(this)
                        .setMessage("获取应用相关权限失败将导致部分功能无法正常使用，需要到设置页面手动授权")
                        .setPositiveButton("去授权", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户至设置页手动授权
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                                intent.setData(uri);
                                startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户手动授权，权限请求失败
                            }
                        }).show();
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "---onActivityResult() requestCode = " + requestCode + ",resultCode = " + resultCode);

        if(requestCode == REQUEST_PERMISSION_SETTING){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
                } else {
                    Log.i(TAG, "该程序已经授予读写存储卡的权限。");
                }
            }
        }
    }

    /**
     * 安装插件
     *
     * @param type 安装类型
     * @param path 插件安装的地址。必须是“绝对路径”
     */
    private void install(int type, String path) {
        Log.i(TAG, "--- install() path = " + path);
        PluginInfo mPluginInfo = RePlugin.install(path);
        if (mPluginInfo != null) {
            Toast.makeText(getApplicationContext(), "插件安装成功", Toast.LENGTH_SHORT).show();
            if (type == TYPE_ADV) {
                btn_adv_install.setVisibility(View.INVISIBLE);
                btn_adv_install.setEnabled(false);
            } else {
                btn_video_install.setVisibility(View.INVISIBLE);
                btn_video_install.setEnabled(false);
            }
        }
    }

}
