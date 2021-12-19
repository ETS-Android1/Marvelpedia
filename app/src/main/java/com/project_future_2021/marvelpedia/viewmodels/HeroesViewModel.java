package com.project_future_2021.marvelpedia.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HeroesViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public HeroesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is heroes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}