package com.efdalincesu.todolist.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.efdalincesu.todolist.Model.User;

public class Auth {

    private static String SHARED_NAME = "Cookie";
    private static String IS_LOGIN = "isLogin";
    private static String UYE_ID = "uyeId";
    private static String UYE_MAIL = "uyeMail";
    static Context context;
    static SharedPreferences preferences;
    static SharedPreferences.Editor editor;
    private static boolean isLogin;
    private static User user;


    public Auth(boolean isLogin, User user, Context context) {
        this.isLogin = isLogin;
        this.user = user;
        this.context = context;
    }

    public static boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean isLogin,User user) {
        Auth.isLogin = isLogin;
        setUser(user);
        commit();
    }

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        if (user!=null)
            Auth.user = user;
        else
            Auth.user.set();
    }

    public void commit() {


        editor.putBoolean(IS_LOGIN, isLogin);
        editor.putString(UYE_ID, user.getUser_id());
        editor.putString(UYE_MAIL, user.getUser_mail());

        editor.commit();

    }


    public static Auth init(Context context){
        preferences=context.getSharedPreferences(SHARED_NAME,Context.MODE_PRIVATE);
        editor=preferences.edit();

        isLogin = preferences.getBoolean(IS_LOGIN, false);
        user=new User();
        user.setUser_id(preferences.getString(UYE_ID, "-1"));
        user.setUser_mail(preferences.getString(UYE_MAIL, "-1"));

        return new Auth(isLogin,user,context);
    }

}
