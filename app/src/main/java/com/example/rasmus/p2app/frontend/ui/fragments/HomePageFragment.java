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

import android.content.Intent;
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
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.frontend.other.MealCompare;
import com.example.rasmus.p2app.frontend.ui.activities.PickMealActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class HomePageFragment extends Fragment {


    ScrollView scrollView;
    TextView textCalories;

    int todaysGoal = 0;
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

        /* Sets todays goal to the calculated amount of calories */
        InRAM.user.getGoal().calcCaloriesPerDay(InRAM.user);
        todaysGoal = InRAM.user.getCaloriesPerDay();

        // Update calorie text
        textCalories = (TextView) view.findViewById(R.id.text_calories);
        // Default text
        updateCalorieText();

        scrollView = (ScrollView) view.findViewById(R.id.scrollView1);


        // Setting up the add button
        final FloatingActionButton fab = view.findViewById(R.id.fab_1);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Open the meal pick page (PickMealActivity)
                Intent intent = new Intent(getActivity(), PickMealActivity.class);
                String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                intent.putExtra("date", date);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }
        });


        return view;
    }


    public int instantiateMeals() {
        // Draw all todays recipes on the front page
        Day today = InRAM.today;

        if (today != null) {
            ArrayList<Meal> todaysMeals = new ArrayList<>(InRAM.today.getMeals());



            Collections.sort(todaysMeals, new MealCompare());
            int startID = R.id.layout_1;

            int counter = 0;
            for (Meal m : todaysMeals) {
                // Creating a bundle of information
                Bundle bundle = new Bundle();

                Recipe recipe = m.getRecipe();

                // Input information to the bundle
                bundle.putInt("calories", recipe.getCalories());
                bundle.putInt("id", recipe.getID());
                bundle.putString("meal", m.getMealName());
                bundle.putString("img", recipe.getPictureLink());

                // Create an instance of the MealFragment
                final MealFragment mealFragment = new MealFragment();
                FragmentManager manager = getFragmentManager();


                // Pass the bundle of information into the meal fragment
                mealFragment.setArguments(bundle);
                // Replace one of the layouts in fragment_home_page.xml with the MealFragment
                manager.beginTransaction()
                        .replace(startID + counter, mealFragment, mealFragment.getTag())
                        .commit();

                counter++;
            }


            return todaysMeals.size();
        }

        return 0;
    }


    public void updateCalorieText() {
        Day today = InRAM.today;

        if (today != null) {
            todaysCalories = today.getCalorieSum();
            String calories = "Calories: " + todaysCalories + " / " + todaysGoal;
            textCalories.setText(calories);
        } else {
            textCalories.setText("No recipes found");
        }
    }


}

