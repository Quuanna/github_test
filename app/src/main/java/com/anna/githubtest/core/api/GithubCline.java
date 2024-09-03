package com.anna.githubtest.core.api;

import retrofit2.Retrofit;
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
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }
            }
        }
        return retrofit.create(GitHubService.class);
    }
}
