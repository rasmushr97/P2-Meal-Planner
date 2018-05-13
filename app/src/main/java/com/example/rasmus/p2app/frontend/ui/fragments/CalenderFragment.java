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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CalenderFragment extends Fragment {
    Calendar c;


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

        View view = inflater.inflate(R.layout.fragment_calender, container, false);

        final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);
        final Button breakfastButton = view.findViewById(R.id.breakfastButton);
        final Button lunchButton = view.findViewById(R.id.lunchButton);
        final Button dinnerButton = view.findViewById(R.id.dinnerButton);
        final TextView breakfastDyn = view.findViewById(R.id.breakfastDyn);
        final TextView lunchDyn = view.findViewById(R.id.lunchDyn);
        final TextView dinnerDyn = view.findViewById(R.id.dinnerDyn);

        c = InRAM.calendar;

        //default
        breakfastDyn.setText(R.string.default_food);
        lunchDyn.setText(R.string.default_food);
        dinnerDyn.setText(R.string.default_food);


        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FoodChoice img_breakfast = new FoodChoice("Breakfast chosen");
                Toast.makeText(getContext(), "Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });
        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img_lunch
            }
        });
        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //img_dinner
            }
        });


            final Day today = c.getDay(LocalDate.now());
            int i = 0;
            if(today != null) {
                for (Meal meal : today.getMeals()) {
                    switch (i) {
                        case 0:
                            breakfastDyn.setText(meal.getRecipe().getTitle());
                            break;

                        case 1:
                            lunchDyn.setText(meal.getRecipe().getTitle());
                            break;

                        case 2:
                            dinnerDyn.setText(meal.getRecipe().getTitle());
                            break;
                    }

                    i++;
                }
            }


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;

                Toast.makeText(getContext(),
                        dayOfMonth + "/" + month + "/" + year, Toast.LENGTH_SHORT).show();

                String clickedDate = dayOfMonth + "/" + month + "/" + year;
                LocalDate date = LocalDate.parse(clickedDate, DateTimeFormatter.ofPattern("d/M/yyyy"));


                // TODO: create fragment instead

                Day clickedDay = c.getDates().get(date);
                if (clickedDay != null) {
                    breakfastDyn.setText("No meal found");
                    lunchDyn.setText("No meal found");
                    dinnerDyn.setText("No meal found");

                    int i = 0;
                    for (Meal meal : clickedDay.getMeals()) {
                        switch (i) {
                            case 0:
                                breakfastDyn.setText(meal.getRecipe().getTitle());
                                break;

                            case 1:
                                lunchDyn.setText(meal.getRecipe().getTitle());
                                break;

                            case 2:
                                dinnerDyn.setText(meal.getRecipe().getTitle());
                                break;
                        }

                        i++;
                    }


                } else {
                    breakfastDyn.setText("No meal found");
                    lunchDyn.setText("No meal found");
                    dinnerDyn.setText("No meal found");
                }


            }
        });


        return view;
    }
}
