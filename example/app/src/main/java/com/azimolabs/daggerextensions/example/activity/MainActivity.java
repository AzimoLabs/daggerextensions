package com.azimolabs.daggerextensions.example.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.azimolabs.daggerextensions.R;
import com.azimolabs.daggerextensions.SampleApplication;
import com.azimolabs.daggerextensions.api.GenerateModule;
import com.azimolabs.daggerextensions.di.MainActivityComponent;
import com.azimolabs.daggerextensions.example.ExampleModule;
import com.azimolabs.daggerextensions.example.exampleinterface.ExampleInterface;
import com.azimolabs.daggerextensions.example.exampleinterface.ExampleSecondInterface;
import com.azimolabs.daggerextensions.example.presenter.MainActivityPresenter;

import javax.inject.Inject;

@GenerateModule(includes = ExampleModule.class)
public class MainActivity extends Activity implements ExampleInterface, ExampleSecondInterface {

    @Inject
    MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SampleApplication
                .get(this)
                .component()
                .plus(new MainActivityComponent.ModuleImpl(this))
                .inject(this);
        presenter.init();
    }

    @Override
    public void setTextFromExampleInterface(String text) {
        ((TextView) findViewById(R.id.firstTV)).setText(text);
    }

    @Override
    public void setTextFromExampleSecondInterface(String text) {
        ((TextView) findViewById(R.id.secondTV)).setText(text);
    }
}