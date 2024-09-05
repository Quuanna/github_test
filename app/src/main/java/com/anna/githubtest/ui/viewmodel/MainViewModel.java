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

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


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
        apiService.fetchListUsers(100)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<ListUsersResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        uiState.setValue(UiState.LOADING);
                    }

                    @Override
                    public void onNext(@NonNull List<ListUsersResponse> listUsersResponses) {
                        List<ListUsers> itemDataList = new ArrayList<>();
                        for (ListUsersResponse data : listUsersResponses) {
                            ListUsers itemData = new ListUsers();
                            itemData.setId(data.getId());
                            itemData.setImageUrl(data.getAvatarUrl());
                            itemData.setLoginID(data.getLogin());
                            itemData.setSiteAdmin(data.isSiteAdmin());
                            itemDataList.add(itemData);
                        }
                        userBasicList.postValue(itemDataList);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        uiState.setValue(UiState.ERROR);
                    }

                    @Override
                    public void onComplete() {
                        uiState.setValue(UiState.SUCCESS);
                    }
                });
    }
}
