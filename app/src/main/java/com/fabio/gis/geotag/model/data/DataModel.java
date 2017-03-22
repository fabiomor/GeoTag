package com.fabio.gis.geotag.model.data;

/**
 * Created by pc on 09/03/2017.
 */

import com.google.gson.annotations.*;

import java.util.Date;

public class DataModel {

    public static class TomapSample {

        // autocompletate
        @SerializedName("terrasystem")
        private Boolean terrasystem;
        @SerializedName("id_utente")
        private Integer id_utente_rilevatore;
        @SerializedName("id_gruppo")
        private Integer id_gruppo_rilevatori;
        @SerializedName("id_squadra")
        private Integer id_squadra;
        @SerializedName("cognome")
        private String cognome_rilevatore;
        private Date data_ora_invio;



        // non utilizzate al momento
        private String cod_punto;
        private String cod_transetto;
        private String nome_varieta;
        private String nome_area_tomap;
        private String azienda_rilevata;
        @SerializedName("x_stima")
        private Boolean x_stima;
        private String icon;
        private String note_importazione;
        private String mappa;
        @SerializedName("id_uso")
        private Integer id_uso;
        private Integer id_classe_tempo_trapianto;
        private String lato_strada;
        private Boolean visibile;



        // utilizzate
        @SerializedName("gid")
        private Long gid;
        @SerializedName("lat")
        private Double lat;
        @SerializedName("lon")
        private Double lon;
        private Date data_ora_rilievo;
        private Integer cod_uso;
        private String tipo_pomodoro;
        @SerializedName("pacciamato")
        private Boolean pacciamato;
        private String note_coltura;
        private Date data_trapianto;
        private String data_trapianto_certezza;
        @SerializedName("id_stadio_accresc")
        private Integer id_stadio_accresc;
        private Date data_raccolta;
        @SerializedName("resa")
        private Double resa;
        @SerializedName("id_avversita")
        private Integer id_avversita;
        private String grado_avversita;
        @SerializedName("id_elemento_qualitativo")
        private Integer id_elemento_qualitativo;
        private String grado_elemento_qualitativo;
        private String note_rilievo;

        public Long getGid() {
            return gid;
        }

        public void setGid(Long gid) {
            this.gid = gid;
        }

        public Boolean getTerrasystem() {
            return terrasystem;
        }

        public void setTerrasystem(Boolean terrasystem) {
            this.terrasystem = terrasystem;
        }

        public Integer getId_utente_rilevatore() {
            return id_utente_rilevatore;
        }

        public void setId_utente_rilevatore(Integer id_utente_rilevatore) {
            this.id_utente_rilevatore = id_utente_rilevatore;
        }

        public Integer getId_gruppo_rilevatori() {
            return id_gruppo_rilevatori;
        }

        public void setId_gruppo_rilevatori(Integer id_gruppo_rilevatori) {
            this.id_gruppo_rilevatori = id_gruppo_rilevatori;
        }

        public Date getData_ora_invio() {
            return data_ora_invio;
        }

        public void setData_ora_invio(Date data_ora_invio) {
            this.data_ora_invio = data_ora_invio;
        }

        public Integer getId_squadra() {
            return id_squadra;
        }

        public void setId_squadra(Integer id_squadra) {
            this.id_squadra = id_squadra;
        }

        public String getCod_punto() {
            return cod_punto;
        }

        public void setCod_punto(String cod_punto) {
            this.cod_punto = cod_punto;
        }

        public String getCod_transetto() {
            return cod_transetto;
        }

        public void setCod_transetto(String cod_transetto) {
            this.cod_transetto = cod_transetto;
        }

        public String getNome_varieta() {
            return nome_varieta;
        }

        public void setNome_varieta(String nome_varieta) {
            this.nome_varieta = nome_varieta;
        }

        public String getCognome_rilevatore() {
            return cognome_rilevatore;
        }

        public void setCognome_rilevatore(String cognome_rilevatore) {
            this.cognome_rilevatore = cognome_rilevatore;
        }

        public String getNome_area_tomap() {
            return nome_area_tomap;
        }

        public void setNome_area_tomap(String nome_area_tomap) {
            this.nome_area_tomap = nome_area_tomap;
        }

