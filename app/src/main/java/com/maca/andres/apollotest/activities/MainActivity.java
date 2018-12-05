package com.maca.andres.apollotest.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.apollographql.apollo.sample.FeedQuery.FeedEntry;
import com.maca.andres.apollotest.Adapter.GitHuntFeedRecyclerViewAdapter;
import com.maca.andres.apollotest.R;
import com.maca.andres.apollotest.viewmodel.DataViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity implements GitHuntNavigator{
    @Inject
    ViewModelProvider.Factory factory;
    ViewGroup content;
    ProgressBar progressBar;
    GitHuntFeedRecyclerViewAdapter feedAdapter;
    DataViewModel dataViewModel;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_githunt_feed);
        content = (ViewGroup) findViewById(R.id.content_holder);
        progressBar = (ProgressBar) findViewById(R.id.loading_bar);

        RecyclerView feedRecyclerView = (RecyclerView) findViewById(R.id.rv_feed_list);
        feedAdapter = new GitHuntFeedRecyclerViewAdapter(this);
        feedRecyclerView.setAdapter(feedAdapter);
        feedRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        configureDagger();
        configureViewModel();


    }


    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    private void configureViewModel(){
        dataViewModel = ViewModelProviders.of(this, factory).get(DataViewModel.class);
        dataViewModel.getListMutableLiveData().observe(this, feedEntries -> updateUI(feedEntries));

    }
    private void updateUI(List<FeedEntry> feedEntries){
        Log.d(TAG,"FirstEntry: "+feedEntries.get(0));
        feedAdapter.setFeed(feedEntries);
        progressBar.setVisibility(View.GONE);
        content.setVisibility(View.VISIBLE);

    }
    @Override
    public void startGithuntActivity(String repoFullName) {
        //StartActivity()
    }
}
