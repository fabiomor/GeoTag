package com.fabio.gis.geotag;

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

/**
 * Created by pc on 13/03/2017.
 */

public class JsonHandler {

    private static JsonHandler singleton;
    private static GsonBuilder gsonBuilder;
    private static Gson gson;

    synchronized static JsonHandler getInstance() {
        if (singleton == null) {
            singleton=new JsonHandler();
        }
        return(singleton);
    }

    private JsonHandler(){
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

        gsonBuilder = new GsonBuilder()
                .setDateFormat(Constants.DATE_FORMAT);
                //.registerTypeAdapter(Date.class, ser)
                //.registerTypeAdapter(Date.class, deser);
        gson = gsonBuilder.create();
    }

    public GsonBuilder getGsonBuilder() {
        return gsonBuilder;
    }

    public void setGsonBuilder(GsonBuilder gsonBuilder) {
        this.gsonBuilder = gsonBuilder;
    }

    public Gson getGson() {
        return gson;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
