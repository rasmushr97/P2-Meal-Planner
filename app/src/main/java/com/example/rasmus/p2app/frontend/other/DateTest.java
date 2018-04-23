package com.example.rasmus.p2app.frontend.other;

public class DateTest {
    public String date;
    private String breakfastText;
    private String lunchText;
    private String dinnerText;

    public DateTest(String date, String breakfastText, String lunchText, String dinnerText) {
        this.date = date;
        this.breakfastText = breakfastText;
        this.lunchText = lunchText;
        this.dinnerText = dinnerText;
    }

    public String getBreakfastText() {
        return breakfastText;
    }

    public void setBreakfastText(String breakfastText) {
        this.breakfastText = breakfastText;
    }

    public String getLunchText() {
        return lunchText;
    }

    public void setLunchText(String lunchText) {
        this.lunchText = lunchText;
    }

    public String getDinnerText() {
        return dinnerText;
    }

    public void setDinnerText(String dinnerText) {
        this.dinnerText = dinnerText;
    }

}
