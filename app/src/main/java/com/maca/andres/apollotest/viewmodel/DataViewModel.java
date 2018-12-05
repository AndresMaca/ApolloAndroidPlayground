package com.maca.andres.apollotest.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.apollographql.apollo.sample.FeedQuery.FeedEntry;
import com.maca.andres.apollotest.repositories.DataObserver;
import com.maca.andres.apollotest.repositories.DataRepository;

import java.util.List;

import javax.inject.Inject;

public class DataViewModel extends ViewModel implements DataObserver {
    private DataRepository dataRepository;

    public MutableLiveData<List<FeedEntry>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    private MutableLiveData<List<FeedEntry>> listMutableLiveData;

    @Inject
    public DataViewModel(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
        dataRepository.register(this);
        if(listMutableLiveData == null)
            listMutableLiveData = new MutableLiveData<>();
    }

    @Override
    public void update(List<FeedEntry> feedEntries) {
        if (feedEntries != null)
            listMutableLiveData.postValue(feedEntries);

    }
}
