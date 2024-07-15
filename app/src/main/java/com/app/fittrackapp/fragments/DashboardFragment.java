package com.app.fittrackapp.fragments;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.app.fittrackapp.WorkoutActivity;
import com.app.fittrackapp.YogaActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.app.fittrackapp.CalendarActivity;
import com.app.fittrackapp.MealsAddDailyEntryActivity;
import com.app.fittrackapp.MealsMealsOfDayActivity;
import com.app.fittrackapp.R;
import com.app.fittrackapp.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DashboardFragment extends Fragment {

    private String date;
    private double[] dataFood;
    private double[] dataGoals;
    private boolean fabOpen = false;

    private int percentOf(double current, double max) {
        return (int) ((current / max) * 100);
    }


    public DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("date")) {
            date = getArguments().getString("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }


        databaseHelper = new DatabaseHelper(getActivity());

        Cursor cursorDataFood = databaseHelper.getConsumedMealsSums(date);
        if (cursorDataFood.getCount() > 0) {
            cursorDataFood.moveToFirst();
            dataFood = new double[7];
            for (int i = 0; i <= 6; i++) {
                dataFood[i] = cursorDataFood.getDouble(i);
            }
        } else {
            dataFood = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        }
        cursorDataFood.close();

        Cursor cursorSettings = databaseHelper.getSettingsGoals();

        if (cursorSettings.getCount() > 0) {
            cursorSettings.moveToFirst();
            dataGoals = new double[] {
                    cursorSettings.getDouble(0),
                    cursorSettings.getDouble(1),
                    cursorSettings.getDouble(2),
                    cursorSettings.getDouble(3)
            };
        } else {
            dataGoals = new double[] {2000, 2000, 2000, 2000};
        }


        cursorSettings.close();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        TextView textDate = view.findViewById(R.id.textViewDBNDate);
        textDate.setText(date);

        CardView cd1 = view.findViewById(R.id.cardView);
        CardView cd2 = view.findViewById(R.id.cardView2);

        cd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YogaActivity.class);
                startActivity(intent);
            }
        });
        cd2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WorkoutActivity.class);
                startActivity(intent);
            }
        });

        ProgressBar progressBarMain = getView().findViewById(R.id.progressBarDBNCaloriesMain);
        TextView textProgressMain = getView().findViewById(R.id.textViewDNCaloriesMain);
        ObjectAnimator.ofInt(progressBarMain, "progress", percentOf(dataFood[0], dataGoals[0])).start();
        textProgressMain.setText(convertDataToIntText(dataFood[0]));

        ProgressBar progressBarFat = getView().findViewById(R.id.progressBarDBNFatMain);
        TextView textProgressFat = getView().findViewById(R.id.textViewProgressDBNFatMain);
        ObjectAnimator.ofInt(progressBarFat, "progress", percentOf(dataFood[1], dataGoals[1])).start();
        String textFat = convertDataToIntText(dataFood[1]) + " g";
        textProgressFat.setText(textFat);

        ProgressBar progressBarCarbohydrates = getView().findViewById(R.id.progressBarDBNCarbsMain);

        TextView textProgressCarbohydrates = getView().findViewById(R.id.textViewProgressCarbohydrates);

        ObjectAnimator.ofInt(progressBarCarbohydrates, "progress", percentOf(dataFood[3], dataGoals[2])).start();
        String textCarbohydrates = convertDataToIntText(dataFood[3]) + " g";
        textProgressCarbohydrates.setText(textCarbohydrates);

        ProgressBar progressBarProtein = getView().findViewById(R.id.progressBarDBNProteinMain);
        TextView textProgressProtein = getView().findViewById(R.id.textViewProgressDBNProteinMain);
        ObjectAnimator.ofInt(progressBarProtein, "progress", percentOf(dataFood[5], dataGoals[3])).start();
        String textProtein = convertDataToIntText(dataFood[5]) + " g";
        textProgressProtein.setText(textProtein);

        TextView textViewDetailsCal = getView().findViewById(R.id.textViewDBNDetailsCalories);
        TextView textViewDetailsSalt = getView().findViewById(R.id.textViewDBNDetailsSalt);
        TextView textViewDetailsFat = getView().findViewById(R.id.textViewDBNDetailsFat);
        TextView textViewDetailsFatSat = getView().findViewById(R.id.textViewDBNDetailsFatSat);
        TextView textViewDetailsCarbs = getView().findViewById(R.id.textViewDBNDetailsCarbs);
        TextView textViewDetailsSugar = getView().findViewById(R.id.textViewDBNDetailsSugar);
        TextView textViewDetailsProtein = getView().findViewById(R.id.textViewDBNDetailsProtein);

        textViewDetailsCal.setText(convertDataToDoubleText(dataFood[0]));
        textViewDetailsFat.setText(convertDataToDoubleText(dataFood[1]));
        textViewDetailsFatSat.setText(convertDataToDoubleText(dataFood[2]));
        textViewDetailsCarbs.setText(convertDataToDoubleText(dataFood[3]));
        textViewDetailsSugar.setText(convertDataToDoubleText(dataFood[4]));
        textViewDetailsProtein.setText(convertDataToDoubleText(dataFood[5]));
        textViewDetailsSalt.setText(convertDataToDoubleText(dataFood[6]));

        ImageButton buttonCalendar = getView().findViewById(R.id.buttonDBNCalendar);
        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), CalendarActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("fragmentID", 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });


        Button buttonAddMeal = getView().findViewById(R.id.buttonDashboardNutritionAddMeal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MealsAddDailyEntryActivity.class);
                intent.putExtra("date", date);
                intent.putExtra("fragmentID", 0);
                startActivity(intent);
            }
        });
        FloatingActionButton fabMealMain = getView().findViewById(R.id.fabMealMain);
        View fragmentBlur = getView().findViewById(R.id.fragmentBlur);
        CardView card = getView().findViewById(R.id.card);
        fabMealMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentBlur.setVisibility(View.VISIBLE);
                card.setVisibility(View.VISIBLE);
                if (fabOpen) {
                    fabOpen = false;

                    fragmentBlur.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                } else {
                    fabOpen = true;
                    fragmentBlur.setVisibility(View.VISIBLE);
                    card.setVisibility(View.VISIBLE);

                }
            }
        });

        Button buttonShowEatenMeals = getView().findViewById(R.id.buttonEatenMeals);
        buttonShowEatenMeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MealsMealsOfDayActivity.class);
                intent.putExtra("date", date);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });





    }
    private String convertDataToDoubleText(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            DecimalFormat df = new DecimalFormat("#####.##");
            return String.valueOf(df.format(value));
        }
    }

    private String convertDataToIntText(double value) {
        return String.valueOf((int) value);
    }

}