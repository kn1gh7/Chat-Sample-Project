package practice.kn1gh7.com.chatproject.view.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import practice.kn1gh7.com.chatproject.MessagesActivity;
import practice.kn1gh7.com.chatproject.R;
import practice.kn1gh7.com.chatproject.model.UserModelCount;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class CountAdapter extends RecyclerView.Adapter<CountAdapter.CountViewHolder> {
    Activity activity;
    List<UserModelCount> userModelCountList;

    public CountAdapter(Activity activity, List<UserModelCount> list) {
        this.activity = activity;
        this.userModelCountList = list;
    }

    @Override
    public CountViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usercount_item, parent, false);
        return new CountViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CountViewHolder holder, int position) {
        final UserModelCount user = userModelCountList.get(position);
        holder.userSName.setText(user.getsName());
        holder.favoriteCount.setText("Favorite Count: " + user.favoriteCount);
        holder.totalCount.setText("Total Count: " + user.conversationCount);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MessagesActivity.newInstance(activity, user.getUserId());
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(activity);
                ActivityCompat.startActivity(activity, intent, activityOptions.toBundle());
            }
        });

        if (user.getImgUrl().length() > 0) {
            Glide.with(activity)
                    .load(user.getImgUrl())
                    .into(holder.imgView);
        } else {
            holder.imgView.setImageBitmap(null);
        }
    }

    @Override
    public int getItemCount() {
        return this.userModelCountList.size();
    }

    public class CountViewHolder extends RecyclerView.ViewHolder {
        public TextView userSName, favoriteCount, totalCount;
        public ImageView imgView;
        public CountViewHolder(final View itemView) {
            super(itemView);
            userSName = (TextView) itemView.findViewById(R.id.user_sname);
            favoriteCount = (TextView) itemView.findViewById(R.id.favorite_count);
            totalCount = (TextView) itemView.findViewById(R.id.total_count);
            imgView = (ImageView) itemView.findViewById(R.id.user_img);
        }
    }
}
