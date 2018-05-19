package com.p2app.backend.time;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day {
    private Set<Meal> meals = new HashSet<>();

    public Set<Meal> getMeals() {
        return meals;
    }

    public Meal getMeal(int recipeID){
        // Find the meal that has a recipe with the input ID and return it
        for(Meal m : meals){
            if(m.getRecipe().getID() == recipeID){
                return m;
            }
        }
        return null;
    }

    public void addMeal(Meal m){
        meals.add(m);
    }

    public void deleteMeal(Meal m){
        meals.remove(m);
    }

    public int getCalorieSum(){
        return meals.stream().mapToInt(x -> x.getRecipe().getCalories()).sum();
    }

    public void setMeals(Set<Meal> meals) {
        this.meals = meals;
    }
}
