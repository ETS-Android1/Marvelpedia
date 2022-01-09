package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

import com.project_future_2021.marvelpedia.data.Hero;

public class AsyncUpdateHero extends AsyncTask<Hero, Void, Integer> {

    private final HeroRoomDatabase heroRoomDatabase;
    private final Listener listener;

    public interface Listener {
        void onResult(Integer resultOfUpdate);
    }

    public AsyncUpdateHero(HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Hero... heroes) {
        try {
            // return the number of rows that were updated successfully
            return heroRoomDatabase.heroDao().updateHero(heroes[0]);
        } catch (Exception e) {
            // if there was any error, just return -1
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer resultOfUpdate) {
        super.onPostExecute(resultOfUpdate);
        listener.onResult(resultOfUpdate);
    }
}
