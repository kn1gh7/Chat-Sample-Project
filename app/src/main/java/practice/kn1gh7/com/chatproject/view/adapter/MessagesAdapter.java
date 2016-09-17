package practice.kn1gh7.com.chatproject.view.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import practice.kn1gh7.com.chatproject.MessagesActivity;
import practice.kn1gh7.com.chatproject.R;
import practice.kn1gh7.com.chatproject.model.MessageModel;
import practice.kn1gh7.com.chatproject.model.UserModelCount;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessagesViewHolder> {
    List<MessageModel> msgsList;
    WeakReference<MessagesActivity> activity;

    public MessagesAdapter(WeakReference<MessagesActivity> activity) {
        this.activity = activity;
    }

    public int getItemPosition(String msgId) {
        int pos = -1;
        for (int i=0; i<msgsList.size(); i++) {
            if (msgsList.get(i).getId().equals(msgId)) {
                pos = i;
                if (msgsList.get(i).getIsFavorite().equals("-1")) {
                    msgsList.get(i).setIsFavorite("1");
                } else {
                    msgsList.get(i).setIsFavorite("-1");
                }
            }
        }

        return pos;
    }

    public void addMessages(List<MessageModel> msgList) {
        if (msgsList == null)
            msgsList = new ArrayList<>();

        this.msgsList.clear();
        this.msgsList.addAll(msgList);
        notifyDataSetChanged();
    }

    @Override
    public MessagesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, null);
        return new MessagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessagesViewHolder holder, int position) {
        final MessageModel model = msgsList.get(position);
        holder.message.setText(model.getMsgBody());
        holder.msg_timestamp.setText(model.getMsgTime());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                activity.get().changeFavoriteStatus(model.getId(),
                        model.getIsFavorite().equals("-1") ? true : false);
                return true;
            }
        });

        if (model.getIsFavorite().equals("1")) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity.get(), R.color.red));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(activity.get(), R.color.grey));
        }
    }

    @Override
    public int getItemCount() {
        if (msgsList == null)
            return 0;

        return msgsList.size();
    }

    public class MessagesViewHolder extends RecyclerView.ViewHolder {
        TextView message, msg_timestamp;
        public MessagesViewHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.message_tv);
            msg_timestamp = (TextView) itemView.findViewById(R.id.msg_timestamp);
        }
    }
}