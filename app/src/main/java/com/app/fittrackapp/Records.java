package com.app.fittrackapp;

public class Records {


    String title,sets,date;

    public Records() {
    }

    public Records(String title, String sets, String date) {
        this.title = title;
        this.sets = sets;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }
}
