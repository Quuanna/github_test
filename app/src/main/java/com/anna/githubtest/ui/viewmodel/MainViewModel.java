package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.viewmodel.ViewModelInitializer;

import com.anna.githubtest.core.api.GitHubService;
import com.anna.githubtest.core.api.GithubCline;
import com.anna.githubtest.core.model.ListUsersResponse;
import com.anna.githubtest.data.ListUsers;
import com.anna.githubtest.ui.UiState;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Headers;
import retrofit2.Response;


public class MainViewModel extends BaseViewModel {

    private final GitHubService apiService;
    private Observable<String> nextPageDeferObservable;

    public MainViewModel(GitHubService apiService) {
        this.apiService = apiService;
    }

    public static final ViewModelInitializer<MainViewModel> initializer = new ViewModelInitializer<>(
            MainViewModel.class, creationExtras -> new MainViewModel(GithubCline.getInstance())
    );

    public LiveData<List<ListUsers>> getUserList() {
        return userBasicList;
    }

    private final MutableLiveData<List<ListUsers>> userBasicList = new MutableLiveData<>();
    private Boolean isApiSpeedLimit = false;

    /**
     * Api ListUsers
     */
    public void callApiGetListUsers() {
        apiService.fetchListUsers(20, 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listUserObserver());
    }

    /**
     * Api ListUsers Next Page
     */
    public void callApiNextPageGetListUsers() {
        if (isApiSpeedLimit) {
            return;
        }
        Observable<Response<List<ListUsersResponse>>> observable =
                nextPageDeferObservable.flatMap(url ->
                        apiService.fetchListUserNextPage(url)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread()));
        observable.subscribe(listUserObserver());
    }

    /**
     * Response
     */
    private Observer<Response<List<ListUsersResponse>>> listUserObserver() {
        return new Observer<Response<List<ListUsersResponse>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                handleUiState(UiState.Loading.getInstance());
            }

            @Override
            public void onNext(@NonNull Response<List<ListUsersResponse>> response) {
                setupAwaitNextUrlObservable(response.headers());
                if (response.isSuccessful() && response.body() != null) {
                    setupItemData(response.body());
                } else {
                    if (response.code() == 403) {
                        isApiSpeedLimit = true;
                    }
                    handelErrorMsg(response.code(), response.errorBody());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                handleUiState(new UiState.Error(e.getMessage()));
            }

            @Override
            public void onComplete() {
                handleUiState(UiState.Success.getInstance());
            }
        };
    }

    private void setupAwaitNextUrlObservable(Headers headers) {
        nextPageDeferObservable = Observable.defer(() -> Observable.create(emitter -> {
            emitter.onNext(requestDynamicUrl(headers));
            emitter.onComplete();
        }));
    }

    private String requestDynamicUrl(Headers headers) {
        Pattern pattern = Pattern.compile("(?<=<)(\\S*)(?=>; rel=\"next\")",
                Pattern.CASE_INSENSITIVE);

        String nextPageUrl = "";
        String url = headers.get("link");
        if (url != null) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                nextPageUrl = matcher.group(1);
            }
        }
        return nextPageUrl;
    }

    private void setupItemData(List<ListUsersResponse> response) {
        List<ListUsers> itemDataList = new ArrayList<>();
        for (ListUsersResponse data : response) {
            ListUsers itemData = new ListUsers();
            itemData.setId(data.getId());
            itemData.setImageUrl(data.getAvatarUrl());
            itemData.setLoginID(data.getLogin());
            itemData.setSiteAdmin(data.isSiteAdmin());
            itemDataList.add(itemData);
        }
        userBasicList.setValue(itemDataList);
    }
}
