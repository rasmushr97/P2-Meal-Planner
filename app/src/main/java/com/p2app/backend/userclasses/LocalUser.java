package com.p2app.backend.userclasses;

import com.p2app.backend.InRAM;
import com.p2app.cloud.DBHandler;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;


public class LocalUser extends User {
    private int age;
    private int height;
    private double weight;
    private double goalWeight;
    private int calorieDeficit;
    private int caloriesPerDay;
    private int wantLoseWeight;
    private double exerciseLvl = 1.375;
    private boolean isMale;
    private Goal goal = new Goal();
    private Preferences preferences;

    public double calcBMI() {
        double newHeight = (double) height / 100;
        return weight / (newHeight * newHeight);
    }

    public int getWantLoseWeight() {
        return wantLoseWeight;
    }

    public void setWantLoseWeight(int wantLoseWeight) {
        this.wantLoseWeight = wantLoseWeight;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int newCaloriesPerDay) {
        caloriesPerDay = newCaloriesPerDay;
    }

    public int getCalorieDeficit() {
        return calorieDeficit;
    }

    public void setCalorieDeficit(int newCalorieDeficit) {
        calorieDeficit = newCalorieDeficit;
    }

    public boolean isMale() {
        return isMale;
    }

    public void setMale(boolean male) {
        isMale = male;
    }

    public double getExerciseLvl() {
        return exerciseLvl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int newAge) {
        age = newAge;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int newHeight) {
        height = newHeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double newWeight) {
        weight = newWeight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double newGoalWeight) {
        goalWeight = newGoalWeight;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal newGoal) {
        goal = newGoal;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences newPreferences) {
        preferences = newPreferences;
    }


    public void initialize(String ID) {
        DBHandler.getUserData(InRAM.userID);
        /* Gets previous weight measurements from database */
        DBHandler.getLocalUser(ID);
        /* Calculates the users daily calorie intake */
        InRAM.user.getGoal().calcCaloriesPerDay(InRAM.user);
    }
}
