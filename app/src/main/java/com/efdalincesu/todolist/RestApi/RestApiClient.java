package com.efdalincesu.todolist.RestApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestApiClient {

    private RestApi mRestApi;

    public RestApiClient(String serviceBaseUrl){

        Gson gson=new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(serviceBaseUrl)
                .client(new OkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        mRestApi=retrofit.create(RestApi.class);

    }

    public RestApi getRestApi() {
        return mRestApi;
    }
}
