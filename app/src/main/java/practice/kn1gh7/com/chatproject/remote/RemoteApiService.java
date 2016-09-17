package practice.kn1gh7.com.chatproject.remote;

import practice.kn1gh7.com.chatproject.model.ConversationModel;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public interface RemoteApiService {
    String END_POINT = "http://haptik.co/android/";

    @GET("test_data")
    Call<ConversationModel> loadConversations();
}
