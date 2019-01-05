package com.efdalincesu.todolist.RestApi;

public class BaseManager {

    private final String BASE_URL="http://192.168.2.10/todolist/";

    protected RestApi getRestApi(){
        RestApiClient client=new RestApiClient(BASE_URL);
        return client.getRestApi();
    }
}
