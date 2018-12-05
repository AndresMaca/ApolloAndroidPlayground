package com.maca.andres.apollotest.repositories;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.fetcher.ApolloResponseFetchers;
import com.apollographql.apollo.sample.FeedQuery;
import com.apollographql.apollo.sample.FeedQuery.Data;
import com.apollographql.apollo.sample.FeedQuery.FeedEntry;
import com.apollographql.apollo.sample.type.FeedType;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataRepository implements DataSubject {
    private final ApolloClient apolloClient;
    private final Executor executor;
    private DataObserver dataObserver;
    //private HashMap<String, DataObserver>  dataObserverHashMap; Uncomment this if you wanna implement multiple observers
    ApolloCall<Data> githuntFeedCall;

    private static final int FEED_SIZE = 20;

    @Inject
    public DataRepository(ApolloClient apolloClient, Executor executor) {
        this.apolloClient = apolloClient;
        this.executor = executor;
        fetchFeed();

    }


    private void fetchFeed() {
        final FeedQuery feedQuery = FeedQuery.builder()
                .limit(FEED_SIZE)
                .type(FeedType.HOT)
                .build();
        githuntFeedCall = apolloClient.query(feedQuery).responseFetcher(ApolloResponseFetchers.NETWORK_FIRST);
        githuntFeedCall.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NotNull Response<Data> response) {
                notifyDataObserver(feedResponseToEntriesWithRepositories(response));
            }

            @Override
            public void onFailure(@NotNull ApolloException e) {

            }
        });
    }

    @Override
    public void register(DataObserver dataObserver) {
        this.dataObserver = dataObserver;

    }

    @Override
    public void delete() {
        dataObserver = null;
    }

    @Override
    public void notifyDataObserver(List<FeedEntry> feedEntryList) {
        if (dataObserver != null)
            dataObserver.update(feedEntryList);

    }

    List<FeedEntry> feedResponseToEntriesWithRepositories(Response<FeedQuery.Data> response) {
        List<FeedEntry> feedEntriesWithRepos = new ArrayList<>();
        final FeedQuery.Data responseData = response.data();
        if (responseData == null) {
            return Collections.emptyList();
        }
        final List<FeedEntry> feedEntries = responseData.feedEntries();
        if (feedEntries == null) {
            return Collections.emptyList();
        }
        for (FeedEntry entry : feedEntries) {
            if (entry.repository() != null) {
                feedEntriesWithRepos.add(entry);
            }
        }
        return feedEntriesWithRepos;
    }
}
