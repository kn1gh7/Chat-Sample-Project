package practice.kn1gh7.com.chatproject.model;

/**
 * Created by kn1gh7 on 17/9/16.
 */
public class UserModelCount {
    public String userId;
    public String userName;
    public String sName;
    public String imgUrl;
    public String favoriteCount;
    public String conversationCount;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFavoriteCount() {
        return favoriteCount;
    }

    public void setFavoriteCount(String favoriteCount) {
        this.favoriteCount = favoriteCount;
    }

    public String getConversationCount() {
        return conversationCount;
    }

    public void setConversationCount(String conversationCount) {
        this.conversationCount = conversationCount;
    }
}
