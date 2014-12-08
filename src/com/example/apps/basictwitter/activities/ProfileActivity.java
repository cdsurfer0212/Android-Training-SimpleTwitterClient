package com.example.apps.basictwitter.activities;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.TwitterApplication;
import com.example.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        loadProfileInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
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
    
    private void loadProfileInfo() {
        JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject json) {
                User u = User.fromJSON(json);
                getActionBar().setTitle("@" + u.getScreenName());
                populateProfileHeader(u);
            }
        };
        
        if (getIntent().hasExtra("id")) {
            long id = getIntent().getLongExtra("id", 0);
            TwitterApplication.getRestClient().getUserInfo(handler, id);
        }
        else {
            TwitterApplication.getRestClient().getMyInfo(handler);
        }
    }
    
    private void populateProfileHeader(User u) {
        TextView tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText(u.getName());
        
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        tvTagline.setText(u.getTagline());
        
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        tvFollowers.setText(String.valueOf(u.getFollowersCount()) + " Followers");
        
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        tvFollowing.setText(String.valueOf(u.getFriendsCount()) + " Following");
        
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
    }
}
