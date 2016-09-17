package practice.kn1gh7.com.chatproject.presenter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import practice.kn1gh7.com.chatproject.MessagesActivity;
import practice.kn1gh7.com.chatproject.database.MessageDBHelper;
import practice.kn1gh7.com.chatproject.model.MessageModel;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class MessagePresenterImpl {
    WeakReference<MessagesActivity> activity;

    public MessagePresenterImpl(WeakReference<MessagesActivity> activity) {
        this.activity = activity;
    }

    public void getAllMessagesForUserId(String userId) {
        new GetMessages().execute(userId);
    }

    public void setResult(List<MessageModel> allConversations) {
        activity.get().setMessages(allConversations);
    }

    private class GetMessages extends AsyncTask<String, Void, List<MessageModel>> {

        @Override
        protected List<MessageModel> doInBackground(String... userIds) {
            MessageDBHelper dbHelper = new MessageDBHelper(activity.get());
            List<MessageModel> allMessages = dbHelper.getMessagesForUserId(userIds[0]);
            return allMessages;
        }

        @Override
        protected void onPostExecute(List<MessageModel> messageModels) {
            super.onPostExecute(messageModels);
            setResult(messageModels);
        }
    }
}
