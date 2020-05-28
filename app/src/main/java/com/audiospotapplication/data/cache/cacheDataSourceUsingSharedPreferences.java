package com.audiospotapplication.data.cache;

import android.content.Context;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.audiospot.DataLayer.Model.AuthResponse;
import com.audiospotapplication.data.retrofit.GlobalKeys;
import com.google.gson.Gson;

public class cacheDataSourceUsingSharedPreferences implements CacheDataSource {

    private static cacheDataSourceUsingSharedPreferences INSTANCE;

    private SharedPreferences mPreferences;

    private cacheDataSourceUsingSharedPreferences(Context context) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static cacheDataSourceUsingSharedPreferences getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new cacheDataSourceUsingSharedPreferences(context);
        }

        return INSTANCE;
    }

    public static cacheDataSourceUsingSharedPreferences getINSTANCE() {
        if (INSTANCE == null) {
            throw new RuntimeException("Trying to use cacheDataSourceUsingSharedPreferences without setting the context first!");
        }
        return INSTANCE;
    }

    @Override
    public void setStringIntoCache(String key, String value) {
        mPreferences.edit().putString(key, value).apply();
    }

    @Override
    public String getStringFromCache(String key, String defaultValue) {
        return mPreferences.getString(key, defaultValue);
    }

    @Override
    public void setBooleanIntoCache(String key, Boolean value) {
        mPreferences.edit().putBoolean(key, value).apply();
    }

    @Override
    public Boolean getBooleanFromCache(String key, Boolean defaultValue) {
        return mPreferences.getBoolean(key, defaultValue);
    }

    @Override
    public void setLoggedInUser(Object b) {
        String str = new Gson().toJson(b);
        mPreferences.edit().putString(GlobalKeys.StoreData.Logged_In_User, str).apply();
    }

    @Override
    public AuthResponse getLoggedInUser() {
        String str = mPreferences.getString(GlobalKeys.StoreData.Logged_In_User, null);
        if (str == null || str.equals(""))
            return null;
        return new Gson().fromJson(str, AuthResponse.class);
    }

    @Override
    public void clear() {
        mPreferences.edit().putBoolean(GlobalKeys.StoreData.IS_LOGGED, false).apply();
        mPreferences.edit().putString(GlobalKeys.StoreData.Logged_In_User, null).apply();
    }
}