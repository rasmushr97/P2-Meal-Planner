package com.example.rasmus.p2app.backend;

import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.recipeclasses.Review;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.example.rasmus.p2app.backend.userclasses.User;
import com.example.rasmus.p2app.cloud.DBHandler;
import com.example.rasmus.p2app.exceptions.NoDBConnectionException;
import com.example.rasmus.p2app.exceptions.NoUserException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InRAM {
    public static Calendar calendar;

    public static String userID = "1";
    public static LocalUser user = new LocalUser();

    public static Map<Integer, Integer> usersRatings = new HashMap<>();
    public static Map<Integer, Recipe> recipesInRAM = new HashMap<>();
    public static Map<String, LocalDate> mealsToMake = new HashMap<>();

    public static List<User> allUsers = new ArrayList<>();

    public static List<List<Integer>> recipeIDsForExplorer = new ArrayList<>();

    public static List<String> sectionNames = new ArrayList<>();


    //TODO: login method

    public static void initializeUser() {
        if (user == null) {
            user = new LocalUser();
        }
        user.initialize(userID);
        user.setID(userID);
        user.setReviews(DBHandler.getRevForUser(userID));
        usersRatings = DBHandler.getUserRatings();
    }

    public static void initializeCalender() {

        if (user == null) {
            throw new NoUserException();
        }

        // maybe get recipes from the recommender  systems
        try {
            calendar = DBHandler.getCalender(user.getID());
            Map<LocalDate, Day> days = calendar.getDates();

        } catch (NoDBConnectionException e) {
            System.out.println("no connection");
        }
    }


    public static void addRecipesToRam(List<Integer> IDList) {
        List<Recipe> recipes = DBHandler.getRecipesFromIDs(IDList);
        for (Recipe r : recipes) {
            recipesInRAM.put(r.getID(), r);
        }
    }

    public static void deleteMeal(LocalDate date, Meal meal) {
        calendar.getDay(date).deleteMeal(meal);
        DBHandler.deleteFromRecipeChosen(date, meal.getRecipe().getID(), user.getID());
    }

    public static void addMeal(LocalDate date, Meal meal) {
        Day day = calendar.getDay(date);
        if (day == null) {
            day = new Day();
            calendar.addDay(date, day);
        }
        calendar.getDay(date).addMeal(meal);

        DBHandler.addToRecipeChosen(date, meal, user.getID());
    }



    public static void findUsers() {
        for (Recipe recipe : recipesInRAM.values()) {
            /* Loads in reviews and creates users based on data */
            if (recipe.getReviews() != null) {
                for (Review r : recipe.getReviews()) {
                    User user1 = new User();
                    if (r != null) {
                        user1.addReview(r);
                    }

                    user1.setUserName(r.getSubmitterName());
                    user1.setID(r.getSubmitterID());

                    /* Makes sure each user is unique and stores unique objects in ArrayList
                     * If not unique: adds non-unique user's reviewed recipes to the unique instance's HashMap */
                    if (isUniqueUser(user1, allUsers) == 0) {
                        allUsers.add(user1);
                    } else {
                        int userIndex = isUniqueUser(user1, allUsers);
                        for (Review aReview : user1.getReviews()) {
                            allUsers.get(userIndex).addReview(aReview);
                        }
                    }
                }
            }
        }

    }

    private static int isUniqueUser(User newUser, List<User> users) {
        for (int i = 0; i < users.size(); i++) {
            if (newUser.getID().equals(users.get(i).getID())) {
                return i;
            }
        }
        return 0;
    }


    public static void initializeRecipes() {
        List<Recipe> recipes = DBHandler.getAllRecipes();

        for(Recipe r : recipes){
            recipesInRAM.put(r.getID(), r);
        }

    }
}
