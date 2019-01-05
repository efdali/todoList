package com.efdalincesu.todolist.RestApi;

import com.efdalincesu.todolist.Model.Result;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestApi {

    @FormUrlEncoded
    @POST("sign.php")
    Call<Result> sign(@Field("mail") String mail, @Field("sifre") String sifre);

}
