package com.projects.stefano.caloriecarbcounter.model;

import org.joda.time.DateTime;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by Stefano on 4/28/2016.
 */
public class FoodEntry extends RealmObject {

    //DB columns
    private int carbs;
    private int calories;
    private Date entryDate;
    private String name;

    //Constructors
    public FoodEntry(){}

    //Getters and Setters
    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(Date entryDate) {
        this.entryDate = entryDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
