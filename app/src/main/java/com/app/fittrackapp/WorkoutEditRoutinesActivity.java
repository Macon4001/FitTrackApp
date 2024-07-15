package com.app.fittrackapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.fittrackapp.adapter.GeneralItemAdapter;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.recyclerview.GeneralItem;

import java.util.ArrayList;

public class WorkoutEditRoutinesActivity extends AppCompatActivity implements GeneralItemAdapter.InterfaceItemEdit, AdapterView.OnItemSelectedListener {


    private DatabaseHelper databaseHelper;

    private boolean savePossible = false;

    private String[] workoutPlans;
    private int selectedPlanIdx = 0;
    private ArrayList<GeneralItem> routinesList;


    private GeneralItemAdapter adapterWorkoutRoutines;

    private RecyclerView recyclerViewRoutines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_editroutines);

        databaseHelper = new DatabaseHelper(WorkoutEditRoutinesActivity.this);
        workoutPlans = loadPlansFromDatabase();
        selectedPlanIdx = 0;
        routinesList = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        Spinner spinnerPlans = findViewById(R.id.spinnerWorkoutRoutines);
        spinnerPlans.setOnItemSelectedListener(this);
        ArrayAdapter adapterPlans = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_middle, workoutPlans);
        adapterPlans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlans.setAdapter(adapterPlans);

        recyclerViewRoutines = findViewById(R.id.recyclerViewRoutines);
        if (!routinesList.isEmpty()) {
            adapterWorkoutRoutines = new GeneralItemAdapter(routinesList, this);
            recyclerViewRoutines.setAdapter(adapterWorkoutRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Button buttonCreatePlan = findViewById(R.id.buttonCreateRoutine);
        buttonCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreateRoutine();
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == selectedPlanIdx) {
            return;
        }

        selectedPlanIdx = position;

        // Update routines
        routinesList.clear();
        adapterWorkoutRoutines.notifyDataSetChanged();

        routinesList = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        adapterWorkoutRoutines = new GeneralItemAdapter(routinesList, this);
        recyclerViewRoutines.setAdapter(adapterWorkoutRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }

    @Override
    public void onItemClicked(int itemPosition) {
        // Open dialog edit plan
        showDialogEditRoutineName(routinesList.get(itemPosition).getTitle(), itemPosition);
    }

    @Override
    public void onButtonRemoveClicked(int itemPosition) {
        databaseHelper.deleteWorkoutRoutine(workoutPlans[selectedPlanIdx], routinesList.get(itemPosition).getTitle());

        // Update recycler view
        routinesList.remove(itemPosition);
        adapterWorkoutRoutines.notifyItemRemoved(itemPosition);
    }
    private String[] loadPlansFromDatabase() {


        Cursor cursor = databaseHelper.getWorkoutPlans();

        String[] loadedPlans = new String[0];


        if (cursor.getCount() > 0) {
            loadedPlans = new String[cursor.getCount()];
            int j = 0;
            while (cursor.moveToNext()) {
                loadedPlans[j] = cursor.getString(0);
                j++;
            }
        }
        cursor.close();

        return loadedPlans;
    }

    private ArrayList<GeneralItem> loadRoutinesFromDatabase(String plan) {
        Cursor cursor = databaseHelper.getWorkoutRoutines(plan);
        ArrayList<GeneralItem> loadedRoutines = new ArrayList<GeneralItem>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedRoutines.add(cursor.getPosition(), new GeneralItem(cursor.getString(0)));
            }
        }

        return loadedRoutines;
    }

    private void showDialogEditRoutineName(String currentRoutineName, int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextRoutineName = view.findViewById(R.id.dialogEditText);
        editTextRoutineName.setText(currentRoutineName);

        builder.setTitle(getResources().getString(R.string.button_edit_routines));
        builder.setNegativeButton(getResources().getString(R.string.button_text_cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.button_text_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newRoutineName = editTextRoutineName.getText().toString();

                if (newRoutineName.length() <= 0) {
                    return;
                }

                databaseHelper.updateWorkoutRoutineName(workoutPlans[selectedPlanIdx], currentRoutineName, newRoutineName);

                routinesList.get(itemPosition).setTitle(newRoutineName);
                adapterWorkoutRoutines.notifyItemChanged(itemPosition);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogCreateRoutine() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextRoutineName = view.findViewById(R.id.dialogEditText);
        editTextRoutineName.setHint("Enter plan name");

        builder.setTitle(getResources().getString(R.string.button_create_routines));
        builder.setNegativeButton(getResources().getString(R.string.button_text_cancel), null);
        builder.setPositiveButton(getResources().getString(R.string.button_create_routines), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = editTextRoutineName.getText().toString();

                if (text.length() <= 0) {
                    return;
                }

                int insertIndex = routinesList.size();
                routinesList.add(insertIndex, new GeneralItem(text));
                adapterWorkoutRoutines.notifyItemInserted(insertIndex);

                // Save to database
                databaseHelper.addWorkoutRoutine(workoutPlans[selectedPlanIdx], text);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



}