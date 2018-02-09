package com.codelang.plugin;

import android.app.Application;
import android.os.Environment;

import java.io.File;

/**
 * @author wangqi
 * @since 2018/2/9 11:29
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FixDexUtils.loadFixedDex(this, Environment.getExternalStorageDirectory());
    }
}
