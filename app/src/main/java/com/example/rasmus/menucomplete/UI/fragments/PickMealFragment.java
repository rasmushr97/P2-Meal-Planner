package com.example.rasmus.menucomplete.UI.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.rasmus.menucomplete.R;
import com.example.rasmus.menucomplete.UI.activities.PickRecipeActivity;
import com.example.rasmus.menucomplete.other.RecipeTest;

/**
 * A simple {@link Fragment} subclass.
 */
public class PickMealFragment extends Fragment {
    ListView listView;
    private RecipeTest recipe = new RecipeTest();

    public static PickMealFragment newInstance() {
        PickMealFragment fragment = new PickMealFragment();
        return fragment;
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
        listView = (ListView) view.findViewById(R.id.listview1);

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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
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
                Intent intent = new Intent(getActivity(), PickRecipeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putString("meal", values[position]);

                switch (position) {
                    case 0:
                        recipe.addRecipe(R.drawable.breakfast, 650, "Breakfast");
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;

                    case 1:
                        recipe.addRecipe(R.drawable.lunch, 760, "Lunch");
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;

                    case 2:
                        recipe.addRecipe(R.drawable.dinner, 1000, "Dinner");
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                        break;

                    default:
                        // Something went wrong
                }
            }

        });

        return view;
    }

}

