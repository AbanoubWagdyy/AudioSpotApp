package com.audiospotapplication.DataLayer.Retrofit;

import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RestClient {

    private static final int MAX_STALE = 60 * 60 * 24 * 7;
    private static RetrofitService retrofitService;

    public static RetrofitService getRetrofitService(String api_key, String lang, String device_key) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(new MyOkHttpInterceptor(api_key, lang, device_key))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalKeys.EndPoints.BaseURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);

        return retrofitService;
    }

    public static RetrofitService getRetrofitService(String token, String api_key, String lang, String device_key) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
//                .addInterceptor(new GzipRequestInterceptor())
                .addNetworkInterceptor(new MyOkHttpInterceptor(token, api_key, lang, device_key))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(GlobalKeys.EndPoints.BaseURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        retrofitService = retrofit.create(RetrofitService.class);

        return retrofitService;
    }

    public static class MyOkHttpInterceptor implements Interceptor {

        String api_key, lang, device_key, token;

        public MyOkHttpInterceptor(String api_key, String lang, String device_key) {
            this.api_key = api_key;
            this.lang = lang;
            this.device_key = device_key;
        }

        public MyOkHttpInterceptor(String token, String api_key, String lang, String device_key) {
            this.api_key = api_key;
            this.lang = lang;
            this.device_key = device_key;
            this.token = token;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request newRequest;
            if (token == null) {
                newRequest = originalRequest.newBuilder()
                        .header("APIKEY", api_key)
                        .header("LANG", lang)
                        .header("DEVICEKEY", device_key)
                        .build();
            } else {
                newRequest = originalRequest.newBuilder()
                        .header("AUTHORIZATION", token)
                        .header("APIKEY", api_key)
                        .header("LANG", lang)
                        .header("DEVICEKEY", device_key)
                        .build();
            }

            return chain.proceed(newRequest);
        }
    }
}