package com.maca.andres.apollotest.di.modules;

import com.maca.andres.apollotest.activities.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity contributesMainActivity();
}
