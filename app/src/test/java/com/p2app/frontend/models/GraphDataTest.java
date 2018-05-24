package com.p2app.frontend.models;

import com.p2app.backend.InRAM;
import com.p2app.backend.userclasses.Goal;
import com.p2app.backend.userclasses.GoalTest;
import com.p2app.backend.userclasses.LocalUser;

import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.*;

public class GraphDataTest {

    private LocalUser createUser(boolean male, boolean loseWeight){
        LocalUser user = new LocalUser();
        user.setAge(25);
        user.setHeight(180);
        user.setWeight(90);
        user.setMale(male);
        if(loseWeight){
            user.setGoalWeight(80);
            user.setWantLoseWeight(1);
        } else{
            user.setGoalWeight(100);
            user.setWantLoseWeight(0);
        }
        return user;
    }

    @Test
    public void initializeUserWeightTest01(){
        GraphData graphData = new GraphData();
        Goal.getUserWeight().clear();
        LocalUser user = createUser(true, true);
        user.getGoal().addUserWeight(LocalDate.of(2018,1,1), 90);
        user.getGoal().addUserWeight(LocalDate.of(2018,2,1), 88);
        user.getGoal().addUserWeight(LocalDate.of(2018,3,1), 86);
        graphData.initializeWeight();
        assertEquals(3, GraphData.userWeight.size());
    }

    @Test
    public void initializeGoalWeightTest01(){
        GraphData graphData = new GraphData();
        LocalUser user = createUser(true, true);
        Goal.getUserWeight().clear();
        Goal.goalStartDate = LocalDate.of(2018,1,1);
        user.getGoal().addUserWeight(Goal.goalStartDate, (float) user.getWeight());
        InRAM.user = user;
        graphData.initializeGoal();
        assertEquals(4, GraphData.goalWeight.size());
    }

}