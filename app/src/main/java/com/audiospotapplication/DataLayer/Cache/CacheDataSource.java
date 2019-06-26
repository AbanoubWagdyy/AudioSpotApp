package com.audiospotapplication.DataLayer.Cache;

import com.audiospot.DataLayer.Model.AuthResponse;

/**
 * This interface contains methods for caching setInboxResponse of key value and be
 * implemented by cacheDataUsingSharedPreferences class
 */

public interface CacheDataSource {

    void setStringIntoCache(String key, String value);

    String getStringFromCache(String key, String defaultValue);

    void setBooleanIntoCache(String key, Boolean value);

    Boolean getBooleanFromCache(String key, Boolean defaultValue);

    void setLoggedInUser(Object b);

    AuthResponse getLoggedInUser();
}
