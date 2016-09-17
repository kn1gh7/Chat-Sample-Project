package practice.kn1gh7.com.chatproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public class ConversationModel implements Parcelable{
    public long count;

    @SerializedName("messages")
    public List<MessageModel> messagesList;

    public ConversationModel() {}

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<MessageModel> getMessagesList() {
        return messagesList;
    }

    public void setMessagesList(List<MessageModel> messagesList) {
        this.messagesList = messagesList;
    }


    public ConversationModel (Parcel parcel) {
        this.count = parcel.readLong();
        parcel.readTypedList(this.messagesList, MessageModel.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Required method to write to Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(count);
        dest.writeList(this.messagesList);
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
