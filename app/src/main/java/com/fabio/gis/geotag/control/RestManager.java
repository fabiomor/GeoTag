package com.fabio.gis.geotag.control;

import com.fabio.gis.geotag.model.callback.TomapService;
import com.fabio.gis.geotag.model.helper.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pc on 22/03/2017.
 */

public class RestManager {
    private TomapService tomapService;

    public TomapService getTomapService() {
        if (tomapService == null) {

            JsonSerializer<Date> ser = new JsonSerializer<Date>() {
                @Override
                public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext
                        context) {
                    return src == null ? null : new JsonPrimitive(src.getTime());
                }
            };

            JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
                    return json == null ? null : new Date(json.getAsLong());
                }
            };

            GsonBuilder gsonBuilder = new GsonBuilder()
                    .setDateFormat(Constants.CONFIG.DATE_FORMAT);
            //.registerTypeAdapter(Date.class, ser)
            //.registerTypeAdapter(Date.class, deser);
            Gson gson = gsonBuilder.create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.HTTP.SERVER_PATH)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            tomapService = retrofit.create(TomapService.class);
        }
        return tomapService;
    }
}
