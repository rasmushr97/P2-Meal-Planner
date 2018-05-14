package com.example.rasmus.p2app.frontend.other;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.example.rasmus.p2app.backend.userclasses.Goal;
import com.example.rasmus.p2app.backend.userclasses.LocalUser;
import com.github.mikephil.charting.data.Entry;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.WEEKS;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Storage {
    public static ArrayList<Entry> userWeight = new ArrayList<>();
    public static ArrayList<Entry> goalWeight = new ArrayList<>();

    //Initialize userweight line
    public void initializeWeight(LocalUser localUser){
        /* Start and End date*/
        LocalDate start = localUser.getGoal().getFirstDate(Goal.getUserWeight());
        LocalDate end = localUser.getGoal().getLastDate(Goal.getUserWeight());
        //TODO if only one weight
        this.userWeight.clear();
        /* Goes through all days between the first and the end day */
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end.plusDays(1)))
                .collect(Collectors.toList());
        for(LocalDate date : dates){
            for(Map.Entry<LocalDate, Float> entry : Goal.getUserWeight().entrySet()){
                if(date.equals(entry.getKey())){
                    this.userWeight.add(new Entry(WEEKS.between(localUser.getGoal().getFirstDate(Goal.getUserWeight()), entry.getKey()), entry.getValue()));
                }
            }
        }
    }

    //Initialize goalweight line
    public void initializeGoal(LocalUser localUser){
        this.goalWeight.clear();
        Goal.getGoalWeight().clear();
        localUser.getGoal().calcGoalDate(localUser);
        /* Start and End date*/
        LocalDate goalStart = localUser.getGoal().getFirstDate(Goal.getUserWeight());
        LocalDate goalEnd = localUser.getGoal().getLastDate(Goal.getUserWeight());
        long extendGraphWeeks = (long) (WEEKS.between(goalStart, goalEnd) * 0.2);
        goalEnd = goalEnd.plusWeeks(extendGraphWeeks);

        /* Goes through all days between the first and the end day */
        List<LocalDate> goalDates = Stream.iterate(goalStart, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(goalStart, goalEnd.plusDays(1).plusWeeks(2)))
                .collect(Collectors.toList());
        for(LocalDate date : goalDates){
            for(Map.Entry<LocalDate, Float> entry : Goal.getGoalWeight().entrySet()){
                if(date.equals(entry.getKey()) && DAYS.between(localUser.getGoal().getFirstDate(Goal.getUserWeight()), entry.getKey()) % 7 == 0){
                    this.goalWeight.add(new Entry(WEEKS.between(localUser.getGoal().getFirstDate(Goal.getUserWeight()), entry.getKey()), entry.getValue()));
                }
            }
        }
    }

    public ArrayList<Entry> getUserWeight() {
        return userWeight;
    }

    public void setUserWeight(ArrayList<Entry> userWeight) {
        this.userWeight = userWeight;
    }

    public ArrayList<Entry> getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(ArrayList<Entry> goalWeight) {
        this.goalWeight = goalWeight;
    }
}
