package com.maca.andres.apollotest.di.modules;

import android.app.Application;
import android.util.Log;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.Operation;
import com.apollographql.apollo.api.ResponseField;
import com.apollographql.apollo.cache.normalized.CacheKey;
import com.apollographql.apollo.cache.normalized.CacheKeyResolver;
import com.apollographql.apollo.cache.normalized.NormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.lru.EvictionPolicy;
import com.apollographql.apollo.cache.normalized.lru.LruNormalizedCacheFactory;
import com.apollographql.apollo.cache.normalized.sql.ApolloSqlHelper;
import com.apollographql.apollo.cache.normalized.sql.SqlNormalizedCacheFactory;
import com.apollographql.apollo.subscription.WebSocketSubscriptionTransport;
import com.maca.andres.apollotest.repositories.DataRepository;

import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module(includes = ViewModelModule.class)
public class AppModule{
    private static final String TAG = AppModule.class.getSimpleName();
    private static final String BASE_URL = "http://192.168.0.14:8080/graphql";
    private static final String SUBSCRIPTION_BASE_URL = "wss://api.githunt.com/subscriptions";
    private static final String SQL_CACHE_NAME = "githuntdb";

    @Singleton
    @Provides
    NormalizedCacheFactory providesNormalizedCacheFactory(Application application){
        return  new LruNormalizedCacheFactory(EvictionPolicy.NO_EVICTION)
                .chain(new SqlNormalizedCacheFactory(new ApolloSqlHelper(application, SQL_CACHE_NAME)));


    }
    @Provides
    @Singleton
    CacheKeyResolver providesCacheKeyResolver(){
        return new CacheKeyResolver() {
            @NotNull
            @Override
            public CacheKey fromFieldRecordSet(@NotNull ResponseField field, @NotNull Map<String, Object> recordSet) {
                String typeName = (String) recordSet.get("__typename");
                if ("User".equals(typeName)) {
                    String userKey = typeName + "." + recordSet.get("login");
                    return CacheKey.from(userKey);
                }
                if (recordSet.containsKey("id")) {
                    String typeNameAndIDKey = recordSet.get("__typename") + "." + recordSet.get("id");
                    return CacheKey.from(typeNameAndIDKey);
                }
                return CacheKey.NO_KEY;
            }

            // Use this resolver to customize the key for fields with variables: eg entry(repoFullName: $repoFullName).
            // This is useful if you want to make query to be able to resolved, even if it has never been run before.
            @NotNull @Override
            public CacheKey fromFieldArguments(@NotNull ResponseField field, @NotNull Operation.Variables variables) {
                return CacheKey.NO_KEY;
            }
        };
    }

    @Provides
    OkHttpClient providesOkHttpClient(){
        return new OkHttpClient.Builder()
                .build();
    }
    @Singleton
    @Provides
    ApolloClient providesApolloClient(OkHttpClient okHttpClient, NormalizedCacheFactory normalizedCacheFactory, CacheKeyResolver cacheKeyResolver){
        Log.d(TAG,"Apollo created");
        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .normalizedCache(normalizedCacheFactory, cacheKeyResolver)
                .subscriptionTransportFactory(new WebSocketSubscriptionTransport.Factory(SUBSCRIPTION_BASE_URL, okHttpClient))
                .build();
    }
    @Provides
    @Singleton
    Executor providesExecutor(){return Executors.newSingleThreadExecutor();}
    @Provides
    DataRepository providesDataRepository(ApolloClient apolloClient, Executor executor){
        return new DataRepository(apolloClient, executor);
    }
}