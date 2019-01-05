package com.efdalincesu.todolist.RestApi;

import com.efdalincesu.todolist.Model.Result;

import retrofit2.Call;

public class ManagerAll extends BaseManager {

    private static final ManagerAll ourInstance = new ManagerAll();

    public static synchronized ManagerAll getInstance() {
        return ourInstance;
    }

    private ManagerAll() {
    }

    public Call<Result> sign(String mail, String pass){

        Call<Result> call=getRestApi().sign(mail,pass);
        return call;
    }
}
