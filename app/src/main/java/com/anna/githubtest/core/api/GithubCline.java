package com.anna.githubtest.core.api;

import com.anna.githubtest.core.api.interceptor.HeaderInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GithubCline {

    private static final String WEB_HOST = "https://api.github.com/";
    private static volatile Retrofit retrofit;

    public static GitHubService getInstance() {
        if (retrofit == null) {
            synchronized (GithubCline.class) {
                if (retrofit == null) {
                    retrofit = new Retrofit.Builder()
                            .baseUrl(WEB_HOST)
                            .client(client())
                            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }

        return retrofit.create(GitHubService.class);
    }

    private static OkHttpClient client() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS);

        return new OkHttpClient()
                .newBuilder()
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
