package com.example.andreea.bookhunt.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {

    public static void setStringValueForUserInfo (String key, String value, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getStringValueForUserInfo (String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static void deleteValueFromSharedPreferences(String key, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }

    public static void deleteAllValuesFromSharedPreferences (Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.USER_INFO,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
