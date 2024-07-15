package com.app.fittrackapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.fittrackapp.fragments.RecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.fragments.DashboardFragment;
import com.app.fittrackapp.fragments.ProfileFragment;
import com.app.fittrackapp.fragments.WorkoutFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Main2Activity extends AppCompatActivity {

    public String date;
    private int currentFragmentID = 0;
    public DatabaseHelper databaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Database
        databaseHelper = new DatabaseHelper(Main2Activity.this);

        // Update language
//        Cursor cursor = databaseHelper.getSettingsLanguage();
//        if (cursor.getCount() > 0) {
//            cursor.moveToFirst();
//            updateLanguage(this, cursor.getString(0));
//        }
//        cursor.close();

        // -----------------------------------------------------------------------------------------
        // Get data if activity was started by another activity
        Intent intent = getIntent();

        // Get current date
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        // If there is a fragmentID submitted take it. Else keep previously set or default one (=0)
        if (getIntent().hasExtra("fragmentID")) {
            currentFragmentID = intent.getIntExtra("fragmentID", 0);
        }

        // Set current fragment based on fragmentID
        switch (currentFragmentID) {
            case 0:
                setFragmentFood(date);
                break;

//            case 1:
//                setFragmentBodyStats();
//                break;

            case 1:
                setFragmentWorkout();
                break;

            case 2:
                setFragmentSettings();
                break;

            default:
                break;
        }


        BottomNavigationView navBar = findViewById(R.id.bottom_navigation);
        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_bar_home:
                        if (currentFragmentID != 0) {
                            setFragmentFood(date);
                            currentFragmentID = 0;
                        }
                        return true;

                    case R.id.third_fragment:
                        if (currentFragmentID != 1) {
                            setFragmentBodyStats();
                            currentFragmentID = 1;
                        }
                        return true;

                    case R.id.nav_bar_exersises:
                        if (currentFragmentID != 2) {
                            setFragmentWorkout();
                            currentFragmentID = 2;
                        }
                        return true;

                    case R.id.nav_bar_settings:
                        if (currentFragmentID != 3) {
                            setFragmentSettings();
                            currentFragmentID = 3;
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }


    private void setFragmentFood(String date) {
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString("date", date);
        fragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setFragmentBodyStats() {
        RecordFragment fragment = new RecordFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();

    }

    private void setFragmentWorkout() {
        WorkoutFragment fragment = new WorkoutFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setFragmentSettings() {
        ProfileFragment fragment = new ProfileFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private static void updateLanguage(Context context, String language) {
//
//        if (language.equals("system")) {
//            return;
//        }
//
//        Locale locale = new Locale(language);
//        Locale.setDefault(locale);
//
//        Resources resources = context.getResources();
//
//        Configuration configuration = resources.getConfiguration();
//        configuration.locale = locale;
//
//        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
    }


}