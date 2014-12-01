package com.example.apps.basictwitter.activities;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.TwitterApplication;
import com.example.apps.basictwitter.TwitterClient;
import com.example.apps.basictwitter.models.Tweet;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetActivity extends Activity {

    private Tweet tweet;
    private TwitterClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet);
        
        client = TwitterApplication.getRestClient();
        getTweet();
        setupView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tweet, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void getTweet() {    
        client.getStatus(new JsonHttpResponseHandler() { 
            @Override
            public void onSuccess(JSONObject response) {
                Log.d("debug", response.toString());
                
                TweetActivity.this.tweet = Tweet.fromJson(response);
            }
            
            @Override
            public void onSuccess(String responseString) {
                Log.d("debug", responseString.toString());
            }
            
            @Override
            public void onFailure(Throwable throwable, JSONObject errorResponse) {
                Log.d("debug", throwable.toString());
                Log.d("debug", errorResponse.toString());
                
                TweetActivity.this.tweet = Tweet.getTweet(Long.valueOf(TweetActivity.this.getIntent().getStringExtra("id"))).get(0);
            }
            
            @Override
            public void onFailure(Throwable throwable, String responseString) {
                Log.d("debug", throwable.toString());
                Log.d("debug", responseString.toString());
                
                TweetActivity.this.tweet = Tweet.getTweet(Long.valueOf(TweetActivity.this.getIntent().getStringExtra("id"))).get(0);
            }
        }, getIntent().getStringExtra("id"));
        
        client.getStatus(new AsyncHttpResponseHandler() { 
            @Override
            public void onSuccess(int statusCode, String responseString) {
                try {
                    TweetActivity.this.tweet = Tweet.fromJson(new JSONObject(responseString));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            @Override
            public void onFailure(Throwable throwable) {
                Log.d("debug", throwable.toString());
                
                TweetActivity.this.tweet = Tweet.getTweet(Long.valueOf(TweetActivity.this.getIntent().getStringExtra("id"))).get(0);
            }
        }, getIntent().getStringExtra("id"));
    }
    
    private void setupView() {
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
        
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(tweet.getUser().getName());
        
        TextView tvText = (TextView) findViewById(R.id.tvText);
        tvText.setText(tweet.getText());
    }
}
