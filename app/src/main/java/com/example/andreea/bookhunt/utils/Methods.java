package com.example.andreea.bookhunt.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.content.Context;
public class Methods {
    public static boolean checkPermissions(Context context, Activity activity) {
        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.PERMISSION_REQUEST_READ_EXTERNAL_STORAGE);
            return false;
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    Constants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            return false;
        }

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.INTERNET},
                    Constants.PERMISSION_REQUEST_INTERNET);
            return false;
        }
//
//        if(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(activity,
//                    new String[]{Manifest.permission.CAMERA},
//                    Constants.PERMISSION_REQUEST_CAMERA);
//            return false;
//        }
     return true;
    }
}
