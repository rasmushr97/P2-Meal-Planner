package com.example.rasmus.p2app.frontend.ui.settings;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;

public class PrefActivity extends AppBackButtonActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pref);
        setTitle("Preferences");

        ListView listView = findViewById(R.id.preferenceListView);

        String[] items = {"Display Name", "Dislike", "Allergies"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(PrefActivity.this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((adapterView, view, i, l) ->  {
            switch (i){
                case 0:
                    displayNameClicked();
                    break;

                case 1:
                    dislikesClicked();
                    break;

                case 2:
                    allergiesClicked();
                    break;


            }
        });

    }

    private void displayNameClicked(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PrefActivity.this);
        builder.setTitle("Enter new Display Name");
        final EditText input = new EditText(PrefActivity.this);
        input.setText("Rasmus");
        //input.setInputType(weightText.getInputType()); //Decimal input
        builder.setView(input);
        builder.setPositiveButton("OK", (dialog, which) -> {
            Toast.makeText(this, input.getText().toString(), Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void dislikesClicked(){

    }

    private void allergiesClicked(){

    }


}
