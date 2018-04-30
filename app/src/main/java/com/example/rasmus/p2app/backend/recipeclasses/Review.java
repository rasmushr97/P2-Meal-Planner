package com.example.rasmus.p2app.backend.recipeclasses;

public class Review {
    private int ID;
    private String review;
    private String submitterName;
    private String submitterID;
    private int rating;

    public Review(int ID, String review, String submitterName, String submitterID, int rating) {
        this.ID = ID;
        this.review = review;
        this.submitterName = submitterName;
        this.submitterID = submitterID;
        this.rating = rating;
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

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSubmitterName() {
        return submitterName;
    }

    public void setSubmitterName(String submitterName) {
        this.submitterName = submitterName;
    }

    public String getSubmitterID() {
        return submitterID;
    }

    public void setSubmitterID(String submitterID) {
        this.submitterID = submitterID;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
