package practice.kn1gh7.com.chatproject.utility;

import android.util.Log;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public class LogHelper {
    private String logHelperTag;

    public LogHelper(String tag) {
        this.logHelperTag = tag;
    }

    public void showLog(String msg) {
        Log.e(logHelperTag, msg);
    }
}
