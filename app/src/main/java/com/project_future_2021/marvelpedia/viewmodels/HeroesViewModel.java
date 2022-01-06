package com.project_future_2021.marvelpedia.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.repositories.HeroRepository;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.List;

public class HeroesViewModel extends AndroidViewModel {
    //TODO have that in a constants class...
    private static final String TAG = "HeroesViewModel";

    private final HeroRepository heroRepository;
    // 'vm' for viewmodel
    private final MediatorLiveData<List<Hero>> vmAllHeroesCombined;
    private final MutableLiveData<List<Hero>> vmServerHeroes;
    private final LiveData<List<Hero>> vmDbHeroes;

    // limit and offset are both (optional) parameters in the url we use to access the Server.
    // limit also, logically, works as the starting offset (if 0 data in DB).
    private final int vmLimit = 6;
    private int vmOffset = 0;
    private final MutableLiveData<Boolean> vmIsLoading;
    private final String vmUrl;
    private final String request_type = "/v1/public/characters";

    public HeroesViewModel(@NonNull Application application) {
        super(application);
        Log.d(TAG, "HeroesViewModel: constructor");

        vmUrl = createUrlForApiCall(request_type);
        vmIsLoading = new MutableLiveData<>();

        heroRepository = new HeroRepository(application, vmUrl, vmOffset, vmLimit);

        vmAllHeroesCombined = new MediatorLiveData<>();

        vmDbHeroes = heroRepository.getRepoDbHeroes();
        vmServerHeroes = heroRepository.getRepoServerHeroes();

        // when new heroes are fetched from the Server, only add them to the DB,
        // [SOS] We will only be observing the DB for changes.
        vmAllHeroesCombined.addSource(vmServerHeroes, newHeroListFromServer -> {
            Log.d(TAG, "HeroesViewModel: newHeroListFromServer");
            insertManyHeroes(newHeroListFromServer);
        });

        // since we are observing the DB,
        // if we get new Heroes from the Server ->
        //      we add them to the DB ->
        //          we show a refreshed list with Heroes from the DB.
        vmAllHeroesCombined.addSource(vmDbHeroes, newHeroListFromDb -> {
            Log.d(TAG, "HeroesViewModel: newHeroListFromDb");
            vmAllHeroesCombined.postValue(newHeroListFromDb);
        });

        // Also, observe the isLoading LiveData variable that is in the Repository.
        // It is true while new Heroes are being fetched, false otherwise.
        vmAllHeroesCombined.addSource(heroRepository.getRepoIsLoading(), newState -> {
            if (newState) {
                Log.d(TAG, ".....LOADING.....: ");
                vmIsLoading.postValue(true);
            } else {
                Log.d(TAG, ".....DONE LOADING.....: ");
                vmIsLoading.postValue(false);
            }
        });
    }

    public LiveData<List<Hero>> getVmServerHeroes() {
        return vmServerHeroes;
    }

    public LiveData<List<Hero>> getVmDbHeroes() {
        return vmDbHeroes;
    }

    public LiveData<Boolean> getVmIsLoading() {
        return vmIsLoading;
    }

    public LiveData<List<Hero>> getVmAllHeroesCombined() {
        return vmAllHeroesCombined;
    }

    public void insertHero(Hero hero) {
        heroRepository.insert(hero);
    }

    public void insertManyHeroes(List<Hero> heroes) {
        heroRepository.insertManyHeroes(heroes);
    }

    public void deleteAllHeroes() {
        heroRepository.deleteAllHeroes();
    }

    public String createUrlForApiCall(String request_type) {
        String timestamp_now = getNow();
        String BASE_URL = getApplication().getString(R.string.base_url);
        String PRIVATE_API_KEY = getApplication().getString(R.string.private_API_key);
        String PUBLIC_API_KEY = getApplication().getString(R.string.public_API_key);

        String hashInput = timestamp_now + PRIVATE_API_KEY + PUBLIC_API_KEY;
        String hashResult = generateMD5HashFromString(hashInput);

        String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult;

        Log.d(TAG, "createUrlForApiCall: Url is: " + final_url);
        return final_url;
    }

    public void loadMore() {
        // We want the next batch of data, so increase the offset.
        vmOffset += vmLimit;

        String newUrl = createUrlForApiCall(request_type);
        heroRepository.RepoLoadMore(getApplication().getBaseContext(), newUrl, vmOffset, vmLimit);
        Log.d(TAG, "loadMore: new Url is: " + newUrl);
    }

    private String getNow() {
        long datetime = System.currentTimeMillis();
        Timestamp timestamp = new Timestamp(datetime);
        return timestamp.toString();
    }

    // Google our friend and savior :)
    private String generateMD5HashFromString(String inputString) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md5 != null) {
            md5.update(StandardCharsets.UTF_8.encode(inputString));
            return String.format("%032x", new BigInteger(1, md5.digest()));
        } else {
            return "";
        }
    }
}