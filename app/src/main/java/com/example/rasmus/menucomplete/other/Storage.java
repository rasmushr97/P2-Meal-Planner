package com.example.rasmus.menucomplete.other;

import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;

public class Storage {
    public static ArrayList<Entry> userWeight = new ArrayList<>();
    public static ArrayList<Entry> goalWeight = new ArrayList<>();
    static private float goalWeightValue = 0;

    public float getGoalWeightValue() {
        return goalWeightValue;
    }

    public void setGoalWeightValue(float goalWeightValue) {
        this.goalWeightValue = goalWeightValue;
    }

    public void intialize(){
        this.userWeight.add(new Entry(1,4));
        this.userWeight.add(new Entry(2,8));
        this.userWeight.add(new Entry(3,3));
        this.userWeight.add(new Entry(4,2));

        this.goalWeight.add(new Entry(1,9));
        this.goalWeight.add(new Entry(3,3));
        this.goalWeight.add(new Entry(6,2));
        this.goalWeight.add(new Entry(9,1));
    }

    public ArrayList<Entry> getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(ArrayList<Entry> userWeight) {
        this.userWeight = userWeight;
    }

    public ArrayList<Entry> getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(ArrayList<Entry> goalWeight) {
        this.goalWeight = goalWeight;
    }
}
