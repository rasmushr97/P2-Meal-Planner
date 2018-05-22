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

package com.p2app.frontend.ui.homeScreenPages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.p2app.frontend.ui.recipePages.PickMealActivity;
import com.p2app.R;
import com.p2app.backend.comparators.MealCompare;
import com.p2app.backend.InRAM;
import com.p2app.backend.time.Day;
import com.p2app.backend.time.Meal;
import com.p2app.frontend.adapters.HomePageListApadater;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomePageFragment extends Fragment {

    private TextView textCalories;
    private ListView lvMeals;

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
        todaysGoal = InRAM.user.getCaloriesPerDay();

        // Update calorie text
        textCalories = (TextView) view.findViewById(R.id.text_calories);
        // Default text
        updateCalorieText();


        // Setting up the add button
        final FloatingActionButton fab = view.findViewById(R.id.fab_1);
        fab.setOnClickListener(v -> {
            // Open the meal pick page (PickMealActivity)
            Intent intent = new Intent(getActivity(), PickMealActivity.class);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            intent.putExtra("date", date);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });


        // Draw all todays recipes on the front page
        lvMeals = view.findViewById(R.id.lvMeals);
        Day today = InRAM.calendar.getDay(LocalDate.now());

        List<Meal> todaysMeals = new ArrayList<>();
        if (today != null) {
            todaysMeals = new ArrayList<>(today.getMeals());
            todaysMeals.sort(new MealCompare());
        }

        final HomePageListApadater adapter = new HomePageListApadater(getActivity(), todaysMeals);
        lvMeals.setAdapter(adapter);

        return view;
    }


    public void updateCalorieText() {
        Day today = InRAM.calendar.getDay(LocalDate.now());

        if (today != null) {
            todaysCalories = today.getCalorieSum();
            String calories = "Calories: " + todaysCalories + " / " + todaysGoal;
            textCalories.setText(calories);
        } else {
            textCalories.setText("No recipes found");
        }
    }

}

