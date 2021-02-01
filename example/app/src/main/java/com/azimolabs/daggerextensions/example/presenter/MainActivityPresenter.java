package com.azimolabs.daggerextensions.example.presenter;

import com.azimolabs.daggerextensions.example.activity.MainActivity;
import com.azimolabs.daggerextensions.example.exampleinterface.ExampleInterface;
import com.azimolabs.daggerextensions.example.exampleinterface.ExampleSecondInterface;

import javax.inject.Inject;

public class MainActivityPresenter {
    private final ExampleInterface exampleInterface;
    private final ExampleSecondInterface exampleSecondInterface;

    @Inject
    public MainActivityPresenter(MainActivity view, ExampleSecondInterface exampleSecondInterface,
                                 ExampleInterface exampleInterface) {
        this.exampleInterface = exampleInterface;
        this.exampleSecondInterface = exampleSecondInterface;
    }

    public void init() {
        exampleInterface.setTextFromExampleInterface("text provided by ExampleInterface");
        exampleSecondInterface.setTextFromExampleSecondInterface("text provided by ExampleSecondInterface");
    }
}