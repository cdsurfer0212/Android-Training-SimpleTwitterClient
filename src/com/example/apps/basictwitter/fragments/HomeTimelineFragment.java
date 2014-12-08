package com.example.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.apps.basictwitter.activities.TweetActivity;
import com.example.apps.basictwitter.listeners.EndlessScrollListener;
import com.example.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeline();
        
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        
        lvTweets.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(HomeTimelineFragment.this.getActivity(), TweetActivity.class);
                Tweet tweet = tweets.get(position);
                i.putExtra("id", String.valueOf(tweet.getTweetId()));
                startActivity(i);
            }
        });
        
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshTimeline();
            }
        });
        
        lvTweets.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore() {
                loadTimeline();
            }
        });
        
        return v;
    }
        
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                //Log.d("debug", json.toString());
                
                addAll(Tweet.fromJSONArray(response));
                
                Tweet.deleteTweets();
                Tweet.insertTweets(Tweet.fromJSONArray(response));  
            }
            
            @Override
            public void onFailure(Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", throwable.toString());
                Log.d("debug", errorResponse.toString());
                
                addAll(Tweet.getTweets(10));
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
                
                addAll(Tweet.getTweets(10));
            }
        }, 10);
    }
    
    private void loadTimeline() {
        if (tweets.size() == 0)
            return;
        
        client.getHomeTimelineWithMaxId(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                //Log.d("debug", json.toString());
                tweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
                
                Tweet.insertTweets(Tweet.fromJSONArray(response));  
            }
            
            @Override
            public void onFailure(Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", throwable.toString());
                Log.d("debug", errorResponse.toString());
                
                tweets.addAll(Tweet.getTweetsWithMaxId(((Tweet)tweets.get(tweets.size() - 1)).getTweetId() - 1, 5));
                aTweets.notifyDataSetChanged();
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
                
                tweets.addAll(Tweet.getTweetsWithMaxId(((Tweet)tweets.get(tweets.size() - 1)).getTweetId() - 1, 5));
                aTweets.notifyDataSetChanged();
            }
        }, ((Tweet) tweets.get(tweets.size() - 1)).getTweetId() - 1, 5);
    }
    
    private void refreshTimeline() {
        if (tweets.size() == 0)
            return;
        
        client.getHomeTimelineWithSinceId(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                Log.d("debug", response.toString());
                tweets.addAll(0, Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
                lvTweets.onRefreshComplete();
                
                Tweet.insertTweets(Tweet.fromJSONArray(response)); 
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
            }
        }, ((Tweet) tweets.get(0)).getTweetId(), 5);
    }
}
