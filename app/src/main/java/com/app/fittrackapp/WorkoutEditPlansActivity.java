package com.app.fittrackapp;

import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.app.fittrackapp.adapter.GeneralItemAdapter;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.recyclerview.GeneralItem;

import java.util.ArrayList;

public class WorkoutEditPlansActivity extends AppCompatActivity implements GeneralItemAdapter.InterfaceItemEdit {



    private int currentEditedPlanIndex;

    private ArrayList<GeneralItem> plansList;
    private GeneralItemAdapter adapterWorkoutPlans;
    private DatabaseHelper databaseHelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_editplans);

        databaseHelper = new DatabaseHelper(WorkoutEditPlansActivity.this);
        plansList = loadPlansFromDatabase();

        adapterWorkoutPlans = new GeneralItemAdapter(plansList, this);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewPlans);
        recyclerView.setAdapter(adapterWorkoutPlans);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Button buttonCreatePlan = findViewById(R.id.buttonCreatePlan);
        buttonCreatePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogCreatePlan();
            }
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }


    @Override
    public void onItemClicked(int itemPosition) {
        showDialogEditPlanName(plansList.get(itemPosition).getTitle(), itemPosition);
        currentEditedPlanIndex = itemPosition;
    }

    @Override
    public void onButtonRemoveClicked(int itemPosition) {
        if (plansList.size() == 1) {
            showDialogError();
            return;
        }

        databaseHelper.deleteWorkoutPlan(plansList.get(itemPosition).getTitle());

        // Update recycler view
        plansList.remove(itemPosition);
        adapterWorkoutPlans.notifyItemRemoved(itemPosition);
    }
    private ArrayList<GeneralItem> loadPlansFromDatabase() {
        Cursor cursor = databaseHelper.getWorkoutPlans();
        ArrayList<GeneralItem> loadedPlans = new ArrayList<GeneralItem>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedPlans.add(cursor.getPosition(), new GeneralItem(cursor.getString(0)));
            }
        }

        return loadedPlans;
    }

    private void showDialogEditPlanName(String currentPlanName, int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        editTextPlanName.setText(currentPlanName);

        builder.setTitle("Edit plan name");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String newPlanName = editTextPlanName.getText().toString();

                if (newPlanName.length() <= 0) {
                    return;
                }

                databaseHelper.updateWorkoutPlanName(currentPlanName, newPlanName);

                plansList.get(itemPosition).setTitle(newPlanName);
                adapterWorkoutPlans.notifyItemChanged(itemPosition);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogCreatePlan() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        editTextPlanName.setHint("Enter plan name");

        builder.setTitle("Create New Workout Plan");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Create plan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String text = editTextPlanName.getText().toString();

                if (text.length() <= 0) {
                    return;
                }

                int insertIndex = plansList.size();
                plansList.add(insertIndex, new GeneralItem(text));
                adapterWorkoutPlans.notifyItemInserted(insertIndex);

                databaseHelper.addWorkoutPlan(text);
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDialogError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        builder.setTitle("You must have at least 1 workout plan!");
        builder.setPositiveButton("Okay", null);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}