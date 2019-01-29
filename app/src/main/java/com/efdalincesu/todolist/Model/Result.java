package com.efdalincesu.todolist.Model;

import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("result")
    private String result;

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return
                "Result{" +
                        "result = '" + result + '\'' +
                        "}";
    }
}