package com.example.rasmus.p2app.backend.time;

import com.example.rasmus.p2app.backend.recipeclasses.Recipe;

import java.util.List;

public class Meal {
    private String mealName;
    private Recipe recipes;

    public Meal(String mealName, Recipe recipes) {
        this.mealName = mealName;
        this.recipes = recipes;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Recipe getRecipes() {
        return recipes;
    }

    public void setRecipes(Recipe recipes) {
        this.recipes = recipes;
    }
}
