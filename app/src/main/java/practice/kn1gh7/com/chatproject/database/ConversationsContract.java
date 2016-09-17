package practice.kn1gh7.com.chatproject.database;

import android.provider.BaseColumns;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class ConversationsContract {
    public static final String CONVERSATIONS_DB = "conversations.db";
    public static final int DB_VERSION = 1;

    public class Users implements BaseColumns { //_ID is the user ID
        public static final String USER_TABLE = "users";

        public static final String USER_USERNAME = "username";
        public static final String USER_SNAME = "sname";
        public static final String USER_IMGURL = "img";
    }

    public class Messages implements BaseColumns { //_ID is the message ID
        public static final String MESSAGES_TABLE = "messages";

        public static final String MESSAGES_USERID = "user_id";
        public static final String MESSAGES_BODY = "body";
        public static final String MESSAGES_MSG_TIME = "msg_time";
        public static final String MESSAGES_IS_FAVORITE = "is_favorite"; //1 for true, -1 for false
    }
}
