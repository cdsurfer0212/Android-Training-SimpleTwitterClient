package com.example.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.TwitterApplication;
import com.example.apps.basictwitter.TwitterClient;
import com.example.apps.basictwitter.adapters.TweetAdapter;
import com.example.apps.basictwitter.models.Tweet;

import eu.erikw.PullToRefreshListView;

public class TweetsListFragment extends Fragment {
    
    protected TwitterClient client;
    
    protected ArrayList<Tweet> tweets;
    //private ArrayAdapter<Tweet> aTweets;
    protected TweetAdapter aTweets;

    protected PullToRefreshListView lvTweets;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Non-view initialization
        tweets = new ArrayList<Tweet>();
        // be wary to use getActivity()...
        aTweets = new TweetAdapter(getActivity(), tweets); 
        
        client = TwitterApplication.getRestClient();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        
        lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
        lvTweets.setAdapter(aTweets);
        
        return v;
    }
    
    // return the adapter to the activity
    public TweetAdapter getAdapter() {
        return aTweets;
    }
    
    // Delegate the adding to the internal adapter
    public void addAll(List<Tweet> tweets) {
        this.tweets.addAll(tweets);
        aTweets.notifyDataSetChanged();
    }
}
