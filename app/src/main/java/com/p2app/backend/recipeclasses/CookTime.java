package com.p2app.backend.recipeclasses;

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


    public String getCookTime() {
        return cookTime;
    }


    public String getReadyIn() {
        return readyIn;
    }

}
