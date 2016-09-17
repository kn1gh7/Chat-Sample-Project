package practice.kn1gh7.com.chatproject.presenter;

import android.os.AsyncTask;

import java.lang.ref.WeakReference;
import java.util.List;

import practice.kn1gh7.com.chatproject.MainActivity;
import practice.kn1gh7.com.chatproject.R;
import practice.kn1gh7.com.chatproject.database.MessageDBHelper;
import practice.kn1gh7.com.chatproject.model.ConversationModel;
import practice.kn1gh7.com.chatproject.model.MessageModel;
import practice.kn1gh7.com.chatproject.model.UserModelCount;
import practice.kn1gh7.com.chatproject.remote.RemoteApiService;
import practice.kn1gh7.com.chatproject.remote.RetrofitAPIHelper;
import practice.kn1gh7.com.chatproject.utility.LogHelper;
import practice.kn1gh7.com.chatproject.utility.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class TabsPresenterImpl implements Callback<ConversationModel> {
    private static final String TAG = TabsPresenterImpl.class.getSimpleName();
    private static final int GET_USERS_LOADER_ID = 10;
    private LogHelper logHelper;
    WeakReference<MainActivity> activity;

    public TabsPresenterImpl(WeakReference<MainActivity> mainActivity) {
        logHelper = new LogHelper(TAG);
        this.activity = mainActivity;
    }

    public void fetchConversationsFromServer() {
        if (Utils.isNetConnected(activity.get())) {
            RemoteApiService rtService = RetrofitAPIHelper.getRetrofitServiceHelper();
            Call<ConversationModel> movieDetails = rtService.loadConversations();
            movieDetails.enqueue(this);
        } else {
            activity.get().showSnackbarMessage(activity.get().getString(R.string.no_internet_msg));
        }
    }

    public void fetchUsersFromDB() {
        new GetUsersTask().execute();
    }

    @Override
    public void onResponse(Call<ConversationModel> call, Response<ConversationModel> response) {
        ConversationModel conversations = response.body();
        logHelper.showLog("count: " + conversations.getCount() + " size: " + conversations.getMessagesList().size());

        new SaveConversationsTask().execute(conversations);
    }

    private class SaveConversationsTask extends AsyncTask<ConversationModel, Void, Void> {

        @Override
        protected Void doInBackground(ConversationModel... conversationModels) {
            ConversationModel conversations = conversationModels[0];

            MessageDBHelper dbHelper = new MessageDBHelper(activity.get());
            dbHelper.bulkInsert(conversations.getMessagesList());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (activity != null && activity.get() != null) {
                activity.get().showSnackbarMessage("Insert Successful.");
                activity.get().setInsertSuccessful();
            }
        }
    }

    private class GetUsersTask extends AsyncTask<Void, Void, List<UserModelCount>> {
        @Override
        protected List<UserModelCount> doInBackground(Void... voids) {
            MessageDBHelper dbHelper = new MessageDBHelper(activity.get());
            List<UserModelCount> usersList = dbHelper.getUniqueUsers();
            return usersList;
        }

        @Override
        protected void onPostExecute(List<UserModelCount> usersList) {
            super.onPostExecute(usersList);
            if (activity != null && activity.get() != null) {
                activity.get().showResult(usersList);
            }
        }
    }

    @Override
    public void onFailure(Call<ConversationModel> call, Throwable t) {
        logHelper.showLog(t.getMessage());
        if (activity != null && activity.get() != null)
            activity.get().showSnackbarMessage(activity.get().getString(R.string.loading_failed_msg));
    }
/*
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader loader = null;
        if (id == GET_USERS_LOADER_ID) {
            MessageDBHelper dbHelper = new MessageDBHelper(activity.get());
            List<MessageModel> messagesList = dbHelper.getUniqueUsers();

            loader = new CursorLoader(activity.get());
        }

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (loader.getId() == GET_USERS_LOADER_ID) {
            activity.get().showSnackbarMessage("Insert Successful.");
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }*/
}
