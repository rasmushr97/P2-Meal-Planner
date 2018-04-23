package com.example.rasmus.p2app.frontend.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.ui.activities.PickRecipeActivity;
import com.example.rasmus.p2app.frontend.other.RecipeTest;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickMealFragment extends Fragment {
    ListView listView;
    private RecipeTest recipe = new RecipeTest();

    public static PickMealFragment newInstance() {
        return new PickMealFragment();
    }

    public PickMealFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pick_meal, container, false);


        // Get ListView object from xml
        listView = view.findViewById(R.id.listview1);

        // Defined Array values to show in ListView
        final String[] values = new String[]{
                "Breakfast",
                "Lunch",
                "Dinner"
        };

        // Define a new Adapter
        // First parameter - Context
        // Second parameter - Layout for the row
        // Third parameter - ID of the TextView to which the data is written
        // Forth - the Array of data

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, values);


        // Assign adapter to ListView
        listView.setAdapter(adapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                // Set the intention for which page to switch to
                Intent intent = new Intent(getActivity(), PickRecipeActivity.class);
                // Create a bundle of information to pass with the page switch
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("meal", values[position]);

                switch (position) {
                    case 0:
                        // Adding the recipe here is temporary
                        recipe.addRecipe(R.drawable.img_breakfast, 650, "Breakfast");
                        break;

                    case 1:
                        recipe.addRecipe(R.drawable.img_lunch, 760, "Lunch");
                        break;

                    case 2:
                        recipe.addRecipe(R.drawable.img_dinner, 1000, "Dinner");
                        break;

                    default:
                        // Something went wrong
                }

                // Switch to the pick recipe page (PickRecipeActivity)
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);

            }

        });

        return view;
    }

}

