package com.example.rasmus.p2app.backend.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Day {
    private List<Meal> meals = new ArrayList<>();

    public List<Meal> getMeals() {
        return meals;
    }

    public void addMeal(Meal m){
        meals.add(m);
    }

    public void deleteMeal(Meal m){
        meals.remove(m);
    }
}
