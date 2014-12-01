package com.example.apps.basictwitter.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.TwitterApplication;
import com.example.apps.basictwitter.TwitterClient;
import com.example.apps.basictwitter.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class ComposeActivity extends SherlockFragmentActivity {

    private TwitterClient client;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        
        client = TwitterApplication.getRestClient();
        setupTabs();
    }

    private void setupTabs() {
        final LayoutInflater inflater = (LayoutInflater) getActionBar().getThemedContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        final View customActionBarView = inflater.inflate(R.layout.actionbar_custom_view_done_cancel, null);
        
        customActionBarView.findViewById(R.id.actionbar_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ComposeActivity.this.client.postStatus(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        Log.d("debug", response.toString());
                        
                        Tweet tweet = Tweet.fromJson(response);
                        Intent intent = new Intent();
                        intent.putExtra("tweet", tweet);
                        setResult(RESULT_OK, intent);
                    }
                    
                    @Override
                    public void onFailure(Throwable throwable, JSONObject errorResponse) {
                        Log.d("debug", throwable.toString());
                        Log.d("debug", errorResponse.toString());
                    }
                    
                    @Override
                    public void onFailure(Throwable throwable, String responseString) {
                        Log.d("debug", throwable.toString());
                        Log.d("debug", responseString.toString());
                    }
                }, ((EditText) ComposeActivity.this.findViewById(R.id.etText)).getText().toString());
                finish();
            }
        });
        customActionBarView.findViewById(R.id.actionbar_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM, ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_TITLE);
        actionBar.setCustomView(customActionBarView, new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
}
