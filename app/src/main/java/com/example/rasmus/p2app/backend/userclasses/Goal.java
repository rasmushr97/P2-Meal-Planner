package com.example.rasmus.p2app.backend.userclasses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Goal {
    public static Map<LocalDate, Double> userWeight = new HashMap<>();
    public static Map<LocalDate, Double> goalWeight = new HashMap<>();

    public void addUserWeight(LocalDate localDate, double weight){
        userWeight.put(localDate, weight);
    }

    public void addGoalWeight(LocalDate localDate, double weight){
        goalWeight.put(localDate, weight);
    }

    public static Map<LocalDate, Double> getUserWeight() { return userWeight; }

    public static Map<LocalDate, Double> getGoalWeight() { return goalWeight; }


    /* Method calculates the date where the user will reach their goal*/
    public LocalDate calcGoalDate(LocalUser localUser){
        //TODO Check if date has changed // Does the goal chart begin from beginning or last weight
        LocalDate firstDate = getFirstDate(this.userWeight);
        //LocalDate lastWeightDate = getLastDate(getUserWeight(), localUser.getWeight());
        //LocalDate goalDate = getLastDate(goalWeight, localUser.getGoalWeight());
        //double userGoalWeight = goalWeight.get(goalDate);
        //double userGoalWeight = localUser.getGoalWeight();
        double tempWeight = userWeight.get(firstDate);

        //TODO Gain weight?
        int weeks = 0;
        //getGoalWeight().clear();
        while(tempWeight > localUser.getGoalWeight()){
            double BMI = tempWeight / ((localUser.getHeight() / 100) * (localUser.getHeight() / 100));
            tempWeight -= calToKilo(calOffset(BMI));
            weeks++;
        }
        //TODO Adds the date of goal to map for app screen, here or in loop
        //addGoalWeight(goalDate.plusWeeks(weeks), localUser.getGoalWeight());
        LocalDate goalDate = firstDate.plusWeeks(weeks);
        return goalDate;
    }

    /*
    // If we want x-axis labels to be dates in text
    public String[] xAxisLabels(int weeks, LocalDate startDate){
        String[] xAxisDates = new String[weeks];
        for(int i = 0; i < weeks; i++) {
            xAxisDates[weeks] = startDate.plusWeeks(weeks).toString();
        }
        return xAxisDates;
    }
    */

    /*
    // when you enter weight on app screen
    public void enterWeight(LocalUser localUser, double weight){
        if(localUser.getGoal().getUserWeight().containsKey(LocalDate.now())){
            //something
        }
        localUser.getGoal().addUserWeight(LocalDate.now(), weight);
    }
    */

    /* Finds the latest date where a certain weight was entered in the user/goal maps */
    public LocalDate getLastDate(Map<LocalDate, Double> map, double weight){
        LocalDate lastDate = LocalDate.of(2000,1,1);
        for(Map.Entry<LocalDate, Double> entry : map.entrySet()){
            if(entry.getValue().equals(weight) && entry.getKey().isAfter(lastDate)) {
                lastDate = entry.getKey();
            }
        }
        return lastDate;
    }

    /* Calculates how many kg you lose by calories*/
    public double calToKilo(int calories){
        return calories / 1102.3;
    }

    /* Method that finds the first date in a Map */
    public LocalDate getFirstDate(Map<LocalDate, Double> map){
        LocalDate firstDate = LocalDate.of(3000,1,1);
        for(Map.Entry<LocalDate, Double> entry : map.entrySet()){
            if(entry.getKey().isBefore(firstDate)) {
                firstDate = entry.getKey();
            }
        }
        return firstDate;
    }

    public void calcCaloriesPerDay(LocalUser localUser){
        //TODO (Maybe the ability to gain weight)
        double BMR = RHB_Equation(localUser);

        if(localUser.getWeight() > localUser.getGoalWeight()) { // user hasn't reached their goal
            double BMI = localUser.calcBMI();
            localUser.setCalorieDeficit(calOffset(BMI)); // calculates the offset in calories
            localUser.setCaloriesPerDay((int) ((BMR * localUser.getExerciseLvl()) - localUser.getCalorieDeficit()));
        }
        else { localUser.setCaloriesPerDay((int) ((BMR * localUser.getExerciseLvl()))); } // goal reached, no deficit
    }

    /* Using 'The Revised Harris-Benedict Equation' to calculate daily burned calories for male or female */
    public double RHB_Equation(LocalUser localUser){
        /* BMR = the amount of calories you burn during a day without exercise */
        double BMR = 0;
        if(localUser.getSex().equals("Male")) {
            BMR = 88.362 + (13.397 * localUser.getWeight()) + (4.799 * localUser.getHeight()) - (5.677 * localUser.getAge());
        }
        else if(localUser.getSex().equals("Female")){
            BMR = 447.593 + (9.247 * localUser.getWeight()) + (3.098 * localUser.getHeight()) - (4.330 * localUser.getAge());
        }
        return BMR;
    }

    public int calOffset(double BMI){
        int offset = (int) ((BMI * BMI) - (BMI * 5)); // calculates the amount of calories less you need to eat
        return offset > 1000 ? 1000 : offset;         // max less calories per week is 1000
    }
}
