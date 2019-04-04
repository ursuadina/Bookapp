package com.example.andreea.bookhunt.utils;

import android.text.TextUtils;

public class UsernameHelper {
    public static boolean isUsername(String username) {
        return (!TextUtils.isEmpty(username) && username.length() >=7);
    }

    public static boolean isUsernameValid(String username) {
        if(username != null && isUsername(username)) {
            return true;
        } else {
            return false;
        }
    }
}
