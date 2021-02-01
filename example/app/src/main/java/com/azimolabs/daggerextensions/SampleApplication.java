package com.azimolabs.daggerextensions;

import android.app.Application;
import android.content.Context;

import com.azimolabs.daggerextensions.di.AppComponent;
import com.azimolabs.daggerextensions.di.DaggerAppComponent;

public class SampleApplication extends Application {
    private AppComponent appComponent;

    public static SampleApplication get(Context context) {
        return (SampleApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().build();
    }

    public AppComponent component() {
        return appComponent;
    }
}