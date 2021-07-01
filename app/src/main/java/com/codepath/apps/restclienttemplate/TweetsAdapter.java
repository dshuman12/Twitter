package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import okhttp3.Headers;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    public static final String TAG = "TweetsAdapter";

    Context mContext;
    List<Tweet> mTweets;
    TwitterClient mClient;
    //Passing in the context and the list of tweets

    public TweetsAdapter(Context context, List<Tweet> tweets, TwitterClient client) {
        this.mContext = context;
        this.mTweets = tweets;
        this.mClient = client;
    }

    //For each row, inflate the layout
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    //Bind values based on the position of the element
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // Get the data at the specific position
        Tweet tweet = mTweets.get(position);
        //Bind the tweet with the view holder
        holder.bind(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView mIvProfileImage;
        TextView mTvBody;
        TextView mTvScreenName;
        TextView mTvRelativeTimestamp;
        ImageView mIvMedia;
        ImageView mIvFavorite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvProfileImage = itemView.findViewById(R.id.ivProfileImage);
            mTvBody = itemView.findViewById(R.id.tvBody);
            mTvScreenName = itemView.findViewById(R.id.tvScreenName);
            mTvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
            mIvMedia = itemView.findViewById(R.id.ivMedia);
            mIvFavorite = itemView.findViewById(R.id.ivFavorite);
        }

        public void bind(final Tweet tweet) {
            mTvBody.setText(tweet.mBody);
            mTvScreenName.setText(tweet.mUser.mScreenName);
            Glide.with(mContext).load(tweet.mUser.mPublicImageUrl).into(mIvProfileImage);
            mTvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.mCreatedAt));
            Glide.with(mContext).load(tweet.getFirstMedia()).into(mIvMedia);

            if(tweet.mFavorited) {
                mIvFavorite.setBackgroundResource(R.drawable.ic_vector_heart);
            }

            mIvFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("TweetAdapter", "Tweet id" + tweet.mId);
                    if (!tweet.mFavorited) {
                        mClient.publishLike(tweet.mId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess to like tweet");
                                mIvFavorite.setBackgroundResource(R.drawable.ic_vector_heart);

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure to publish tweet", throwable);
                            }
                        });
                    }
                    else {
                        mClient.publishUnlike(tweet.mId, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Headers headers, JSON json) {
                                Log.i(TAG, "onSuccess to like tweet");
                                mIvFavorite.setBackgroundResource(R.drawable.ic_vector_heart_stroke);

                            }

                            @Override
                            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                                Log.e(TAG, "onFailure to publish tweet", throwable);
                            }
                        });
                    }
                }
            });

        }

        public void onLike(View v) {
            // Get the movie that was clicked
            int position = getAdapterPosition();
            // Ensures a valid position
            if (position != RecyclerView.NO_POSITION) {
                Tweet tweet = mTweets.get(position);
                // Send data to the Movie Details Activity
                //Intent intent = new Intent(mcontext, MovieDetailsActivity.class);
                //intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                // Shows the Movie Details page
                //mcontext.startActivity(intent);
                Log.d("TweetAdapter", "Tweet id" + tweet.mId);
                //mClient.publishLike(tweet.mId, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Headers headers, JSON json) {
//                        Log.i(TAG, "onSuccess to publish tweet");
//                        try {
//                            Tweet tweet = Tweet.fromJson(json.jsonObject);
//                            Log.i(TAG, "Published tweet says: ", tweet);
//                            Intent intent = new Intent();
//                            intent.putExtra("tweet", Parcels.wrap(tweet));
//                            setResult(RESULT_OK, intent);
//                            //Closes the activity, pass data to the parent
//                            finish();
//                        }
//                        catch (JSONException e){
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
//                        Log.e(TAG, "onFailure to publish tweet", throwable);
//                    }
//              });
            }
        }
    }

    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
