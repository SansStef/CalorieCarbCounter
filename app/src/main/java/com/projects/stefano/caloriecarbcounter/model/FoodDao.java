package com.projects.stefano.caloriecarbcounter.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface FoodDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFood(FoodEntry... foodEntries);


    @Query("SELECT * FROM foodentry")
    FoodEntry[] loadTodaysFood();
}
