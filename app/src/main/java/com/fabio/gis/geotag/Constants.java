package com.fabio.gis.geotag;


public interface Constants {


    // preferences
    String SHARED_PREFERENCES_NAME = "geotagsharedprefs";
    String USERNAME_KEY = "pref_username";
    String PASSWORD_KEY = "pref_password";
    String SERVER_ADDRESS_KEY = "pref_serverHostAddress";
    String SERVER_PORT_KEY = "pref_serverHostPort";
    String SERVER_PROTOCOL_KEY = "pref_connectionType";
    String ZOOM_LEVEL = "pref_zoomLevel";
    // tomap preferences
    String TOMAP_ID_UTENTE = "pref_tomap_id_utente";
    String TOMAP_GOGNOME = "pref_tomap_cognome";
    String TOMAP_ID_GRUPPO = "pref_tomap_id_gruppo";
    String TOMAP_ID_SQUADRA = "pref_tomap_id_squadra";

    boolean IS_LOGIN_REQUIRED = true;

    String MAP_FRAGMENT_TAG = "map_fragment_tag";
    String TOMAP_FRAGMENT_TAG = "topmap_fragment_tag";
    String DATE_FORMAT =  "dd/MM/yyyy";
    //String SERVER_PATH = "http://5.249.152.25:3000";
    String SERVER_PATH = "http://5.249.152.25:3000";
    String TOMAP_API = "api/v1/tomap";

    int APPLICATION_PERMISSIONS_REQUEST = 123;
}
