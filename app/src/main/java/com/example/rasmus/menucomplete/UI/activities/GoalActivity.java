package com.example.rasmus.menucomplete.UI.activities;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rasmus.menucomplete.AppBackButtonActivity;
import com.example.rasmus.menucomplete.R;
import com.example.rasmus.menucomplete.UI.fragments.ChartFragment;
import com.example.rasmus.menucomplete.other.Storage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;

public class GoalActivity extends AppBackButtonActivity {
    //TODO Do stuff on notepad

    private static final String TAG = "GoalActivity";
    private Button goalButton;
    private TextView goalEdit;
    private LineChart mChart;
    static Storage storage = new Storage();
    private int userWeight;
    private Button enterWeightButton;
    private EditText weightText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        setTitle("Weight control");
        storage.intialize();
        refreshGraph();

        goalButton = findViewById(R.id.enterGoalWeight);
        goalEdit = findViewById(R.id.goalText);
        //goalEdit.setEnabled(false);

        if(storage.getGoalWeightValue() == 0){
            goalEdit.setText("No Goal Set");
            goalButton.setText("Set Goal");
        }
        else{
            goalEdit.setText(storage.getGoalWeightValue() + "kg");
        }

        goalButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                        builder.setTitle("Enter Goal Weight");

                        final EditText input = new EditText(GoalActivity.this);

                        input.setInputType(InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!input.getText().toString().equals("")) {
                                    storage.setGoalWeightValue(Integer.valueOf(input.getText().toString()));
                                    storage.goalWeight.add(new Entry(5 + storage.getGoalWeightValue(), storage.getGoalWeightValue()));
                                    String text = storage.getGoalWeightValue() + " kg";
                                    goalEdit.setText(text);
                                    refreshGraph();
                                }
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                });

        weightText = findViewById(R.id.weightText);
        enterWeightButton = findViewById(R.id.enterWeight);
        enterWeightButton.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!weightText.getText().toString().equals("")) {
                    userWeight = Integer.valueOf(weightText.getText().toString());
                    weightText.setText("" + userWeight);
                    storage.userWeight.add(new Entry(userWeight + 3, userWeight));
                    refreshGraph();
                }
            }
        });


    }

    // Makes the three dots in upper right corner
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.goal_settings,menu);

        return true;
    }

    // Adds functionality to the three dots in upper right corner :)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id){
            case R.id.undo_weight:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            //Yes button clicked
                            case DialogInterface.BUTTON_POSITIVE:
                                break;

                            //No button clicked
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                builder.setMessage("You are about to delete your last weight which was...?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
        }

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item);
    }

    public void refreshGraph() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.chart_layout_replacer, ChartFragment.newInstance()); // newInstance() is a static factory method.
        transaction.commit();
    }
}

