package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

public class AsyncGetAllHeroesFromDb extends AsyncTask<Void, Void, LiveData<List<Hero>>> {

    private final HeroRoomDatabase heroRoomDatabase;
    private final Listener listener;

    @Override
    protected LiveData<List<Hero>> doInBackground(Void... voids) {
        try {
            return heroRoomDatabase.heroDao().getAllHeroes();
        } catch (Exception exception) {
            return null;
        }
    }

    public interface Listener {
        void onResult(LiveData<List<Hero>> result);
    }

    public AsyncGetAllHeroesFromDb(HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    /*@Override
    protected List<Hero> doInBackground(Void... voids) {
        try {
            return heroRoomDatabase.heroDao().getAllHeroes();
        } catch (Exception exception) {
            return new ArrayList<>();
        }
    }*/

    @Override
    protected void onPostExecute(LiveData<List<Hero>> heroes) {
        super.onPostExecute(heroes);
        listener.onResult(heroes);
    }
}