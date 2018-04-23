package com.example.rasmus.p2app.frontend.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.other.RecipeTest;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

public class RecipeClickedActivity extends AppBackButtonActivity {

    RecipeTest recipe = new RecipeTest();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_clicked);
        setTitle("Den Valgte Opskrift");

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
