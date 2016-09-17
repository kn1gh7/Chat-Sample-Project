package practice.kn1gh7.com.chatproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public class MessageModel implements Parcelable{
    @SerializedName("body")
    public String msgBody;

    public String username;

    @SerializedName("Name")
    public String sName;

    @SerializedName("image-url")
    public String sImgUrl;

    @SerializedName("message-time")
    public String msgTime;

    public String isFavorite; //Used by DB

    public String id;    //Used by DB

    public MessageModel() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getMsgBody() {
        return msgBody;
    }

    public void setMsgBody(String msgBody) {
        this.msgBody = msgBody;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsImgUrl() {
        return sImgUrl;
    }

    public void setsImgUrl(String sImgUrl) {
        this.sImgUrl = sImgUrl;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }


    public MessageModel (Parcel parcel) {
        this.id = parcel.readString();
        this.msgBody = parcel.readString();
        this.sName = parcel.readString();
        this.username = parcel.readString();
        this.sImgUrl = parcel.readString();
        this.msgTime = parcel.readString();
        this.isFavorite = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(msgBody);
        dest.writeString(sName);
        dest.writeString(username);
        dest.writeString(sImgUrl);
        dest.writeString(msgTime);
        dest.writeString(isFavorite);
    }

    // Method to recreate a Question from a Parcel
    public static Creator<MessageModel> CREATOR = new Creator<MessageModel>() {

        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }

    };
}
