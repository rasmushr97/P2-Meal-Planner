package com.example.rasmus.p2app.backend.database;

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

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://35.198.191.151:3306/p2?useSSL=false";

    //  Database credentials
    static final String USER = "root";
    static final String PASS = "admin";

    static Connection conn = null;
    static Statement stmt = null;
    static ResultSet rs = null;

    static public void createCon() {

        //Register JDBC driver
        System.out.println("Registering JDBC drivers");
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Open a connection
        System.out.println("Connecting to database...");
        try {
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    static public List<Recipe> getRecipesFromList(List<Integer> IDs) {
        List<Recipe> recipeList = new ArrayList<>();

        try {
            stmt = conn.createStatement();

            // Create string for SQL Query
            String sql = createQueryString("recipe", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                Recipe recipe = new Recipe();
                recipe.setID(rs.getInt("recipe_id"));
                recipe.setTitle(rs.getString("title"));
                recipe.setSubmitterName(rs.getString("submitter"));
                recipe.setPictureLink(rs.getString("picture_link"));
                recipe.setWebsiteLink(rs.getString("link"));
                recipe.setDescription(rs.getString("description"));
                recipe.setServings(rs.getInt("servings"));
                recipe.setCalories(rs.getInt("calories"));
                recipe.setRating(rs.getInt("rating"));
                recipeList.add(recipe);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        HashMap<Integer, CookTime> cookTimeMap = getTimeFromList(IDs);
        for (Recipe r : recipeList) {
            r.setTime(cookTimeMap.get(r.getID()));
        }

        HashMap<Integer, List<Ingredients>> ingredientsListMap = getIngrFromList(IDs);
        for (Recipe r : recipeList) {
            r.setIngredients(ingredientsListMap.get(r.getID()));
        }

        HashMap<Integer, List<String>> catListMap = getCatFromList(IDs);
        for (Recipe r : recipeList) {
            r.setCategories(catListMap.get(r.getID()));
        }

        HashMap<Integer, List<String>> dirListMap = getDirectionsFromList(IDs);
        for (Recipe r : recipeList) {
            r.setDirections(dirListMap.get(r.getID()));
        }

        HashMap<Integer, List<Review>> revListMap = getRevFromList(IDs);
        for (Recipe r : recipeList) {
            r.setReviews(revListMap.get(r.getID()));
        }

        return recipeList;
    }


    static public HashMap<Integer, CookTime> getTimeFromList(List<Integer> IDs) {
        HashMap<Integer, CookTime> cookTimeMap = new HashMap<>();

        try {
            stmt = conn.createStatement();
            // Create string for SQL Query
            String sql = createQueryString("time", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int recipeID = rs.getInt("recipe_id");
                String prepTime = rs.getString("prep_time");
                String cookTime = rs.getString("cook_time");
                String readyIn = rs.getString("ready_in");
                cookTimeMap.put(recipeID , new CookTime(prepTime, cookTime, readyIn));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cookTimeMap;
    }


    public static HashMap<Integer, List<String>> getCatFromList(List<Integer> IDs) {
        int prevRecipeID = 0;
        int recipeID = 0;

        HashMap<Integer, List<String>> categoryMap = new HashMap<>();
        List<String> categoryList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("categories", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                recipeID = rs.getInt("recipe_id");
                String categoryName = rs.getString("category_name");

                if(prevRecipeID != recipeID && prevRecipeID != 0){
                    categoryMap.put(prevRecipeID, categoryList);
                    categoryList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                categoryList.add(categoryName);
            }

            if(recipeID != 0) {
                categoryMap.put(recipeID, categoryList);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return categoryMap;
    }


    public static HashMap<Integer, List<Ingredients>> getIngrFromList(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;

        HashMap<Integer, List<Ingredients>> ingredientsMap = new HashMap<>();
        List<Ingredients> ingredientsList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("ingredients_formatted", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                recipeID = rs.getInt("recipe_id");
                double amount = rs.getDouble("amount");
                String unit = rs.getString("unit");
                String other_unit = rs.getString("other_unit");
                String ingr_name = rs.getString("ingr_name");
                int ingrID = rs.getInt("ingr_id");

                if(prevRecipeID != recipeID && recipeID != 0){
                    ingredientsMap.put(prevRecipeID, ingredientsList);
                    ingredientsList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                ingredientsList.add(new Ingredients(ingrID, ingr_name, amount, unit, other_unit));
            }

            if(recipeID != 0) {
                ingredientsMap.put(recipeID, ingredientsList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientsMap;
    }

    public static HashMap<Integer, List<String>> getDirectionsFromList(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;

        HashMap<Integer, List<String>> directionsMap = new HashMap<>();
        List<String> directionList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("directions", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                recipeID = rs.getInt("recipe_id");
                String recipeDir = rs.getString("directions");

                if(prevRecipeID != recipeID && recipeID != 0){
                    directionsMap.put(prevRecipeID, directionList);
                    directionList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                directionList.add(recipeDir);
            }

            if(recipeID != 0) {
                directionsMap.put(recipeID, directionList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return directionsMap;
    }


    public static HashMap<Integer, List<Review>> getRevFromList(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;

        HashMap<Integer, List<Review>> reviewMap = new HashMap<>();
        List<Review> reviewList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = createQueryString("reviews", IDs);
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                recipeID = rs.getInt("recipe_id");
                int reviewID = rs.getInt("review_id");
                String reviewText = rs.getString("review");
                String submitterName = rs.getString("submitter");
                String submitterID = rs.getString("submitter_id");
                int individualRating = rs.getInt("individual_rating");


                if(prevRecipeID != recipeID && recipeID != 0){
                    reviewMap.put(prevRecipeID, reviewList);
                    reviewList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                reviewList.add(new Review(reviewID, reviewText, submitterName, submitterID, individualRating));
            }

            if(recipeID != 0) {
                reviewMap.put(recipeID, reviewList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviewMap;
    }

    //Gets information to the recipe class from the database
    static public Recipe getRecipe(int ID) {
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

        try {
            stmt = conn.createStatement();
            String sql = "SELECT title, submitter, picture_link, link, description, servings, calories, rating FROM recipe WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            if (rs.next()) {
                //Retrieve by column name
                title = rs.getString("title");
                submitterName = rs.getString("submitter");
                pictureLink = rs.getString("picture_link");
                websiteLink = rs.getString("link");
                description = rs.getString("description");
                servings = rs.getInt("servings");
                calories = rs.getInt("calories");
                rating = rs.getInt("rating");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        time = getTime(ID);
        categories = getCat(ID);
        ingredients = getIngr(ID);


        return new Recipe(ID, title, submitterName, pictureLink, websiteLink, description, servings, calories, rating, time, categories, ingredients, directions, reviews);
    }

    //Gets information to the cooktime class from the database
    static public CookTime getTime(int ID) {
        String prepTime = null;
        String cookTime = null;
        String readyIn = null;

        try {
            stmt = conn.createStatement();
            String sql = "SELECT prep_time, cook_time, ready_in FROM time WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            if (rs.next()) {
                //Retrieve by column name
                prepTime = rs.getString("prep_time");
                cookTime = rs.getString("cook_time");
                readyIn = rs.getString("ready_in");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new CookTime(prepTime, cookTime, readyIn);
    }

    //Gets information to the recipe class from the database
    public static List<String> getCat(int ID) {
        List<String> listOfCategoryNames = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT category_name FROM categories WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);


            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                String categoryName = rs.getString("category_name");

                //Adds all the elements to a new list - that later can be initialized in Recipe class
                listOfCategoryNames.add(categoryName);
            }

            //Initialize the field categories in Recipe class

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfCategoryNames;
    }

    //Gets information to the recipe class from the database
    public static List<Ingredients> getIngr(int ID) {
        String ingr_name = null;
        double amount = 0;
        String unit = null;
        String other_unit = null;

        List<Ingredients> ingredientsList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT amount, unit, other_unit, ingr_name FROM ingredients_formatted WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                amount = rs.getDouble("amount");
                unit = rs.getString("unit");
                other_unit = rs.getString("other_unit");
                ingr_name = rs.getString("ingr_name");

                ingredientsList.add(new Ingredients(ID, ingr_name, amount, unit, other_unit));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ingredientsList;
    }

    //Gets information to the recipe class from the database
    public static List<String> getDir(int ID) {
        System.out.println("Running getDir");
        List<String> listOfDirections = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT directions FROM directions WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                String recipeDir = rs.getString("directions");
                //Adds all the elements to a new list - that later can be initialized in Recipe class
                listOfDirections.add(recipeDir);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listOfDirections;
    }

    public static List<Review> getRev(int ID) {
        String reviewText;
        String submitterName;
        String submitterID;
        int individualRating;
        List<Review> reviewList = new ArrayList<>();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT review, submitter, submitter_id, individual_rating FROM reviews WHERE recipe_id=" + ID;
            rs = stmt.executeQuery(sql);

            //5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                reviewText = rs.getString("review");
                submitterName = rs.getString("submitter");
                submitterID = rs.getString("submitter_id");
                individualRating = rs.getInt("individual_rating");

                //Initializing fields in the Review class
                reviewList.add(new Review(ID, reviewText, submitterName, submitterID, individualRating));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviewList;
    }


    /*** Retrieving data for all the classes in the folder xxx  ***/

    static public User getRevToUser(int userID) {
        User user = new User();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reviews WHERE submitter_id=" + userID;
            rs = stmt.executeQuery(sql);



            //5: Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int revID = rs.getInt("recipe_id");
                String reviewText = rs.getString("review");
                String submitterName = rs.getString("submitter");
                String submitterID = rs.getString("submitter_id");
                int individualRating = rs.getInt("individual_rating");

                //Initializing fields in the Review class
                user.addReview(new Review(revID, reviewText, submitterName, submitterID, individualRating));
            }


        } catch(SQLException e){
            e.printStackTrace();
        }

        return user;
    }



    //Gets information to the recipe class from the database
    static public LocalUser getLocalUser(int userID) {
        Goal goal = new Goal();
        LocalUser localUser = new LocalUser();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT date, current_weight, goal_weight FROM goals WHERE user_id=" + userID;
            rs = stmt.executeQuery(sql);


            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                // make date
                String date = rs.getString("date");
                double curWeight = rs.getDouble("current_weight");
                double goalWeight = rs.getDouble("goal_weight");

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(date, dateFormatter);

                goal.addUserWeight(localDate, (float) curWeight);
                goal.addGoalWeight(localDate, (float) goalWeight);
            }

            localUser.setGoal(goal);

        } catch(SQLException e){
            e.printStackTrace();
        }
        return localUser;
    }


    static public Calendar getCalender(int userID) {
        Calendar calendar = null;

        try {
            stmt = conn.createStatement();
            String sql = "SELECT recipe_id, date, meal_title FROM recipes_chosen WHERE user_id=" + userID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                int recipe_id= rs.getInt("recipe_id");
                String date = rs.getString("date");
                String mealTitle = rs.getString("meal_title");

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(date, dateFormatter);

                Recipe recipe = getRecipe(recipe_id);

                Meal meal = new Meal(mealTitle, recipe);
                meal.setRecipes(recipe);
                meal.setMealName(mealTitle);

                Day day = new Day();
                day.addMeal(meal);

                calendar = new Calendar(localDate);
                calendar.addDay(localDate, day);
            }

            System.out.println(calendar.getDates());


        } catch(SQLException e){
            e.printStackTrace();
        }

        return calendar;
    }

    static public User getUser(int userID) {
        User user = new User();

        try {
            stmt = conn.createStatement();
            String sql = "SELECT user_id, username, password FROM user WHERE user_id=" + userID;
            rs = stmt.executeQuery(sql);

            //Extract data from result set
            while (rs.next()) {
                //Retrieve by column name
                String userName = rs.getString("username");
                String passWord = rs.getString("password");

                user.setID(userID);
                user.setUserName(userName);
                user.setPassWord(passWord);

            }


        } catch(SQLException e){
            e.printStackTrace();
        }

        return user;
    }



    public static void closeCon() {
        // what exception to use here
        try {
            rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

}
