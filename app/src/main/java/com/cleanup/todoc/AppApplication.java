package com.cleanup.todoc;

import android.app.Application;
import android.content.Context;

/**
 * Class that allows to get the context of the application
 */
public class AppApplication extends Application {

    public static Context contextApp;

    @Override
    public void onCreate() {
        super.onCreate();
        contextApp = getApplicationContext();
    }
}
