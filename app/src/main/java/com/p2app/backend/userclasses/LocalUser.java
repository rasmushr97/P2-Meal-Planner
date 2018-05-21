package com.p2app.backend.userclasses;

import com.p2app.backend.InRAM;
import com.p2app.cloud.DBHandler;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;


public class LocalUser extends User {
    private static int age;
    private static int height;
    private static double weight;
    private static double goalWeight;
    private static int calorieDeficit;
    private static int caloriesPerDay;
    private static int wantLoseWeight;
    private static double exerciseLvl = 1.375;
    private static boolean isMale;
    private static Goal goal = new Goal();
    private static Preferences preferences;

    @Override
    public String toString() {
        return "LocalUser{" +
                "age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", goalWeight=" + goalWeight +
                ", calorieDeficit=" + calorieDeficit +
                ", caloriesPerDay=" + caloriesPerDay +
                ", exerciseLvl=" + exerciseLvl +
                ", isMale=" + isMale +
                '}';
    }

    public double calcBMI() {
        double newHeight = (double) height / 100;
        return weight / (newHeight * newHeight);
    }

    public int getWantLoseWeight() {
        return wantLoseWeight;
    }

    public void setWantLoseWeight(int wantLoseWeight) {
        LocalUser.wantLoseWeight = wantLoseWeight;
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

    public void setExerciseLvl(double newExerciseLvl) {
        exerciseLvl = newExerciseLvl;
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
