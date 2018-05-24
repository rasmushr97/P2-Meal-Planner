package com.p2app.backend.time;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Calendar {
    private LocalDate today;
    private Map<LocalDate, Day> dates = new HashMap<LocalDate, Day>();

    public Calendar(LocalDate today) {
        this.today = today;
    }

    public Map<LocalDate, Day> getDates() {
        return dates;
    }

    public Day getDay(LocalDate date){
        return dates.get(date);
    }

    public void addDay(LocalDate localDate, Day day){
        this.dates.put(localDate,day);
    }


    public List<Day> get7DayList() {
        // This method is made for the shopping list
        List<Day> res = new ArrayList<>();

        // Get Day objects of the Map "dates" where the key is from today til 6 days from now
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
