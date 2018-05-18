package com.example.rasmus.p2app.frontend.ui.recipePages;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.Recommender;
import com.example.rasmus.p2app.backend.recipeclasses.Recipe;
import com.example.rasmus.p2app.backend.recipeclasses.Review;
import com.example.rasmus.p2app.frontend.adapters.RecyclerViewDataAdapter;
import com.example.rasmus.p2app.frontend.models.SectionDataModel;
import com.example.rasmus.p2app.frontend.models.SingleItemModel;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedMap;
import java.util.TreeMap;

public class PickRecipeActivity extends AppBackButtonActivity {

    private Toolbar toolbar;



    /* SectionDataModel klassen er for hver kategori. SingleItemModel klassen er for hver ting inde i hver kategori.
     * Dataen der bliver indlæst er fra createDummyData metoden
     * RecyclerViewDataAdapter*/

    List<SectionDataModel> allSampleData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);
        setTitle("Recipe Explorer");


        loadRecipes();

        allSampleData = createModelData();

        RecyclerView my_recycler_view = findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);


    }


    public List<SectionDataModel> createModelData() {

        List<SectionDataModel> sampleData = new ArrayList<>();

        List<List<Integer>> IDlist = InRAM.recipeIDsForExplorer;

        int i = 0;
        for (List<Integer> list : IDlist) {

            SectionDataModel dm = new SectionDataModel();
            dm.setHeaderTitle(InRAM.sectionNames.get(i));

            ArrayList<SingleItemModel> singleItem = new ArrayList<>();
            for (int recipeID : list) {
                singleItem.add(new SingleItemModel(recipeID));
            }

            dm.setAllItemsInSection(singleItem);

            sampleData.add(dm);
            i++;
        }

        return sampleData;
    }

    private void loadRecipes() {
        InRAM.recipeIDsForExplorer = new ArrayList<>();
        List<Recipe> recipes = new ArrayList<>(InRAM.recipesInRAM.values());

        // Use the recommender system to find recipes
        Recommender recommender = new Recommender(InRAM.user, InRAM.allUsers, recipes);
        recommender.recommendRecipe();
        InRAM.recipeIDsForExplorer.add(new ArrayList<>(InRAM.user.getRecommendedRecipes().keySet()));
        InRAM.sectionNames.add("Recommended Recipes");


        // Find the users most used tags

        Map<String, Integer> tags = new HashMap<>();

        for (Review r : InRAM.user.getReviews()) {
            int recipeID = r.getRecipeID();
            List<String> categories = InRAM.recipesInRAM.get(recipeID).getCategories();
            for (String s : categories) {
                if (tags.containsKey(s)) {
                    int count = tags.get(s) + 1;
                    tags.put(s, count);
                } else {
                    tags.put(s, 1);
                }
            }
        }
        tags = sortByValue(tags);

        // Take the users 3 most popular tags and save them
        List<String> tagList = new ArrayList<>();
        int i = 0;
        for (String s : tags.keySet()) {
            if (i < 3) {
                tagList.add(s);
            } else {
                break;
            }
            i++;
        }
        InRAM.sectionNames.addAll(tagList);




        // Find all recipes using those 3 tags
        List<List<Integer>> tagList2d = new ArrayList<>();
        tagList2d.add(new ArrayList<>());
        tagList2d.add(new ArrayList<>());
        tagList2d.add(new ArrayList<>());

        List<Recipe> recipeList = new ArrayList<>(InRAM.recipesInRAM.values());
        for (Recipe recipe : recipeList) {
            if (recipe != null) {
                List<String> categories = recipe.getCategories();
                if(categories != null) {
                    for (String string : categories) {
                        for (int j = 0; j < tagList.size(); j++) {
                            if (tagList.get(j).equals(string)) {
                                tagList2d.get(j).add(recipe.getID());
                            }
                        }
                    }
                }
            }
        }

        // Reduce the number of recipes randomly, and add them to InRAM
        for(List<Integer> ids : tagList2d){
            reduceList(ids, 15);
            InRAM.recipeIDsForExplorer.add(ids);
        }

    }


    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
                return (e2.getValue()).compareTo(e1.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    private void reduceList(List<Integer> recipeIDs, int reduceTo){
        Random rand = new Random();
        while (recipeIDs.size() > reduceTo){
            recipeIDs.remove(rand.nextInt(recipeIDs.size()));
        }
    }

}
