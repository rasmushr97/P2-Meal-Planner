package com.example.rasmus.p2app.backend.userclasses;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;

public class Goal {
    public static Map<LocalDate, Float> userWeight = new HashMap<>();
    public static Map<LocalDate, Float> goalWeight = new HashMap<>();

    public void addUserWeight(LocalDate localDate, float weight) {
        userWeight.put(localDate, weight);
    }

    public void addGoalWeight(LocalDate localDate, float weight) {
        goalWeight.put(localDate, weight);
    }

    public static Map<LocalDate, Float> getUserWeight() {
        return userWeight;
    }

    public static Map<LocalDate, Float> getGoalWeight() {
        return goalWeight;
    }


    /* Method calculates the date where the user will reach their goal*/
    public LocalDate calcGoalDate(LocalUser localUser) {
        LocalDate firstDate = getFirstDate(this.userWeight);
        float tempWeight = userWeight.get(firstDate);

        // determines whether the user wants to lose or gain weight
        if (localUser.getGoalWeight() > localUser.getWeight()) {
            localUser.setWantLoseWeight(0); // 0 is false the user want to gain weight
        } else {
            localUser.setWantLoseWeight(1); // 1 is true the user want to lose weight
        }

        int days = 0;
        //getGoalWeight().clear();
        if (localUser.getWantLoseWeight() == 1) {
            while (tempWeight > localUser.getGoalWeight()) {
                double height = (double) localUser.getHeight() / 100;
                double BMI = tempWeight / (height * height);
                tempWeight -= (calToKilo(calDeficit(BMI))) / 7;
                addGoalWeight(firstDate.plusDays(days), tempWeight); //Adds a weight for each week for the graph
                days++;
            }
        } else {
            while (tempWeight < localUser.getGoalWeight()) {
                double height = (double) localUser.getHeight() / 100;
                double BMI = tempWeight / (height * height);
                tempWeight += (calToKilo(calDeficit(BMI))) / 7;
                addGoalWeight(firstDate.plusDays(days), tempWeight); //Adds a weight for each week for the graph
                days++;
            }
        }
        LocalDate goalDate = firstDate.plusDays(days);
        return goalDate;
    }

    /* Finds the latest date where a certain weight was entered in the user/goal maps */
    public LocalDate getLastDate(Map<LocalDate, Float> map) {
        LocalDate lastDate = LocalDate.of(2000, 1, 1);
        for (Map.Entry<LocalDate, Float> entry : map.entrySet()) {
            if (entry.getKey().isAfter(lastDate)) {
                lastDate = entry.getKey();
            }
        }
        return lastDate;
    }

    /* Calculates how many kg you lose by calories*/
    public double calToKilo(int calories) {
        return calories / 1102.3;
    }

    /* Method that finds the first date in a Map */
    public LocalDate getFirstDate(Map<LocalDate, Float> map) {
        LocalDate firstDate = LocalDate.of(3000, 1, 1);
        for (Map.Entry<LocalDate, Float> entry : map.entrySet()) {
            if (entry.getKey().isBefore(firstDate)) {
                firstDate = entry.getKey();
            }
        }
        return firstDate;
    }

    public void calcCaloriesPerDay(LocalUser localUser) {
        double BMR = RHB_Equation(localUser);
        int calSurplus = 500;

        switch (localUser.getWantLoseWeight()) {
            case 1:
                if (localUser.getWeight() > localUser.getGoalWeight()) { // user hasn't reached their goal
                    double BMI = localUser.calcBMI();
                    localUser.setCalorieDeficit(calDeficit(BMI)); // calculates the offset in calories
                    localUser.setCaloriesPerDay((int) ((BMR * localUser.getExerciseLvl()) - localUser.getCalorieDeficit()));
                    break;
                } else {
                    localUser.setCaloriesPerDay((int) ((BMR * localUser.getExerciseLvl())));
                    break;
                } // goal reached, no deficit

            case 0:
                if (localUser.getWeight() < localUser.getGoalWeight()) {
                    localUser.setCaloriesPerDay((int) (BMR * localUser.getExerciseLvl() + calSurplus));
                    break;
                } else {
                    localUser.setCaloriesPerDay((int) ((BMR * localUser.getExerciseLvl())));
                    break;
                } // goal reached, no surplus
        }
    }

    // recalculates the amount of the calories for the user if it fluctuates with more than 2.3kg after weighing 2 times
    public void recalcCalories(LocalUser localUser) {
        double maxDiffInKg = 2.27;
        int week = 7;

        for (Map.Entry<LocalDate, Float> entry : localUser.getGoal().getUserWeight().entrySet()) {
            if (entry.getValue() - localUser.getGoal().getGoalWeight().get(entry.getKey()) > maxDiffInKg ||
                    localUser.getGoal().getGoalWeight().get(entry.getKey()) - entry.getValue() > maxDiffInKg) {
                double calDeficit = this.calDeficit(localUser.calcBMI());
                double gramADay = calDeficit / week;
                // multiply with 1000 in order to get it in grams
                double difInCal = (entry.getValue() - localUser.getGoal().getGoalWeight().get(entry.getKey())) * 1000;
                int days = ((int) DAYS.between(this.getFirstDate(goalWeight), this.getLastDate(userWeight)));

                if (localUser.getWantLoseWeight() == 1) {
                    localUser.setCalorieDeficit((int) recalcDeficit(calDeficit, gramADay, difInCal, days));
                    localUser.setCaloriesPerDay(localUser.getCaloriesPerDay() - localUser.getCalorieDeficit());
                } else {
                    localUser.setCalorieDeficit((int) recalcSurplus(gramADay, difInCal, days));
                    localUser.setCaloriesPerDay(localUser.getCaloriesPerDay() + localUser.getCalorieDeficit());
                }
            }
        }
    }

    /* Using 'The Revised Harris-Benedict Equation' to calculate daily burned calories for male or female */
    public double RHB_Equation(LocalUser localUser) {
        /* BMR = the amount of calories you burn during a day without exercise */
        double BMR;
        if (localUser.isMale()) {
            BMR = 88.362 + (13.397 * localUser.getWeight()) + (4.799 * localUser.getHeight()) - (5.677 * localUser.getAge());
        } else {
            BMR = 447.593 + (9.247 * localUser.getWeight()) + (3.098 * localUser.getHeight()) - (4.330 * localUser.getAge());
        }
        return BMR;
    }

    public int calDeficit(double BMI) {
        int offset = (int) ((BMI * BMI) - (BMI * 5)); // calculates the amount of calories less you need to eat
        return offset > 1000 ? 1000 : offset;         // max less calories per week is 1000
    }

    public double recalcDeficit(double calDeficit, double gramADay, double diffInCal, int days) {
        int week = 7;
        double missCalCalories;
        double newDeficit = 0;

        missCalCalories = ((((diffInCal / days) - gramADay) * days) / (days / week)) + calDeficit;
        newDeficit = missCalCalories;

        return newDeficit;
    }

    public double recalcSurplus(double gramADay, double diffInCal, int days) {
        int week = 7;
        double calSurplus = 500;
        double missCalCalories;
        double newSurplus = 0;

        missCalCalories = ((((diffInCal / days) + gramADay) * days) / (days / week)) - calSurplus;
        newSurplus = missCalCalories;

        return newSurplus;
    }

}
