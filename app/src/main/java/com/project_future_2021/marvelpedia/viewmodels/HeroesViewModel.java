package com.project_future_2021.marvelpedia.viewmodels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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

public class HeroesViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    private static final String TAG = "HeroesViewModel";
    //private final Repository repository;
    private final MutableLiveData<String> mText;

    private List<String> heroesList = new ArrayList<>();
    private final MutableLiveData<List<String>> mList;

    public HeroesViewModel(/*Repository repository*/) {
        Log.d(TAG, "HeroesViewModel: constructor");
        //this.repository = repository;
        mText = new MutableLiveData<>();
        mText.setValue("This is heroes fragment");

        //repository = new Repository();
        mList = new MutableLiveData<>();
        //mList.setValue("Test initial value");
    }

    /*public void fetchHeroes(Context context, String url, String requestTag) {
        Log.d(TAG, "fetchHeroes: viewModel context is:" + context.toString());
        //answer.setValue(repository.getHeroes(context, url, requestTag));
        mList.setValue(getHeroes(context, url, requestTag));
    }*/

    /*public void exampleInsert(Object myModel) {
        repository.insert(myModel);
    }*/

    public LiveData<String> getText() {
        return mText;
    }

    public MutableLiveData<List<String>> getmList() {
        return mList;
    }

    public String createUrlforApiCall(String BASE_URL, String request_type, String PRIVATE_API_KEY, String PUBLIC_API_KEY) {
        String timestamp_now = getNow();

        String hashInput = timestamp_now + PRIVATE_API_KEY + PUBLIC_API_KEY;
        String hashResult = generateMD5HashFromString(hashInput);

        String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult;
        //String final_url = BASE_URL + request_type + "?ts=" + timestamp_now + "&apikey=" + PUBLIC_API_KEY + "&hash=" + hashResult + "&limit=22";
        //String final_url = "https://gateway.marvel.com/v1/public/characters?ts=2021-12-20 22:47:01.833&apikey=da311cab234d0c1a1134f345bc99fa2a&hash=518eb8a9d697be91f2ec15911024d4a0&limit=2&offset=20";
        Log.i("api", timestamp_now + PRIVATE_API_KEY + PUBLIC_API_KEY);
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

    public void getHeroes(Context context, String url, String requestTag) {
        Log.d(TAG, "repShow: repository context is: " + context.toString());

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
                            resultSize = Integer.parseInt(response.getJSONObject("data").getString("count"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            results.getJSONObject(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            for (int i = 0; i < resultSize; i++) {
                                heroesList.add(results.getJSONObject(i).getString("name"));
                                heroesList.add("\n");
                            }
                            mList.setValue(heroesList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d(TAG, "onResponse: I arrived!");
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        heroesList.add("Something went wrong");
                        heroesList.add(error.getMessage());
                    }
                });
        jsonObjectRequest.setTag(requestTag);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}