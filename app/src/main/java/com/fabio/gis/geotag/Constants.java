package com.fabio.gis.geotag;


public interface Constants {
    int APPLICATION_PERMISSIONS_REQUEST = 123;
    boolean IS_LOGIN_REQUIRED = true;

    // DB settings


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



    String MAP_FRAGMENT_TAG = "map_fragment_tag";
    String TOMAP_FRAGMENT_TAG = "topmap_fragment_tag";
    String DATE_FORMAT =  "dd/MM/yyyy";
    //String SERVER_PATH = "http://5.249.152.25:3000";
    String SERVER_PATH = "http://5.249.152.25:3000";
    String TOMAP_API = "api/v1/tomap";
    String TOMAP_SAMPLE_INSERT_API_PATH = TOMAP_API + "/" + "sample";

    String DB_CREATE_TABLE_RILIEVI = "CREATE TABLE `Rilievi` (\n" +
            "\t`_ID`\tINTEGER,\n" +
            "\t`terrasystem`\tINTEGER,\n" +
            "\t`id_squadra`\tINTEGER,\n" +
            "\t`id_utente_rilevatore`\tINTEGER,\n" +
            "\t`id_gruppo_rilevatori`\tINTEGER,\n" +
            "\t`cognome_rilevatore`\tTEXT,\n" +
            "\t`nome_area_tomap`\tTEXT,\n" +
            "\t`azienda_rilevata`\tTEXT,\n" +
            "\t`data_ora_rilievo`\tTEXT,\n" +
            "\t`data_ora_invio`\tTEXT,\n" +
            "\t`cod_punto`\tTEXT,\n" +
            "\t`cod_transetto`\tTEXT,\n" +
            "\t`cod_uso`\tTEXT,\n" +
            "\t`nome_varieta`\tTEXT,\n" +
            "\t`pacciamato`\tINTEGER,\n" +
            "\t`lato_strada`\tTEXT,\n" +
            "\t`visibile`\tINTEGER,\n" +
            "\t`note_coltura`\tTEXT,\n" +
            "\t`data_trapianto`\tTEXT,\n" +
            "\t`id_classe_tempo_trapianto`\tINTEGER,\n" +
            "\t`id_stadio_accresc`\tINTEGER,\n" +
            "\t`resa`\tREAL,\n" +
            "\t`note_rilievo`\tTEXT,\n" +
            "\t`note_importazione`\tTEXT,\n" +
            "\t`lat`\tREAL,\n" +
            "\t`lon`\tREAL,\n" +
            "\t`mappa`\tTEXT,\n" +
            "\t`geometry`\tREAL,\n" +
            "\t`x_stima`\tINTEGER,\n" +
            "\t`id_uso`\tINTEGER,\n" +
            "\t`icon`\tTEXT,\n" +
            "\t`id_avversita`\tINTEGER,\n" +
            "\t`grado_avversita`\tTEXT,\n" +
            "\t`id_elemento_qualitativo`\tINTEGER,\n" +
            "\t`grado_elemento_qualitativo`\tTEXT,\n" +
            "\t`tipo_pomodoro`\tTEXT,\n" +
            "\t`data_raccolta`\tTEXT,\n" +
            "\t`data_trapianto_certezza`\tTEXT,\n" +
            "\tPRIMARY KEY(`_ID`)\n" +
            ");";

}
