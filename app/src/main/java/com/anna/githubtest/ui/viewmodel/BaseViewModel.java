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

    public LiveData<UiState> getUiState() {return uiState;}
    protected boolean isSingleEvent = false;
    protected final MutableLiveData<UiState> uiState = new MutableLiveData<>();

    /**
     * Handle Loading、Success
     */
    protected void handleUiState(UiState state) {
        uiState.setValue(state);
    }

    /**
     * Handle Exception SingleEvent
     */
    protected void handelExceptionErrorMsg(UiState.Error error) {
        if (isSingleEvent) {return;}
        uiState.setValue(error);
        isSingleEvent = true;
    }


    /**
     * Handle ApiError SingleEvent
     */
    protected void handelApiErrorMsg(int code, ResponseBody errorBody) {
        if (isSingleEvent) {return;}
        try {
            if (errorBody != null) {
                String errorJson = errorBody.string();
                JSONObject jsonObject = new JSONObject(errorJson);
                String message = jsonObject.getString("message");
                uiState.setValue(new UiState.Error(code + "\n" + message));
                isSingleEvent = true;
            }
        } catch (IOException | JSONException e) {
            handelExceptionErrorMsg(new UiState.Error(e.getMessage()));
        }
    }
}
