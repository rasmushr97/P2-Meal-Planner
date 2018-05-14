package com.example.rasmus.p2app.backend;

import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.cloud.DBHandler;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.userclasses.User;
import com.example.rasmus.p2app.exceptions.NoDBConnectionException;
import com.example.rasmus.p2app.exceptions.NoUserException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InRAM {

    public static Calendar calendar;
    public static Calendar savedDays;
    public static HashMap<LocalDate, Meal> deletedMeals = new HashMap<>();

    public static User user;
    public static Day today = null;
    public static Map<Integer, Recipe> recipesInRAM = new HashMap<>();
    public static Map<String, LocalDate> mealsToMake = new HashMap<>();

    public static List<Integer> recommendedRecipes = new ArrayList<>();
    public static List<Integer> section1 = new ArrayList<>();
    public static List<Integer> section2 = new ArrayList<>();
    public static List<Integer> section3 = new ArrayList<>();


    //TODO: login method

    public static void initializeUser(int ID) {
        user = DBHandler.getUser(ID);
    }

    public static void initializeCalender() {

        savedDays = new Calendar(LocalDate.now());

        if (user == null) {
            throw new NoUserException();

        }

        // maybe get recipes from the recommender  systems
        try {
            calendar = DBHandler.getCalender(user.getID());
            Map<LocalDate, Day> days = calendar.getDates();
            if (days.get(LocalDate.now()) != null) {
                today = days.get(LocalDate.now());
            }

            if (today != null) {
                for (Meal meal : today.getMeals()) {
                    Recipe recipe = meal.getRecipe();
                    recipesInRAM.put(recipe.getID(), recipe);
                }
            } else {
                today = new Day();
            }

        } catch (NoDBConnectionException e) {
            System.out.println("no connection");
        }
    }

    public static void updateRecipesChosen(){
        DBHandler.addToRecipeChosen(savedDays);
        DBHandler.deleteFromRecipeChosen(deletedMeals);
    }

    public static void addRecipesToRam(List<Integer> IDList) {
        List<Recipe> recipes = DBHandler.getRecipesFromIDs(IDList);
        for (Recipe r : recipes) {
            recipesInRAM.put(r.getID(), r);
        }
    }


    // Updates calender with the meals from savedDays
    public static void syncCalender() {
        calendar.getDates().putAll(savedDays.getDates());
        today.setMeals(calendar.getDay(LocalDate.now()).getMeals());
    }

    public static void test() {
        int count = 2500;
        for (List<Integer> list : getRecipeIDsForExplorer()) {
            for (int i = 0; i < 20; i++) {
                list.add(count);
                count++;
            }
        }
        List<Recipe> recipes = DBHandler.getRecipesFromIDs(recommendedRecipes);
        recipes.addAll(DBHandler.getRecipesFromIDs(section1));
        recipes.addAll(DBHandler.getRecipesFromIDs(section2));
        recipes.addAll(DBHandler.getRecipesFromIDs(section3));
        for (Recipe recipe : recipes) {
            recipesInRAM.put(recipe.getID(), recipe);
        }
    }

    public static List<List<Integer>> getRecipeIDsForExplorer() {
        List<List<Integer>> res = new ArrayList<>();

        res.add(recommendedRecipes);
        res.add(section1);
        res.add(section2);
        res.add(section3);

        return res;
    }

}
