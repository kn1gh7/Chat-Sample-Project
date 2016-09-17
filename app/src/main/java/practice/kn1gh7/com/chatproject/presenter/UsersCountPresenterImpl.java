package practice.kn1gh7.com.chatproject.presenter;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import practice.kn1gh7.com.chatproject.database.MessageDBHelper;
import practice.kn1gh7.com.chatproject.model.UserModelCount;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class UsersCountPresenterImpl {
    UserCountPresenterOps presenterOps;

    public UsersCountPresenterImpl(UserCountPresenterOps presenterOps) {
        this.presenterOps = presenterOps;
    }

    public void fetchUsersWithCountFromDB() {
        new GetUserCountTask().execute();
    }

    private class GetUserCountTask extends AsyncTask<Void, Void, List<UserModelCount>> {

        @Override
        protected List<UserModelCount> doInBackground(Void... voids) {
            MessageDBHelper dbHelper = new MessageDBHelper(presenterOps.getContext());
            List<UserModelCount> usersWithCount = dbHelper.getUsersWithCount();
            return usersWithCount;
        }

        @Override
        protected void onPostExecute(List<UserModelCount> userModelCounts) {
            super.onPostExecute(userModelCounts);
            presenterOps.setResult(userModelCounts);
        }
    }

    public interface UserCountPresenterOps {
        public Context getContext();
        public void setResult(List<UserModelCount> userModelList);
    }
}
