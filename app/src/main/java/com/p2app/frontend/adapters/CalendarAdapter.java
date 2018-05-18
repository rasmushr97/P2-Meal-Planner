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
    private List<Meal> mealList = new ArrayList<>();

    public CalendarAdapter(@NonNull Context context, List<Meal> list) {
        super(context, 0 , list);
        mContext = context;
        mealList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        if(view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.list_view_item_calendar, parent, false);
        }

        Meal meal = mealList.get(position);

        if(meal != null){
            TextView tvMeal = (TextView) view.findViewById(R.id.tvMeal);
            tvMeal.setText(meal.getMealName());

            TextView tvRecipeName = (TextView) view.findViewById(R.id.tvRecipeName);
            tvRecipeName.setText(meal.getRecipe().getTitle());
        }

        return view;
    }
}

