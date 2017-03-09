package com.fabio.gis.geotag;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.internal.zzf;

/**
 * Created by pc on 09/03/2017.
 */

public class MapMarker {

    private Marker marker;
    private LatLng positon;
    private MarkerOptions markerOptions;

    public MapMarker(Marker marker, LatLng position) {
        this.marker = marker;
        this.positon = position;
    }

    public MapMarker(Marker marker, LatLng position, MarkerOptions options) {
        this.marker = marker;
        this.positon = position;
        this.markerOptions = options;
    }

    public LatLng getPositon() {
        return positon;
    }

    public void setPositon(LatLng positon) {
        this.positon = positon;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions) {
        this.markerOptions = markerOptions;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
