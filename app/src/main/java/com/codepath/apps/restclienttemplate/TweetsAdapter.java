package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    Context mContext;
    List<Tweet> mTweets;
    //Passing in the context and the list of tweets

    public TweetsAdapter(Context mContext, List<Tweet> tweets) {
        this.mContext = mContext;
        this.mTweets = tweets;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mIvProfileImage = itemView.findViewById(R.id.ivProfileImage);
            mTvBody = itemView.findViewById(R.id.tvBody);
            mTvScreenName = itemView.findViewById(R.id.tvScreenName);
            mTvRelativeTimestamp = itemView.findViewById(R.id.tvRelativeTimestamp);
        }

        public void bind(Tweet tweet) {
            mTvBody.setText(tweet.mBody);
            mTvScreenName.setText(tweet.mUser.mScreenName);
            Glide.with(mContext).load(tweet.mUser.mPublicImageUrl).into(mIvProfileImage);
            mTvRelativeTimestamp.setText(tweet.getRelativeTimeAgo(tweet.mCreatedAt));
        }
    }
}
