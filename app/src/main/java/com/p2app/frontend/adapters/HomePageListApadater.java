package com.p2app.frontend.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;


import com.p2app.frontend.ui.recipePages.RecipeClickedActivity;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.p2app.R;
import com.p2app.backend.InRAM;
import com.p2app.backend.recipeclasses.Recipe;
import com.p2app.backend.time.Meal;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HomePageListApadater extends ArrayAdapter {
    private Context mContext;
    private List<Meal> mealList = new ArrayList<>();

    public HomePageListApadater(@NonNull Context context, List<Meal> list) {
        super(context, 0, list);
        mContext = context;
        mealList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        // Works the same as the calendarAdapter

        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_view_item_meal, parent, false);
        }

        // Get the meal which corresponds to this position in the ListView
        Meal meal = mealList.get(position);

        if (meal == null) {
            return view;
        }

        // Initialization of the text above the picture describing the meal
        TextView mealText = view.findViewById(R.id.text_meal_1);
        mealText.setText(meal.getMealName());

        // Bring the markup lines to front
        View line1 = view.findViewById(R.id.h_line_1);
        line1.bringToFront();
        View line2 = view.findViewById(R.id.h_line_2);
        line1.bringToFront();

        // Initialization of the pie chart beside the picture describing the meal
        PieChart chart = view.findViewById(R.id.home_chart_1);
        List<PieEntry> pieChartEntries = new ArrayList<>();

        Recipe recipe = meal.getRecipe();

        int goalCalories = InRAM.user.getCaloriesPerDay() - recipe.getCalories();
        pieChartEntries.add(new PieEntry(recipe.getCalories(), "Meal"));
        pieChartEntries.add(new PieEntry(goalCalories, "Total"));

        setupPieChart(chart, pieChartEntries, recipe.getCalories());

        // Initialization of the recipe's image
        ImageButton imgbtn = view.findViewById(R.id.imgbtn_meal_1);
        new DownloadImageTask((ImageButton) view.findViewById(R.id.imgbtn_meal_1)).execute(recipe.getPictureLink());


        // Image button OnClickListener
        imgbtn.setOnClickListener(v -> {
            // Write code that just gives the id to RecipeClickedActivity
            Intent intent = new Intent(v.getContext(), RecipeClickedActivity.class);
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("d/M/yyyy"));
            intent.putExtra("delete", true);
            intent.putExtra("id", recipe.getID());
            intent.putExtra("date", date);
            getContext().startActivity(intent);
        });

        return view;
    }

    private void setupPieChart(PieChart chart, List<PieEntry> entryList, int calories) {
        // Colors scheme for the pie chart
        final int[] MY_COLORS = {Color.rgb(240, 185, 98), Color.rgb(149, 199, 105)};
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
        chart.animateY(250, Easing.EasingOption.EaseInOutQuad);
        String centerText = calories + " / " + InRAM.user.getCaloriesPerDay();
        chart.setCenterText(centerText);
        chart.setCenterTextColor(Color.parseColor("#333333"));

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

