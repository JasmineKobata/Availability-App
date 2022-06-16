package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mygdx.game.SerializableObjects.AvailabilityData;
import com.mygdx.game.SerializableObjects.Client;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class JsonFactory {
    public static FileHandle clientFile = Gdx.files.local("clientdata.json");
    static Json json = new Json();

    public static String classToJsonString(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

    public static void classToJsonFile(FileHandle file, Object obj) {
        file.writeString(json.toJson(obj), false);
    }

    public static void stringToJsonFile(FileHandle file, String s) {
        file.writeString(s, false);
    }

    public static <T> T classFromJsonFile(FileHandle file, Class<T> type) {
        return (T) json.fromJson(type, file);
    }

    public static <T> T classFromJsonString(String s, Class<T> type) {
        Gson gson = new Gson();
        return (T) gson.fromJson(s, type);
//        return (T) json.fromJson(type, s);
    }

    public static <T> List<Client> listFromJsonString(String s) {
        Type listType = new TypeToken<List<Client>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(s, listType);
    }

    public static <T> HashMap<Integer, AvailabilityData> mapFromJsonString(String s) {
        Type listType = new TypeToken<HashMap<Integer, AvailabilityData>>() {}.getType();
        Gson gson = new Gson();
        return gson.fromJson(s, listType);
    }
}
