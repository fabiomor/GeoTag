package com.fabio.gis.geotag.model.callback;

import com.fabio.gis.geotag.model.data.DataModel;
import com.fabio.gis.geotag.model.helper.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pc on 22/03/2017.
 */

public interface TomapService {

    @GET("api/v1/tomap/sample")
    Call<List<DataModel.TomapSample>> getTomapSamples();
}
