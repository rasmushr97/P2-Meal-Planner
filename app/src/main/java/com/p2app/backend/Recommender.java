package com.p2app.backend;

import com.p2app.backend.comparators.ValueRecipeComparator;
import com.p2app.backend.comparators.ValueUserComparator;
import com.p2app.backend.recipeclasses.Recipe;
import com.p2app.backend.recipeclasses.Review;
import com.p2app.backend.userclasses.User;


import java.util.*;

public class Recommender {

    private User target;
    private List<User> users;
    List<Recipe> recipes;

    /* Recommender needs a single target user, a list of users and a list of recipes as input to recommend recipes
     * Output is stored in target user's map of recommended recipes: recommendedRecipes<recipeID, expectedRating> */
    public Recommender(User targetUser, List<User> allUsers, List<Recipe> allRecipes){
        target = targetUser;
        users = allUsers;
        recipes = allRecipes;
    }

    /* Method to recommend a recipe to target user */
    public void recommendRecipe() {
        //System.out.println("USER: " + target.getUserID());

        for (User user : users) { //Calculates similarity scores for each user in list of user compared to target user
            if (!target.getID().equals(user.getID())) {
                double similarity = euclideanDistance(user);
                //System.out.println("Similarity: " + similarity);
                if (similarity > 0) { //A similarity score of 0 indicates no similarity between the users
                    target.addSimilarityScore(user.getID(), similarity);
                } else {
                    target.addSimilarityScore(user.getID(), 0.0);
                }
            }
        }
        LinkedHashMap<String, Double> sortedSimMap = new LinkedHashMap<>(sortStringDoubleMap(target.getSimilarityScores()));
        LinkedHashMap<String, Double> kNearestMap = nearestNeighbors(sortedSimMap);

        target.addRecommendedRecipes(gatherRecipes(kNearestMap));
        //System.out.println(target.getRecommendedRecipes());

    }


    /* Calculates the similarity between two users, based on the recipes they both have rated */
    private double euclideanDistance(User user) {
        double sumDiffSquares = 0;
        double sim;
        double similarity;
        int mutualCounter = 0;

        for (Review review : target.getReviews()) { //For each pair in the map
            int recipeID = review.getRecipeID();
            int targetRating = review.getRating();
            int userRating = 0;

             /* If a recipe has been rated by both target user and another user, calculate similarity */
            for (Review userReview : user.getReviews()){
                if (userReview.getRecipeID() == recipeID){
                    userRating = userReview.getRating();
                }
            }
            double diff = targetRating - userRating;

            sumDiffSquares += diff * diff; //Sums Euclidean distances for all rated recipes between the two users. Squared to make sure result is positive
            mutualCounter++;
        }

        /* Similarity calculation */
        sim = Math.sqrt(sumDiffSquares);
        similarity = 1 / (1 + sim);

        /* Returns similarity, if both users have at least 1 recipe in common */
        if (mutualCounter > 0) {
            return similarity;
        } else {
            return 0;
        }

    }


    /* Sorts a map and returns a TreeMap */
    private static TreeMap<String, Double> sortStringDoubleMap(LinkedHashMap<String, Double> unsortedMap) {
        Comparator<String> comparator = new ValueUserComparator(unsortedMap);
        TreeMap<String, Double> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }


    /* Sorts a map and returns a TreeMap */
    private static TreeMap<Integer, Double> sortIntegerDoubleMap(LinkedHashMap<Integer, Double> unsortedMap) {
        Comparator<Integer> comparator = new ValueRecipeComparator(unsortedMap);
        TreeMap<Integer, Double> sortedMap = new TreeMap<>(comparator);
        sortedMap.putAll(unsortedMap);
        return sortedMap;
    }


    /* Finds and compiles K nearest neighbors in a LinkedHashMap */
    private LinkedHashMap<String, Double> nearestNeighbors(LinkedHashMap<String, Double> sortedMap) {
        LinkedHashMap<String, Double> kNearest = new LinkedHashMap<>();

        int k = 5;
        Iterator iterator = sortedMap.entrySet().iterator();

        for (int i = 0; i < k; i++) {
            if (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                String entryKey = (String) entry.getKey();
                Double entryValue = Double.parseDouble(entry.getValue().toString());

                kNearest.put(entryKey, entryValue);
            }
        }

        return kNearest;
    }


