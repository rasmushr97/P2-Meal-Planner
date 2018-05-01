package com.example.rasmus.p2app.frontend.ui.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;
import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.ui.fragments.ChartFragment;
import com.example.rasmus.p2app.frontend.other.Storage;
import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;

public class GoalActivity extends AppBackButtonActivity {

    private static final String TAG = "GoalActivity";
    private Button goalButton;
    private TextView goalEdit;
    static Storage storage = new Storage();
    private Button enterWeightButton;
    private EditText weightText;
    private LocalUser localUser = new LocalUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        setTitle("Weight control");
        localUser.setGoalWeight(75);
        localUser.setAge(25);
        localUser.setHeight(180);
        localUser.setWeight(80);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,1,1), (float) 80);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,1,9), (float) 79.6);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,1,17), (float) 78);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,2,1), (float) 76);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,3,9), (float) 73.4);
        localUser.getGoal().addUserWeight(LocalDate.of(2018,4,1), (float) 65);
        storage.initializeWeight(localUser);
        storage.initializeGoal(localUser);
        refreshGraph();

        goalButton = findViewById(R.id.enterGoalWeight);
        goalEdit = findViewById(R.id.goalText);

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

                        input.setInputType(weightText.getInputType());
                        builder.setView(input);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(!input.getText().toString().equals("")) {
                                    localUser.setGoalWeight(Float.valueOf(input.getText().toString()));
                                    goalEdit.setText(localUser.getGoalWeight() + " kg");
                                    storage.initializeGoal(localUser);
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
                    localUser.setWeight(Float.valueOf(weightText.getText().toString()));
                    weightText.setText("" + localUser.getWeight());
                    localUser.getGoal().addUserWeight(LocalDate.now(), (float) localUser.getWeight());
                    hideKeyboard(GoalActivity.this);
                    storage.initializeGoal(localUser);
                    storage.initializeWeight(localUser);
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
                LocalDate lastMeasurement = localUser.getGoal().getLastDate(localUser.getGoal().getUserWeight());
                Object lastWeight = localUser.getGoal().getUserWeight().get(lastMeasurement);
                builder.setMessage("You are about to delete your last weight measurement: " +
                        "\nWeight: " + lastWeight.toString() + " kg" +
                        "\nTime: " + lastMeasurement);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        localUser.getGoal().getUserWeight().remove(lastMeasurement, lastWeight);
                        storage.initializeGoal(localUser);
                        storage.initializeWeight(localUser);
                        refreshGraph();
                    }
                });
                builder.setNegativeButton("NO", dialogClickListener).show();
                break;
        }

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return super.onOptionsItemSelected(item);
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void refreshGraph() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.chart_layout_replacer, ChartFragment.newInstance()); // newInstance() is a static factory method.
        transaction.commit();
    }
}

