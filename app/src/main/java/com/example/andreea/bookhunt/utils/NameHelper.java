package com.example.andreea.bookhunt.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class NameHelper {
    public static boolean isName(String name) {
        String regex = "[A-Z][a-z ,.'-]+";
        return (!TextUtils.isEmpty(name) && Pattern.compile(regex).matcher(name).matches());
    }

    public static boolean isNameValid(String name) {
        if(name != null && isName(name)) {
            return true;
        } else {
            return false;
        }
    }
}
