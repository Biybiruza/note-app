package com.biybiruza.noteapp;

import android.content.Context;
import android.content.SharedPreferences;

import com.biybiruza.noteapp.data.NoteModels;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MyShared {
    private SharedPreferences sharedPreferences;
    private Gson gson;

    public MyShared(Context context, Gson gson) {
        this.sharedPreferences = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE);
        this.gson = gson;
    }

    public <T> void putList(String key, List<T> list) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, gson.toJson(list));
        editor.apply();
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        String data = sharedPreferences.getString(key, null);

        Type typeOfT = new TypeToken<List<NoteModels>>() {
        }.getType();

        return gson.fromJson(data, typeOfT);
    }
}
