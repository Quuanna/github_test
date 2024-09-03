package com.anna.githubtest.core.api;

import com.anna.githubtest.core.model.ListUsersResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GitHubService {
    @GET("users")
    Call<List<ListUsersResponse>> getListUsers(@Query("per_page") int perPage);
}
