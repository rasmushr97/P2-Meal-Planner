package com.p2app.frontend.models;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.github.mikephil.charting.data.Entry;
import com.p2app.backend.InRAM;
import com.p2app.backend.userclasses.Goal;

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
public class GraphData {
    public static ArrayList<Entry> userWeight = new ArrayList<>();
    public static ArrayList<Entry> goalWeight = new ArrayList<>();

    //Initialize userweight line
    public void initializeWeight(){
        /* Start and End date*/
        LocalDate start = Goal.getFirstDate(Goal.getUserWeight());
        LocalDate end = Goal.getLastDate(Goal.getUserWeight());

        userWeight.clear();
        /* Goes through all days between the first weight date and the last weight date */
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end.plusDays(1)))
                .collect(Collectors.toList());

        /* Iterates the dates */
        for(LocalDate date : dates){
            /* Iterates the userWeight map from Goal */
            for(Map.Entry<LocalDate, Float> entry : Goal.getUserWeight().entrySet()){
                /* If a measurement is done that day */
                if(date.equals(entry.getKey())){
                    float weeks = (int) WEEKS.between(start, entry.getKey());
                    /* If not an exact amount of weeks had passed, the days are added */
                    if(DAYS.between(start, entry.getKey()) % 7 != 0){
                        float days = (int) (DAYS.between(start, entry.getKey()) % 7);
                        days = days / 7;
                        weeks += days;
                    }
                    /* Adds a point to the line*/
                    userWeight.add(new Entry(weeks, entry.getValue()));
                }
            }
        }
    }

    //Initialize goalweight line
    public void initializeGoal(){
        goalWeight.clear();
        LocalDate finishDate = InRAM.user.getGoal().calcGoalDate(InRAM.user);
        /* Start and End date*/
        LocalDate graphEndDate = Goal.getLastDate(Goal.getUserWeight()).plusWeeks(3);

        /* Goes through all days between the day the user began its goal and the calculated finish date */
        List<LocalDate> goalDates = Stream.iterate(Goal.goalStartDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(Goal.goalStartDate, finishDate.plusDays(1)))
                .collect(Collectors.toList());

        /* Iterates all the dates */
        for(LocalDate date : goalDates){
            /* If there is more than three weeks till goal, no more points are added*/
            if(date.isAfter(graphEndDate)){
                break;
            }
            /* Iterates the goalWeigh map from Goal class */
            for(Map.Entry<LocalDate, Float> entry : Goal.getGoalWeight().entrySet()){
                /* Every time a week passes, if statement is true*/
                if(date.equals(entry.getKey()) && DAYS.between(Goal.goalStartDate, entry.getKey()) % 7 == 0){
                    int weeks = (int) WEEKS.between(Goal.getFirstDate(Goal.getUserWeight()), entry.getKey());
                    goalWeight.add(new Entry(weeks, entry.getValue())); //Adds a point to the graph
                }
            }
        }
    }
}
