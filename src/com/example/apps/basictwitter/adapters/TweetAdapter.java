package com.example.apps.basictwitter.adapters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apps.basictwitter.R;
import com.example.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetAdapter extends GenericAdapter<Tweet> {

    public TweetAdapter(Activity activity, List<Tweet> tweets) {
        super(activity, tweets);
    }

    @Override
    public View getDataRow(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
        
        View v;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mActivity.getBaseContext());
            v = inflater.inflate(R.layout.tweet_item, parent, false);

        
        ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
        ivProfileImage.setImageResource(android.R.color.transparent);
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);

        try {
            SimpleDateFormat f = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");
            Date d;
            d = f.parse(tweet.getCreatedAt());
            long ms = d.getTime();
            
            TextView tvCreatedAt = (TextView) v.findViewById(R.id.tvCreatedAt);
            tvCreatedAt.setText(DateUtils.getRelativeTimeSpanString(Long.valueOf(ms), System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        TextView tvName = (TextView) v.findViewById(R.id.tvName);
        tvName.setText(tweet.getUser().getName());
        
        TextView tvText = (TextView) v.findViewById(R.id.tvText);
        tvText.setText(tweet.getText());
        
        if (tweet.getMedias() != null && tweet.getMedias().size() > 0 && tweet.getMedias().get(0).getMediaUrl() != null) {
            String text = tweet.getText().replace(tweet.getMedias().get(0).getUrl(), "");
            tvText.setText(text);
            
            LinearLayout llMedia = (LinearLayout) v.findViewById(R.id.llMedia);
            ImageView ivMedia = new ImageView(llMedia.getContext());
            ivMedia.setImageResource(android.R.color.transparent);
            llMedia.addView(ivMedia);
            
            imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(tweet.getMedias().get(0).getMediaUrl(), ivMedia);
        }
        }
        else {
            v = convertView;
        }

        return v;
    }
}
