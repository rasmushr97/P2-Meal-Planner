package com.example.rasmus.p2app.frontend.ui.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.adapters.RecyclerViewDataAdapter;
import com.example.rasmus.p2app.frontend.models.SectionDataModel;
import com.example.rasmus.p2app.frontend.models.SingleItemModel;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

import java.util.ArrayList;

public class PickRecipeActivity extends AppBackButtonActivity {

    private Toolbar toolbar;


    /* SectionDataModel klassen er for hver kategori. SingleItemModel klassen er for hver ting inde i hver kategori.
     * Dataen der bliver indlæst er fra createDummyData metoden
     * RecyclerViewDataAdapter*/

    ArrayList<SectionDataModel> allSampleData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_recipe);

        // App bar back button

        //skal kommenteres ind hvis man vil have toolbaren i toppen
        //toolbar = (Toolbar) findViewById(R.id.toolbar);

        allSampleData = new ArrayList<>();

        createDummyData();

        RecyclerView my_recycler_view = findViewById(R.id.my_recycler_view);

        my_recycler_view.setHasFixedSize(true);

        RecyclerViewDataAdapter adapter = new RecyclerViewDataAdapter(this, allSampleData);

        my_recycler_view.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        my_recycler_view.setAdapter(adapter);


    }

    //Metode der siger dataen der skal bruges
    public void createDummyData() {
        int counter = 0;
        for (int i = 1; i <= 5; i++) {

            SectionDataModel dm = new SectionDataModel();

            dm.setHeaderTitle("Category " + i);

            ArrayList<SingleItemModel> singleItem = new ArrayList<>();
            for (int j = 0; j < 20; j++) {
                singleItem.add(new SingleItemModel(counter + ""));
                counter++;
            }

            dm.setAllItemsInSection(singleItem);

            allSampleData.add(dm);
        }
    }

}
