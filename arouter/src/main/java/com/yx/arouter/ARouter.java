package com.yx.arouter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * Author by YX, Date on 2019/8/1.
 * 中间人 代理
 */
public class ARouter {

    private static ARouter aRouter = new ARouter();

    //装载所有的activity的类对象的容器
    private Map<String,Class<? extends Activity>> activityList;

    private Context context;

    private ARouter(){
        activityList = new HashMap<>();
    }

    public static ARouter getInstance(){
        return aRouter;
    }

    public void init(Application application){
        this.context = application;
    }

    /**
     * 将activity的类对象装到map中
     * @param path
     * @param clazz
     */
    public void putActivity(String path,Class<? extends Activity> clazz){
        if(path!=null && clazz!=null){
            activityList.put(path,clazz);
        }
    }

    /**
     * 跳转
     * @param path
     * @param bundle
     */
    public void jumpActivity(String path, Bundle bundle){
        //这个就是目标activity
        Class<? extends Activity> aClass = activityList.get(path);
        if(aClass==null){
            return;
        }
        Intent intent = new Intent(context, aClass);
        if(bundle!=null){
            intent.putExtra("bundle",bundle);
        }
        context.startActivity(intent);
    }

}
