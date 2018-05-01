package com.example.rasmus.p2app.backend.userclasses;

import java.time.LocalDate;

import static java.time.temporal.ChronoUnit.YEARS;


public class LocalUser extends User {
    private int age;
    private LocalDate birthday;
    private int height;
    private double weight;
    private double goalWeight;
    private int calorieDeficit;
    private int caloriesPerDay;
    private double exerciseLvl = 1.3750;
    private String sex;

    private Goal goal = new Goal();
    private Preferences preferences;

    public double calcBMI() {
        return this.weight / ((this.height / 100) * (this.height / 100));
    }

    public void calcBirthday() {
       this.age = (int) YEARS.between(this.birthday, LocalDate.now());
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }

    public int getCalorieDeficit() {
        return calorieDeficit;
    }

    public void setCalorieDeficit(int calorieDeficit) {
        this.calorieDeficit = calorieDeficit;
    }

    public double getExerciseLvl() {
        return exerciseLvl;
    }

    public void setExerciseLvl(double exerciseLvl) {
        this.exerciseLvl = exerciseLvl;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(double goalWeight) {
        this.goalWeight = goalWeight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Preferences getPreferences() {
        return preferences;
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

}
