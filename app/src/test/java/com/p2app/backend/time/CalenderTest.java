package com.p2app.backend.time;

import com.p2app.backend.recipeclasses.Recipe;

import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class CalenderTest {

    @Test
    public void get7DayListTest01(){
        Calendar calendar = new Calendar(LocalDate.now());
        for(int i = 0; i < 7; i++) {
            calendar.addDay(LocalDate.now().plusDays(i), new Day());
        }
        List<Day> dayList = calendar.get7DayList();
        assertEquals(7, dayList.size());
    }

    @Test
    public void get7DayListTest02(){
        Calendar calendar = new Calendar(LocalDate.now());
        for(int i = 0; i < 4; i++) {
            calendar.addDay(LocalDate.now().plusDays(i), new Day());
        }
        List<Day> dayList = calendar.get7DayList();
        assertEquals(4, dayList.size());
    }

}