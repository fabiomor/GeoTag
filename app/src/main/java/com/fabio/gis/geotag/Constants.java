package com.fabio.gis.geotag;


public interface Constants {


    // preferences
    String USERNAME_KEY = "pref_username";
    String PASSWORD_KEY = "pref_password";
    String SERVER_ADDRESS_KEY = "pref_serverHostAddress";
    String SERVER_PORT_KEY = "pref_serverHostPort";
    String SERVER_PROTOCOL_KEY = "pref_connectionType";
    String ZOOM_LEVEL = "pref_zoomLevel";

    boolean IS_LOGIN_REQUIRED = false;

    String MAP_FRAGMENT_TAG = "map_fragment_tag";
    String TOMAP_FRAGMENT_TAG = "topmap_fragment_tag";
    String DATE_FORMAT =  "dd/MM/yyyy";
    String DATE_CERTIFIED = "si";
    String DATE_ESTIMATED = "no, la data è stimata";


    int APPLICATION_PERMISSIONS_REQUEST = 123;
}
