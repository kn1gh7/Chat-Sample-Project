package practice.kn1gh7.com.chatproject;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.List;

import practice.kn1gh7.com.chatproject.database.MessageDBHelper;
import practice.kn1gh7.com.chatproject.model.MessageModel;
import practice.kn1gh7.com.chatproject.presenter.MessagePresenterImpl;
import practice.kn1gh7.com.chatproject.utility.LogHelper;
import practice.kn1gh7.com.chatproject.view.adapter.MessagesAdapter;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class MessagesActivity extends AppCompatActivity {
    public static final String TAG = MessagesActivity.class.getSimpleName();
    public static final String USER_ID = "user_id";
    RecyclerView messagesRV;
    private MessagePresenterImpl presenter;
    private MessagesAdapter msgsAdapter;
    private LogHelper logHelper;

    public static Intent newInstance(Context context, String userId) {
        Intent intent = new Intent(context, MessagesActivity.class);
        intent.putExtra(USER_ID, userId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_layout);

        initViews();
        initOthers();

        String userId = getIntent().getExtras().getString(USER_ID);
        presenter = new MessagePresenterImpl(new WeakReference<MessagesActivity>(this));
        presenter.getAllMessagesForUserId(userId);
    }

    private void initViews() {
        messagesRV = (RecyclerView) findViewById(R.id.messages_rv);
        messagesRV.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initOthers() {
        msgsAdapter = new MessagesAdapter(new WeakReference<MessagesActivity>(this));
        messagesRV.setAdapter(msgsAdapter);

        logHelper = new LogHelper(TAG);
    }

    public void setMessages(List<MessageModel> allConversations) {
        if (allConversations != null)
            msgsAdapter.addMessages(allConversations);
    }

    public void changeFavoriteStatus(String msgId, boolean requestedNewStatus) {
        new ChangeFavoriteStatus(msgId, requestedNewStatus).execute();
    }

    private class ChangeFavoriteStatus extends AsyncTask<Void, Void, Boolean> {
        String msgId;
        boolean newFavStatus;
        public ChangeFavoriteStatus(String msgId, boolean newFavStatus) {
            this.msgId = msgId;
            this.newFavStatus = newFavStatus;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            MessageDBHelper dbHelper = new MessageDBHelper(MessagesActivity.this);
            boolean successStatus = dbHelper.setFavoriteStatusForMsgId(msgId, newFavStatus);
            return successStatus;
        }

        @Override
        protected void onPostExecute(Boolean successfulUpdate) {
            super.onPostExecute(successfulUpdate);
            logHelper.showLog("msgId: " + msgId + " newFavStatus: " + newFavStatus + " successStatus: " + successfulUpdate);
            msgsAdapter.notifyItemChanged(msgsAdapter.getItemPosition(msgId));
            if (newFavStatus) {
                showSnackbar(getString(R.string.fav_added));
            } else {
                showSnackbar(getString(R.string.fav_removed));
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSnackbar(String msg) {
        Snackbar.make(messagesRV, msg, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void finishAfterTransition() {
        messagesRV.setVisibility(View.GONE);
        super.finishAfterTransition();
    }
}
