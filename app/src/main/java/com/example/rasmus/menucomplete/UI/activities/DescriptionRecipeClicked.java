package com.example.rasmus.menucomplete.UI.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.rasmus.menucomplete.AppBaseActivity;
import com.example.rasmus.menucomplete.R;

public class DescriptionRecipeClicked extends AppBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_recipe_clicked);
        setTitle("Pasta Primavera - Description");
    }
}
