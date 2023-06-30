package com.cleanup.todoc;

import android.app.Application;
import android.content.Context;

public class AppApplication extends Application {

    public static Context contextApp;

    @Override
    public void onCreate() {
        super.onCreate();
        contextApp = getApplicationContext();
    }
}
