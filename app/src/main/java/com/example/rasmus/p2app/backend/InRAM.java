package com.example.rasmus.p2app.backend;

import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.cloud.DBHandler;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.userclasses.User;
import com.example.rasmus.p2app.frontend.exception.NoDBConnectionException;
import com.example.rasmus.p2app.frontend.exception.NoUserException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InRAM {

    public static Calendar calendar;
    public static User user;
    public static Day today = null;
    public static Map<Integer, Recipe> recipesInRAM = new HashMap<>();


    //TODO: login method

    public static void initializeUser(int ID) {
        user = DBHandler.getUser(ID);
    }

    public static void initializeTodaysRecipes() {

        if (user == null) {
            throw new NoUserException();

        }

        // maybe get recipes from the recommender  systems
        try{
            calendar = DBHandler.getCalender(user.getID());
            Map<LocalDate, Day> days = calendar.getDates();
            if(days.get(LocalDate.now()) != null){
                today = days.get(LocalDate.now());
            }

            for(Meal meal : today.getMeals()){
                Recipe recipe = meal.getRecipe();
                recipesInRAM.put(recipe.getID(), recipe);
            }

        }catch (NoDBConnectionException e){
            System.out.println("no connection");
        }
    }

    public static void addRecipesToRam(List<Integer> IDList) {
        List<Recipe> recipes = DBHandler.getRecipesFromIDs(IDList);
        for (Recipe r : recipes) {
            recipesInRAM.put(r.getID(), r);
        }
    }


}
