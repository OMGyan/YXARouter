package com.yx.arouter;

import android.app.Application;

/**
 * Author by YX, Date on 2019/8/1.
 */
public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.getInstance().init(this);
    }
}
