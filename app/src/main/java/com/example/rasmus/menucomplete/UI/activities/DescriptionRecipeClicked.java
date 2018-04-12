package com.example.rasmus.menucomplete.UI.activities;

import android.os.Bundle;

import com.example.rasmus.menucomplete.AppBackButtonActivity;
import com.example.rasmus.menucomplete.AppDrawerActivity;
import com.example.rasmus.menucomplete.R;

public class DescriptionRecipeClicked extends AppBackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_recipe_clicked);
        setTitle("Pasta Primavera - Description");
    }
}
