package com.p2app.cloud;

import android.annotation.SuppressLint;
import android.os.StrictMode;

import com.p2app.backend.InRAM;
import com.p2app.backend.recipeclasses.CookTime;
import com.p2app.backend.recipeclasses.Ingredient;
import com.p2app.backend.recipeclasses.Recipe;
import com.p2app.backend.recipeclasses.Review;
import com.p2app.backend.time.Calendar;
import com.p2app.backend.time.Day;
import com.p2app.backend.time.Meal;
import com.p2app.backend.userclasses.Goal;
import com.p2app.exceptions.NoDBConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

        // Normally android studio does not allow for slower operation such as reading from a file or a database
        // There you have to use these to lines in order to permit these slow operations
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        Connection connection = null;
        try {
            // Create connection to the database using the JDBC driver
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

        conn = connection;
    }

    public static boolean isConnected(){
        return !(conn == null);
    }

    public static void closeCon() {
        try {
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean login(String username, String password){
        ResultSet resultSet;
        // Create string for query
        String sql = "SELECT user_id FROM user " +
                "Where username='" + username + "' AND password='" +  password + "'";

        try {
            // Execute the query
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            // The result of the query is loading into a ResultSet object
            if(resultSet.next()){
                InRAM.userID = resultSet.getString("user_id");
            }else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void getUserData(String userID){
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT height, sex, age, for_weight_loss FROM user WHERE user_id=" + userID;
            resultSet = stmt.executeQuery(sql);

            //Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                InRAM.user.setHeight(resultSet.getInt("height"));
                InRAM.user.setAge(resultSet.getInt("age"));
                if(resultSet.getInt("sex") == 1){
                    InRAM.user.setMale(true);
                } else{
                    InRAM.user.setMale(false);
                }
                InRAM.user.setWantLoseWeight(resultSet.getInt("for_weight_loss"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

    }

    public static boolean registerUser(String username, String password, int height, int sex, int age, int loseWeight){
        boolean registerSuccess = true;

        String sql = "INSERT INTO user (username, password, height, sex, age, for_weight_loss) " +
                "VALUES ('" + username + "', '" + password + "', " + height + ", " + sex + ", " + age + ", " + loseWeight + ")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            registerSuccess = false;
        }

        return registerSuccess;
    }


    public static void addToRecipeChosen(LocalDate date, Meal meal, String userID){
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

    public static void addWeightMeasurement(LocalDate date, float weight, float goalweight, String userID, LocalDate goal_changed_date){
        String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        String goal_changed_formatted = goal_changed_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        String sql = "INSERT INTO goals (date, current_weight, goal_weight, user_id, goal_changed_date) " +
                "VALUES ('" + dateFormatted + "', " + weight + ", " +
                goalweight + ", " + userID + ", '" + goal_changed_formatted + "')";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromGoals(String userID){
        String sql = "DELETE FROM goals WHERE user_id=" + userID;

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFromRecipeChosen(LocalDate date, int recipeID, String userID){
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

    public static List<Recipe> getAllRecipes() {
        List<Integer> IDs = new ArrayList<>();
        for (int i = 0; i < 2779; i++) {
            IDs.add(2357 + i);
        }
        return getRecipesFromIDs(IDs);
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
                recipe.setRating(resultSet.getDouble("rating"));
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

        HashMap<Integer, List<Ingredient>> ingredientsListMap = getIngredientsFromRecipeIDs(IDs);
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

    private static HashMap<Integer, List<Ingredient>> getIngredientsFromRecipeIDs(List<Integer> IDs) {
        int recipeID = 0;
        int prevRecipeID = 0;
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        HashMap<Integer, List<Ingredient>> ingredientsMap = new HashMap<>();
        List<Ingredient> ingredientList = new ArrayList<>();

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
                    ingredientsMap.put(prevRecipeID, ingredientList);
                    ingredientList = new ArrayList<>();
                }
                prevRecipeID = recipeID;

                ingredientList.add(new Ingredient(ingrID, ingr_name, amount, unit, other_unit));
            }

            if (recipeID != 0) {
                ingredientsMap.put(recipeID, ingredientList);
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

                reviewList.add(new Review(reviewID, reviewText, submitterName, submitterID, individualRating, recipeID));
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
        List<Ingredient> ingredients = new ArrayList<>();
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
                rating = resultSet.getDouble("rating");
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
    public static List<Ingredient> getIngr(int ID) {
        ResultSet resultSet = null;
        String ingr_name = null;
        double amount = 0;
        String unit = null;
        String other_unit = null;
        List<Ingredient> ingredientList = new ArrayList<>();

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

                ingredientList.add(new Ingredient(ID, ingr_name, amount, unit, other_unit));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return ingredientList;
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
        int recipeID;
        List<Review> reviewList = new ArrayList<>();

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT * FROM reviews WHERE recipe_id=" + ID;
            resultSet = stmt.executeQuery(sql);

            //5: Extract data from result set
            while (resultSet.next()) {
                //Retrieve by column name
                recipeID = resultSet.getInt("recipe_id");
                reviewText = resultSet.getString("review");
                submitterName = resultSet.getString("submitter");
                submitterID = resultSet.getString("submitter_id");
                individualRating = resultSet.getInt("individual_rating");

                //Initializing fields in the Review class
                reviewList.add(new Review(ID, reviewText, submitterName, submitterID, individualRating, recipeID));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);

        return reviewList;
    }

    public static List<Review> getRevForUser(String userID) {
        ResultSet resultSet = null;
        List<Review> res = new ArrayList<>();

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
                int recipeID = resultSet.getInt("recipe_id");
                int revID = resultSet.getInt("recipe_id");
                String reviewText = resultSet.getString("review");
                String submitterName = resultSet.getString("submitter");
                String submitterID = resultSet.getString("submitter_id");
                int individualRating = resultSet.getInt("individual_rating");

                //Initializing fields in the Review class
                res.add(new Review(revID, reviewText, submitterName, submitterID, individualRating, recipeID));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);
        return res;
    }

    public static void updateLoseWeightOrNot(int toLostWeight, String userID){
        String sql = "UPDATE user SET for_weight_loss = " + toLostWeight + " WHERE user_id= " + userID;
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Gets information to the recipe class from the database
    public static void getLocalUser(String userID) {
        ResultSet resultSet = null;

        if (conn == null) {
            throw new NoDBConnectionException();
        }

        try {
            stmt = conn.createStatement();
            String sql = "SELECT date, current_weight, goal_weight, goal_changed_date FROM goals WHERE user_id=" + userID;
            resultSet = stmt.executeQuery(sql);


            //Extract data from result set
            int i = 0;
            while (resultSet.next()) {
                String date = resultSet.getString("date");
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate localDate = LocalDate.parse(date, dateFormatter);
                double curWeight = resultSet.getFloat("current_weight");
                InRAM.user.getGoal().addUserWeight(localDate, (float) curWeight);
                InRAM.user.setWeight(curWeight);

                double goalWeight = resultSet.getFloat("goal_weight");
                if(i == 0) {
                    InRAM.user.setGoalWeight(goalWeight);
                    String goal_changed_date = resultSet.getString("goal_changed_date");
                    Goal.goalStartDate = LocalDate.parse(goal_changed_date, dateFormatter);
                }

                i++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeResultSet(resultSet);
    }

    public static Calendar getCalender(String userID) {
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

    public static Map<Integer, Integer> getUserRatings(){
        Map<Integer, Integer> res = new HashMap<>();

        ResultSet resultSet;
        String sql = "SELECT recipe_id, individual_rating FROM reviews " +
                "WHERE submitter_id= '1'";

        try {
            stmt = conn.createStatement();
            resultSet = stmt.executeQuery(sql);

            while (resultSet.next()){
                int rating = resultSet.getInt("individual_rating");
                int recipeID = resultSet.getInt("recipe_id");
                res.put(recipeID, rating);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void uploadRating(int recipeID, int rating) {
        String sql = "INSERT INTO reviews (review, submitter, submitter_id, individual_rating, recipe_id) " +
                "VALUES ('" + "" + "', '" + "" + "', '" + InRAM.userID + "', " + rating + ", " + recipeID + ")";
        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
