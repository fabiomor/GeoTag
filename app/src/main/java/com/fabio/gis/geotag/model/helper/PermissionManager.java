package com.fabio.gis.geotag.model.helper;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {

    private static List<String> deniedPermissions = new ArrayList<>();

    public static boolean checkPermissions(Context context, List<String> permissions) {
        boolean hasAllNeededPermissions = true;
        for (String permission : permissions) {
            int result = ContextCompat.checkSelfPermission(context, permission);
            boolean granted = PackageManager.PERMISSION_GRANTED == result;
            hasAllNeededPermissions &= granted;
            if (!granted) {
                deniedPermissions.add(permission);
            }
        }
        return hasAllNeededPermissions;
    }

    public static void requestPermissions(Activity activity, int applicationRequest){
        ActivityCompat.requestPermissions(activity,
                deniedPermissions.toArray(new String[deniedPermissions.size()]),
               applicationRequest);
    }
}
