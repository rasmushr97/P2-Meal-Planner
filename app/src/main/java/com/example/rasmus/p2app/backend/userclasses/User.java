package com.example.rasmus.p2app.backend.userclasses;

import com.example.rasmus.p2app.backend.recipeclasses.Review;

import java.util.List;
import java.util.Objects;

public class User {
    private int ID;
    private String userName;
    private String passWord;
    private List<Review> reviews;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() { return passWord; }

    public void setPassWord(String passWord) { this.passWord = passWord; }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review R){
        reviews.add(R);
    }

    void removeReview(Review R){
        reviews.remove(R);
    }

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
