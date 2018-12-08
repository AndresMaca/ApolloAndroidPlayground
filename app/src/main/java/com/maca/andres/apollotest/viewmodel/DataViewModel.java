package com.maca.andres.apollotest.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.maca.andres.apollotest.repositories.DataRepository;

import javax.inject.Inject;

public class DataViewModel extends ViewModel {
    private DataRepository dataRepository;
    private static final String TAG = DataViewModel.class.getSimpleName();


    @Inject
    public DataViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        Log.d(TAG,"DataViewModel INIT. ");

    }

    public void uploadImage(){

    }
}
