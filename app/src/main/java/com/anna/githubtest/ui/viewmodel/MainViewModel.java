package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.anna.githubtest.core.api.GitHubService;
import com.anna.githubtest.core.api.GithubCline;
import com.anna.githubtest.core.model.ListUsersResponse;
import com.anna.githubtest.data.ListUsers;
import com.anna.githubtest.element.UiState;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends BaseViewModel {

    private final GitHubService apiService;

    public MainViewModel(GitHubService apiService) {
        this.apiService = apiService;
    }

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class, creationExtras -> new MainViewModel(GithubCline.getInstance())
    );

    public LiveData<List<ListUsers>> getUserBasicList() {
        return userBasicList;
    }

    private final MutableLiveData<List<ListUsers>> userBasicList = new MutableLiveData<>();

    /**
     * Api ListUsers
     */
    public void callApiGetListUsers() {
        uiState.setValue(UiState.LOADING);
        apiService.fetchListUsers(100).enqueue(new Callback<List<ListUsersResponse>>() {
            @Override
            public void onResponse(Call<List<ListUsersResponse>> call, Response<List<ListUsersResponse>> response) {
                List<ListUsers> itemDataList = new ArrayList<>();
                if (response.isSuccessful() && response.body() != null) {
                    for (ListUsersResponse data : response.body()) {
                        ListUsers itemData = new ListUsers();
                        itemData.setId(data.getId());
                        itemData.setImageUrl(data.getAvatarUrl());
                        itemData.setLoginID(data.getLogin());
                        itemData.setSiteAdmin(data.isSiteAdmin());
                        itemDataList.add(itemData);
                    }
                }
                userBasicList.postValue(itemDataList);
                uiState.setValue(UiState.SUCCESS);
            }

            @Override
            public void onFailure(Call<List<ListUsersResponse>> call, Throwable throwable) {
                uiState.setValue(UiState.ERROR);
            }
        });
    }
}
