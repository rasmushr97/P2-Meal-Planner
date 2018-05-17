package com.example.rasmus.p2app.frontend.models;

import com.example.rasmus.p2app.backend.time.Meal;

public class CalendarMealModel {
    private Meal meal;

    public CalendarMealModel(Meal meal) {
        this.meal = meal;
    }

    public Meal getMeal() {
        return meal;
    }
}
