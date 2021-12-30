<LinearLayout xmls:android=="http://schemas.android.com/apk/res/android"
        xmls:tools=="http://schemas.android.com/tools"
        android:layout_width=="match_parent"
        android:layout_height=="match_parent"
        tools:context==".UserProfile"
        android:orientation=="vertical">

        <RelativeLayout
             android:layout_width=="match_parent"
             android:layout_width=="300dp"
             android:layout_width=="20dp">
</LinearLayout>

package com.project_future_2021.marvelpedia.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private final MutableLiveData<String> mText;

    public ProfileViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is profile fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}