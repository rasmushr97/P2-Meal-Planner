package com.p2app.backend.recipeclasses;

public class Review {
    private int ID;
    private int recipeID;
    private String review;
    private String submitterName;
    private String submitterID;
    private int rating;

    public Review(int ID, String review, String submitterName, String submitterID, int rating, int recipeID) {
        this.ID = ID;
        this.review = review;
        this.submitterName = submitterName;
        this.submitterID = submitterID;
        this.rating = rating;
        this.recipeID = recipeID;
    }

    @Override
    public String toString() {
        return "Review{" +
                "ID=" + ID +
                ", review='" + review + '\'' +
                ", submitterName='" + submitterName + '\'' +
                ", submitterID='" + submitterID + '\'' +
                ", rating=" + rating +
                '}';
    }

    public int getID() {
        return ID;
    }

    public int getRecipeID() {
        return recipeID;
    }

    public String getReview() {
        return review;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public String getSubmitterID() {
        return submitterID;
    }

    public int getRating() {
        return rating;
    }

}
