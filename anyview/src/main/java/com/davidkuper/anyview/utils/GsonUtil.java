package com.davidkuper.anyview.utils;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by malingyi on 2017/3/1.
 */

public class GsonUtil {
    private static Gson gson = new Gson();

    public static Gson getInstance(){
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }

    public static String toJsonString(Object object) {
        if (gson == null) {
            return "";
        }
        return gson.toJson(object);
    }

    public static <T> String toJsonString(Object object,Class<T> clazz) {
        if (gson == null) {
            return "";
        }
        return gson.toJson(object,clazz);
    }

    public JsonElement toJsonObject(String key, String value){
        JsonPrimitive jsonPri = new JsonPrimitive(value);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(key,jsonPri);
        return jsonObject;
    }


    public static  <T> T fromJson(String jsonStr, Class<T> typeOfstr){
        if (gson == null) {
            return null;
        }
        return gson.fromJson(jsonStr,typeOfstr);
    }

    public static  <T> T fromJsonMap(LinkedTreeMap map, Class<T> typeOfstr){
        if (gson == null) {
            return null;
        }
        Log.e("GsonUtil","jsonSrc = " + gson.toJson(map));
        return gson.fromJson(gson.toJson(map),typeOfstr);
    }

}
