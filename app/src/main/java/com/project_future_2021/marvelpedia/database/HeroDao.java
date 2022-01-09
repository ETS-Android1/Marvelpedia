package com.project_future_2021.marvelpedia.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

@Dao
public interface HeroDao {

    @Query("SELECT * FROM heroes_table ORDER BY name ASC")
    LiveData<List<Hero>> getAllHeroes();

    /*@Query("SELECT * FROM heroes_table")
    LiveData<List<Hero>> getAllHeroes();*/
    @Query("SELECT COUNT (id) FROM heroes_table")
    Integer howManyHeroes();

    @Query("SELECT * FROM heroes_table WHERE id=(:heroId) ")
    Hero getHeroWithId(int heroId);

    //@Query("SELECT * FROM heroes_table WHERE isFavorite = '1')
    @Query("SELECT * FROM heroes_table WHERE isFavorite = 1 ")
    LiveData<List<Hero>> getAllFavoriteHeroes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHeroes(List<Hero> heroes);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHero(Hero hero);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateHeroes(Hero... heroes);
    // Room uses the primary key to match passed entity instances to rows in the database.
    // If there is no row with the same primary key, Room makes no changes.
    // An @Update method can optionally return an int value
    // indicating the number of rows that were updated successfully.
    //public int updateHeroes(Hero... heroes);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    int updateHero(Hero hero);

    @Query("DELETE FROM heroes_table")
    void deleteAllHeroes();
}
