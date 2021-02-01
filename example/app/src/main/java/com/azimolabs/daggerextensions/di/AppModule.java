package com.azimolabs.daggerextensions.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
class AppModule {

    private final Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}