package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.anna.githubtest.core.api.GitHubService;
import com.anna.githubtest.core.api.GithubCline;
import com.anna.githubtest.core.model.GetUserDetailResponse;
import com.anna.githubtest.core.model.PublicReposResponse;
import com.anna.githubtest.data.DetailInfo;
import com.anna.githubtest.data.PublicRepos;
import com.anna.githubtest.element.UiState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailViewModel extends BaseViewModel {

    private final GitHubService apiService;

    public DetailViewModel(GitHubService apiService) {
        this.apiService = apiService;
    }

    public static final ViewModelInitializer<DetailViewModel> initializer = new ViewModelInitializer<>(
            DetailViewModel.class, creationExtras -> new DetailViewModel(GithubCline.getInstance())
    );

    public LiveData<DetailInfo> getDetailInfo() {
        return detailInfo;
    }
    public LiveData<List<PublicRepos>> getPublicRepos() {
        return publicRepos;
    }

    private final MutableLiveData<DetailInfo> detailInfo = new MutableLiveData<>();
    private final MutableLiveData<List<PublicRepos>> publicRepos = new MutableLiveData<>();

    /**
     * Api Get User
     *
     * @param name login
     */
    public void callApiGetUserDetail(String name) {
        uiState.setValue(UiState.LOADING);
        apiService.fetchUsersDetail(name).enqueue(new Callback<GetUserDetailResponse>() {
            @Override
            public void onResponse(Call<GetUserDetailResponse> call, Response<GetUserDetailResponse> response) {
                DetailInfo info = new DetailInfo();
                if (response.isSuccessful() && response.body() != null) {
                    info.setImageUrl(response.body().getAvatarUrl());
                    info.setLogin(response.body().getLogin());
                    info.setName(response.body().getName());
                    info.setFollowers(response.body().getFollowers());
                    info.setFollowing(response.body().getFollowing());
                    info.setLocation(response.body().getLocation());
                    info.setCompany(response.body().getCompany());
                    info.setBlog(response.body().getBlog());
                }
                detailInfo.postValue(info);
                uiState.setValue(UiState.SUCCESS);
            }

            @Override
            public void onFailure(Call<GetUserDetailResponse> call, Throwable throwable) {
                uiState.setValue(UiState.ERROR);
            }
        });
    }

    /**
     * Api PublicRepos
     *
     * @param name login
     */
    public void callApiPublicRepos(String name) {
        uiState.setValue(UiState.LOADING);
        apiService.fetchUserRepos(name).enqueue(new Callback<List<PublicReposResponse>>() {
            @Override
            public void onResponse(Call<List<PublicReposResponse>> call, Response<List<PublicReposResponse>> response) {
                List<PublicRepos> list = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    for (PublicReposResponse data : response.body()) {
                        PublicRepos repos = new PublicRepos();
                        repos.setName(data.getName());
                        repos.setDescription(data.getDescription());
                        repos.setVisibility(data.getVisibility());
                        list.add(repos);
                    }
                }
                publicRepos.postValue(list);
                uiState.setValue(UiState.SUCCESS);
            }

            @Override
            public void onFailure(Call<List<PublicReposResponse>> call, Throwable throwable) {
                uiState.setValue(UiState.ERROR);
            }
        });
    }
}
