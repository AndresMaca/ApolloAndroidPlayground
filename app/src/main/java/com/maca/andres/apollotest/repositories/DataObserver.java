package com.maca.andres.apollotest.repositories;

import com.apollographql.apollo.sample.FeedQuery.FeedEntry;

import java.util.List;

public interface DataObserver {
    void update(List<FeedEntry> feedEntries);
}
