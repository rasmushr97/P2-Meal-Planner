package com.example.rasmus.p2app.frontend.ui.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.adapters.DownloadImageTask;
import com.example.rasmus.p2app.frontend.ui.activities.RecipeClickedActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;


public class MealFragment extends Fragment {
    private String meal = "Breakfast";
    private String image = null;
    private int calories = 0;
    private int id = 0;

    public void setMeal(String meal) {
        this.meal = meal;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_meal, container, false);

        setHasOptionsMenu(true);

        // pick up the bundle sent from MainActivity
        // Use the information to create the title, image and piechart of the Fragment
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            image = bundle.getString("img");
            calories = bundle.getInt("calories");
            id = bundle.getInt("id");
            meal = bundle.getString("meal");
        }

        // Initialization of the text above the picture describing the meal
        TextView mealText = view.findViewById(R.id.text_meal_1);
        mealText.setText(meal);

        // Bring the markup lines to front
        View line1 =  view.findViewById(R.id.h_line_1);
        line1.bringToFront();
        View line2 = view.findViewById(R.id.h_line_2);
        line1.bringToFront();

        // Initialization of the pie chart beside the picture describing the meal
        PieChart chart = view.findViewById(R.id.home_chart_1);
        List<PieEntry> pieChartEntries = new ArrayList<>();

        int goalCalories = 2500 - calories;
        pieChartEntries.add(new PieEntry(calories, "Meal"));
        pieChartEntries.add(new PieEntry(goalCalories, "Total"));
        setupPieChart(chart, pieChartEntries);

        // Initialization of the recipe's image
        ImageButton imgbtn = view.findViewById(R.id.imgbtn_meal_1);
        new DownloadImageTask((ImageButton) view.findViewById(R.id.imgbtn_meal_1)).execute(image);


        // Image button OnClickListener
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Write code that just gives the id to RecipeClickedActivity
                Intent intent = new Intent(getActivity(), RecipeClickedActivity.class);
                intent.putExtra("delete", true);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        return view;
    }


    private void setupPieChart(PieChart chart, List<PieEntry> entryList) {
        // Colors scheme for the pie chart
        final int[] MY_COLORS = {Color.rgb(0, 100, 0), Color.rgb(0, 175, 0)};
        ArrayList<Integer> colors = new ArrayList<>();
        for (int c : MY_COLORS) colors.add(c);

        // Create dataset for the pie chart
        PieDataSet dataSet = new PieDataSet(entryList, "Calories");

        // Applying color scheme
        dataSet.setColors(colors);
        dataSet.setDrawValues(false);


        // Bundle the dataset together
        PieData data = new PieData(dataSet);
        // Change font size


        // Use data in chart
        chart.setData(data);
        // Cool starting animation for the creation of the pie chart
        chart.animateY(750, Easing.EasingOption.EaseInOutQuad);
        String centerText = calories + " / " + 2500;
        chart.setCenterText(centerText);
        chart.setCenterTextColor(Color.WHITE);

        // middle hole color
        chart.setHoleColor(Color.TRANSPARENT);

        // Some default design features that are disabled
        chart.setTransparentCircleAlpha(0);
        chart.getDescription().setEnabled(false);
        chart.getLegend().setEnabled(false);
        chart.setRotationEnabled(false);
        chart.setEntryLabelTextSize(12f);

        chart.invalidate();
    }

}
