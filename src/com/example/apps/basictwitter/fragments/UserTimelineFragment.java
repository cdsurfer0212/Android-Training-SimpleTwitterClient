package com.example.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.example.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeline();
    }
    
    private void populateTimeline() {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                //Log.d("debug", json.toString());
                
                addAll(Tweet.fromJSONArray(response));
                
                //Tweet.deleteTweets();
                //Tweet.insertTweets(Tweet.fromJSONArray(response));  
            }
            
            @Override
            public void onFailure(Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", throwable.toString());
                Log.d("debug", errorResponse.toString());
                
                //addAll(Tweet.getTweets(10));
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
                
                //addAll(Tweet.getTweets(10));
            }
        };
        
        if (this.getActivity().getIntent().hasExtra("id")) {
            long id = this.getActivity().getIntent().getLongExtra("id", 0);
            client.getUserTimeline(handler, id, 10);
        }
        else
            client.getUserTimeline(handler, 10);
    }
}

