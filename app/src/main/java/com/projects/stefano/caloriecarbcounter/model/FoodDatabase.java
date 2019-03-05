package com.projects.stefano.caloriecarbcounter.model;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(version = 1, entities = {FoodEntry.class})
public abstract class FoodDatabase extends RoomDatabase {
    abstract public FoodDao foodDao();
}