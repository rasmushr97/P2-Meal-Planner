package com.example.rasmus.p2app.cloud;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.CookTime;
import com.example.rasmus.p2app.backend.recipeclasses.Ingredients;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.recipeclasses.Review;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.backend.userclasses.Goal;
import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.example.rasmus.p2app.backend.userclasses.User;
import com.example.rasmus.p2app.exceptions.NoDBConnectionException;

import java.sql.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    private static Connection conn = null;
    private static Statement stmt = null;

    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://35.198.191.151:3306/p2?useSSL=false";

    //  Database credentials
    private static final String USER = "root";
    private static final String PASS = "admin";


    @SuppressLint("NewApi")
    public static void createCon(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER).newInstance();
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException se) {
            System.out.println("Driver connection FAILED");
            //Log.e("error here 1 : ", se.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Database connection FAILED");
            //Log.e("error here 2 : ", e.getMessage());
        }catch (Exception ex){
            ex.getMessage();
        }

       /* try {
            connection.setReadOnly(false);
        } catch (SQLException e) {
            e.printStackTrace();
        } */
        conn = connection;

    }


    public static void closeCon() {
        // what exception to use here
        try {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addToRecipeChosen(LocalDate date, Meal meal, int userID){
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String sql = "INSERT INTO recipes_chosen (recipe_id, date, meal_title, user_id) " +
                "VALUES (" + meal.getRecipe().getID() + ", '" + dateFormatted + "', '" +
                meal.getMealName() + "', " + userID + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void addWeightMeasurement(LocalDate date, float weight, float goalweight, int userID){
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String sql = "INSERT INTO goals (date, current_weight, goal_weight, user_id) " +
                "VALUES ('" + dateFormatted + "', " + weight + ", " +
                goalweight + ", " + userID + ")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void deleteFromRecipeChosen(LocalDate date, int recipeID, int userID){
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String sql = "DELETE FROM recipes_chosen WHERE recipe_id = " + recipeID
                + " AND date = '" + dateFormatted + "' AND user_id = " + userID;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Recipe> getRecipesFromIDs(List<Integer> IDs) {
        List<Recipe> recipeList = new ArrayList<>();
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            // Create string for SQL Query
            String sql = createQueryString("recipe", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                Recipe recipe = new Recipe();
                recipe.setID(resultSet.getInt("recipe_id"));
                recipe.setTitle(resultSet.getString("title"));
                recipe.setSubmitterName(resultSet.getString("submitter"));
                recipe.setPictureLink(resultSet.getString("picture_link"));
                recipe.setWebsiteLink(resultSet.getString("link"));
                recipe.setDescription(resultSet.getString("description"));
                recipe.setServings(resultSet.getInt("servings"));
                recipe.setCalories(resultSet.getInt("calories"));
                recipe.setRating(resultSet.getInt("rating"));
                recipeList.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        HashMap<Integer, CookTime> cookTimeMap = getTimeFromRecipeIDs(IDs);
        for (Recipe r : recipeList) {
            r.setTime(cookTimeMap.get(r.getID()));
        }

        HashMap<Integer, List<Ingredients>> ingredientsListMap = getIngredientsFromRecipeIDs(IDs);
        for (Recipe r : recipeList) {
            r.setIngredients(ingredientsListMap.get(r.getID()));
        }

        HashMap<Integer, List<String>> catListMap = getCategoriesFromRecipeIDs(IDs);
        for (Recipe r : recipeList) {
            r.setCategories(catListMap.get(r.getID()));
        }

        HashMap<Integer, List<String>> dirListMap = getDirectionsFromRecipeIDs(IDs);
        for (Recipe r : recipeList) {
            r.setDirections(dirListMap.get(r.getID()));
        }

        HashMap<Integer, List<Review>> revListMap = getReviewsFromRecipeIDs(IDs);
        for (Recipe r : recipeList) {
            r.setReviews(revListMap.get(r.getID()));
        }

        return recipeList;
    }

    private static HashMap<Integer, CookTime> getTimeFromRecipeIDs(List<Integer> IDs) {
        HashMap<Integer, CookTime> cookTimeMap = new HashMap<>();
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            // Create string for SQL Query
            String sql = createQueryString("time", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                int recipeID = resultSet.getInt("recipe_id");
                String prepTime = resultSet.getString("prep_time");
                String cookTime = resultSet.getString("cook_time");
                String readyIn = resultSet.getString("ready_in");
                cookTimeMap.put(recipeID, new CookTime(prepTime, cookTime, readyIn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return cookTimeMap;
    }

    private static HashMap<Integer, List<String>> getCategoriesFromRecipeIDs(List<Integer> IDs) {
        int prevRecipeID = 0;
        int recipeID = 0;
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        HashMap<Integer, List<String>> categoryMap = new HashMap<>();
        List<String> categoryList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("categories", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                recipeID = resultSet.getInt("recipe_id");
                String categoryName = resultSet.getString("category_name");

                if (prevRecipeID != recipeID && prevRecipeID != 0) {
                    categoryMap.put(prevRecipeID, categoryList);
                    categoryList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                categoryList.add(categoryName);
            }

            if (recipeID != 0) {
                categoryMap.put(recipeID, categoryList);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return categoryMap;
    }

    private static HashMap<Integer, List<Ingredients>> getIngredientsFromRecipeIDs(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        HashMap<Integer, List<Ingredients>> ingredientsMap = new HashMap<>();
        List<Ingredients> ingredientsList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("ingredients_formatted", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                recipeID = resultSet.getInt("recipe_id");
                double amount = resultSet.getDouble("amount");
                String unit = resultSet.getString("unit");
                String other_unit = resultSet.getString("other_unit");
                String ingr_name = resultSet.getString("ingr_name");
                int ingrID = resultSet.getInt("ingr_id");

                if (prevRecipeID != recipeID && recipeID != 0) {
                    ingredientsMap.put(prevRecipeID, ingredientsList);
                    ingredientsList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                ingredientsList.add(new Ingredients(ingrID, ingr_name, amount, unit, other_unit));
            }

            if (recipeID != 0) {
                ingredientsMap.put(recipeID, ingredientsList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return ingredientsMap;
    }

    private static HashMap<Integer, List<String>> getDirectionsFromRecipeIDs(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        HashMap<Integer, List<String>> directionsMap = new HashMap<>();
        List<String> directionList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("directions", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                recipeID = resultSet.getInt("recipe_id");
                String recipeDir = resultSet.getString("directions");

                if (prevRecipeID != recipeID && recipeID != 0) {
                    directionsMap.put(prevRecipeID, directionList);
                    directionList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                directionList.add(recipeDir);
            }

            if (recipeID != 0) {
                directionsMap.put(recipeID, directionList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return directionsMap;
    }

    private static HashMap<Integer, List<Review>> getReviewsFromRecipeIDs(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;
        ResultSet resultSet = null;
        HashMap<Integer, List<Review>> reviewMap = new HashMap<>();
        List<Review> reviewList = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("reviews", IDs);
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                recipeID = resultSet.getInt("recipe_id");
                int reviewID = resultSet.getInt("review_id");
                String reviewText = resultSet.getString("review");
                String submitterName = resultSet.getString("submitter");
                String submitterID = resultSet.getString("submitter_id");
                int individualRating = resultSet.getInt("individual_rating");


                if (prevRecipeID != recipeID && recipeID != 0) {
                    reviewMap.put(prevRecipeID, reviewList);
                    reviewList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                reviewList.add(new Review(reviewID, reviewText, submitterName, submitterID, individualRating));
            }

            if (recipeID != 0) {
                reviewMap.put(recipeID, reviewList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return reviewMap;
    }

    //Gets information to the recipe class from the database
    public static Recipe getRecipe(int ID) {
        ResultSet resultSet = null;
        Recipe recipe = new Recipe();
        String title = null;
        String submitterName = null;
        String pictureLink = null;
        String websiteLink = null;
        String description = null;
        int servings = 0;
        int calories = 0;
        double rating = 0;
        CookTime time = null;
        List<String> categories = new ArrayList<>();
        List<Ingredients> ingredients = new ArrayList<>();
        List<String> directions = new ArrayList<>();
        List<Review> reviews = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT title, submitter, picture_link, link, description, servings, calories, rating FROM recipe WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            if (resultSet.next()) {
                //Retrieve by column name
                title = resultSet.getString("title");
                submitterName = resultSet.getString("submitter");
                pictureLink = resultSet.getString("picture_link");
                websiteLink = resultSet.getString("link");
                description = resultSet.getString("description");
                servings = resultSet.getInt("servings");
                calories = resultSet.getInt("calories");
                rating = resultSet.getInt("rating");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        time = getTime(ID);
        categories = getCat(ID);
        ingredients = getIngr(ID);

        return new Recipe(ID, title, submitterName, pictureLink, websiteLink, description, servings, calories, rating, time, categories, ingredients, directions, reviews);
    }

    //Gets information to the cooktime class from the database
    public static CookTime getTime(int ID) {
        ResultSet resultSet = null;
        String prepTime = null;
        String cookTime = null;
        String readyIn = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT prep_time, cook_time, ready_in FROM time WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            if (resultSet.next()) {
                //Retrieve by column name
                prepTime = resultSet.getString("prep_time");
                cookTime = resultSet.getString("cook_time");
                readyIn = resultSet.getString("ready_in");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return new CookTime(prepTime, cookTime, readyIn);
    }

    //Gets information to the recipe class from the database
    public static List<String> getCat(int ID) {
        List<String> listOfCategoryNames = new ArrayList<>();
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT category_name FROM categories WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);


            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                String categoryName = resultSet.getString("category_name");

                //Adds all the elements to a new list - that later can be initialized in Recipe class
                listOfCategoryNames.add(categoryName);
            }

            //Initialize the field categories in Recipe class

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return listOfCategoryNames;
    }

    //Gets information to the recipe class from the database
    public static List<Ingredients> getIngr(int ID) {
        ResultSet resultSet = null;
        String ingr_name = null;
        double amount = 0;
        String unit = null;
        String other_unit = null;
        List<Ingredients> ingredientsList = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT amount, unit, other_unit, ingr_name FROM ingredients_formatted WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                amount = resultSet.getDouble("amount");
                unit = resultSet.getString("unit");
                other_unit = resultSet.getString("other_unit");
                ingr_name = resultSet.getString("ingr_name");

                ingredientsList.add(new Ingredients(ID, ingr_name, amount, unit, other_unit));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return ingredientsList;
    }

    //Gets information to the recipe class from the database
    public static List<String> getDir(int ID) {
        ResultSet resultSet = null;
        List<String> listOfDirections = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT directions FROM directions WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                String recipeDir = resultSet.getString("directions");
                //Adds all the elements to a new list - that later can be initialized in Recipe class
                listOfDirections.add(recipeDir);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return listOfDirections;
    }

    public static List<Review> getRev(int ID) {
        ResultSet resultSet = null;
        String reviewText;
        String submitterName;
        String submitterID;
        int individualRating;
        List<Review> reviewList = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT review, submitter, submitter_id, individual_rating FROM reviews WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                reviewText = resultSet.getString("review");
                submitterName = resultSet.getString("submitter");
                submitterID = resultSet.getString("submitter_id");
                individualRating = resultSet.getInt("individual_rating");

                //Initializing fields in the Review class
                reviewList.add(new Review(ID, reviewText, submitterName, submitterID, individualRating));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return reviewList;
    }

    public static User getRevToUser(int userID) {
        ResultSet resultSet = null;
        User user = new User();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reviews WHERE submitter_id=" + userID;
            resultSet = stmt.executeQuery(sql);


            //5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                int revID = resultSet.getInt("recipe_id");
                String reviewText = resultSet.getString("review");
                String submitterName = resultSet.getString("submitter");
                String submitterID = resultSet.getString("submitter_id");
                int individualRating = resultSet.getInt("individual_rating");

                //Initializing fields in the Review class
                user.addReview(new Review(revID, reviewText, submitterName, submitterID, individualRating));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return user;
    }

    //Gets information to the recipe class from the database
    public static void getLocalUser(int userID) {
        ResultSet resultSet = null;
        //Goal goal = new Goal();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT date, current_weight, goal_weight FROM goals WHERE user_id=" + userID;
            resultSet = stmt.executeQuery(sql);


            //Extract data from result set
            int i = 0;
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                double curWeight = resultSet.getFloat("current_weight");
                double goalWeight = resultSet.getFloat("goal_weight");
                if(i == 0) { InRAM.user.setGoalWeight(goalWeight); }

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(date, dateFormatter);

                InRAM.user.getGoal().addUserWeight(localDate, (float) curWeight);
                //goal.addGoalWeight(localDate, (float) goalWeight);
                i++;
            }
            //LocalUser.setGoal(goal);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);
    }

    public static Calendar getCalender(int userID) {
        ResultSet resultSet = null;
        Calendar calendar = new Calendar(LocalDate.now());
        String prevDate = null;
        Day day = new Day();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM recipes_chosen WHERE user_id=" + userID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                int recipe_id = resultSet.getInt("recipe_id");
                String date = resultSet.getString("date");
                String mealTitle = resultSet.getString("meal_title");

                localDate = LocalDate.parse(date, dateFormatter);

                Recipe recipe = getRecipe(recipe_id);

                Meal meal = new Meal(mealTitle, recipe);
                meal.setRecipe(recipe);
                meal.setMealName(mealTitle);

                if (prevDate != null && (!date.equals(prevDate))) {
                    calendar.addDay(localDate, day);
                    day = new Day();
                }
                prevDate = date;

                day.addMeal(meal);

            }

            if (localDate != null) {
                calendar.addDay(localDate, day);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return calendar;
    }

    public static User getUser(int userID) {
        ResultSet resultSet = null;
        User user = new User();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT user_id, username, password FROM user WHERE user_id=" + userID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                String userName = resultSet.getString("username");
                String passWord = resultSet.getString("password");

                user.setID(userID);
                user.setUserName(userName);
                user.setPassWord(passWord);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return user;
    }

    private static String createQueryString(String tableName, List<Integer> IDs) {
        String res = "select * from " + tableName + " WHERE recipe_id IN (";
        int i = 0;
        for (int id : IDs) {
            if (i == IDs.size() - 1) {
                res = res.concat(id + ")");
            } else {
                res = res.concat(id + ",");
            }
            i++;
        }
        return res;
    }

    private static void closeResultSet(ResultSet r) {
        try {
            if (r != null)
                r.close();
        } catch (SQLException e) {
            e.getMessage();
        }
    }

}
