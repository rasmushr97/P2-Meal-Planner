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

package com.example.rasmus.p2app.frontend.ui.homeScreenPages;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.frontend.adapters.CalendarAdapter;
import com.example.rasmus.p2app.backend.Comparators.MealCompare;
import com.example.rasmus.p2app.frontend.ui.recipePages.PickMealActivity;
import com.example.rasmus.p2app.frontend.ui.recipePages.RecipeClickedActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CalenderFragment extends Fragment {
    private Calendar calend;
    private View view;
    private ListView lvCalendar;
    private FloatingActionButton fab;

    public static CalenderFragment newInstance() {
        CalenderFragment fragment = new CalenderFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_calender, container, false);

        final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);
        lvCalendar = view.findViewById(R.id.lvCalendar);

        setFabListener(LocalDate.now());

        calend = InRAM.calendar;

        Day today = calend.getDay(LocalDate.now());

        List<Meal> todaysMeals = new ArrayList<>();
        if (today != null) {
            todaysMeals = new ArrayList<>(today.getMeals());
            todaysMeals.sort(new MealCompare());
        }
        drawMeals(todaysMeals, LocalDate.now());

        calendar.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            month += 1;

            Toast.makeText(getContext(),
                    dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();

            String clickedDate = dayOfMonth + "/" + month + "/" + year;
            LocalDate date = LocalDate.parse(clickedDate, DateTimeFormatter.ofPattern("d/M/yyyy"));
            setFabListener(date);

            Day day = calend.getDay(date);

            List<Meal> mealList = new ArrayList<>();
            if (day != null) {
                mealList = new ArrayList<>(day.getMeals());
                mealList.sort(new MealCompare());
            }
            drawMeals(mealList, date);
        });

        return view;
    }


    private void drawMeals(List<Meal> meals, LocalDate date) {

        meals.add(new Meal("", new Recipe()));

        final CalendarAdapter adapter = new CalendarAdapter(getContext(), meals);
        lvCalendar.setAdapter(adapter);

        lvCalendar.setOnItemClickListener((parent, view1, i, l) -> {
            Meal meal = meals.get(i);
            String dateString = date.format(DateTimeFormatter.ofPattern("d/M/yyyy"));

            Intent intent = new Intent(getActivity(), RecipeClickedActivity.class);

            intent.putExtra("delete", true);
            intent.putExtra("id", meal.getRecipe().getID());
            intent.putExtra("date", dateString);

            getActivity().startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

        });
    }

    private void setFabListener(LocalDate date){
        fab = view.findViewById(R.id.fabCalendar);
        fab.setOnClickListener(v ->  {
            Intent intent = new Intent(getActivity(), PickMealActivity.class);
            String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            intent.putExtra("date", formattedDate);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        });
    }

}
