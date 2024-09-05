package com.anna.githubtest.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.anna.githubtest.element.UiState;
import com.anna.githubtest.util.SingleLiveEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;

public class BaseViewModel extends ViewModel {

    public LiveData<UiState> getUiState() {
        return uiState;
    }

    public LiveData<String> getApiErrorMsg() {
        return apiErrorMsg;
    }

    protected final MutableLiveData<UiState> uiState = new MutableLiveData<>();
    protected final SingleLiveEvent<String> apiErrorMsg = new SingleLiveEvent<>();

    protected void handelErrorMsg(int code, ResponseBody errorBody) {
        try {
            if (errorBody != null) {
                String errorJson = errorBody.string();
                JSONObject jsonObject = new JSONObject(errorJson);
                String message = jsonObject.getString("message");
                apiErrorMsg.setValue(code + "\n" + message);
            }
        } catch (IOException | JSONException e) {
            uiState.setValue(UiState.ERROR);
        }
    }
}
