package com.app.fittrackapp.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.app.fittrackapp.Main2Activity;
import com.app.fittrackapp.R;
import com.app.fittrackapp.WorkoutCreateEditExerciseActivity;
import com.app.fittrackapp.WorkoutEditRoutinesActivity;
import com.app.fittrackapp.adapter.WorkoutExerciseAdapter;
import com.app.fittrackapp.adapter.WorkoutRoutineAdapter;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.recyclerview.WorkoutExerciseItem;
import com.app.fittrackapp.recyclerview.WorkoutRoutineItem;

import java.util.ArrayList;


public class WorkoutFragment extends Fragment implements WorkoutRoutineAdapter.interfaceWorkoutRoutine, WorkoutExerciseAdapter.InterfaceWorkoutExercises, AdapterView.OnItemSelectedListener {


    private int selectedPlanIdx = -1;

    private int selectedRoutineIdx = -1;
    private RecyclerView recyclerViewRoutines;
    private RecyclerView recyclerViewExercises;

    private WorkoutRoutineAdapter adapterRoutines;
    private WorkoutExerciseAdapter adapterExercises;


    private String[] workoutPlans;
    private ArrayList<WorkoutRoutineItem> workoutRoutines;
    private ArrayList<WorkoutExerciseItem> workoutExercises;




    private boolean fabOpen = false;
    private FloatingActionButton fabMain;

    private View backgroundBlur;

    TextView sectionTitleRoutines;
    TextView sectionTitleExercises;
    CardView card;
    public DatabaseHelper databaseHelper;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        workoutPlans = loadPlansFromDatabase();
        selectedPlanIdx = 0;

        workoutRoutines = loadRoutinesFromDatabase(workoutPlans[0]);

        if (!workoutRoutines.isEmpty()) {
            workoutExercises = loadExercisesFromDatabase(workoutPlans[0], workoutRoutines.get(0).getTitle());
            Log.d("uwu", "routines size: " + workoutRoutines.size());
            Log.d("uwu", "exerzises size: " + workoutExercises.size());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Set up recycler-views

        sectionTitleRoutines = view.findViewById(R.id.sectionTitleRoutines);
        sectionTitleExercises = view.findViewById(R.id.sectionTitleExercises);

        Spinner spinnerPlans = view.findViewById(R.id.spinnerWorkoutPlans);
        spinnerPlans.setOnItemSelectedListener(this);
        ArrayAdapter adapterPlans = new ArrayAdapter(getContext(), R.layout.spinner_item_purple_middle, workoutPlans);
        adapterPlans.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPlans.setAdapter(adapterPlans);

        recyclerViewRoutines = view.findViewById(R.id.recyclerViewRoutines);
        if (!workoutRoutines.isEmpty()) {
            adapterRoutines = new WorkoutRoutineAdapter(workoutRoutines, this);
            recyclerViewRoutines.setAdapter(adapterRoutines);
            recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            selectedRoutineIdx = 0;
        } else {
            sectionTitleRoutines.setVisibility(View.INVISIBLE);
        }

        recyclerViewExercises = view.findViewById(R.id.recyclerViewExercises);
        if (!workoutExercises.isEmpty()) {
            adapterExercises = new WorkoutExerciseAdapter(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        } else {
            sectionTitleExercises.setVisibility(View.INVISIBLE);
        }


        backgroundBlur = view.findViewById(R.id.fragmentExercisesBlur);
        card = view.findViewById(R.id.card);
        fabMain = view.findViewById(R.id.fabExercisesMain);
        TextView addExercise = view.findViewById(R.id.addExercise);
        TextView addRoutine = view.findViewById(R.id.addRoutine);

        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });

        backgroundBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workoutRoutines.isEmpty()) {
                    Snackbar.make(view, "Please create at least one workout routine first!", Snackbar.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), WorkoutCreateEditExerciseActivity.class);
                intent.putExtra("date", ((Main2Activity) requireContext()).date);
                startActivity(intent);
            }
        });

        addRoutine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (workoutPlans.length <= 0) {
                    Toast.makeText(getContext(), "You must create at least 1 workout plan first!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(view.getContext(), WorkoutEditRoutinesActivity.class);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onRoutineItemClick(int itemPosition) {
        if (selectedRoutineIdx == -1) {
            return;
        }

        if (itemPosition == selectedRoutineIdx) {
            return;
        }

        // Update routine-items visually
        workoutRoutines.get(itemPosition).setIsSelected(true);
        workoutRoutines.get(selectedRoutineIdx).setIsSelected(false);
        adapterRoutines.notifyItemChanged(itemPosition);
        adapterRoutines.notifyItemChanged(selectedRoutineIdx);

        selectedRoutineIdx = itemPosition;

        // Update exercises
        workoutExercises.clear();
        if (adapterExercises != null) {
            adapterExercises.notifyDataSetChanged();
        }


        workoutExercises = loadExercisesFromDatabase(workoutPlans[selectedPlanIdx], workoutRoutines.get(selectedRoutineIdx).getTitle());

        if (!workoutExercises.isEmpty()) {
            adapterExercises = new WorkoutExerciseAdapter(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        }


        // adapterExercises.notifyDataSetChanged();
    }

    @Override
    public void onExerciseItemClick(int itemPosition) {
        Intent intent = new Intent(getContext(), WorkoutCreateEditExerciseActivity.class);
        intent.putExtra("date", ((Main2Activity) requireContext()).date);
        intent.putExtra("mode", "edit");
        intent.putExtra("planName", workoutPlans[selectedPlanIdx]);
        intent.putExtra("routineName", workoutRoutines.get(selectedRoutineIdx).getTitle());
        intent.putExtra("exerciseName", workoutExercises.get(itemPosition).getTitle());
        intent.putExtra("exerciseSets", workoutExercises.get(itemPosition).getSets());
        intent.putExtra("exerciseReps", workoutExercises.get(itemPosition).getRepetitions());
        intent.putExtra("exerciseWeight", workoutExercises.get(itemPosition).getWeight());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == selectedPlanIdx) {
            return;
        }

        selectedPlanIdx = position;

        // Update routines
        workoutRoutines.clear();
        if (adapterRoutines != null) {
            adapterRoutines.notifyDataSetChanged();
        }

        workoutExercises.clear();
        if (adapterExercises != null) {
            adapterExercises.notifyDataSetChanged();
        }

        workoutRoutines = loadRoutinesFromDatabase(workoutPlans[selectedPlanIdx]);

        if (workoutRoutines.isEmpty()) {
            sectionTitleRoutines.setVisibility(View.INVISIBLE);
            sectionTitleExercises.setVisibility(View.INVISIBLE);
            return;
        }

        adapterRoutines = new WorkoutRoutineAdapter(workoutRoutines, this);
        recyclerViewRoutines.setAdapter(adapterRoutines);
        recyclerViewRoutines.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        selectedRoutineIdx = 0;
        sectionTitleRoutines.setVisibility(View.VISIBLE);

        // Updates exercises
        workoutExercises = loadExercisesFromDatabase(workoutPlans[selectedPlanIdx], workoutRoutines.get(selectedRoutineIdx).getTitle());

        if (!workoutExercises.isEmpty()) {
            adapterExercises = new WorkoutExerciseAdapter(workoutExercises, this);
            recyclerViewExercises.setAdapter(adapterExercises);
            recyclerViewExercises.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            sectionTitleExercises.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
    }


    private String[] loadPlansFromDatabase() {
        databaseHelper = new DatabaseHelper(getActivity());

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

    private ArrayList<WorkoutRoutineItem> loadRoutinesFromDatabase(String workoutPlan) {
        Cursor c = ((Main2Activity) requireContext()).databaseHelper.getWorkoutRoutines(workoutPlan);
        ArrayList<WorkoutRoutineItem> loadedRoutines = new ArrayList<WorkoutRoutineItem>();

        if (c.getCount() > 0) {
            c.moveToFirst();
            loadedRoutines.add(c.getPosition(), new WorkoutRoutineItem(c.getString(0), true));

            while (c.moveToNext()) {
                loadedRoutines.add(c.getPosition(), new WorkoutRoutineItem(c.getString(0), false));
            }
        }

        return loadedRoutines;
    }

    private ArrayList<WorkoutExerciseItem> loadExercisesFromDatabase(String workoutPlan, String workoutRoutine) {
        Cursor cursor = ((Main2Activity) requireContext()).databaseHelper.getWorkoutExercises(workoutPlan, workoutRoutine);
        ArrayList<WorkoutExerciseItem> loadedExercises = new ArrayList<WorkoutExerciseItem>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedExercises.add(
                        cursor.getPosition(),
                        new WorkoutExerciseItem(
                                cursor.getString(0),
                                cursor.getInt(1),
                                cursor.getInt(2),
                                cursor.getDouble(3)
                        )
                );
            }
        }

        return loadedExercises;
    }

    private void openMenu() {
        if (fabOpen) {
            fabOpen = false;


            backgroundBlur.setVisibility(View.GONE);
            card.setVisibility(View.GONE);
        } else {
            fabOpen = true;
            backgroundBlur.setVisibility(View.VISIBLE);

            card.setVisibility(View.VISIBLE);

        }
    }


}