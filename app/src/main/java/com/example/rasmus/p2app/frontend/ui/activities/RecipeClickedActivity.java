package com.example.rasmus.p2app.frontend.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.Ingredients;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.frontend.adapters.DownloadImageTask;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeClickedActivity extends AppBackButtonActivity {

    private int recipeID;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_clicked);
        setTitle("Recipe Picked");

        recipeID = getIntent().getExtras().getInt("id");
        recipe = InRAM.recipesInRAM.get(recipeID);

        new DownloadImageTask((ImageView) findViewById(R.id.RecipeImg)).execute(recipe.getPictureLink());

        TextView title = findViewById(R.id.recipeTitel);
        title.setText(recipe.getTitle());

        TextView calories = findViewById(R.id.RecipeCalories);
        String calorieText = "Calories: " + recipe.getCalories();
        calories.setText(calorieText);

        TextView time = findViewById(R.id.RecipeCookTime);
        String timeText = "Time: " + recipe.getTime().getReadyIn();
        time.setText(timeText);

        TextView ingredients = findViewById(R.id.RecipeItems);
        String ingredientsText = "";
        for(Ingredients ingredient : recipe.getIngredients()){
            String inParentheses = "";
            if(!ingredient.getInParentheses().equals("")){
                inParentheses = "(" + ingredient.getInParentheses() + ")";
            }
            ingredientsText = ingredientsText.concat(ingredient.getName() + " " + inParentheses + "\nAmount: " + ingredient.getAmount() + " " + ingredient.getUnit() + "\n\n");
        }
        ingredients.setText(ingredientsText);


        // App bar back button and onClickListener
        Button button = findViewById(R.id.description_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Switches to the Description clicked page (activity)
                Intent intent = new Intent(RecipeClickedActivity.this, DescriptionRecipeClickedActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

        // Add recipe button and onClickListener
        Button addRecipeButton = findViewById(R.id.btn_add_recipe);
        addRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Find the meal with a recipe that is null
                Map<LocalDate, Day> addesDays = InRAM.addedDays.getDates();

                // when found input the chosen recipe
                for(Day day : InRAM.addedDays.getDates().values()){
                    for(Meal meal : day.getMeals()){
                        if(meal.getRecipe() == null){
                            meal.setRecipe(InRAM.recipesInRAM.get(recipeID));
                        }
                    }
                }

                InRAM.syncCalender();

                // Switches back to the home page (activity)
                Intent intent = new Intent(RecipeClickedActivity.this, MainActivity.class);
                // Clears all previous activities from the stack
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });

    }

}
