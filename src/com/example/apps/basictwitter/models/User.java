package com.example.apps.basictwitter.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Users")
public class User extends Model {

    @Column(name = "user_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private long userId;
    
    @Column(name = "user_name")
    private String name;
    
    @Column(name = "user_profileImageUrl")
    private String profileImageUrl;
    
    @Column(name = "user_screenName")
    private String screenName;
    
    public long getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public User() {
        super();
    }
    
    public static User fromJSON(JSONObject json) {
        User user = new User();
        try {
            user.userId = json.getLong("id");
            user.name = json.getString("name");
            user.profileImageUrl = json.getString("profile_image_url");
            user.screenName = json.getString("screen_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return user;
    }
}
