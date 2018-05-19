package com.p2app.frontend.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.p2app.R;
import com.p2app.backend.time.Meal;


import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends ArrayAdapter<Meal> {

    private Context mContext;
    private List<Meal> mealList;

    public CalendarAdapter(@NonNull Context context, List<Meal> list) {
        super(context, 0 , list);
        mContext = context;
        mealList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        // This method decides whats drawn in a single element of the ListView
        // Somewhere this method is loops through in order to draw the entire list view

        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_view_item_calendar, parent, false);
        }

        // Get the meal that i the reference for this element in the ListView
        Meal meal = mealList.get(position);

        // If there is a meal to draw, draw it
        if(meal != null){
            // Draw the meals title on the screen
            TextView tvMeal = (TextView) view.findViewById(R.id.tvMeal);
            tvMeal.setText(meal.getMealName());

            // Draw the recipe's name on the screen
            TextView tvRecipeName = (TextView) view.findViewById(R.id.tvRecipeName);
            tvRecipeName.setText(meal.getRecipe().getTitle());
        }

        return view;
    }
}