    /* Finds K nearest neighbors' highest rated recipes to recommend to target user */
    private Map<Integer, Double> gatherRecipes(LinkedHashMap<String, Double> nearestNeighbors) {
        HashMap<Integer, Double> recRecipes = new HashMap<>();
        int recRecipesAmount = 6; //Amount of recipes to be recommended to the user
        int numRecRecipes = 0;
        double expRating;

        Set<String> nearNeighbors = nearestNeighbors.keySet();

        /* For each nearest neighbor, iterate through each user. If user is a NN, iterate through each of their reviewed recipes.
         * If the recipe is not in the list of recommended recipes, calculate expected rating and place in list of recommended recipes */
        for (String nearNeighbor : nearNeighbors) {
            for (User user : users) {
                if (user.getID().equals(nearNeighbor)) {
                    for (Review review : user.getReviews()) {
                        /* Recommend recipe and calculate expected rating */
                        if (!recRecipes.containsKey(review.getRecipeID())) {
                            expRating = expectedRating(review.getRecipeID());

                            if (numRecRecipes < recRecipesAmount) {
                                recRecipes.put(review.getRecipeID(), expRating);
                                numRecRecipes++;
                            }
                        }
                    }
                }
            }
        }
            /* If recRecipesAmount amount of recipes has not been recommended and NN does
             * not have more recipes to recommend, recommend highest rated */
            int numMissingRec = recRecipesAmount - numRecRecipes;
            recRecipes.putAll(recHighlyRated(recRecipes, numMissingRec));

        return recRecipes;
    }


    /* Recommends generally highly rated recipes */
    private Map<Integer, Double> recHighlyRated(HashMap<Integer, Double> recRecipes, int numMissingRec) {
        Map<Integer, Double> highestRated = new HashMap<>();
        LinkedHashMap<Integer, Double> highestRatedRecipes = new LinkedHashMap<>(); //Recipes <recipeID, rating>
        Map<Integer, List<Integer>> recipes = new HashMap<>();

        for (User user : users) {
            for (Review review : user.getReviews()) {
                if (!recRecipes.containsKey(review.getRecipeID())) { //Checks if recipe is already in map of recommended recipes for target user
                    int rating = review.getRating();
                    List<Integer> ratingList = new ArrayList<>();

                    if (recipes.containsKey(review.getRecipeID())){
                        ratingList = recipes.get(review.getRecipeID());
                    }

                    ratingList.add(rating);
                    recipes.put((review.getRecipeID()), ratingList);
                }
            }
        }

        Iterator recipeIt = recipes.entrySet().iterator();
        while (recipeIt.hasNext()){
            Map.Entry recEntry = (Map.Entry) recipeIt.next();
            Integer recipeID = (Integer) recEntry.getKey();
            List<Integer> ratings = (List) recEntry.getValue();

            int sumRatings = 0;

            for (int aRating : ratings){
                sumRatings += aRating;
            }

            double aggregateRating = sumRatings / ratings.size();
            highestRatedRecipes.put((recipeID), aggregateRating);
        }

        TreeMap<Integer, Double> sortedHighRatedRec = sortIntegerDoubleMap(highestRatedRecipes);
        Iterator sortedHighRatedRecIter = sortedHighRatedRec.entrySet().iterator();

        /* Puts missing recommended recipes amount in recommended recipes map */
        for (int i = 0; i < numMissingRec; i++) {
            if (sortedHighRatedRecIter.hasNext()) {
                Map.Entry<Integer, Double> entry = (Map.Entry) sortedHighRatedRecIter.next();
                Integer entryKey = entry.getKey();
                Double entryValue = entry.getValue();
                highestRated.put(entryKey, entryValue);
            }
        }
        return highestRated;
    }


    /* Calculates an expected rating for a given recipe, for a given user */
    private double expectedRating(int recRecipe) {
        double weightedSum = 0;
        double expRating;
        double weightedRating = 0;

        for (User user : users) {
            for (Review review : user.getReviews()){
                if (review.getRecipeID() == recRecipe) {
                    if (target.getSimilarityScores().containsKey(user.getID())) {
                        double similarity = target.getSimilarityScores().get(user.getID());
                        double otherUserRating = review.getRating();

                        weightedRating += similarity * otherUserRating;
                        weightedSum += similarity;
                    }
                }
            }
        }

        expRating = weightedRating / weightedSum; //Expected rating calculation

        return expRating;
    }

}
