package com.p2app.frontend.models;

    // Hold the information of a single item of the explorer
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
