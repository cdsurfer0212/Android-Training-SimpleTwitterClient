package com.example.apps.basictwitter.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.TwitterApplication;
import com.example.apps.basictwitter.TwitterClient;
import com.example.apps.basictwitter.adapters.TweetAdapter;
import com.example.apps.basictwitter.models.Tweet;
import com.example.apps.basictwitter.widgets.EndlessScrollListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends SherlockFragmentActivity {

    private TwitterClient client;
    private ArrayList<Tweet> tweets;
    //private ArrayAdapter<Tweet> aTweets;
    private TweetAdapter aTweets;
    
    private PullToRefreshListView lvTweets;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        
        client = TwitterApplication.getRestClient();
        populateTimeline();
        
        lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
        tweets = new ArrayList<Tweet>();
        aTweets = new TweetAdapter(this, tweets);
        lvTweets.setAdapter(aTweets);
        
        lvTweets.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(TimelineActivity.this, TweetActivity.class);
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
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        switch (item.getItemId()) {
            case R.id.compose:
                Intent i = new Intent(this, ComposeActivity.class);
                startActivityForResult(i, 0);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    private void populateTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONArray response) {
                //Log.d("debug", json.toString());
                
                tweets.addAll(Tweet.fromJSONArray(response));
                aTweets.notifyDataSetChanged();
                
                Tweet.deleteTweets();
                Tweet.insertTweets(tweets);  
            }
            
            @Override
            public void onFailure(Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", throwable.toString());
                Log.d("debug", errorResponse.toString());
                
                tweets.addAll(Tweet.getTweets(10));
                aTweets.notifyDataSetChanged();
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
                
                tweets.addAll(Tweet.getTweets(10));
                aTweets.notifyDataSetChanged();
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
        }, ((Tweet)tweets.get(tweets.size() - 1)).getTweetId() - 1, 5);
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
        }, ((Tweet)tweets.get(0)).getTweetId(), 5);
    }
    
    public boolean onCompose(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, 0);
        
        return true;
    }
    
}
