package com.project_future_2021.marvelpedia.database;

import android.os.AsyncTask;

import com.project_future_2021.marvelpedia.data.Hero;

public class AsyncGetHeroFromDb extends AsyncTask<Integer, Void, Hero> {

    private HeroRoomDatabase heroRoomDatabase;
    private Listener listener;

    public interface Listener {
        public void onResult(Hero resultHero);
    }

    public AsyncGetHeroFromDb(HeroRoomDatabase heroRoomDatabase, Listener listener) {
        this.heroRoomDatabase = heroRoomDatabase;
        this.listener = listener;
    }

    @Override
    protected Hero doInBackground(Integer... integers) {
        try {
            return heroRoomDatabase.heroDao().getHeroWithId(integers[0]);
        } catch (Exception e) {
            return new Hero();
        }
    }

    @Override
    protected void onPostExecute(Hero resultHero) {
        super.onPostExecute(resultHero);
        listener.onResult(resultHero);
    }
}