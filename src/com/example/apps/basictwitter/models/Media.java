package com.example.apps.basictwitter.models;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Media {

    private long mediaId;

    private String mediaUrl;
    
    private String url;
    
    public long getMediaId() {
        return mediaId;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getUrl() {
        return url;
    }

    public Media() {
        super();
    }
    
    public static Media fromJSON(JSONObject json) {
        Media media = new Media();
        try {
            media.mediaId = json.getLong("id");
            media.mediaUrl = json.getString("media_url_https");
            media.url = json.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        
        return media;
    }
    
    public static ArrayList<Media> fromJSONArray(JSONArray jsonArray) {
        if (jsonArray == null)
            return new ArrayList<Media>();
        
        ArrayList<Media> medias = new ArrayList<Media>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject mediaJson = null;
            try {
                mediaJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            
            Media media = Media.fromJSON(mediaJson);
            if (media != null) {
                medias.add(media);
            }
        }
        
        return medias;
    }
    
}
