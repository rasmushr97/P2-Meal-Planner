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

package com.example.rasmus.menucomplete.UI.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rasmus.menucomplete.R;
import com.example.rasmus.menucomplete.other.DateTest;
import com.example.rasmus.menucomplete.other.FoodChoice;

import java.util.ArrayList;
import java.util.List;

public class CalenderFragment extends Fragment {
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

        //default
        breakfastDyn.setText(R.string.default_food);
        lunchDyn.setText(R.string.default_food);
        dinnerDyn.setText(R.string.default_food);

        final List<DateTest> dateTests = new ArrayList<>();
        ArrayList<FoodChoice> Choice = new ArrayList<FoodChoice>();

        dateTests.add(new DateTest("21/4/2018", "Cake", "Sandwich", "Lasagne"));

        breakfastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FoodChoice breakfast = new FoodChoice("Breakfast chosen");

            }
        });

        lunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //lunch
            }
        });

        dinnerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dinner
            }
        });


        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                month += 1;

                Toast.makeText(getContext(),
                        dayOfMonth +"/"+month+"/"+ year,Toast.LENGTH_SHORT).show();

                String clickedDate = dayOfMonth +"/"+month+"/"+ year;
                for(DateTest d : dateTests){
                    if (d.date.equals(clickedDate)){
                        System.out.println("hello");
                        breakfastDyn.setText(d.getBreakfastText());
                        lunchDyn.setText(d.getLunchText());
                        dinnerDyn.setText(d.getDinnerText());
                    }
                }

            }});




        return view;
    }
}
