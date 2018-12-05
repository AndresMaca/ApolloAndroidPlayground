package com.maca.andres.apollotest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.maca.andres.apollotest.di.component.DaggerAppComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class App extends Application implements HasActivityInjector{
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        initDagger();
        context = getApplicationContext();
    }

    void initDagger(){
        DaggerAppComponent.builder().application(this).build().inject(this);
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
