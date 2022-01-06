package com.project_future_2021.marvelpedia.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class SearchViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    public static final String TAG = "SearchViewModel";
    private static final String REQUEST_TAG = "SearchFragmentRequest";
    private final MutableLiveData<String> mText;


    public SearchViewModel(@NonNull Application application) {
        super(application);
        mText = new MutableLiveData<>();
        mText.setValue("This is search fragment");

    }

    public LiveData<String> getText() {
        return mText;
    }


}