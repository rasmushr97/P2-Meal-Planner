package com.example.rasmus.menucomplete.UI.activities;

import android.os.Bundle;
import android.widget.TextView;

import com.example.rasmus.menucomplete.AppBackButtonActivity;
import com.example.rasmus.menucomplete.AppDrawerActivity;
import com.example.rasmus.menucomplete.R;

public class DescriptionRecipeClickedActivity extends AppBackButtonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_recipe_clicked);
        setTitle("Pasta Primavera - Description");


        TextView descriptionText = (TextView) findViewById(R.id.description_text);
        //descriptionText.setText("Hello");     input description text here
    }
}
