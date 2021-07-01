package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Parcel
public class Tweet extends Throwable {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;
    private static final String TAG = "Tweet";

    public String mBody;
    public String mCreatedAt;
    public User mUser;
    public List<String> mMedias;
    public String mId;
    public Boolean mFavorited;
    public Boolean mRetweeted;

    // empty constructor for the Parceler library
    public Tweet(){}

    public static Tweet fromJson(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();
        if (jsonObject.has("full_text")) {
            tweet.mBody = jsonObject.getString("full_text");
        }
        else {
            tweet.mBody = jsonObject.getString("text");
        }
        tweet.mCreatedAt = jsonObject.getString("created_at");
        tweet.mUser = User.fromJson(jsonObject.getJSONObject("user"));
        tweet.mId = jsonObject.getString("id_str");
        tweet.mFavorited = jsonObject.getBoolean("favorited");
        tweet.mRetweeted = jsonObject.getBoolean("retweeted");

        JSONObject entities = jsonObject.getJSONObject("entities");
        if (entities.has("media")) {
            Log.d(TAG, "HAS MEDIA");
            tweet.mMedias = tweet.fromJsonArrayMedia(entities.getJSONArray("media"));
            Log.d(TAG, tweet.mBody + tweet.mMedias.toString());
        }
        else {
            //If there is no media
            tweet.mMedias = new ArrayList<String>();
        }
        return tweet;
    }

    public static List<Tweet> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            tweets.add(fromJson(jsonArray.getJSONObject(i)));
        }
        return tweets;
    }

    public static List<String> fromJsonArrayMedia(JSONArray jsonArray) throws JSONException {
        List<String> medias = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            medias.add(jsonArray.getJSONObject(i).getString("media_url_https"));
        }
        return medias;
    }

    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        try {
            long time = sf.parse(rawJsonDate).getTime();
            long now = System.currentTimeMillis();

            final long diff = now - time;
            if (diff < MINUTE_MILLIS) {
                return "just now";
            } else if (diff < 2 * MINUTE_MILLIS) {
                return "a minute ago";
            } else if (diff < 50 * MINUTE_MILLIS) {
                return diff / MINUTE_MILLIS + "m";
            } else if (diff < 90 * MINUTE_MILLIS) {
                return "an hour ago";
            } else if (diff < 24 * HOUR_MILLIS) {
                return diff / HOUR_MILLIS + " h";
            } else if (diff < 48 * HOUR_MILLIS) {
                return "yesterday";
            } else {
                return diff / DAY_MILLIS + "d";
            }
        } catch (ParseException e) {
            Log.i(TAG, "getRelativeTimeAgo failed");
            e.printStackTrace();
        }

        return "";
    }

    public String getFirstMedia() {
        if (mMedias.size() > 0) {
            return mMedias.get(0);
        }
        return "";
    }
}

