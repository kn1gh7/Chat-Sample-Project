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
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    Activity activity;
    List<UserModelCount> userModelCountList;

    public UsersAdapter(Activity activity, List<UserModelCount> list) {
        this.activity = activity;
        this.userModelCountList = list;
    }

    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlastmsg_item, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UsersAdapter.UsersViewHolder holder, int position) {
        final UserModelCount user = userModelCountList.get(position);
        holder.userSName.setText(user.getsName());
        holder.lastMsg.setText(user.getLastMsg());

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

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        public TextView userSName, lastMsg;
        public ImageView imgView;
        public UsersViewHolder(final View itemView) {
            super(itemView);
            userSName = (TextView) itemView.findViewById(R.id.user_sname);
            lastMsg = (TextView) itemView.findViewById(R.id.last_msg);
            imgView = (ImageView) itemView.findViewById(R.id.user_img);
        }
    }
}
