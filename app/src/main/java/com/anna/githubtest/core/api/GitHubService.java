package com.anna.githubtest.core.api;

import com.anna.githubtest.core.model.GetUserDetailResponse;
import com.anna.githubtest.core.model.ListUsersResponse;
import com.anna.githubtest.core.model.PublicReposResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface GitHubService {

    @GET("users")
    Observable<Response<List<ListUsersResponse>>> fetchListUsers(@Query("per_page") int perPage,
                                                                 @Query("since") int since);
    @GET
    Observable<Response<List<ListUsersResponse>>> fetchListUserNextPage(@Url String url);

    @GET("users/{name}")
    Observable<Response<GetUserDetailResponse>> fetchUsersDetail(@Path("name") String name);

    @GET("users/{name}/repos")
    Observable<Response<List<PublicReposResponse>>> fetchUserRepos(@Path("name") String name);
}
