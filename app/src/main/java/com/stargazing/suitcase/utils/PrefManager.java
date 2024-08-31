package com.stargazing.suitcase.utils;

import android.content.Context;

import com.stargazing.suitcase.database.entities.User;

public class PrefManager {

    private static final String PREF_NAME = "SUITCASE";

    public static void setUser(Context context, String email) {
        var sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        var spEditor = sp.edit();
        spEditor.putString("user_email", email);
        spEditor.apply();
    }

    public static String getUser(Context context) {
        var sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sp.getString("user_email", null);
    }

    public static void clear(Context context) {
        var sp = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        var spEditor = sp.edit();
        spEditor.clear();
        spEditor.apply();
    }

}
