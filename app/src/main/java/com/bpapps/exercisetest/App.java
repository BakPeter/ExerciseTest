package com.bpapps.exercisetest;

import android.app.Application;
import android.content.Context;

import com.bpapps.exercisetest.repository.Repository;

public class App extends Application {

    private static App sInstance;

    public static Context getAppContext() {
        return App.sInstance.getApplicationContext();
    }

    private static void setInstance(App sInstance) {
        App.sInstance = sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setInstance(this);

        Repository.getInstance().loadData();
    }
}
