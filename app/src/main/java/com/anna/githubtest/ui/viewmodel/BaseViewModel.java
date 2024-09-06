package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anna.githubtest.ui.UiState;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class BaseViewModel extends ViewModel {

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    protected final MutableLiveData<UiState> uiState = new MutableLiveData<>();

    protected void handleUiState(UiState state) {
        uiState.setValue(state);
    }

    protected void handelErrorMsg(int code, ResponseBody errorBody) {
        try {
            if (errorBody != null) {
                String errorJson = errorBody.string();
                JSONObject jsonObject = new JSONObject(errorJson);
                String message = jsonObject.getString("message");
                handleUiState(new UiState.Error(code + "\n" + message));
            }
        } catch (IOException | JSONException e) {
            handleUiState(new UiState.Error());
        }
    }
}
