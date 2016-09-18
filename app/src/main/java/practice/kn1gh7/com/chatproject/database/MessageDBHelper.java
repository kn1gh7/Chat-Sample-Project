package practice.kn1gh7.com.chatproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import practice.kn1gh7.com.chatproject.model.MessageModel;
import practice.kn1gh7.com.chatproject.database.ConversationsContract.*;
import practice.kn1gh7.com.chatproject.model.UserModelCount;
import practice.kn1gh7.com.chatproject.utility.LogHelper;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class MessageDBHelper {
    private final String TAG = MessageDBHelper.class.getSimpleName();
    private SQLiteDatabase db;
    private LogHelper logHelper;

    public MessageDBHelper(Context context) {
        db = DatabaseHelper.getInstance(context).getDb();
        logHelper = new LogHelper(TAG);
    }

    public void bulkInsert(List<MessageModel> messages) {
        if (messages == null)
            return;

        try {
            db.beginTransaction();
            for (MessageModel model : messages) {
                ContentValues contentVals = new ContentValues();
                contentVals.put(Users.USER_USERNAME, model.getUsername());
                contentVals.put(Users.USER_SNAME, model.getsName());
                contentVals.put(Users.USER_IMGURL, model.getsImgUrl());

                long rowId = -1;
                Cursor cursor = db.query(Users.USER_TABLE,
                        null,
                        Users.USER_USERNAME + "=?",
                        new String[]{model.getUsername()},
                        null, null, null);
                if (cursor != null && !cursor.isAfterLast()) {
                    try {
                        while (cursor.moveToNext())
                            rowId = Long.parseLong(cursor.getString(cursor.getColumnIndex(Users._ID)));
                    } finally {
                        if (cursor != null && !cursor.isClosed())
                            cursor.close();
                    }
                }

                //If User was not present in DB then insert it.
                if (rowId == -1) {
                    rowId = db.insert(Users.USER_TABLE,
                            null,
                            contentVals);
                }

                contentVals.clear();

                if (rowId > 0) {
                    contentVals.put(Messages.MESSAGES_USERID, rowId);
                    contentVals.put(Messages.MESSAGES_BODY, model.getMsgBody());
                    contentVals.put(Messages.MESSAGES_MSG_TIME, model.getMsgTime());
                    contentVals.put(Messages.MESSAGES_IS_FAVORITE, "-1");
                    db.insert(Messages.MESSAGES_TABLE,
                            null,
                            contentVals);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + Users.USER_TABLE);
                }
            }
            db.setTransactionSuccessful();
        } finally {
            if (db.inTransaction()) //Ensures Rollback, if some problem happens during insertion in loop
                db.endTransaction();
        }
    }

    public boolean setFavoriteStatusForMsgId(String msgId, boolean markFavorite) {
        if (msgId == null)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(Messages.MESSAGES_IS_FAVORITE, markFavorite ? "1" : "-1");
        int rowId = db.update(Messages.MESSAGES_TABLE, contentValues, Messages._ID + "=?", new String[]{msgId});
        return rowId > 0;
    }

    public String getLastMessageByUser(String userId) {
        String lastMessage = null;
        if (userId == null)
            return null;

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + Messages.MESSAGES_BODY +
                    " FROM  " + Messages.MESSAGES_TABLE + " as t1 " +
                    " WHERE " +
                    " t1." + Messages.MESSAGES_USERID + " = " + userId +
                    " AND " +
                    " NOT EXISTS ( " +
                    "    SELECT *" +
                    "    FROM   " + Messages.MESSAGES_TABLE + " as t2 " +
                    "    WHERE " +
                    "    t2." + Messages.MESSAGES_USERID + " = " + userId +
                    "    AND t1." + Messages.MESSAGES_MSG_TIME +  " < t2." + Messages.MESSAGES_MSG_TIME +
                    ")", null);

            if (!cursor.isAfterLast()) {
                while (cursor.moveToNext()) {
                    lastMessage = cursor.getString(cursor.getColumnIndex(Messages.MESSAGES_BODY));
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return lastMessage;
    }

    public List<UserModelCount> getUniqueUsers() {
        List<UserModelCount> uniqueUsers = null;
        Cursor cursor = null;
        try {
            cursor = db.query(Users.USER_TABLE, null, null, null, null, null, null);
            if (cursor == null)
                return null;

            if (!cursor.isAfterLast()) {
                if (cursor.getCount() > 0) {
                    uniqueUsers = new ArrayList<>();
                }

                while (cursor.moveToNext()) {
                    UserModelCount model = new UserModelCount(); //User details saved here.
                    model.setUserId(cursor.getString(cursor.getColumnIndex(Users._ID)));
                    model.setUserName(cursor.getString(cursor.getColumnIndex(Users.USER_USERNAME)));
                    model.setsName(cursor.getString(cursor.getColumnIndex(Users.USER_SNAME)));
                    model.setImgUrl(cursor.getString(cursor.getColumnIndex(Users.USER_IMGURL)));

                    uniqueUsers.add(model);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return uniqueUsers;
    }

    public List<UserModelCount> getUsersWithCount() {
        List<UserModelCount> usersListWithCount = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(Users.USER_TABLE,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
            if (cursor == null)
                return null;

            if (!cursor.isAfterLast()) {
                while (cursor.moveToNext()) {
                    UserModelCount userModelCount = new UserModelCount();
                    userModelCount.setUserId(cursor.getString(cursor.getColumnIndex(Users._ID)));
                    userModelCount.setUserName(cursor.getString(cursor.getColumnIndex(Users.USER_USERNAME)));
                    userModelCount.setsName(cursor.getString(cursor.getColumnIndex(Users.USER_SNAME)));
                    userModelCount.setImgUrl(cursor.getString(cursor.getColumnIndex(Users.USER_IMGURL)));
                    usersListWithCount.add(userModelCount);
                }
            }

            for (int i = 0; i < usersListWithCount.size(); i++) {
                UserModelCount userModel = usersListWithCount.get(i);
                int[] reqCount = getCountForUserId(userModel.getUserId());
                logHelper.showLog("1stcount _Count: " + userModel.getConversationCount() +
                        " reqCount:Fav: " + reqCount[0] + " total: " + reqCount[1]);

                userModel.setFavoriteCount(String.valueOf(reqCount[0]));
                userModel.setConversationCount(String.valueOf(reqCount[1]));

                String lastMsg = getLastMessageByUser(userModel.getUserId());
                userModel.setLastMsg(lastMsg);
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return usersListWithCount;
    }

    public int[] getCountForUserId(String userId) {
        int[] reqCount = new int[2];//0 will be favoriteCount, 1 will be total Count
        Cursor cursor = null;
        try {
            cursor = db.query(Messages.MESSAGES_TABLE,
                    null,
                    Messages.MESSAGES_USERID + "=? AND " + Messages.MESSAGES_IS_FAVORITE + "=?",
                    new String[]{userId, "1"},
                    null,
                    null,
                    Messages.MESSAGES_MSG_TIME);

            if (cursor == null) {
                return null;
            }
            if (!cursor.isAfterLast()) {
                reqCount[0] = cursor.getCount();
            }
            reqCount[1] = getMessagesForUserId(userId).size();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return reqCount;
    }

    public List<MessageModel> getMessagesForUserId(String userId) {
        List<MessageModel> conversations = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(Messages.MESSAGES_TABLE,
                    null,
                    Messages.MESSAGES_USERID + "=?",
                    new String[]{userId},
                    null,
                    null,
                    Messages.MESSAGES_MSG_TIME);

            if (cursor == null)
                return null;

            if (!cursor.isAfterLast()) {
                while (cursor.moveToNext()) {
                    MessageModel model = new MessageModel(); //User details saved here.
                    model.setId(cursor.getString(cursor.getColumnIndex(Messages._ID)));
                    model.setMsgBody(cursor.getString(cursor.getColumnIndex(Messages.MESSAGES_BODY)));
                    model.setMsgTime(cursor.getString(cursor.getColumnIndex(Messages.MESSAGES_MSG_TIME)));
                    model.setIsFavorite(cursor.getString(cursor.getColumnIndex(Messages.MESSAGES_IS_FAVORITE)));
                    conversations.add(model);
                }
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }

        return conversations;
    }


}
