package com.example.rasmus.p2app.frontend.ui.settings;

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
import android.widget.Toast;

import com.example.rasmus.p2app.backend.InRAM;
import com.example.rasmus.p2app.backend.userclasses.Goal;
import com.example.rasmus.p2app.cloud.DBHandler;
import com.example.rasmus.p2app.frontend.AppBackButtonActivity;
import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.ui.misc.ChartFragment;
import com.example.rasmus.p2app.frontend.models.GraphData;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.rasmus.p2app.backend.userclasses.Goal.getFirstDate;

public class GoalActivity extends AppBackButtonActivity {

    private static final String TAG = "GoalActivity";
    private Button goalButton;
    private TextView goalEdit;
    static GraphData graphData = new GraphData();
    private Button enterWeightButton;
    private EditText weightText;
    private boolean goalChanged = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);
        setTitle("Weight control");

        /* If there only is one weight measurement stored (graph needs atleast two)*/
        if(Goal.getUserWeight().size() == 1){
            Map.Entry<LocalDate,Float> entry = Goal.getUserWeight().entrySet().iterator().next();
            /* Adds another weight a day before thats the same */
            LocalDate key = entry.getKey().minusDays(1);
            Float value = entry.getValue();
            InRAM.user.getGoal().addUserWeight(key, value);
        }
        /* Loads graph */
        graphData.initializeWeight();
        graphData.initializeGoal();
        refreshGraph();

        /* Userweight button and textbox */
        weightText = findViewById(R.id.weightText);
        enterWeightButton = findViewById(R.id.enterWeight);
        enterWeightButton.setOnClickListener(view -> {
            if (!weightText.getText().toString().equals("")) { //Checks if the input is empty
                if (Float.valueOf(weightText.getText().toString()) < 500) { //TODO right now checks if weight is below 500
                    /* Changes the user weight and adds it to the history of weight*/
                    InRAM.user.setWeight(Float.valueOf(weightText.getText().toString()));
                    InRAM.user.getGoal().addUserWeight(LocalDate.now(), (float) InRAM.user.getWeight());
                    hideKeyboard(GoalActivity.this);
                    /* Refreshes the graph with new weight */
                    graphData.initializeGoal();
                    graphData.initializeWeight();
                    refreshGraph();
                } else { //TODO If you are above 500kg
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    builder.setTitle("You Are Fat! :)"); //TODO do something here (Positive button)
                    final EditText input = new EditText(GoalActivity.this);
                    builder.setPositiveButton("CANCEL", (dialog, which) -> dialog.cancel());
                    builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
                    builder.show();
                }
            }
        });

        goalButton = findViewById(R.id.enterGoalWeight);
        goalEdit = findViewById(R.id.goalText);

        /* Checks if there currently is a goal for the user */
        if (InRAM.user.getGoalWeight() == 0) {
            goalEdit.setText("No Goal Set");
            goalButton.setText("Set Goal");
        } else {
            goalEdit.setText(InRAM.user.getGoalWeight() + "kg");
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
                    builder.setPositiveButton("CHANGE", (dialog, which) -> {
                        if (!input.getText().toString().equals("")) { //Checks if input is empty)
                            InRAM.user.setGoalWeight(Float.valueOf(input.getText().toString()));

                            /* If user changes to gain weight */
                            if(InRAM.user.getGoalWeight() > InRAM.user.getWeight() && InRAM.user.getWantLoseWeight() == 1){
                                InRAM.user.setWantLoseWeight(0);
                                Goal.startDate = Goal.getLastDate(Goal.getUserWeight());
                                goalChanged = true;
                            }
                            /* If user changes to lose weight */
                            else if (InRAM.user.getGoalWeight() < InRAM.user.getWeight() && InRAM.user.getWantLoseWeight() == 0){
                                InRAM.user.setWantLoseWeight(1);
                                Goal.startDate = Goal.getLastDate(Goal.getUserWeight());
                                goalChanged = true;
                            }
                            /* Select goal graph start point */
                            else if (!Goal.startDate.equals(getFirstDate(Goal.getUserWeight()))) {
                                if(InRAM.user.getGoalWeight() < InRAM.user.getWeight() && InRAM.user.getWantLoseWeight() == 1){
                                    goalChanged = false;
                                } else if(InRAM.user.getGoalWeight() > InRAM.user.getWeight() && InRAM.user.getWantLoseWeight() == 1){
                                    goalChanged = false;
                                } else {
                                    Goal.startDate = Goal.getLastDate(Goal.getUserWeight());
                                }
                            }

                            goalEdit.setText(InRAM.user.getGoalWeight() + " kg"); //Shows the goal weight
                            /* Updates graph for goal line */
                            graphData.initializeGoal();
                            refreshGraph();
                        }
                    });
                    builder.setNegativeButton("CANCEL", (dialog, which) -> dialog.cancel());
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
                if(Goal.getUserWeight().size() > 2) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(GoalActivity.this);
                    //Last date of entering a weight
                    LocalDate lastMeasurement = Goal.getLastDate(Goal.getUserWeight());
                    Object lastWeight = Goal.getUserWeight().get(lastMeasurement);
                    builder.setTitle("Are you sure?");
                    builder.setMessage("You are about to delete your last weight measurement forever, this cannot be undone!\n" +
                            "\nWeight: " + lastWeight.toString() + " kg" +
                            "\nTime: " + (lastMeasurement.equals(LocalDate.now()) ? "Today" : lastMeasurement));
                    /* Yes button deletes the latest weight measurement */
                    builder.setPositiveButton("DELETE", (dialog, which) -> {
                        Goal.getUserWeight().remove(lastMeasurement, lastWeight);
                        LocalDate prevWeightDate = Goal.getLastDate(Goal.getUserWeight());
                        InRAM.user.setWeight(Goal.getUserWeight().get(prevWeightDate));
                        /* Updates the date where goal line begins, if goal has been changed */
                        if(goalChanged){
                            Goal.startDate = Goal.getLastDate(Goal.getUserWeight());
                        }
                        Toast.makeText(this, "Measurement deleted", Toast.LENGTH_SHORT).show();
                        /* Refreshes the graph */
                        graphData.initializeGoal();
                        graphData.initializeWeight();
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

    @Override
    protected void onPause() {
        super.onPause();
        /* Clears the goals data base */
        DBHandler.deleteFromGoals(InRAM.user.getID());
        DBHandler.updateLoseWeightOrNot(InRAM.user.getWantLoseWeight(), InRAM.user.getID());

        float goalWeight = (float) InRAM.user.getGoalWeight();

        LocalDate first = getFirstDate(Goal.getUserWeight());
        LocalDate last = Goal.getLastDate(Goal.getUserWeight());

        /* Creates a list of dates between 'first' and 'last' date */
        List<LocalDate> dates = Stream.iterate(first, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(first, last.plusDays(1)))
                .collect(Collectors.toList());
        /* Goes through all days, and adds all weight measurements to the database */
        for(LocalDate date : dates){
            if(Goal.getUserWeight().keySet().contains(date)){
                DBHandler.addWeightMeasurement(date, Goal.getUserWeight().get(date), goalWeight, InRAM.user.getID(), Goal.startDate);
            }
        }
    }
}

