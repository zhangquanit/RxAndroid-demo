package com.rxandroid.demo;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.List;


public class App extends Application {
    private static List<Activity> activities=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
    }
    public static void fun(Activity act){
        activities.add(act);
    }
}
