package com.example.hoangha.lab2;


import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HoangHa on 10/23/2016.
 */

public class RetrofitUtils {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final Gson GSON = new Gson();

    public static Retrofit get() {
        return new Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .client(client())
                .addConverterFactory(GsonConverterFactory.create())//me
                .build();
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .addInterceptor(apiKeyInterceptor())
                .addInterceptor(responseInterceptor())
                .build();
    }

    private static Interceptor responseInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Response response = chain.proceed(request);
                ResponseBody body = response.body();

                ApiResponse apiResponse = GSON.fromJson(body.string(), ApiResponse.class);
                body.close();

                return response.newBuilder()
                        .body(ResponseBody.create(JSON, apiResponse.getResponse().toString()))
                        .build();
            }
        };
    }

    private static Interceptor apiKeyInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                HttpUrl httpUrl = request.url()
                        .newBuilder()
                        .addQueryParameter("api_key", Constant.API_KEY)
                        .build();

                request = request.newBuilder()
                        .url(httpUrl)
                        .build();

                return chain.proceed(request);
            }
        };
    }


}