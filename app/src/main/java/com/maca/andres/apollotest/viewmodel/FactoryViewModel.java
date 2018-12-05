package com.maca.andres.apollotest.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider.Factory;
import android.support.annotation.NonNull;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class FactoryViewModel implements Factory {
    private final static String TAG = FactoryViewModel.class.getSimpleName();
    private final Map<Class<? extends ViewModel>, Provider<ViewModel>> creators;

    @Inject
    public FactoryViewModel(Map<Class<? extends ViewModel>, Provider<ViewModel>> creators) {
        this.creators = creators;
    }

    @NonNull
    @SuppressWarnings("unchecked")
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        Provider<? extends ViewModel> creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, Provider<ViewModel>> entry :
                    creators.entrySet())
                     {
                         if(modelClass.isAssignableFrom(entry.getKey())){
                             creator = entry.getValue();
                             break;
                         }

            }
        }
        if (creator==null){
            throw new IllegalArgumentException("unknown model class");

        }try {
            return (T) creator.get();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
