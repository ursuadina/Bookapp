package com.example.andreea.bookhunt.utils;

import android.text.TextUtils;
import android.util.Patterns;

public class EmailHelper {
    public static boolean isEmailAddress(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isEmailValid(String email) {
        if (email != null && isEmailAddress(email)) {
            return true;
        } else {
            return false;
        }
    }
}
