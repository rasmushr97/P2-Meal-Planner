/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package com.example.rasmus.p2app.frontend.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.other.RecipeTest;

public class HomePageFragment extends Fragment {

    static private int layoutCounter = 0;

    // Temporary
    private RecipeTest recipe = new RecipeTest();

    ScrollView scrollView;
    TextView textCalories;

    int todaysGoal = 2500;
    int todaysCalories = 0;

    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home_page, container, false);

        // Temporary, Adds 3 recipes to the front page, when you open the app for the first time
        initializeTodaysRecipe();

        // Update calorie text
        textCalories = (TextView) view.findViewById(R.id.text_calories);
        updateCalorieText();

        scrollView = (ScrollView) view.findViewById(R.id.scrollView1);


        // Setting up the add button
        final FloatingActionButton fab = view.findViewById(R.id.fab_1);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open the meal pick page (PickMealFragment)
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                Fragment fragment = PickMealFragment.newInstance();
                transaction.replace(R.id.frame_layout1, fragment);
                transaction.commit();
            }
        });


        int prevLayoutCounter = layoutCounter;

        // Draw all todays recipes on the front page
        int startID = R.id.layout_1;
        for (int i = 0; i < recipe.getIdCounter(); i++) {
            // Creating a bundle of information
            Bundle bundle = new Bundle();
            // Create an instance of the MealFragment
            final MealFragment mealFragment = new MealFragment();
            FragmentManager manager = getFragmentManager();
            // Input information to the bundle
            bundle.putInt("img", recipe.getImgID(i));
            bundle.putInt("calories", recipe.getCalories(i));
            bundle.putInt("id", recipe.getIdCounter());
            bundle.putString("meal", recipe.getMealName(i));
            // Pass the bundle of information into the meal fragment
            mealFragment.setArguments(bundle);
            // Replace one of the layouts in fragment_home_page.xml with the MealFragment
            manager.beginTransaction()
                    .replace(startID + i, mealFragment, mealFragment.getTag())
                    .commit();
            // Update the layout counter, so it knows how many meals is on the page
            layoutCounter = i;
        }

        // Scroll down the page after a recipe has been added
        if(prevLayoutCounter != layoutCounter && layoutCounter > 2){
            scrollView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            }, 250);
        }

        return view;
    }


    public void updateCalorieText() {
        // Setting up the text at the top of the front screen
        int sum = 0;
        for (int i = 0; i < recipe.getIdCounter(); i++) {
            sum += recipe.getCalories(i);
        }
        todaysCalories = sum;
        String calories = "Calories: " + todaysCalories + " / " + todaysGoal;
        textCalories.setText(calories);
    }

    public void initializeTodaysRecipe() {
        if (layoutCounter == 0) {
            recipe.addRecipe(R.drawable.img_breakfast, 650, "Breakfast");
            recipe.addRecipe(R.drawable.img_lunch, 760, "Lunch");
            recipe.addRecipe(R.drawable.img_dinner, 1000, "Dinner");
        }
    }

}
