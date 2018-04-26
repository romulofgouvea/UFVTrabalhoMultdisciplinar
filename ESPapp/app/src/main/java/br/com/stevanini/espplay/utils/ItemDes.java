package br.com.stevanini.espplay.utils;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import br.com.stevanini.espplay.domain.User;


/**
 * Created by Romulo-note on 08/02/2016.
 */
public class ItemDes implements JsonDeserializer<Object>{
    @Override
    public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonElement item = json.getAsJsonObject();

        if(json.getAsJsonObject().get("user") != null){
            item = json.getAsJsonObject().get("user");
        }

        return ( new Gson().fromJson(item,User.class));
    }
}
