package com.maca.andres.apollotest.di.modules;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.maca.andres.apollotest.di.key.ViewModelKey;
import com.maca.andres.apollotest.viewmodel.DataViewModel;
import com.maca.andres.apollotest.viewmodel.FactoryViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(DataViewModel.class)
    abstract ViewModel bindDataViewModel(DataViewModel dataViewModel);

    @Binds
    abstract ViewModelProvider.Factory factory(FactoryViewModel factoryViewModel);
}
