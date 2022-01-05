package com.project_future_2021.marvelpedia.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.project_future_2021.marvelpedia.data.Comics;
import com.project_future_2021.marvelpedia.data.Events;
import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.data.Image;
import com.project_future_2021.marvelpedia.data.Series;
import com.project_future_2021.marvelpedia.data.Stories;
import com.project_future_2021.marvelpedia.data.Url;
import com.project_future_2021.marvelpedia.database.HeroDao;
import com.project_future_2021.marvelpedia.database.HeroRoomDatabase;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeroRepository {
    private static final String TAG = "HeroRepository";
    private HeroDao heroDao;
    //private LiveData<List<Hero>> allHeroes;
    private MediatorLiveData<List<Hero>> allHeroes;
    private LiveData<List<Hero>> allDbHeroes;

    private List<Hero> simpleServerHeroesList = new ArrayList<>();
    private MutableLiveData<List<Hero>> allServerHeroes;

    //private MutableLiveData<List<Hero>> allRepoFavorites;

    /*LiveData  liveData1 = ...;
    LiveData  liveData2 = ...;

    MediatorLiveData  liveDataMerger = new MediatorLiveData<>();
   liveDataMerger.addSource(liveData1, value -> liveDataMerger.setValue(value));
   liveDataMerger.addSource(liveData2, value -> liveDataMerger.setValue(value));*/

    //private List<Hero> simpleCompleteDbAndServerList = new ArrayList<>();


    public MediatorLiveData<List<Hero>> getAllHeroes() {
        return allHeroes;
    }

    // Note that in order to unit test the HeroRepository, we have to remove the Application
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public HeroRepository(Application application) {
        HeroRoomDatabase db = HeroRoomDatabase.getDatabase(application);
        heroDao = db.heroDao();
        //allHeroes = heroDao.getAllHeroes();

        allHeroes = new MediatorLiveData<>();

        allDbHeroes = heroDao.getAllHeroes();

        allServerHeroes = new MutableLiveData<>();

        allHeroes.addSource(allDbHeroes, value -> allHeroes.setValue(value));
        allHeroes.addSource(allServerHeroes, value -> allHeroes.setValue(value));
        //allHeroes = new MediatorLiveData<>();
        //allHeroes = getAllHeroes(application.getBaseContext(), )
        //allHeroes.setValue(heroDao.getAllHeroes().getValue());

        //allRepoFavorites = new MutableLiveData<>();

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public MediatorLiveData<List<Hero>> getAllHeroes(Context context, String Url, String REQUEST_TAG) {
        getHeroesFromDb();
        //allHeroes = heroDao.getAllHeroes();
        getHeroesFromServer(context, Url, REQUEST_TAG, 5, 5);

        return allHeroes;
    }

    /*public LiveData<List<Hero>> getDbFavoriteHeroes() {
        Log.d(TAG, "getDbFavoriteHeroes: Start");
        ArrayList<Hero> simpleFavorites = new ArrayList<>();
        Transformations.map(allHeroes, input -> {
            for (int i = 0; i < input.size(); i++) {
                Hero temp_hero = input.get(i);
                if (temp_hero.getFavorite()) {
                    simpleFavorites.add(temp_hero);
                    Log.d(TAG, "getDbFavoriteHeroes: Added hero to favorites: " + temp_hero.getName());
                }
            }
            allRepoFavorites.postValue(simpleFavorites);
            return allRepoFavorites;
        });
        Log.d(TAG, "getDbFavoriteHeroes: End");
        return allRepoFavorites;
    }*/

    private void getHeroesFromDb() {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.getAllHeroes();
        });
    }

    // returns a value so it's a Callable<Integer>
    /*final Future<Integer> submit2 = es.submit(() -> counter++);
    System.out.println(submit2.get());*/

    //https://stackoverflow.com/questions/38540481/return-value-by-lambda-in-java
    /*private Hero getHeroFromDWithId(int id) {
        final Future<?> submit2 =
                HeroRoomDatabase.databaseWriteExecutor.submit(() -> {
                    heroDao.getHeroWithId(id);
                });
        //Log.d(TAG, "getHeroFromDWithId: " + result[0]);
        try {
            Log.d(TAG, "getHeroFromDWithId: submit2 is : " + submit2.get());
            return (Hero) submit2.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getHeroFromDWithId: sad");
        return new Hero();
    }*/

    /*public LiveData<List<Hero>> getAllServerHeroes() {
        return liveDataServerHeroesList;
    }*/

    public void getHeroesFromServer(Context context, String url, String requestTag, int offset, int limit) {
        //isLoading.setValue(true);
        //JsonObjectRequest jsonObjectRequest;
        Gson gson = new Gson();

        /*if (Objects.equals(getLiveDataHeroesList().getValue(), heroesList)){
            Log.d(TAG, "getHeroesFromServer: hi there, been here before.");
            isLoading.setValue(false);
            return;
        }*/

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
                            //mOffset = offset;
                            //mLimit = limit;

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
                                /*if (temp_hero_thumbnail.getPath().endsWith("image_not_available")) {
                                    temp_hero_thumbnail.setPath("");
                                }*/

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

                                /*Hero dbHeroToBeReplaced = */
                                //getHeroFromDWithId(temp_hero_id);
                                /*new AsyncGetHeroFromDb(database, new AsyncGetHeroFromDb.Listener() {
                                    @Override
                                    public void onResult(Hero resultHero) {
                                        if (resultHero.getFavorite())
                                        temp_hero.setFavorite(true);
                                        heroesList.add(temp_hero);
                                    }
                                }).execute(temp_hero_id);*/

                                temp_hero = new Hero(temp_hero_id, temp_hero_name, temp_hero_description, temp_hero_modified, temp_hero_thumbnail, temp_hero_resourceURI, temp_hero_comics, temp_hero_series, temp_hero_stories, temp_hero_events, temp_hero_urls/*, wasHeFavorite[0]*/);

                                // TODO: subject to change, will see.
                                // Do not re-add items already on the list.
                                if (simpleServerHeroesList.contains(temp_hero)) {
                                    //isLoading.setValue(false);
                                    Log.d(TAG, "onResponse: Did not fetch " + temp_hero_name + " again, no need.");
                                    //return;
                                    continue;
                                }
                                simpleServerHeroesList.add(temp_hero);

                                Log.d(TAG, "onResponse: just fetched and added hero: " + temp_hero_name + " Favorite?: " + temp_hero.getFavorite());
                            }
                            allServerHeroes.setValue(simpleServerHeroesList);
                            //allHeroes.postValue(simpleServerHeroesList);

                            // TODO: SOS, check this again
                            // save the heroes to DB too
                            //insertManyHeroes(simpleServerHeroesList);
                            //isLoading.setValue(false);
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
                        //Toast.makeText(getApplication().getBaseContext(), "Something went wrong " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onErrorResponse: Something went wrong with the server call: " + error.getMessage());
                    }
                });
        //put an identifying TAG to the request queue so we can cancel it anytime.
        jsonObjectRequest.setTag(requestTag);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        //return jsonObjectRequest;
    }

    // We must call this on a non-UI thread or our app will throw an exception. Room ensures
    // that we're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Hero hero) {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.insertHero(hero);
        });
    }

    public void insertManyHeroes(List<Hero> heroes) {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.insertHeroes(heroes);
        });
    }

    public void deleteAllHeroes() {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.deleteAllHeroes();
        });
    }

    /*public MutableLiveData<List<Hero>> getAllFavoriteHeroes() {
        List<Hero> temp = allDbHeroes.getValue();
        ArrayList<Hero> temp2 = new ArrayList<>();
        MutableLiveData<List<Hero>> temp_fav_list = new MutableLiveData<>();
        if (temp != null) {
            for (Hero hero : temp) {
                if (hero.getFavorite()) {
                    temp2.add(hero);
                    Log.d(TAG, "getAllFavoriteHeroes: Hero " + hero.getName() + " was favorite from DB");
                }
            }
            temp_fav_list.setValue(temp2);
            return temp_fav_list;
        }
        return null;
    }*/
}