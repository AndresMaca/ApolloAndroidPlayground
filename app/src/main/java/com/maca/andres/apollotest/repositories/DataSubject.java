package com.maca.andres.apollotest.repositories;

import com.apollographql.apollo.sample.FeedQuery.FeedEntry;

import java.util.List;

public interface DataSubject {
    void register(DataObserver dataObserver);
    //void delete(String dataObserverName); Uncomment when you have multiples observers
    void delete();
    void notifyDataObserver(List<FeedEntry> feedEntryList);

}
