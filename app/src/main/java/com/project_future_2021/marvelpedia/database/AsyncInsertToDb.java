package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

public class AsyncInsertToDb extends AsyncTask<List<Hero>, Void, Boolean> {

    private final HeroRoomDatabase heroRoomDatabase;
    private final Listener listener;

    public interface Listener {
        void onResult(boolean result);
    }

    public AsyncInsertToDb(@NonNull HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    @Override
    protected Boolean doInBackground(List<Hero>... heroes) {
        try {
            heroRoomDatabase.heroDao().insertHeroes(heroes[0]);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onResult(aBoolean);
    }
}