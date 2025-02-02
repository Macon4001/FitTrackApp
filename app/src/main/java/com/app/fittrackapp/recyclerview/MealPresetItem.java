package com.app.fittrackapp.recyclerview;

public class MealPresetItem {


    private String _uuid;
    private int _calories;
    private double _amount;

    private String _mealTitle;


    public MealPresetItem(String mealTitle, String uuid, int calories, double amount){
        this._mealTitle = mealTitle;
        this._uuid = uuid;
        this._calories = calories;
        this._amount = amount;
    }

    public String getMealTitle() {
        return this._mealTitle;
    }

    public String getMealUUID() {
        return this._uuid;
    }

    public int getCalories() {
        return this._calories;
    }

    public double getAmount() {
        return this._amount;
    }

    public void setAmount(double newAmount) {
        this._amount = newAmount;
    }

}
