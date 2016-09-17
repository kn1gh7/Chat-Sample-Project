package practice.kn1gh7.com.chatproject.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by kn1gh7 on 16/9/16.
 */
public class RetrofitAPIHelper {

    public static RemoteApiService getRetrofitServiceHelper() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RemoteApiService.END_POINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RemoteApiService rtService = retrofit.create(RemoteApiService.class);

        return rtService;
    }
}
