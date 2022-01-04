package com.project_future_2021.marvelpedia.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.project_future_2021.marvelpedia.data.Hero;
import com.project_future_2021.marvelpedia.database.HeroDao;
import com.project_future_2021.marvelpedia.database.HeroRoomDatabase;

import java.util.List;

public class HeroRepository {

    private HeroDao heroDao;
    private LiveData<List<Hero>> allHeroes;

    // // Note that in order to unit test the HeroRepository, we have to remove the Application
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public HeroRepository(Application application) {
        HeroRoomDatabase db = HeroRoomDatabase.getDatabase(application);
        heroDao = db.heroDao();
        allHeroes = heroDao.getAllHeroes();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Hero>> getAllHeroes() {
        return allHeroes;
    }

    // We must call this on a non-UI thread or our app will throw an exception. Room ensures
    // that we're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Hero hero) {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.insertHero(hero);
        });
    }

    public void deleteAllHeroes() {
        HeroRoomDatabase.databaseWriteExecutor.execute(() -> {
            heroDao.deleteAllHeroes();
        });
    }
}