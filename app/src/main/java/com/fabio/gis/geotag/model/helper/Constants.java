package com.fabio.gis.geotag.model.helper;


public interface Constants {
    int APPLICATION_PERMISSIONS_REQUEST = 001;
    int LOGIN_REQUEST_CODE = 002;
    boolean IS_LOGIN_REQUIRED = true;

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
    String TOMAP_FRAGMENT_TAG = "tomap_fragment_tag";
    String DATE_FORMAT =  "dd/MM/yyyy";

    String SERVER_PATH = "http://5.249.152.25:3000";
    String TOMAP_API = "api/v1/tomap";
    String TOMAP_SAMPLE_INSERT_API_PATH = TOMAP_API + "/" + "sample";

    // DB settings
    String DB_NAME = "tomap.db";
    String TABLE_NAME_RILIEVI = "rilievi";
    String TABLE_NAME_USI_SUOLO = "usi_suolo";
    String TABLE_NAME_AVVERSITA = "avversita";
    String TABLE_NAME_STADI_ACCRESCIMENTO = "stadi_accrescimento";
    String TABLE_NAME_ELEMENTI_QUALITATIVI = "elementi_qualitativi";


    String DB_CREATE_TABLE_RILIEVI = "CREATE TABLE " + TABLE_NAME_RILIEVI + "( " +
            " id_rilievi INTEGER PRIMARY KEY, " +
            " terrasystem INTEGER, " +
            " id_squadra INTEGER, " +
            " id_utente_rilevatore INTEGER, " +
            " id_gruppo_rilevatori INTEGER, " +
            " cognome_rilevatore TEXT, " +
            " nome_area_tomap TEXT, " +
            " azienda_rilevata TEXT, " +
            " data_ora_rilievo TEXT, " +
            " data_ora_invio TEXT, " +
            " cod_punto TEXT, " +
            " cod_transetto TEXT, " +
            " cod_uso TEXT, " +
            " nome_varieta TEXT, " +
            " pacciamato INTEGER, " +
            " lato_strada TEXT, " +
            " visibile INTEGER, " +
            " note_coltura TEXT, " +
            " data_trapianto TEXT, " +
            " id_classe_tempo_trapianto INTEGER, " +
            " id_stadio_accresc INTEGER, " +
            " resa REAL, " +
            " note_rilievo TEXT, " +
            " note_importazione TEXT, " +
            " lat REAL, " +
            " lon REAL, " +
            " mappa TEXT, " +
            " geometry REAL, " +
            " x_stima INTEGER, " +
            " id_uso INTEGER, " +
            " icon TEXT, " +
            " id_avversita INTEGER, " +
            " grado_avversita TEXT, " +
            " id_elemento_qualitativo INTEGER, " +
            " grado_elemento_qualitativo TEXT, " +
            " tipo_pomodoro TEXT, " +
            " data_raccolta TEXT, " +
            " data_trapianto_certezza TEXT " +
            ");";

    String DB_CREATE_TABLE_USI_SUOLO =
            "CREATE TABLE " + TABLE_NAME_USI_SUOLO + " ( " +
            "  id_uso INTEGER PRIMARY KEY, " +
            "  cod_uso TEXT NOT NULL, " +
            "  nome_uso TEXT, " +
            "  nome_coltura TEXT " +
            ");";

    String DB_CREATE_TABLE_AVVERSITA =
            "CREATE TABLE " + TABLE_NAME_AVVERSITA + " ( " +
            " id_avversita INTEGER PRIMARY KEY, " +
            " nome_avversita TEXT " +
            ");";

    String DB_CREATE_TABLE_STADI_ACCRESCIMENTO =
            "CREATE TABLE " + TABLE_NAME_STADI_ACCRESCIMENTO + " ( " +
                    " id_stadio_accresc INTEGER PRIMARY KEY, " +
                    " descr_stadio_accresc TEXT " +
                    ");";

    String DB_CREATE_TABLE_ELEMENTI_QUALITATIVI =
            "CREATE TABLE " + TABLE_NAME_ELEMENTI_QUALITATIVI + " ( " +
                    " id_elemento_qualitativo INTEGER PRIMARY KEY, " +
                    " nome_elemento_qualitativo TEXT " +
                    ");";



}
