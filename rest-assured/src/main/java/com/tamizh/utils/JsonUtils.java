package com.tamizh.utils;


import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson INSTANCE = new Gson();


    public static Gson getInstance() {
        return INSTANCE;
    }
}
