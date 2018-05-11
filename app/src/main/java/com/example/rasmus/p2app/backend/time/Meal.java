package com.example.rasmus.p2app.backend.time;

import com.example.rasmus.p2app.backend.recipeclasses.Recipe;

import java.util.Objects;

public class Meal {
    private String mealName;
    private Recipe recipe = null;

    public Meal(String mealName, Recipe recipe) {
        this.mealName = mealName;
        this.recipe = recipe;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meal meal = (Meal) o;
        return Objects.equals(recipe, meal.recipe);
    }

    @Override
    public int hashCode() {

        return Objects.hash(recipe);
    }
}
