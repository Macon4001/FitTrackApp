package com.app.fittrackapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.app.fittrackapp.adapter.WorkoutPlansAdapter;
import com.app.fittrackapp.adapter.WorkoutRoutineAdapter;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.recyclerview.WorkoutPlanItem;
import com.app.fittrackapp.recyclerview.WorkoutRoutineItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class WorkoutCreateEditExerciseActivity extends AppCompatActivity implements WorkoutRoutineAdapter.interfaceWorkoutRoutine, WorkoutPlansAdapter.InterfaceWorkoutPlans {

    private String exerciseName;
    private int exerciseSets = 0;
    private int exerciseRepetitions = 0;
    private double exerciseWeight = 0;
    private boolean savePossible = false;


    private String mode = "create";
    private String date;

    private String oldPlanName;
    private String oldRoutineName;
    private String oldExerciseName;


    private Button saveButton;
    private Button cancelButton;

    private Toolbar toolbarActivityCreateExercise;

    private RecyclerView recyclerViewRoutines;

    private ArrayList<WorkoutRoutineItem> routinesList;
    private ArrayList<WorkoutPlanItem> plansList;

    private WorkoutRoutineAdapter adapterRoutines;
    private WorkoutPlansAdapter adapterPlans;
    private int currentSelectedRoutineIdx;
    private int currentSelectedPlanIdx;

    private DatabaseHelper databaseHelper;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_createeditexercise);

        Intent intent = getIntent();

        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        databaseHelper = new DatabaseHelper(WorkoutCreateEditExerciseActivity.this);

        getPlansFromDatabase();

        getRoutinesFromDatabase(plansList.get(0).getTitle());

        recyclerViewRoutines = findViewById(R.id.recyclerViewRoutines);
        recyclerViewRoutines.setAdapter(adapterRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        RecyclerView recyclerViewPlans = findViewById(R.id.recyclerViewPlans);
        recyclerViewPlans.setAdapter(adapterPlans);
        recyclerViewPlans.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));


        //default mode if available
        if (getIntent().hasExtra("mode")) {
            if (getIntent().getStringExtra("mode").equals("edit")) {
                mode = "edit";

                oldPlanName = getIntent().getStringExtra("planName");
                oldRoutineName = getIntent().getStringExtra("routineName");
                exerciseName = getIntent().getStringExtra("exerciseName");
                oldExerciseName = exerciseName;
                exerciseSets = getIntent().getIntExtra("exerciseSets", 0);
                exerciseRepetitions = getIntent().getIntExtra("exerciseReps", 0);
                exerciseWeight = getIntent().getDoubleExtra("exerciseWeight", 0);

                int newSelectedPlanIndex = -1;
                for (WorkoutPlanItem plan : plansList) {
                    newSelectedPlanIndex++;
                    if (plan.getTitle().equals(oldPlanName)) {
                        break;
                    }
                }

                int newSelectedRoutineIndex = -1;
                for (WorkoutRoutineItem routine : routinesList) {
                    newSelectedRoutineIndex++;
                    if (routine.getTitle().equals(oldRoutineName)) {
                        break;
                    }
                }

                if (currentSelectedRoutineIdx != newSelectedRoutineIndex) {
                    updateSelectedRoutine(newSelectedRoutineIndex);
                }
                if (currentSelectedPlanIdx != newSelectedPlanIndex) {
                    updateSelectedPlan(newSelectedPlanIndex);
                }

            }
        }


        EditText editTextExerciseName = findViewById(R.id.editTextExerciseName);

        EditText editTextExerciseSets = findViewById(R.id.editTextCreateExerciseSets);

        EditText editTextExerciseRepetitions = findViewById(R.id.editTextCreateExerciseRepetitions);
        EditText editTextExerciseWeight = findViewById(R.id.editTextCreateExerciseWeight);

        if (mode.equals("edit")) {
            editTextExerciseName.setText(exerciseName);
            editTextExerciseSets.setText(String.valueOf(exerciseSets));

            editTextExerciseRepetitions.setText(String.valueOf(exerciseRepetitions));

            editTextExerciseWeight.setText(convertDataToText(exerciseWeight));
        }

        editTextExerciseName.addTextChangedListener(new textWatcher(0));
        editTextExerciseSets.addTextChangedListener(new textWatcher(1));
        editTextExerciseRepetitions.addTextChangedListener(new textWatcher(2));
        editTextExerciseWeight.addTextChangedListener(new textWatcher(3));

        // Set up toolbar

        toolbarActivityCreateExercise = findViewById(R.id.toolbarActivityCreateExercise);
        if (mode.equals("edit")) {
            toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_edit_exercises));
        } else {
            toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_create_exercises));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        saveButton = findViewById(R.id.buttonSaveNewExercise);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    if (exerciseName == null) {
                        // If name was not set up yet remind user to add one
                        Toast.makeText(getApplicationContext(), "Please enter a name first!", Toast.LENGTH_SHORT).show();
                        Snackbar.make(view, "Please enter a name first!", Snackbar.LENGTH_SHORT).show();
                        return;
                    }

                    savePossible = false;

                    if (mode.equals("edit")) {
                        databaseHelper.deleteWorkoutExercise(oldPlanName, oldRoutineName, oldExerciseName);
                    }

                    databaseHelper.addWorkoutExercise(
                            plansList.get(currentSelectedPlanIdx).getTitle(),
                            routinesList.get(currentSelectedRoutineIdx).getTitle(),
                            exerciseName,
                            exerciseSets,
                            exerciseRepetitions,
                            exerciseWeight
                    );
                    databaseHelper.close();


                    mode = "edit";
                    oldPlanName = plansList.get(currentSelectedPlanIdx).getTitle();
                    oldRoutineName = routinesList.get(currentSelectedRoutineIdx).getTitle();
                    oldExerciseName = exerciseName;

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    saveButton.setTextColor(getColor(R.color.dark_purple));
                    cancelButton.setText(R.string.button_text_back);
                    toolbarActivityCreateExercise.setTitle(getResources().getString(R.string.button_create_exercises));

                    Snackbar.make(view, "Exercise saved!", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = findViewById(R.id.buttonCancelNewExercise);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Main2Activity.class);
                if (date != null) { intent.putExtra("date", date); }
                intent.putExtra("fragmentID", 1);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }



    @Override
    public void onPlanItemClick(int itemPosition) {
        if (itemPosition != currentSelectedPlanIdx) {
            updateSelectedPlan(itemPosition);

            getRoutinesFromDatabase(plansList.get(itemPosition).getTitle());
            recyclerViewRoutines.setAdapter(adapterRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        }
    }

    @Override
    public void onRoutineItemClick(int itemPosition) {
        // -> This method updates the selected routine visually
        if (itemPosition != currentSelectedRoutineIdx) {
            updateSelectedRoutine(itemPosition);
        }
    }


    private class textWatcher implements TextWatcher {
        private int id;
        private textWatcher(int id) {
            this.id = id;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // pass
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            // pass
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // Update value
            switch (id) {
                case 0:
                    if (editable.toString().equals("")) {
                        exerciseName = "No name";
                    } else {
                        exerciseName = editable.toString();
                    }
                    break;

                case 1:
                    if (editable.toString().equals("")) {
                        exerciseSets = 0;
                    } else {
                        exerciseSets = Integer.parseInt(editable.toString());
                    }
                    break;

                case 2:
                    if (editable.toString().equals("")) {
                        exerciseRepetitions = 0;
                    } else {
                        exerciseRepetitions = Integer.parseInt(editable.toString());
                    }
                    break;

                case 3:
                    if (editable.toString().equals("")) {
                        exerciseWeight = 0;
                    } else {
                        exerciseWeight= Double.parseDouble(editable.toString());
                    }
                    break;
            }

            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.dark_purple));
            savePossible = true;
        }
    }

    private void getPlansFromDatabase() {
        plansList = new ArrayList<WorkoutPlanItem>();

        Cursor cursor = databaseHelper.getWorkoutPlans();

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            plansList.add(
                    cursor.getPosition(),
                    new WorkoutPlanItem(cursor.getString(0), true)
            );

            while (cursor.moveToNext()) {
                plansList.add(
                        cursor.getPosition(),
                        new WorkoutPlanItem(cursor.getString(0), false)
                );
            }


            adapterPlans = new WorkoutPlansAdapter(plansList, this);
            currentSelectedPlanIdx = 0;
        }
        cursor.close();
    }

    private void getRoutinesFromDatabase(String workoutPlanName) {
        // Initiate list
        routinesList = new ArrayList<WorkoutRoutineItem>();

        Cursor cursor = databaseHelper.getWorkoutRoutines(workoutPlanName);

        // Populate routines list
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            routinesList.add(
                    cursor.getPosition(),
                    new WorkoutRoutineItem(cursor.getString(0), true)
            );

            while (cursor.moveToNext()) {
                routinesList.add(
                        cursor.getPosition(),
                        new WorkoutRoutineItem(cursor.getString(0), false)
                );
            }

            // Set up adapter
            adapterRoutines = new WorkoutRoutineAdapter(routinesList, this);
            currentSelectedRoutineIdx = 0;
        }
        cursor.close();
    }

    public void updateSelectedPlan(int newSelectedIndex) {
        plansList.get(newSelectedIndex).setIsSelected(true);
        adapterPlans.notifyItemChanged(newSelectedIndex);

        plansList.get(currentSelectedPlanIdx).setIsSelected(false);
        adapterPlans.notifyItemChanged(currentSelectedPlanIdx);

        currentSelectedPlanIdx = newSelectedIndex;
    }

    public void updateSelectedRoutine(int newSelectedIndex) {
        routinesList.get(newSelectedIndex).setIsSelected(true);
        adapterRoutines.notifyItemChanged(newSelectedIndex);

        routinesList.get(currentSelectedRoutineIdx).setIsSelected(false);
        adapterRoutines.notifyItemChanged(currentSelectedRoutineIdx);

        currentSelectedRoutineIdx = newSelectedIndex;
    }

    private String convertDataToText(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            DecimalFormat df = new DecimalFormat("#####.##");
            return String.valueOf(df.format(value));
        }
    }


}