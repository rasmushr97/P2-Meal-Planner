package com.example.rasmus.p2app.backend.userclasses;

import java.util.List;

public class Preferences {
    private List<String> dislikes;
    private List<String> allergies;
    private boolean vegan = false;


    public void addDislike(String dislike){
        dislikes.add(dislike);
    }

    public void addAllergy(String allergy){
        allergies.add(allergy);
    }

    public void removeDislike(String dislike){
        dislikes.remove(dislike);
    }

    public void removeAllergy(String allergy){
        dislikes.remove(allergy);
    }

    public boolean isVegan() {
        return vegan;
    }

    public void setVegan(boolean vegan) {
        this.vegan = vegan;
    }

}
