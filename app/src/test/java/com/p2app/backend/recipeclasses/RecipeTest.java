package com.p2app.backend.recipeclasses;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RecipeTest {

    public Recipe createRecipe(int ID){
        CookTime cookTime = new CookTime("1H", "20M", "1H20M");
        List<String> categories = new ArrayList<>();
        categories.add("category1");
        categories.add("category2");
        List<String> directions = new ArrayList<>();
        directions.add("first direction");
        directions.add("second direction");
        List<Ingredient> ingredients = new ArrayList<>();
        ingredients.add(new Ingredient(1, "ingredient", 3, "lites", null));
        List<Review> reviews = new ArrayList<>();
        reviews.add(new Review(1, "review", "name", "submitterID", 4, ID));

        Recipe recipe = new Recipe(ID, "Test Recipe", "Submitter name", "picturelink",
                "weblink", "Description", 4, 700, 4.5, cookTime, categories,
                ingredients, directions, reviews);
        return recipe;
    }

    @Test
    public void uniqueRecipeTest01(){
        Recipe recipe1 = createRecipe(1);
        Recipe recipe2 = createRecipe(2);
        assertNotEquals(recipe1, recipe2);
    }

    @Test
    public void sameRecipeTest01(){
        Recipe recipe1 = createRecipe(1);
        Recipe recipe2 = new Recipe();
        recipe2.setTitle(recipe1.getTitle());
        recipe2.setCalories(recipe1.getCalories());
        recipe2.setCategories(recipe1.getCategories());
        recipe2.setDescription(recipe1.getDescription());
        recipe2.setDirections(recipe1.getDirections());
        recipe2.setID(recipe1.getID());
        recipe2.setIngredients(recipe1.getIngredients());
        recipe2.setPictureLink(recipe1.getPictureLink());
        recipe2.setReviews(recipe1.getReviews());
        recipe2.setRating(recipe1.getRating());
        recipe2.setSubmitterName(recipe1.getSubmitterName());
        recipe2.setWebsiteLink(recipe1.getWebsiteLink());
        recipe2.setTime(recipe1.getTime());
        recipe2.setServings(recipe1.getServings());
        assertEquals(recipe1, recipe2);
    }



}