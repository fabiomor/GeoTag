package com.fabio.gis.geotag;

import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
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

    public static HttpURLConnection httpGetConnection(String urlString) {
        URL url = null;
        HttpURLConnection conn = null;
        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(READ_TIMEOUT);
            conn.setConnectTimeout(CONNECT_TIMEOUT);
            Log.i(TAG,"connecting to " +url);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (IOException e) {
            Log.e(TAG,e.toString());
            e.printStackTrace();
        }
        return conn;
    }

    public static HttpURLConnection httpPostJson(String urlString, String payload) {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            Log.i(TAG,"connecting to " + url);
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            OutputStreamWriter wr= new OutputStreamWriter(conn.getOutputStream());
            wr.write(payload);
            wr.flush();
            wr.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (ProtocolException e) {
            e.printStackTrace();
            Log.e(TAG,e.toString());
        } catch (IOException e) {
            Log.e(TAG,e.toString());
            e.printStackTrace();
        }
        return conn;
    }

}
