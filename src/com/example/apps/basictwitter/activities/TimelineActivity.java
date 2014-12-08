package com.example.apps.basictwitter.activities;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.fragments.HomeTimelineFragment;
import com.example.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.example.apps.basictwitter.listeners.FragmentTabListener;

public class TimelineActivity extends SherlockFragmentActivity {

    // private TweetsListFragment fragmentTweetsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        // fragmentTweetsList = (TweetsListFragment)
        // getSupportFragmentManager().findFragmentById(R.id.fragment_timeline);

        setupTabs();
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
        case R.id.profile:
            i = new Intent(this, ProfileActivity.class);
            startActivity(i);
            finish();
            return true;    
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    public boolean onCompose(MenuItem mi) {
        Intent i = new Intent(this, ComposeActivity.class);
        startActivityForResult(i, 0);

        return true;
    }

    public boolean onProfileView(MenuItem mi) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        
        return true;
    }
    
    private void setupTabs() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        Tab tab1 = actionBar
                  .newTab()
                  .setText("Home")
                  .setIcon(R.drawable.ic_home)
                  .setTabListener(new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home", HomeTimelineFragment.class));

        actionBar.addTab(tab1);
        actionBar.selectTab(tab1);

        Tab tab2 = actionBar
                .newTab()
                .setText("Mentions")
                .setIcon(R.drawable.ic_mention)
                .setTabListener(new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions", MentionsTimelineFragment.class));
        actionBar.addTab(tab2);
    }
}
