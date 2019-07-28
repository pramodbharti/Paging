package com.db.paging.network;


import com.db.paging.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient INSTANCE;
    private static Retrofit retrofit;

    private RetrofitClient(){
       //Optional
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        //^

        retrofit=new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance(){
        if(INSTANCE==null){
            INSTANCE = new RetrofitClient();
        }
        return INSTANCE;
    }

    public ApiService getApiService(){
        return retrofit.create(ApiService.class);
    }
}
