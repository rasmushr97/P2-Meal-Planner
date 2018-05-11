package com.example.rasmus.p2app.frontend.models;

import com.example.rasmus.p2app.backend.recipeclasses.Recipe;

public class SingleItemModel {
    private int recipeID;

    public SingleItemModel(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }



}
