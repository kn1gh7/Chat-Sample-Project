package practice.kn1gh7.com.chatproject.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import practice.kn1gh7.com.chatproject.database.ConversationsContract.*;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    final String SQL_CREATE_USER_TABLE = "CREATE TABLE IF NOT EXISTS " + Users.USER_TABLE + " (" +
            Users._ID + " INTEGER PRIMARY KEY, " +
            Users.USER_USERNAME + " TEXT UNIQUE NOT NULL, " +
            Users.USER_SNAME + " TEXT, " +
            Users.USER_IMGURL + " TEXT " +
            " );";

    final String SQL_CREATE_MESSAGES_TABLE = "CREATE TABLE IF NOT EXISTS " + Messages.MESSAGES_TABLE + " (" +
            Messages._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            Messages.MESSAGES_BODY + " TEXT, " +
            Messages.MESSAGES_USERID + " TEXT, " +
            Messages.MESSAGES_MSG_TIME + " TEXT, " +
            Messages.MESSAGES_IS_FAVORITE + " TEXT, " +
            " FOREIGN KEY (" + Users._ID + ") REFERENCES " +
            Messages.MESSAGES_TABLE + " (" + Messages.MESSAGES_USERID + ")" +
            " );";

    private static SQLiteDatabase db;

    private static DatabaseHelper dbMgrInstance;

    public static DatabaseHelper getInstance(Context mContext) {
        if (dbMgrInstance == null)
            dbMgrInstance = new DatabaseHelper(mContext);

        return dbMgrInstance;
    }

    public SQLiteDatabase getDb() {
        if (db == null)
            db = getWritableDatabase();
        return db;
    }

    public DatabaseHelper(Context context) {
        super(context, ConversationsContract.CONVERSATIONS_DB, null, ConversationsContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MESSAGES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
