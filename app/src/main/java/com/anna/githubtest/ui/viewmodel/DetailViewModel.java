package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.anna.githubtest.core.api.GitHubService;
import com.anna.githubtest.core.api.GithubCline;
import com.anna.githubtest.core.model.GetUserDetailResponse;
import com.anna.githubtest.core.model.PublicReposResponse;
import com.anna.githubtest.data.DetailInfo;
import com.anna.githubtest.data.PublicRepos;
import com.anna.githubtest.ui.UiState;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
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
        apiService.fetchUsersDetail(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<GetUserDetailResponse>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        handleUiState(UiState.Loading.getInstance());
                    }

                    @Override
                    public void onNext(@NonNull Response<GetUserDetailResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            setupDetailInfoViewData(response.body());
                        } else {
                            handelApiErrorMsg(response.code(), response.errorBody());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handelExceptionErrorMsg(new UiState.Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        handleUiState(UiState.Success.getInstance());
                    }
                });
    }

    /**
     * Api PublicRepos
     *
     * @param name login
     */
    public void callApiPublicRepos(String name) {
        apiService.fetchUserRepos(name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<PublicReposResponse>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        handleUiState(UiState.Loading.getInstance());
                    }

                    @Override
                    public void onNext(@NonNull Response<List<PublicReposResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            setupPublicReposViewDta(response.body());
                        } else {
                            handelApiErrorMsg(response.code(), response.errorBody());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handelExceptionErrorMsg(new UiState.Error(e.getMessage()));
                    }

                    @Override
                    public void onComplete() {
                        handleUiState(UiState.Success.getInstance());
                    }
                });
    }

    private void setupPublicReposViewDta(List<PublicReposResponse> response) {
        List<PublicRepos> list = new ArrayList<>();
        for (PublicReposResponse data : response) {
            PublicRepos repos = new PublicRepos();
            repos.setName(data.getName());
            repos.setDescription(data.getDescription());
            repos.setVisibility(data.getVisibility());
            list.add(repos);
        }
        publicRepos.setValue(list);
    }

    private void setupDetailInfoViewData(GetUserDetailResponse response) {
        DetailInfo info = new DetailInfo();
        info.setImageUrl(response.getAvatarUrl());
        info.setLogin(response.getLogin());
        info.setName(response.getName());
        info.setFollowers(response.getFollowers());
        info.setFollowing(response.getFollowing());
        info.setLocation(response.getLocation());
        info.setCompany(response.getCompany());
        info.setBlog(response.getBlog());
        detailInfo.postValue(info);
    }
}
