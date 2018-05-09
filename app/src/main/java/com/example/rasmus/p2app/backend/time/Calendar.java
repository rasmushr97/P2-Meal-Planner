package com.example.rasmus.p2app.backend.time;

import java.time.LocalDate;
import java.util.*;

public class Calendar {
    private LocalDate today;
    private Map<LocalDate, Day> dates = new HashMap<LocalDate, Day>();

    public Calendar(LocalDate today) {
        this.today = today;
    }

    public LocalDate getToday() {
        return today;
    }

    public void setToday(LocalDate today) {
        this.today = today;
    }

    public Map<LocalDate, Day> getDates() {
        return dates;
    }

    public void setDates(Map<LocalDate, Day> dates) {
        this.dates = dates;
    }
    public Day getDay(LocalDate date){
        return dates.get(date);
    }

    public void addDay(LocalDate localDate, Day day){
        this.dates.put(localDate,day);
    }


    public List<Day> get7DayList() {
        List<Day> res = new ArrayList<>();

        LocalDate date;
        for(int i = 0; i < 7; i++){
            date = LocalDate.now().plusDays(i);

            if(dates.get(date) != null){
                res.add(dates.get(date));
            }

        }

        return res;
    }

}