        public String getAzienda_rilevata() {
            return azienda_rilevata;
        }

        public void setAzienda_rilevata(String azienda_rilevata) {
            this.azienda_rilevata = azienda_rilevata;
        }

        public Boolean getX_stima() {
            return x_stima;
        }

        public void setX_stima(Boolean x_stima) {
            this.x_stima = x_stima;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getNote_importazione() {
            return note_importazione;
        }

        public void setNote_importazione(String note_importazione) {
            this.note_importazione = note_importazione;
        }

        public String getMappa() {
            return mappa;
        }

        public void setMappa(String mappa) {
            this.mappa = mappa;
        }

        public Integer getId_uso() {
            return id_uso;
        }

        public void setId_uso(Integer id_uso) {
            this.id_uso = id_uso;
        }

        public Integer getId_classe_tempo_trapianto() {
            return id_classe_tempo_trapianto;
        }

        public void setId_classe_tempo_trapianto(Integer id_classe_tempo_trapianto) {
            this.id_classe_tempo_trapianto = id_classe_tempo_trapianto;
        }

        public String getLato_strada() {
            return lato_strada;
        }

        public void setLato_strada(String lato_strada) {
            this.lato_strada = lato_strada;
        }

        public Boolean getVisibile() {
            return visibile;
        }

        public void setVisibile(Boolean visibile) {
            this.visibile = visibile;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public Date getData_ora_rilievo() {
            return data_ora_rilievo;
        }

        public void setData_ora_rilievo(Date data_ora_rilievo) {
            this.data_ora_rilievo = data_ora_rilievo;
        }

        public Integer getCod_uso() {
            return cod_uso;
        }

        public void setCod_uso(Integer cod_uso) {
            this.cod_uso = cod_uso;
        }

        public String getTipo_pomodoro() {
            return tipo_pomodoro;
        }

        public void setTipo_pomodoro(String tipo_pomodoro) {
            this.tipo_pomodoro = tipo_pomodoro;
        }

        public Boolean getPacciamato() {
            return pacciamato;
        }

        public void setPacciamato(Boolean pacciamato) {
            this.pacciamato = pacciamato;
        }

        public String getNote_coltura() {
            return note_coltura;
        }

        public void setNote_coltura(String note_coltura) {
            this.note_coltura = note_coltura;
        }

        public Date getData_trapianto() {
            return data_trapianto;
        }

        public void setData_trapianto(Date data_trapianto) {
            this.data_trapianto = data_trapianto;
        }

        public String getData_trapianto_certezza() {
            return data_trapianto_certezza;
        }

        public void setData_trapianto_certezza(String data_trapianto_certezza) {
            this.data_trapianto_certezza = data_trapianto_certezza;
        }

        public Integer getId_stadio_accresc() {
            return id_stadio_accresc;
        }

        public void setId_stadio_accresc(Integer id_stadio_accresc) {
            this.id_stadio_accresc = id_stadio_accresc;
        }

        public Date getData_raccolta() {
            return data_raccolta;
        }

        public void setData_raccolta(Date data_raccolta) {
            this.data_raccolta = data_raccolta;
        }

        public Double getResa() {
            return resa;
        }

        public void setResa(Double resa) {
            this.resa = resa;
        }

        public Integer getId_avversita() {
            return id_avversita;
        }

        public void setId_avversita(Integer id_avversita) {
            this.id_avversita = id_avversita;
        }

        public String getGrado_avversita() {
            return grado_avversita;
        }

        public void setGrado_avversita(String grado_avversita) {
            this.grado_avversita = grado_avversita;
        }

        public Integer getId_elemento_qualitativo() {
            return id_elemento_qualitativo;
        }

        public void setId_elemento_qualitativo(Integer id_elemento_qualitativo) {
            this.id_elemento_qualitativo = id_elemento_qualitativo;
        }

        public String getGrado_elemento_qualitativo() {
            return grado_elemento_qualitativo;
        }

        public void setGrado_elemento_qualitativo(String grado_elemento_qualitativo) {
            this.grado_elemento_qualitativo = grado_elemento_qualitativo;
        }

        public String getNote_rilievo() {
            return note_rilievo;
        }

        public void setNote_rilievo(String note_rilievo) {
            this.note_rilievo = note_rilievo;
        }




    }

}
