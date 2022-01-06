package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

public class AsyncHowManyRecordsInDb extends AsyncTask<Void, Void, Integer> {

    private final HeroRoomDatabase heroRoomDatabase;
    private final Listener listener;

    public interface Listener {
        void onResult(Integer resultNumber);
    }

    public AsyncHowManyRecordsInDb(HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        try {
            return heroRoomDatabase.heroDao().howManyHeroes();
        } catch (Exception e) {
            return -1;
        }
    }

    @Override
    protected void onPostExecute(Integer resultNumber) {
        super.onPostExecute(resultNumber);
        listener.onResult(resultNumber);
    }
}