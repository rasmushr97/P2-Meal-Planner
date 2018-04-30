package com.example.rasmus.p2app.backend.recipeclasses;

import java.util.List;
import java.util.Objects;

public class Recipe {
    private int ID;
    private String title;
    private String submitterName;
    private String pictureLink;
    private String websiteLink;
    private String description;
    private int servings;
    private int calories;
    private double rating;
    private CookTime time;
    private List<String> categories;
    private List<Ingredients> ingredients;
    private List<String> directions;
    private List<Review> reviews;

    public Recipe(int ID, String title, String submitterName,
                  String pictureLink, String websiteLink, String description,
                  int servings, int calories, double rating,
                  CookTime time, List<String> categories, List<Ingredients> ingredients,
                  List<String> directions, List<Review> reviews) {
        this.ID = ID;
        this.title = title;
        this.submitterName = submitterName;
        this.pictureLink = pictureLink;
        this.websiteLink = websiteLink;
        this.description = description;
        this.servings = servings;
        this.calories = calories;
        this.rating = rating;
        this.time = time;
        this.categories = categories;
        this.ingredients = ingredients;
        this.directions = directions;
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "ID=" + ID + "\n" +
                ", title='" + title + '\'' + "\n" +
                ", submitterName='" + submitterName + '\'' + "\n" +
                ", pictureLink='" + pictureLink + '\'' + "\n" +
                ", websiteLink='" + websiteLink + '\'' + "\n" +
                ", description='" + description + '\'' + "\n" +
                ", servings=" + servings + "\n" +
                ", calories=" + calories + "\n" +
                ", rating=" + rating + "\n" +
                '}';
    }

    public Recipe() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getPictureLink() {
        return pictureLink;
    }

    public void setPictureLink(String pictureLink) {
        this.pictureLink = pictureLink;
    }

    public String getWebsiteLink() {
        return websiteLink;
    }

    public void setWebsiteLink(String websiteLink) {
        this.websiteLink = websiteLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public CookTime getTime() {
        return time;
    }

    public void setTime(CookTime time) {
        this.time = time;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public List<Ingredients> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredients> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getDirections() {
        return directions;
    }

    public void setDirections(List<String> directions) {
        this.directions = directions;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return ID == recipe.ID;
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID);
    }
}
