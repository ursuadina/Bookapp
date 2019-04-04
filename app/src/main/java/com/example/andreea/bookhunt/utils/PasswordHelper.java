package com.example.andreea.bookhunt.utils;

import android.text.TextUtils;

import java.util.regex.Pattern;

public class PasswordHelper {
    public static boolean isPassword(String password) {
        return (!TextUtils.isEmpty(password) && password.length() >=7);
    }

    public static boolean isPasswordValid(String password) {
        if(password != null && isPassword(password)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isConfirmationPassValid(String pass1, String pass2) {
        if (isPasswordValid(pass1) && isPasswordValid(pass2) && pass2.equals(pass1)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isPassowrdEqUsername(String password, String username) {
        if (password.equals(username)) {
            return true;
        } else {
            return false;
        }
    }
}
