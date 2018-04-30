package com.example.rasmus.p2app.backend.recipeclasses;

public class CookTime {
    private String prepTime;
    private String cookTime;
    private String readyIn;

    public CookTime(String prepTime, String cookTime, String readyIn) {
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.readyIn = readyIn;
    }

    @Override
    public String toString() {
        return "CookTime{" +
                "prepTime='" + prepTime + '\'' +
                ", cookTime='" + cookTime + '\'' +
                ", readyIn='" + readyIn + '\'' +
                '}';
    }

    public String getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(String prepTime) {
        this.prepTime = prepTime;
    }

    public String getCookTime() {
        return cookTime;
    }

    public void setCookTime(String cookTime) {
        this.cookTime = cookTime;
    }

    public String getReadyIn() {
        return readyIn;
    }

    public void setReadyIn(String readyIn) {
        this.readyIn = readyIn;
    }
}
