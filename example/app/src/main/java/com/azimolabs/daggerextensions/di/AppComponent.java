package com.azimolabs.daggerextensions.di;


import dagger.Component;

@Component(modules = {AppModule.class})
public interface AppComponent {
    MainActivityComponent plus(MainActivityComponent.ModuleImpl module1);
}