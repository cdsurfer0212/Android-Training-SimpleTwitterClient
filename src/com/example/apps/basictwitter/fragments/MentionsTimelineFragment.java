package com.example.apps.basictwitter.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.util.Log;

import com.example.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsTimelineFragment extends TweetsListFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        populateTimeline();
    }
    
    private void populateTimeline() {
        client.getMentionsTimeline(new JsonHttpResponseHandler() {
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
        }, 10);
    }
}
