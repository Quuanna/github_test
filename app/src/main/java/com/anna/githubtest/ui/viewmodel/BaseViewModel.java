package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anna.githubtest.element.UiState;

public class BaseViewModel extends ViewModel {

    public LiveData<UiState> getUiState() {return uiState;}
    protected final MutableLiveData<UiState> uiState = new MutableLiveData<>();
}
