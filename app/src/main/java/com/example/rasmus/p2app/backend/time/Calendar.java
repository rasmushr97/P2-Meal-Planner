package com.example.rasmus.p2app.backend.time;

import java.time.LocalDate;
import java.util.*;

public class Calendar {
    private LocalDate today;
    private Map<LocalDate, Day> dates = new HashMap<LocalDate, Day>();

    public Calendar(LocalDate today) { this.today = LocalDate.now(); }

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

    public void addDay(LocalDate localDate, Day day){
        this.dates.put(localDate,day);
    }


    public List<Day> get7DayList() {
        List<Day> res = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            if(i <= dates.size()){
                res.add(dates.get(i));
            }
        }

        return res;
    }

}
