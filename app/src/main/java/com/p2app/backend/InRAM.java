package com.p2app.backend;

import com.p2app.backend.recipeclasses.Recipe;
import com.p2app.backend.recipeclasses.Review;
import com.p2app.backend.time.Calendar;
import com.p2app.backend.time.Day;
import com.p2app.backend.time.Meal;
import com.p2app.backend.userclasses.LocalUser;
import com.p2app.backend.userclasses.User;
import com.p2app.cloud.DBHandler;
import com.p2app.exceptions.NoDBConnectionException;
import com.p2app.exceptions.NoUserException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

;

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


    public static void initializeUser() {
        user = new LocalUser();
        user.initialize(userID);
        user.setID(userID);
        user.setReviews(DBHandler.getRevForUser(userID));
        usersRatings = DBHandler.getUserRatings();
    }

    public static void initializeCalender() {
        if (user == null) {
            throw new NoUserException("User Failed to initialize");
        }

        try { calendar = DBHandler.getCalender(user.getID()); }
        catch (NoDBConnectionException e) {
            System.out.println("no connection");
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
        // Go through all recipes and their reviews to find users
        for (Recipe recipe : recipesInRAM.values()) {
            // Loads in reviews and creates users based on data
            if (recipe.getReviews() != null) {
                for (Review r : recipe.getReviews()) {
                    User user1 = new User();
                    if (r != null) {
                        user1.addReview(r);
                    }

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
        for (Recipe r : recipes) {
            recipesInRAM.put(r.getID(), r);
        }
    }
}
