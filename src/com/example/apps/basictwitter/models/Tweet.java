package com.example.apps.basictwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.activeandroid.util.SQLiteUtils;

@Table(name = "Tweets")
public class Tweet extends Model implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1168884680425837754L;
    
    @Column(name = "tweet_createdAt")
    private String createdAt;
    
    @Column(name = "tweet_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long tweetId;
    
    @Column(name = "tweet_text")
    private String text;
    
    @Column(name = "tweet_user", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
    private User user;
    
    private List<Media> medias;
    
    public String getCreatedAt() {
        return createdAt;
    }

    public long getTweetId() {
        return tweetId;
    }

    public String getText() {
        return text;
    }

    public User getUser() {
        return user;
    }
    
    public List<Media> getMedias() {
        return medias;
    }
    
    public Tweet() {
        super();
    }
    
    public static Tweet fromJson(JSONObject json) {
        Tweet tweet = new Tweet();
        try {
            tweet.createdAt = json.getString("created_at");
            tweet.tweetId = json.getLong("id");
            tweet.text = json.getString("text");
            tweet.user = User.fromJSON(json.getJSONObject("user"));
            if (json.getJSONObject("entities").has("media"))
                tweet.medias = Media.fromJSONArray(json.getJSONObject("entities").getJSONArray("media"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject tweetJson = null;
            try {
                tweetJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            
            Tweet tweet = Tweet.fromJson(tweetJson);
            if (tweet != null) {
                tweets.add(tweet);
            }
        }
        
        return tweets;
    }
    
    public static void deleteTweets() {
         SQLiteUtils.execSql("DELETE FROM Tweets");
         //SQLiteUtils.execSql("drop table if exists Tweets");
    }
    
    public static List<Tweet> getTweet(long id) {
        return new Select()
            .from(Tweet.class)
            .where("tweet_id = ?", id)
            .execute();
    }
    
    public static List<Tweet> getTweets(int count) {
        return new Select()
            .from(Tweet.class)
            .orderBy("tweet_id DESC")
            .limit(count)
            .execute();
    }
    
    public static List<Tweet> getTweetsWithMaxId(long maxId, int count) {
        return new Select()
            .from(Tweet.class)
            .where("tweet_id < ?", maxId)
            .orderBy("tweet_id DESC")
            .limit(count)
            .execute();
    }
    
    public static void insertTweets(List<Tweet> tweets) {
        for (Tweet tweet: tweets) {
            tweet.getUser().save();
            tweet.save(); 
        }
    }

}
