package com.maca.andres.apollotest.repositories;

import android.util.Log;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.ApolloCall.Callback;
import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.apollographql.apollo.sample.RootMutation;
import com.apollographql.apollo.sample.RootMutation.Data;
import com.apollographql.apollo.sample.RootQuery;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DataRepository  {
    private final ApolloClient apolloClient;
    private final Executor executor;
    //private DataObserver dataObserver;
    private static final String TAG = DataRepository.class.getSimpleName();
    //private HashMap<String, DataObserver>  dataObserverHashMap; Uncomment this if you wanna implement multiple observers

    private static final int FEED_SIZE = 20;

    @Inject
    public DataRepository(ApolloClient apolloClient, Executor executor) {
        this.apolloClient = apolloClient;
        this.executor = executor;
        Log.d(TAG,"Repository initializated" );
        init();
    }
    private void init(){
        RootMutation rootMutation = RootMutation.builder().cadena("TRex mk").build();
        apolloClient.mutate(rootMutation).enqueue(new Callback<Data>() {
            @Override
            public void onResponse(@NotNull Response<Data> response) {
                Log.d(TAG,"Response: "+response.toString());
            }

            @Override
            public void onFailure(@NotNull ApolloException e)
            {
                Log.d(TAG,"Exception: "+e.toString());


            }
        });
    }
    private void init2(){
        RootQuery rootQuery = RootQuery.builder().build();
        apolloClient.query(rootQuery).enqueue(new Callback<RootQuery.Data>() {
            @Override
            public void onResponse(@NotNull Response<RootQuery.Data> response) {
                Log.d(TAG,"Response: "+response.toString());

            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(TAG,"Exception: "+e.toString());


            }
        });
        apolloClient.query(rootQuery).enqueue(new ApolloCall.Callback<RootQuery.Data>(){

            @Override
            public void onResponse(@NotNull Response<RootQuery.Data> response) {
                Log.d(TAG,"Response: "+response.toString());


            }

            @Override
            public void onFailure(@NotNull ApolloException e) {
                Log.d(TAG,"Exception: "+e.toString());
                Log.d(TAG,"Exception: "+e.getMessage()+" \n tu no mete cabra "+e.getLocalizedMessage());


            }
        });
    }




}
