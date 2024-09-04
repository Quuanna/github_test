package com.anna.githubtest.core.api;

import com.anna.githubtest.core.model.GetUserDetailResponse;
import com.anna.githubtest.core.model.ListUsersResponse;
import com.anna.githubtest.core.model.PublicReposResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("users")
    Call<List<ListUsersResponse>> fetchListUsers(@Query("per_page") int perPage);

    @GET("users/{name}")
    Call<GetUserDetailResponse> fetchUsersDetail(@Path("name") String name);

    @GET("users/{name}/repos")
    Call<List<PublicReposResponse>> fetchUserRepos(@Path("name") String name);
}
