package com.project_future_2021.marvelpedia.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginViewModel extends ViewModel {
    // TODO: Implement the ViewModel


    @Override
    protected void onCleared() {
        super.onCleared();
    }

    private final MutableLiveData<String> mText;

    public LoginViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the login fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}