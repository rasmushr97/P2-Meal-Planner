
package com.example.rasmus.p2app.frontend.ui.homeScreenPages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.recipeclasses.Ingredients;
import com.example.rasmus.p2app.backend.time.Calendar;
import com.example.rasmus.p2app.backend.time.Day;
import com.example.rasmus.p2app.backend.time.Meal;
import com.example.rasmus.p2app.frontend.adapters.ShoppingListAdapter;
import com.example.rasmus.p2app.frontend.models.ShoppingListItemModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



public class ShoppingListFragment extends Fragment {

    Calendar calendar;

    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);

        calendar = InRAM.calendar;

        ListView listView = view.findViewById(R.id.listview_shopping_list);

        final List<ShoppingListItemModel> items = new ArrayList<>();
        List<Day> week = calendar.get7DayList();
        List<Ingredients> ingredientList = new ArrayList<>();


        for (Day day : week) {
            for (Meal meal : day.getMeals()) {
                ingredientList.addAll(meal.getRecipe().getIngredients());
            }
        }
        List<Ingredients> shoppingList = new ArrayList<>();
        List<String> stringShoppingList = new ArrayList<>();
        String[] removeWords = {"CHOPPED ", "COOKED ", "SHREDDED ", "UNCOOKED "};
        String[] removeUnits = {"TEASPOON", "TABLESPOON", "CLOVE", "PINCH"};


        for (Iterator<Ingredients> iterator = ingredientList.iterator(); iterator.hasNext(); ) {
            Ingredients ing = iterator.next();
            /* If statements to remove unnecessary ingredients from the shoppinglist */
            if (ing.getName().toUpperCase().contains("SALT") && ing.getName().toUpperCase().contains("PEPPER")) {
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().equals("SALT") || ing.getName().toUpperCase().equals("PEPPER")) {
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().contains("SALT") && ing.getName().toUpperCase().contains("OPTIONAL")){
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().contains("SALT") && ing.getName().toUpperCase().contains("TASTE")){
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().contains("PEPPER") && ing.getName().toUpperCase().contains("OPTIONAL")){
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().contains("BLACK") && ing.getName().toUpperCase().contains("PEPPER")) {
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().equals("WATER") || ing.getName().toUpperCase().contains("WATER")){
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().equals("SALT ") || ing.getName().toUpperCase().equals("SALT")){
                iterator.remove();
                continue;
            } else if (ing.getName().toUpperCase().equals("PEPPER ") || ing.getName().toUpperCase().equals("PEPPER")){
                iterator.remove();
                continue;
            }

            /* Removes the unwanted words */
            for(int i = 0; i < removeWords.length; i++){
                String[] temp = ing.getName().split(" ");
                if(temp[0].toUpperCase().equals(removeWords[i])){
                    ing.setName(ing.getName().replace(removeWords[i], ""));
                }
            }
            /* Removes the unwanted units */
            for(int i = 0; i < removeUnits.length; i++){
                if(ing.getUnit().toUpperCase().contains(removeUnits[i])){
                    ing.setUnit("");
                    ing.setAmount(0);
                }
            }
            /* Cup gets recalculated to milliliter */
            if(ing.getUnit().equals("cup")){
                ing.setAmount(ing.getAmount() * 240);
                ing.setUnit("milliliter");
            }

            /* The units first letter is capitalized */
            if(!ing.getUnit().equals("")) {
                ing.setUnit(ing.getUnit().substring(0, 1).toUpperCase() + ing.getUnit().substring(1));
            }

            /* Removing long decimals from the amount */
            String stringAmount = Double.toString(Math.abs(ing.getAmount()));
            int integerPlaces = stringAmount.indexOf('.');
            int decimalPlaces = stringAmount.length() - integerPlaces - 1;

            /* The max decimals is set to 2, if the amount is #.###... */
            if(decimalPlaces > 2){
                DecimalFormat numberFormat = new DecimalFormat("#.00");
                ing.setAmount(Double.parseDouble(numberFormat.format(ing.getAmount()).replace(",",".")));
            }

            boolean alreadyExist = false;
            /* If the item already exists on the shoppinglist the amount is added up */
            for(Ingredients i : shoppingList){
                if(i.getName().toUpperCase().equals(ing.getName().toUpperCase()) && i.getUnit().equals(ing.getUnit())){
                    i.setAmount(i.getAmount() + ing.getAmount());
                    alreadyExist = true;
                    break;
                }
            }
            /* If its a new item, it is added to the shoppinglist */
            if(!alreadyExist){
                shoppingList.add(ing);
            }
        }

        /* Every item on the shopping list gets turned into a string*/
        for(Ingredients ing : shoppingList){
            String ingredient;
            /* Uppercase the first letter */
            String tempName = ing.getName().substring(0, 1).toUpperCase() + ing.getName().substring(1);

            /* If there is no amount or unit, only the name and parenthesis (if there is one)*/
            if(ing.getAmount() == 0 && ing.getUnit().equals("")){
                if(ing.getInParentheses().equals("")){
                    ingredient = tempName;
                } else {
                    ingredient = tempName + "(" + ing.getInParentheses() + ")";
                }
            } else{ // Otherwise the amount and the unit is added aswell
                if(ing.getInParentheses().equals("")){
                    if(ing.getAmount() == (int)ing.getAmount()) { // #.0 is made to #
                        ingredient = ((int)ing.getAmount()) + " " + ing.getUnit() + " " + tempName;
                    } else{
                        ingredient = ing.getAmount() + " " + ing.getUnit() + " " + tempName;
                    }
                } else {
                    if(ing.getAmount() == (int)ing.getAmount()) { // #.0 is made to #
                        ingredient = ((int)ing.getAmount()) + " " + ing.getUnit() + " " + tempName + "(" + ing.getInParentheses() + ")";
                    } else {
                        ingredient = ing.getAmount() + " " + ing.getUnit() + " " + tempName + "(" + ing.getInParentheses() + ")";
                    }
                }
            }
            /* Item is added to the string shoppinglist */
            stringShoppingList.add(ingredient);
        }

        for(String ing : stringShoppingList){
            items.add(new ShoppingListItemModel(false, ing));
        }

        final ShoppingListAdapter adapter = new ShoppingListAdapter(getActivity(), items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view1, i, l) -> {
            ShoppingListItemModel model = items.get(i);

            if (model.isSelected())
                model.setSelected(false);

            else
                model.setSelected(true);

            items.set(i, model);

            adapter.updateRecords(items);
        });

        return view;
    }
}
