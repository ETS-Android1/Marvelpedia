package com.project_future_2021.marvelpedia.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
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
    // TODO: Implement the ViewModel

    private static final String TAG = "HeroesViewModel";

    //TODO have that in a constants class...
    private static final String REQUEST_TAG = "HeroesFragmentRequest";
    private final HeroRepository clHeroRepository;
    private /*final*/ LiveData<List<Hero>> clAllHeroes;
    //private LiveData<List<Hero>> favoriteHeroesList;

    private String mFinalUrl;
    private String request_type = "/v1/public/characters";


    public int mOffset = 0;
    // TODO: should change where its value changes, probably...
    public MutableLiveData<Boolean> isLoading;
    // limit AND starting offset..
    private int mLimit = 30;

    public HeroesViewModel(@NonNull Application application) {
        super(application);

        mFinalUrl = createUrlForApiCall(request_type);

        isLoading = new MutableLiveData<>();

        clHeroRepository = new HeroRepository(application);
        clAllHeroes = clHeroRepository.getAllHeroes(application.getBaseContext(), mFinalUrl, request_type);

        //favoriteHeroesList = clHeroRepository.getDbFavoriteHeroes();

        //favorites = clHeroRepository.getAllFavoriteHeroes();
        //database = Room.databaseBuilder(application, HeroRoomDatabase.class, "Marvelpedia").build();
        Log.d(TAG, "HeroesViewModel: constructor");
    }

    public LiveData<List<Hero>> getClAllHeroes() {
        return clAllHeroes;
    }

    public void clInsert(Hero hero) {
        clHeroRepository.insert(hero);
    }

    public void clInsertManyHeroes(List<Hero> heroes) {
        clHeroRepository.insertManyHeroes(heroes);
    }

    /*public LiveData clgetAllFavoriteHeroes() {
        return favorites;
    }*/

    public void clDeleteAllHeroes() {
        clHeroRepository.deleteAllHeroes();
    }


    //TODO actually bad, try moving it completely to repo
    /*public void getHeroesFromServer(String url, String requestTag) {
        isLoading.setValue(true);
        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest = clHeroRepository.getHeroesFromServer(url, requestTag, mOffset, mLimit);
        /*Gson gson = new Gson();

//        if (Objects.equals(getLiveDataHeroesList().getValue(), heroesList)){
//            Log.d(TAG, "getHeroesFromServer: hi there, been here before.");
//            isLoading.setValue(false);
//            return;
//        }

        //getHeroesFromDb();

        // TODO:
        // not really a fan of this, temporary solutions,
        // because each call re-adds the same items on our list
        // for example, if the user swaps fragments...
        //heroesList.clear();
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = null;
                        int resultSize = 0;
                        int offset = 0;
                        int limit = 100;
                        try {
                            results = response.getJSONObject("data").getJSONArray("results");
                            resultSize = response.getJSONObject("data").getInt("count");
                            offset = response.getJSONObject("data").getInt("offset");
                            limit = response.getJSONObject("data").getInt("limit");
                            mOffset = offset;
                            mLimit = limit;

                            Hero temp_hero;
                            Image temp_hero_thumbnail;
                            int temp_hero_id;
                            String temp_hero_name;
                            String temp_hero_description;
                            String temp_hero_modified;
                            String temp_hero_resourceURI;

                            Comics temp_hero_comics;
                            Series temp_hero_series;
                            Stories temp_hero_stories;
                            Events temp_hero_events;
                            JSONArray urls;
                            List<Url> temp_hero_urls = new ArrayList<>();

                            for (int i = 0; i < resultSize; i++) {
                                temp_hero_id = results.getJSONObject(i).getInt("id");
                                temp_hero_name = results.getJSONObject(i).getString("name");

                                temp_hero_description = results.getJSONObject(i).getString("description");
                                if (temp_hero_description.isEmpty()) {
                                    temp_hero_description = "This hero has no description yet. \nWe will add one later :)";
                                }

                                temp_hero_modified = results.getJSONObject(i).getString("modified");

                                temp_hero_thumbnail = gson.fromJson(results.getJSONObject(i).getString("thumbnail"), Image.class);
//                                if (temp_hero_thumbnail.getPath().endsWith("image_not_available")) {
//                                    temp_hero_thumbnail.setPath("");
//                                }

                                temp_hero_resourceURI = results.getJSONObject(i).getString("resourceURI");
                                temp_hero_comics = gson.fromJson(results.getJSONObject(i).getString("comics"), Comics.class);
                                temp_hero_series = gson.fromJson(results.getJSONObject(i).getString("series"), Series.class);
                                temp_hero_stories = gson.fromJson(results.getJSONObject(i).getString("stories"), Stories.class);
                                temp_hero_events = gson.fromJson(results.getJSONObject(i).getString("events"), Events.class);

                                urls = results.getJSONObject(i).getJSONArray("urls");
                                for (int j = 0; j < urls.length(); j++) {
                                    Url temp_url = gson.fromJson(urls.getString(j), Url.class);
                                    temp_hero_urls.add(temp_url);
                                }

                                temp_hero = new Hero(temp_hero_id, temp_hero_name, temp_hero_description, temp_hero_modified, temp_hero_thumbnail, temp_hero_resourceURI, temp_hero_comics, temp_hero_series, temp_hero_stories, temp_hero_events, temp_hero_urls//, wasHeFavorite[0]);

                                // TODO: subject to change, will see.
                                // Do not re-add items already on the list.
                                if (heroesList.contains(temp_hero)) {
                                    isLoading.setValue(false);
                                    Log.d(TAG, "onResponse: Did not fetch heroes again, no need.");
                                    return;
                                }
                                heroesList.add(temp_hero);

                                Log.d(TAG, "onResponse: just fetched and added hero: " + temp_hero_name + " Favorite?: " + temp_hero.getFavorite());
                            }
                            liveDataHeroesList.postValue(heroesList);
                            isLoading.setValue(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: Something went wrong: " + e.getMessage());
                        }
                        Log.d(TAG, "onResponse: I arrived!");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        // TODO: comment or uncomment next line,
                        //  we should decide if the user should or should not see the progress bar constantly loading.
                        //isLoading.setValue(false);
                        Toast.makeText(getApplication().getBaseContext(), "Something went wrong " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });*//*



        //put an identifying TAG to the request queue so we can cancel it anytime.
        jsonObjectRequest.setTag(requestTag);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplication().getBaseContext()).addToRequestQueue(jsonObjectRequest);
        isLoading.setValue(false);
    }*/

    public String createUrlForApiCall(String request_type) {
        String timestamp_now = getNow();
        String BASE_URL = getApplication().getString(R.string.base_url);
        String PRIVATE_API_KEY = getApplication().getString(R.string.private_API_key);
        String PUBLIC_API_KEY = getApplication().getString(R.string.public_API_key);

        String hashInput = timestamp_now + PRIVATE_API_KEY + PUBLIC_API_KEY;
        String hashResult = generateMD5HashFromString(hashInput);

        String final_url;

        //String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult;

        if (mOffset != 0) {
            final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult + "&limit=" + mLimit + "&offset=" + mOffset;
        } else {
            final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult + "&limit=" + mLimit;
        }
        //String final_url = "https://gateway.marvel.com/v1/public/characters?ts=2021-12-20 22:47:01.833&apikey=da311cab234d0c1a1134f345bc99fa2a&hash=518eb8a9d697be91f2ec15911024d4a0&limit=2&offset=20";

        Log.d(TAG, "createUrlforApiCall: final url is: " + final_url);
        return final_url;
    }

    public void loadMore(/*String request_type*/) {
        mOffset += mLimit;
        String newUrl = createUrlForApiCall(request_type);
        clHeroRepository.getHeroesFromServer(getApplication().getBaseContext(), mFinalUrl, REQUEST_TAG, mOffset, mLimit);
        //getHeroesFromServer(newUrl, REQUEST_TAG);
        Log.d(TAG, "loadMore: ");
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

    @Override
    protected void onCleared() {
        super.onCleared();
        //Log.d(TAG, "onCleared: Will clear this list now: " + heroesList.toString());
        //heroesList.clear();
    }
}