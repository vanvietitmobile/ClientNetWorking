package com.example.clientnetworking;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferencesGetKey {
    Context context;
    SharedPreferences sharedPreferences;
    public static final String NAMELOGIN_USER = "isLogin";
    public static final String EMAIL_USER = "email";
    public static final String HASH_USER = "hash";
    public static final String SAVE_USER = "isSave";


    public SharedPreferencesGetKey(Context context) {
        this.context = context;
    }

    public void SaveUser(String email, String hash, Boolean save) {
        sharedPreferences = context.getSharedPreferences(NAMELOGIN_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_USER, email);
        editor.putString(HASH_USER, hash);
        editor.putBoolean(SAVE_USER, save);
        editor.apply();
    }

    public String isLogin() {
        sharedPreferences = context.getSharedPreferences(NAMELOGIN_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getString(EMAIL_USER, "");
    }

    public Boolean checkSaveLogin() {
        sharedPreferences = context.getSharedPreferences(NAMELOGIN_USER, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(SAVE_USER, false);
    }

    public void LogOut(String email, String hash, Boolean save) {
        sharedPreferences = context.getSharedPreferences(NAMELOGIN_USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(EMAIL_USER, email);
        editor.putBoolean(SAVE_USER, save);
        editor.putString(HASH_USER, hash);
        editor.apply();
    }
}
