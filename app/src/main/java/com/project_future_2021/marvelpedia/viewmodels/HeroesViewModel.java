package com.project_future_2021.marvelpedia.viewmodels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project_future_2021.marvelpedia.R;
import com.project_future_2021.marvelpedia.data.Comics;
import com.project_future_2021.marvelpedia.data.Events;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.data.Image;
import com.project_future_2021.marvelpedia.data.Items;
import com.project_future_2021.marvelpedia.data.Series;
import com.project_future_2021.marvelpedia.data.Stories;
import com.project_future_2021.marvelpedia.data.Url;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class HeroesViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel

    private static final String TAG = "HeroesViewModel";
    //private final Repository repository;

    private final List<Hero> heroesList = new ArrayList<>();
    private final MutableLiveData<List<Hero>> liveDataHeroesList;
    //public String temp_image_link;

    public HeroesViewModel(@NonNull Application application /*,Repository repository*/) {
        super(application);
        Log.d(TAG, "HeroesViewModel: constructor");

        liveDataHeroesList = new MutableLiveData<>();
    }

    public MutableLiveData<List<Hero>> getLiveDataHeroesList() {
        return liveDataHeroesList;
    }

    public void getHeroesFromServer(String url, String requestTag) {
        Gson gson = new Gson();

        // TODO:
        // not really a fan of this, temporary solutions,
        // because each call re-adds the same items on our list
        // for example, if the user swaps fragments...
        heroesList.clear();
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray results = null;
                        int resultSize = 0;
                        try {
                            results = response.getJSONObject("data").getJSONArray("results");
                            resultSize = response.getJSONObject("data").getInt("count");
                            Image nImage = gson.fromJson(String.valueOf(results.getJSONObject(0).getJSONObject("thumbnail")), Image.class);
                            Items nItems = gson.fromJson(String.valueOf(results.getJSONObject(0).getJSONObject("comics").getJSONArray("items").getJSONObject(0)), Items.class);
                            Comics nComics = gson.fromJson(String.valueOf(results.getJSONObject(0).getJSONObject("comics")), Comics.class);
                            Comics nnComics = gson.fromJson(String.valueOf(results.getJSONObject(0).getJSONObject("comics")), Comics.class);
                            System.out.println();

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
                            List<Url> temp_hero_urls = null;

                            for (int i = 0; i < resultSize; i++) {
                                /*heroesList.add(results.getJSONObject(i).getString("name"));
                                heroesList.add("\n");*/

                                temp_hero_id = results.getJSONObject(i).getInt("id");
                                temp_hero_name = results.getJSONObject(i).getString("name");
                                temp_hero_description = results.getJSONObject(i).getString("description");
                                temp_hero_modified = results.getJSONObject(i).getString("modified");

                                temp_hero_thumbnail = gson.fromJson(results.getJSONObject(i).getString("thumbnail"), Image.class);

                                temp_hero_resourceURI = results.getJSONObject(i).getString("resourceURI");

                                temp_hero_comics = gson.fromJson(results.getJSONObject(i).getString("comics"), Comics.class);
                                temp_hero_series = gson.fromJson(results.getJSONObject(i).getString("series"), Series.class);
                                temp_hero_stories = gson.fromJson(results.getJSONObject(i).getString("stories"), Stories.class);
                                temp_hero_events = gson.fromJson(results.getJSONObject(i).getString("events"), Events.class);
                                //temp_hero_urls = gson.fromJson(results.getJSONObject(i).getString("urls"), Url.class);

                                temp_hero = new Hero(temp_hero_id, temp_hero_name, temp_hero_description, temp_hero_modified, temp_hero_thumbnail, temp_hero_resourceURI, temp_hero_comics, temp_hero_series, temp_hero_stories, temp_hero_events, null);
                                Log.d(TAG, "onResponse: just fetched hero: " + temp_hero);
                                heroesList.add(temp_hero);
                            }
                            liveDataHeroesList.setValue(heroesList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: I arrived!");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                        Toast.makeText(getApplication().getBaseContext(), "Something went wrong " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        //put an identifying TAG to the request queue so we can cancel it anytime.
        jsonObjectRequest.setTag(requestTag);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(getApplication().getBaseContext()).addToRequestQueue(jsonObjectRequest);
    }

    public String createUrlforApiCall(String request_type) {
        String timestamp_now = getNow();
        String BASE_URL = getApplication().getString(R.string.base_url);
        String PRIVATE_API_KEY = getApplication().getString(R.string.private_API_key);
        String PUBLIC_API_KEY = getApplication().getString(R.string.public_API_key);

        String hashInput = timestamp_now + PRIVATE_API_KEY + PUBLIC_API_KEY;
        String hashResult = generateMD5HashFromString(hashInput);

        String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult;
        //String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult + "&limit=100";
        //String final_url = "https://gateway.marvel.com/v1/public/characters?ts=2021-12-20 22:47:01.833&apikey=da311cab234d0c1a1134f345bc99fa2a&hash=518eb8a9d697be91f2ec15911024d4a0&limit=2&offset=20";

        Log.i("api", final_url);
        return final_url;
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
        Log.d(TAG, "onCleared: ");
    }
}