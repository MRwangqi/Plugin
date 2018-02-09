package com.codelang.plugin;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    String path;
    private static final int REQUEST_PERMISSION = 0x007;
    PackageInfo packageInfo;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.edit);
        editText.setText("12312");

        requestPermission();

        path = Environment.getExternalStorageDirectory() + "/library-debug.apk";

        loadApk(path);

        loadAsset(path);

        packageInfo = getPackageManager().getPackageArchiveInfo(path, PackageManager.GET_ACTIVITIES);
        for (int i = 0; i < packageInfo.activities.length; i++) {
            Log.i("packageInfo", packageInfo.activities[i].name);
        }


        ClipHelper.binder();


    }

    /**
     * 打开插件化activity
     *
     * @param view
     */
    public void open(View view) {
        try {
            Class<?> clazz = dexClassLoader.loadClass(packageInfo.activities[0].name);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.READ_PHONE_STATE},
                REQUEST_PERMISSION);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if ((grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
            } else {
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    DexClassLoader dexClassLoader;

    public void loadApk(String apkPath) {
        File dexOpt = this.getDir("dexOpt", MODE_PRIVATE);
        dexClassLoader = new DexClassLoader(apkPath, dexOpt.getAbsolutePath(), null, this.getClassLoader());
        try {
            Class<?> clazz = dexClassLoader.loadClass("com.codelang.library.TestDexClass");
            Object obj = clazz.newInstance();
            Method m = clazz.getMethod("method");
            m.invoke(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void loadAsset(String path) {

        try {

            AssetManager assetManager = AssetManager.class.newInstance();
            AssetManager.class.getDeclaredMethod("addAssetPath", String.class).invoke(assetManager, path);

            Resources resources = new Resources(assetManager, getResources().getDisplayMetrics(), getResources().getConfiguration());

            String str = resources.getString(resources.getIdentifier("code_lang", "string", "com.codelang.library"));


            Log.i("str", str + "--------");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
