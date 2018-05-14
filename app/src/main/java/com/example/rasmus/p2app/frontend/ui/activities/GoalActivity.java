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

import com.example.rasmus.p2app.backend.userclasses.Goal;
import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;
import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.ui.fragments.ChartFragment;
import com.example.rasmus.p2app.frontend.other.Storage;

import java.time.LocalDate;
import java.util.Map;

public class GoalActivity extends AppBackButtonActivity {

    private static final String TAG = "GoalActivity";
    private Button goalButton;
    private TextView goalEdit;
    static Storage storage = new Storage();
    private Button enterWeightButton;
    private EditText weightText;
    private LocalUser localUser = new LocalUser();

    //TODO needs an onStop() or something method that saves the data
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        setTitle("Weight control");
        /* TODO All this should be loaded in */
        localUser.setGoalWeight(40);
        localUser.setAge(25);
        localUser.setHeight(180);
        localUser.setWeight(80);
        localUser.setMale(true);
        /*
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 1, 1), (float) 80);
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 1, 9), (float) 79.6);
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 1, 17), (float) 78);
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 2, 1), (float) 76);
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 3, 9), (float) 73.4);
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 3, 21), (float) 71.8); */
        localUser.getGoal().addUserWeight(LocalDate.of(2018, 4, 1), (float) 90);

        /* If there only is one weight measurement stored (graph needs atleast two)*/
        if(localUser.getGoal().getUserWeight().size() == 1){
            Map.Entry<LocalDate,Float> entry = localUser.getGoal().getUserWeight().entrySet().iterator().next();
            /* Adds another weight a day before thats the same */
            LocalDate key = entry.getKey().minusDays(1);
            Float value = entry.getValue();
            localUser.getGoal().addUserWeight(key, value);
        }
        /* Loads graph */
        storage.initializeWeight(localUser);
        storage.initializeGoal(localUser);
        refreshGraph();

        /* Userweight button and textbox */
        weightText = findViewById(R.id.weightText);
        enterWeightButton = findViewById(R.id.enterWeight);
        enterWeightButton.setOnClickListener(view -> {
            if (!weightText.getText().toString().equals("")) { //Checks if the input is empty
                if (Float.valueOf(weightText.getText().toString()) < 500) { //TODO right now checks if weight is below 500
                    /* Changes the user weight and adds it to the history of weight*/
                    localUser.setWeight(Float.valueOf(weightText.getText().toString())); //TODO add to database/XML
                    localUser.getGoal().addUserWeight(LocalDate.now(), (float) localUser.getWeight()); //TODO same as above
                    hideKeyboard(GoalActivity.this);
                    /* Refreshes the graph with new weight */
                    storage.initializeGoal(localUser);
                    storage.initializeWeight(localUser);
                    refreshGraph();
                } else { //TODO If you are above 500kg
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    builder.setTitle("You Are Fat! :)"); //TODO do something here (Positive button)
                    final EditText input = new EditText(GoalActivity.this);
                    builder.setPositiveButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
            }
        });

        goalButton = findViewById(R.id.enterGoalWeight);
        goalEdit = findViewById(R.id.goalText);

        /* Checks if there currently is a goal for the user */
        if (localUser.getGoalWeight() == 0) {
            goalEdit.setText("No Goal Set");
            goalButton.setText("Set Goal");
        } else {
            goalEdit.setText(localUser.getGoalWeight() + "kg");
        }

        /* 'Change goal weight' button */
        goalButton.setOnClickListener(
                view -> {
                    /* Creates alertbox */
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    builder.setTitle("Enter Goal Weight");
                    final EditText input = new EditText(GoalActivity.this);
                    input.setInputType(weightText.getInputType()); //Decimal input
                    builder.setView(input);
                    builder.setPositiveButton("OK", (dialog, which) -> {
                        if (!input.getText().toString().equals("")) { //Checks if input is empty
                            localUser.setGoalWeight(Float.valueOf(input.getText().toString())); //TODO update database/XML
                            goalEdit.setText(localUser.getGoalWeight() + " kg"); //Shows the goal weight
                            /* Updates graph for goal line */
                            storage.initializeGoal(localUser);
                            refreshGraph();
                        }
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
                    builder.show();
                });
    }

    /* Makes the 'more options' menu  */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.goal_settings, menu);
        return true;
    }

    /* Adds functionality to the 'more options' menu */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        /* Switch for what option is clicked */
        switch (id) {
            case R.id.undo_weight:
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    switch (which) {
                        //Yes button clicked
                        case DialogInterface.BUTTON_POSITIVE:
                            break;
                        //No button clicked
                        case DialogInterface.BUTTON_NEGATIVE:
                            break;
                    }
                };
                /* Alertbox for 'undo weight' */
                if(localUser.getGoal().getUserWeight().size() > 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    //Last date of entering a weight
                    LocalDate lastMeasurement = localUser.getGoal().getLastDate(Goal.getUserWeight());
                    Object lastWeight = Goal.getUserWeight().get(lastMeasurement);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("You are about to delete your last weight measurement forever, this cannot be undone!\n" +
                            "\nWeight: " + lastWeight.toString() + " kg" +
                            "\nTime: " + (lastMeasurement.equals(LocalDate.now()) ? "Today" : lastMeasurement));
                    /* Yes button deletes the latest weight measurement */
                    builder.setPositiveButton("DELETE", (dialog, which) -> {
                        //TODO Next 3 lines should maybe do something with database/XML
                        Goal.getUserWeight().remove(lastMeasurement, lastWeight);
                        LocalDate prevWeightDate = localUser.getGoal().getLastDate(Goal.getUserWeight());
                        localUser.setWeight(Goal.getUserWeight().get(prevWeightDate));
                        /* Refreshes the graph */
                        storage.initializeGoal(localUser);
                        storage.initializeWeight(localUser);
                        refreshGraph();
                    });
                    builder.setNegativeButton("CANCEL", dialogClickListener).show();
                    break;
                } else {
                    /* You can't delete your first measurement */ //TODO maybe needs change
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    builder.setTitle("WARNING!");
                    builder.setMessage("You cant delete all of your measurements");
                    builder.setNegativeButton("GOT IT", dialogClickListener).show();
                    break;
                }
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

