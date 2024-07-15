package com.app.fittrackapp.recyclerview;

public class WorkoutPlanItem {


    private String title;

    private boolean isSelected;

    public WorkoutPlanItem(String title, boolean isSelected) {
        this.title = title;
        this.isSelected = isSelected;
    }

    public String getTitle() { return this.title; }

    public boolean getIsSelected() { return this.isSelected; }

    public void setIsSelected(boolean newStatus) { this.isSelected = newStatus; }

}
