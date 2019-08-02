package com.yx.arouterx;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dalvik.system.DexFile;

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
        List<String> classNames = getClassName("com.yx.util");
        for (String className : classNames) {

            try {
                Class<?> aClass = Class.forName(className);
                //判断这个类是否是IRouter的实现类
                if(IRouter.class.isAssignableFrom(aClass)){
                    IRouter iRouter = (IRouter) aClass.newInstance();
                    iRouter.putActivity();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 收集生成文件名
     * @param s
     * @return
     */
    private List<String> getClassName(String s) {
        //创建一个类名的集合
        List<String> classList = new ArrayList<>();
        String path = null;
        try {
            //通过包管理器 获取到应用信息类从而获取到apk的完整路径
            path = context.getPackageManager().getApplicationInfo(context.getPackageName(),0).sourceDir;
            //获取到dex文件
            DexFile dexFile = new DexFile(path);
            //获得编译后的dex文件中所有的class
            Enumeration<String> entries = dexFile.entries();
            while (entries.hasMoreElements()){
                String name = entries.nextElement();
                Log.i("yx", "getClassName: "+name);
                if(name.contains(s)){
                    classList.add(name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classList;
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
        Intent intent = new Intent().setClass(context,aClass);
        if(bundle!=null){
            intent.putExtra("bundle",bundle);
        }
        context.startActivity(intent);

    }

}
