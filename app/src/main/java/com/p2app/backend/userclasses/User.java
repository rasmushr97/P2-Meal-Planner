package com.p2app.backend.userclasses;

import com.p2app.backend.recipeclasses.Recipe;
import com.p2app.backend.recipeclasses.Review;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class User {
    private String ID;

    private List<Review> reviews = new ArrayList<>();

    private LinkedHashMap<String, Double> similarityScores = new LinkedHashMap<>(); //Stores this users similarity scores to other users <otherUserID, simScore>
    private Map<Integer, Double> recommendedRecipes = new HashMap<>(); //Recommended recipes for this user <RecipeID, ExpectedRating>

    public User() {
    }

    public User(String ID) {
        this.ID = ID;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }


    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review R){
        reviews.add(R);
    }

    public void removeReview(Review R){
        reviews.remove(R);
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    // For Recommender
    public void addSimilarityScore(String otherUser, Double simScore){ similarityScores.put(otherUser, simScore); }

    public LinkedHashMap<String, Double> getSimilarityScores(){ return similarityScores; }

    public void addRecommendedRecipes(Map<Integer, Double> recRecipes){ recommendedRecipes.putAll(recRecipes); }

    public Map<Integer, Double> getRecommendedRecipes(){ return recommendedRecipes; }

    public void removeRecommendedRecipe(Recipe remRecipe){ recommendedRecipes.remove(remRecipe); }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(ID, user.ID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
