package com.azimolabs.daggerextensions.di;

import com.azimolabs.daggerextensions.example.activity.MainActivity;
import com.azimolabs.daggerextensions.example.activity.MainActivityModule;

import dagger.Module;
import dagger.Provides;
import dagger.Subcomponent;

@Subcomponent(modules = {MainActivityModule.class, MainActivityComponent.ModuleImpl.class})
public interface MainActivityComponent {

    void inject(MainActivity extendingTransferStatusActivity);

    @Module
    class ModuleImpl {
        private final MainActivity activity;

        public ModuleImpl(MainActivity activity) {
            this.activity = activity;
        }

        @Provides
        public MainActivity provideActivity() {
            return activity;
        }
    }
}