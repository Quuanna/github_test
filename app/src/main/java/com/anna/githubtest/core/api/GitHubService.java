package com.anna.githubtest.core.api;

import com.anna.githubtest.core.model.GetUserDetailResponse;
import com.anna.githubtest.core.model.ListUsersResponse;
import com.anna.githubtest.core.model.PublicReposResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GitHubService {

    @GET("users")
    Observable<List<ListUsersResponse>> fetchListUsers(@Query("per_page") int perPage);

    @GET("users/{name}")
    Observable<GetUserDetailResponse> fetchUsersDetail(@Path("name") String name);

    @GET("users/{name}/repos")
    Observable<List<PublicReposResponse>> fetchUserRepos(@Path("name") String name);
}
