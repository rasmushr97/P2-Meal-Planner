package com.example.rasmus.p2app.frontend.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickMealActivity extends AppBackButtonActivity {
    ListView listView;
    LocalDate date = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_meal);
        setTitle("Pick Meal");

        if (getIntent().getExtras() != null) {
            String dateInput = getIntent().getExtras().getString("date");
            date = LocalDate.parse(dateInput, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        // Get ListView object from xml
        listView = findViewById(R.id.listview1);

        // Defined Array values to show in ListView
        final String[] values = {
                "Breakfast",
                "Lunch",
                "Dinner",
                "Other"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                // Set the intention for which page to switch to
                Intent intent = new Intent(PickMealActivity.this, PickRecipeActivity.class);

                Meal meal = new Meal(values[position], null);
                Day day1 = new Day();
                day1.addMeal(meal);

                // TODO: this looks stupid, refactor
                Calendar calendar = InRAM.addedDays;
                Map<LocalDate, Day> dates = calendar.getDates();
                if (dates != null) {
                    if (dates.containsKey(date)) {
                        Day day = dates.get(date);
                        day.addMeal(meal);
                    } else {
                        calendar.getDates().put(date, day1);
                    }

                } else {
                    calendar.addDay(date, day1);
                }


                // Switch to the pick recipe page (PickRecipeActivity)
                startActivity(intent);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }

        });

    }


}

