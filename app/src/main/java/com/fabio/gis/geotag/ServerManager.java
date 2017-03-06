package com.fabio.gis.geotag;

import android.util.Log;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pc on 03/03/2017.
 */

public class ServerManager {

    private static final String TAG = ServerManager.class.getSimpleName();
    private static final int READ_TIMEOUT = 5000;
    private static final int CONNECT_TIMEOUT = 15000;

    public static int httpGet(String urlString) {
        int responseCode = 401;
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            conn.setRequestMethod("GET");
            Log.i(TAG,"connecting to " +url);
            conn.connect();
            responseCode =  conn.getResponseCode();
        } catch (Exception e) {
            Log.e(TAG, "multipart post error " + e + "(" + urlString + ")");
        }
        return responseCode;
    }

}
