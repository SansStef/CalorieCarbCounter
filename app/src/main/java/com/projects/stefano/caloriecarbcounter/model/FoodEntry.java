package com.projects.stefano.caloriecarbcounter.model;

import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

/**
 * Created by Stefano on 4/28/2016.
 */
@Entity
@TypeConverters(DateConverter.class)
public class FoodEntry {

    //DB columns
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int carbs;
    public int calories;
    public Date entryDate;
    public String name;
}
