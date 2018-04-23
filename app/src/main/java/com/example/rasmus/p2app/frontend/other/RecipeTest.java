package com.example.rasmus.p2app.frontend.other;

import java.util.ArrayList;
import java.util.List;

// Just a some place to some recipe information before the database i running
public class RecipeTest {
    private static int idCounter = 0;
    private static List<Integer> imageID = new ArrayList<>();
    private static List<Integer> calorieCount = new ArrayList<>();
    private static List<String> mealName = new ArrayList<>();
    private static int totalCalories = 0;


    public int getIdCounter() {
        return idCounter;
    }

    public void addRecipe(int imgID, int calories, String meal) {
        imageID.add(imgID);
        calorieCount.add(calories);
        mealName.add(meal);
        idCounter++;
    }

    public int getCalories(int key) {
        return calorieCount.get(key);
    }

    public int getImgID(int key) {
        return imageID.get(key);
    }

    public String getMealName(int key) {
        return mealName.get(key);
    }


    public int getLastCalories() {
        return calorieCount.get(calorieCount.size() - 1);

    }

    public int getLastImgID() {
        return imageID.get(imageID.size() - 1);
    }

    public String getLastMealName() {
        return mealName.get(mealName.size() - 1);
    }


}
