package com.project_future_2021.marvelpedia.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.project_future_2021.marvelpedia.data.Hero;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Hero.class}, version = 1)
@TypeConverters({ImageConverter.class})
public abstract class HeroRoomDatabase extends RoomDatabase {
    public abstract HeroDao heroDao();

    private static volatile HeroRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static HeroRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (HeroRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            HeroRoomDatabase.class, "hero_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
