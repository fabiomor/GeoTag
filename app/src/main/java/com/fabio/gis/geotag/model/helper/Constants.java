package com.fabio.gis.geotag.model.helper;


public interface Constants {


    public static final class CONFIG {
        public static final int APPLICATION_PERMISSIONS_REQUEST = 001;
        public static final int LOGIN_REQUEST_CODE = 002;
        public static final boolean IS_LOGIN_REQUIRED = true;
        public static final String DATE_FORMAT =  "dd/MM/yyyy";

    }

    public static final class FRAGMENT_TAG {
        public static final String MAP_FRAGMENT_TAG = "map_fragment_tag";
        public static final String TOMAP_FRAGMENT_TAG = "tomap_fragment_tag";
    }

    public static final class PREFERENCES {
        public static final String USERNAME_KEY = "pref_username";
        public static final String PASSWORD_KEY = "pref_password";
        public static final String SERVER_ADDRESS_KEY = "pref_serverHostAddress";
        public static final String SERVER_PORT_KEY = "pref_serverHostPort";
        public static final String SERVER_PROTOCOL_KEY = "pref_connectionType";
        public static final String ZOOM_LEVEL = "pref_zoomLevel";
    }

    public static final class HTTP {
        public static final String SERVER_PATH = "http://5.249.152.25:3000/";
    }


    public static final class TOMAP {
        public static final class HTTP {
            public static final String API_PATH = "api/v1/tomap";
            public static final String BASE_URL = Constants.HTTP.SERVER_PATH +  Constants.TOMAP.HTTP.API_PATH;
        }

        public static final class DATABASE {
            public static final String DB_NAME = "tomap.db";
            public static final String TABLE_NAME_RILIEVI = "rilievi";
            public static final String TABLE_NAME_USI_SUOLO = "usi_suolo";
            public static final String TABLE_NAME_AVVERSITA = "avversita";
            public static final String TABLE_NAME_STADI_ACCRESCIMENTO = "stadi_accrescimento";
            public static final String TABLE_NAME_ELEMENTI_QUALITATIVI = "elementi_qualitativi";


            public static final String DB_CREATE_TABLE_RILIEVI = "CREATE TABLE " + TABLE_NAME_RILIEVI + "( " +
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

            public static final String DB_CREATE_TABLE_USI_SUOLO =
                    "CREATE TABLE " + TABLE_NAME_USI_SUOLO + " ( " +
                            "  id_uso INTEGER PRIMARY KEY, " +
                            "  cod_uso TEXT NOT NULL, " +
                            "  nome_uso TEXT, " +
                            "  nome_coltura TEXT " +
                            ");";

            public static final String DB_CREATE_TABLE_AVVERSITA =
                    "CREATE TABLE " + TABLE_NAME_AVVERSITA + " ( " +
                            " id_avversita INTEGER PRIMARY KEY, " +
                            " nome_avversita TEXT " +
                            ");";

            public static final String DB_CREATE_TABLE_STADI_ACCRESCIMENTO =
                    "CREATE TABLE " + TABLE_NAME_STADI_ACCRESCIMENTO + " ( " +
                            " id_stadio_accresc INTEGER PRIMARY KEY, " +
                            " descr_stadio_accresc TEXT " +
                            ");";

            public static final String DB_CREATE_TABLE_ELEMENTI_QUALITATIVI =
                    "CREATE TABLE " + TABLE_NAME_ELEMENTI_QUALITATIVI + " ( " +
                            " id_elemento_qualitativo INTEGER PRIMARY KEY, " +
                            " nome_elemento_qualitativo TEXT " +
                            ");";
        }

        public static final class PREFERENCES {
            public static final String TOMAP_ID_UTENTE = "pref_tomap_id_utente";
            public static final String TOMAP_GOGNOME = "pref_tomap_cognome";
            public static final String TOMAP_ID_GRUPPO = "pref_tomap_id_gruppo";
            public static final String TOMAP_ID_SQUADRA = "pref_tomap_id_squadra";
        }
    }
    //String TOMAP_SAMPLE_INSERT_API_PATH = TOMAP_API + "/" + "sample";
}
