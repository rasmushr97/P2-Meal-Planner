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
        /* Goes through all days between the first and the end day */
        List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end.plusDays(1)))
                .collect(Collectors.toList());
        for(LocalDate date : dates){
            for(Map.Entry<LocalDate, Float> entry : Goal.getUserWeight().entrySet()){
                if(date.equals(entry.getKey())){
                    float weeks = (int) WEEKS.between(start, entry.getKey());
                    if(DAYS.between(start, entry.getKey()) % 7 != 0){
                        float days = (int) (DAYS.between(start, entry.getKey()) % 7);
                        days = days / 7;
                        weeks += days;
                    }
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

        /* Goes through all days between the first and the end day */
        List<LocalDate> goalDates = Stream.iterate(Goal.goalStartDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(Goal.goalStartDate, finishDate.plusDays(1)))
                .collect(Collectors.toList());

        for(LocalDate date : goalDates){
            if(date.isAfter(graphEndDate)){
                break;
            }
            for(Map.Entry<LocalDate, Float> entry : Goal.getGoalWeight().entrySet()){
                if(date.equals(entry.getKey()) && DAYS.between(Goal.goalStartDate, entry.getKey()) % 7 == 0){
                    int weeks = (int) WEEKS.between(Goal.getFirstDate(Goal.getUserWeight()), entry.getKey());
                    goalWeight.add(new Entry(weeks, entry.getValue()));
                }
            }
        }
    }
}
