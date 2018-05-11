package com.example.rasmus.p2app.frontend.models;

import com.example.rasmus.p2app.backend.recipeclasses.Recipe;

public class SingleItemModel {
    private Recipe recipe;
    private String name;
    private String url;
    private String description;


    public SingleItemModel(String name) {
        this.name = name;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
