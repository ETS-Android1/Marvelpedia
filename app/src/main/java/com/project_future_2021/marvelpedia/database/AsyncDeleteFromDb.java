package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

public class AsyncDeleteFromDb extends AsyncTask<Void, Void, Boolean> {

    private final HeroRoomDatabase heroRoomDatabase;
    private final Listener listener;

    public interface Listener {
        void onResult(boolean result);
    }

    public AsyncDeleteFromDb(@NonNull HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            heroRoomDatabase.heroDao().deleteAllHeroes();
        } catch (Exception exception) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onResult(aBoolean);
    }
}