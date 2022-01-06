package com.project_future_2021.marvelpedia.repositories;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import com.project_future_2021.marvelpedia.database.AsyncHowManyRecordsInDb;
import com.project_future_2021.marvelpedia.database.HeroDao;
import com.project_future_2021.marvelpedia.database.HeroRoomDatabase;
import com.project_future_2021.marvelpedia.network.HeroApi;
import com.project_future_2021.marvelpedia.singletons.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeroRepository {
    private static final String TAG = "HeroRepository";
    private static final String REQUEST_TAG = "HeroesFragmentRequest";

    private final HeroDao heroDao;
    private final HeroApi.TalkWithServer heroApi;
    // 'repo' for repository
    private final LiveData<List<Hero>> repoDbHeroes;
    private final MutableLiveData<List<Hero>> repoServerHeroes;
    private final List<Hero> simpleRepoServerHeroes = new ArrayList<>();
    private final MutableLiveData<Boolean> repoIsLoading;
    // repo counters for offset and limit in our Urls.
    int repoOffset = -1;
    int repoLimit = -1;


    public HeroRepository(Application application, String urlFromViewModel, int offsetFromViewmodel, int limitFromViewmodel) {
        Log.d(TAG, "HeroRepository: Initiating the Repository...");
        // init the Database.
        HeroRoomDatabase db = HeroRoomDatabase.getDatabase(application);

        repoIsLoading = new MutableLiveData<>();
        repoIsLoading.setValue(false);

        repoServerHeroes = new MutableLiveData<>();

        heroDao = db.heroDao();
        heroApi = initHeroApi();

        // check how many Heroes we already have in the db,
        // if we have more than the offset we got from the ViewModel,
        //      that means we already have data in the DB,
        //      and we should re-fetch all these Heroes first, so that the users have up-to-date results.
        // else
        //      this is the Users' first time using the App, bring new Heroes starting from 0.
        new AsyncHowManyRecordsInDb(db, new AsyncHowManyRecordsInDb.Listener() {
            @Override
            public void onResult(Integer numberOfHeroesAlreadyInDb) {
                repoOffset = numberOfHeroesAlreadyInDb;
                if (repoOffset < offsetFromViewmodel) {
                    heroApi.getHeroesFromServer(application, urlFromViewModel, REQUEST_TAG, offsetFromViewmodel, limitFromViewmodel);
                } else {
                    heroApi.getHeroesFromServer(application, urlFromViewModel, REQUEST_TAG, repoOffset, limitFromViewmodel);
                }
            }
        }).execute();

        // our LiveData repoDbHeroes will update every time a change happens in the Database.
        repoDbHeroes = heroDao.getAllHeroes();
    }

    public MutableLiveData<Boolean> getRepoIsLoading() {
        return repoIsLoading;
    }

    public MutableLiveData<List<Hero>> getRepoServerHeroes() {
        return repoServerHeroes;
    }

    public LiveData<List<Hero>> getRepoDbHeroes() {
        return repoDbHeroes;
    }

    // heroApi is interface with x-methods. We need to implement these methods here,
    // so the compiler knows what to do when we call "heroApi.getHeroesFromServer()" or w/e hero.x-method...
    @NonNull
    private HeroApi.TalkWithServer initHeroApi() {
        return new HeroApi.TalkWithServer() {
            @Override
            public List<Hero> getHeroesFromServer(Context context, String url, String requestTag, int offset, int limit) {

                return RepoGetHeroesFromServer(context, url, requestTag, offset, limit);
            }

            @Override
            public void test() {
                Log.d(TAG, "test: inside test");
            }
        };
    }


    public void RepoLoadMore(Context context, String newUrl, int offset, int limit) {
        // increase the counter for offset, to load the next batch of data.
        // [SOS] we HAVE to decrease it if the call fails. We do it inside the RepoGetHeroesFromServer()
        repoOffset += repoLimit;
        RepoGetHeroesFromServer(context, newUrl, REQUEST_TAG, repoOffset, limit);
        Log.d(TAG, "RepoLoadMore: new url is: " + newUrl);
    }

    //TODO should delete in later push.
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

    public List<Hero> RepoGetHeroesFromServer(Context context, String url, String requestTag, int offset, int limit) {
        repoIsLoading.setValue(true);

        Gson gson = new Gson();

        // if we have a limit!=0 we need to edit the url accordingly.
        if (limit != 0) {
            url = url + "&limit=" + limit;
            repoLimit = limit;
        }
        // same goes for offset.
        if (offset != 0) {
            url = url + "&offset=" + offset;
        }
        Log.d(TAG, "RepoGetHeroesFromServer: called with FINAL url: " + url);

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

                                temp_hero = new Hero(temp_hero_id, temp_hero_name, temp_hero_description, temp_hero_modified, temp_hero_thumbnail, temp_hero_resourceURI, temp_hero_comics, temp_hero_series, temp_hero_stories, temp_hero_events, temp_hero_urls/*, wasHeFavorite[0]*/);

                                // TODO: subject to change, will see.
                                // Do not re-add items already on the list.
                                if (simpleRepoServerHeroes.contains(temp_hero)) {
                                    repoIsLoading.setValue(false);
                                    Log.d(TAG, "onResponse: Did not fetch " + temp_hero_name + " again, no need.");
                                    continue;
                                }
                                simpleRepoServerHeroes.add(temp_hero);

                                Log.d(TAG, "onResponse: just fetched and added hero: " + temp_hero_name + " Favorite?: " + temp_hero.getFavorite());
                            }
                            repoServerHeroes.setValue(simpleRepoServerHeroes);

                            repoIsLoading.setValue(false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Server not responding!\n Try again later.", Toast.LENGTH_LONG).show();
                            // TODO should we add repoIsLoading.setValue(false); here too?
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

                        // [SOS]
                        // undo our previous action of increasing the counter by {limit amount},
                        // because we are offline and we did not fetch the next batch of Heroes.
                        // This is really important for the overall UX.
                        repoOffset -= limit;
                        Toast.makeText(context, "You are in offline mode \n" +
                                "check network or try again later.", Toast.LENGTH_LONG).show();
                        Log.e(TAG, "onErrorResponse: Something went wrong with the server call: " + error.getMessage());
                    }
                });
        //put an identifying TAG to the request queue so we can cancel it anytime.
        jsonObjectRequest.setTag(requestTag);

        // Add the request to the RequestQueue.
        VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

        return simpleRepoServerHeroes;
    }

    // We must call these Database-calls on a non-UI thread or our app will throw an exception. Room ensures
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
}