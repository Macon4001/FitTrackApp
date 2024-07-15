package com.app.fittrackapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.app.fittrackapp.adapter.MealPresetsAdapter;
import com.app.fittrackapp.data.DatabaseHelper;
import com.app.fittrackapp.recyclerview.MealPresetItem;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MealsMealsOfDayActivity extends AppCompatActivity implements MealPresetsAdapter.mealPresetItemInterface {


    private String date;
    private boolean savePossible = false;

    private Button saveButton;
    private Button cancelButton;

    private ArrayList<MealPresetItem> mealsList;
    private MealPresetsAdapter adapter;
    private HashMap<String, Double> mealsStart = new HashMap<>();

    private DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_mealsofday);

        FrameLayout loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        getSupportActionBar().setTitle(getResources().getString(R.string.dn_button_edit));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
     //   getSupportActionBar().setDisplayShowHomeEnabled(true);

        mealsList = new ArrayList<MealPresetItem>();

        databaseHelper = new DatabaseHelper(MealsMealsOfDayActivity.this);
        Cursor cursor = databaseHelper.getConsumedMeals(date);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                mealsList.add(cursor.getPosition(), new MealPresetItem(
                        cursor.getString(1),  // Title
                        cursor.getString(0),  // UUID
                        (int) cursor.getDouble(2),  // Calories
                        cursor.getDouble(3)  // Amount
                ));

                mealsStart.put(cursor.getString(0), cursor.getDouble(3));
            }
        }
        cursor.close();

        adapter = new MealPresetsAdapter(mealsList, this, getApplicationContext());

        RecyclerView recyclerViewMeals = findViewById(R.id.recyclerViewMealsEaten);
        recyclerViewMeals.setAdapter(adapter);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        // Remove loading screen -------------------------------------------------------------------
        if (mealsList.isEmpty()) {
            TextView noEntries = findViewById(R.id.textViewNoEntries);
            noEntries.setVisibility(View.VISIBLE);
        }

        loadingLayout.setVisibility(View.INVISIBLE);

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(date);

        // Set up buttons --------------------------------------------------------------------------

        saveButton = findViewById(R.id.buttonAddMealSave);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;

                    for (MealPresetItem currentMeal : mealsList) {
                        String currentUUID = currentMeal.getMealUUID();
                        double currentAmount = currentMeal.getAmount();
                        double startAmount = mealsStart.get(currentUUID);

                        if ((currentAmount > 0) && (currentAmount != startAmount)) {
                            // Delete entry
                            databaseHelper.removeConsumedMeal(date, currentUUID);
                            // Add entry with new amount
                            databaseHelper.addOrReplaceConsumedMeal(date, currentUUID, currentAmount);
                        } else if (currentAmount <= 0) {
                            // Delete entry
                            databaseHelper.removeConsumedMeal(date, currentUUID);
                        }

                        mealsStart.replace(currentUUID, currentAmount);
                    }

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    saveButton.setTextColor(getColor(R.color.white));
                    cancelButton.setText(R.string.button_text_back);
                    Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton = findViewById(R.id.buttonAddMealCancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start new activity Activity_Main (= go back to main screen)
                Intent intent = new Intent(view.getContext(), Main2Activity.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
                startActivity(intent);
            }
        });

        Button buttonAddMeal = findViewById(R.id.buttonAddMeal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MealsAddDailyEntryActivity.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
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
    public void updateItemAmount(int itemPosition, String mealUUID, double newAmount) {

        if (!savePossible) {
            savePossible = true;
            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.white));
        }

        adapter.notifyItemChanged(itemPosition);
    }

    @Override
    public void onItemClick(String mealUUID) {
        // Pass
    }

    @Override
    public void onAmountClick(int itemPosition) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        if (mealsList.get(itemPosition).getAmount() != 0) {
            editTextPlanName.setText(convertDataToText(mealsList.get(itemPosition).getAmount()));
        }
        editTextPlanName.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        builder.setTitle("Change amount");
        builder.setNegativeButton("Cancel", null);
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textInput = editTextPlanName.getText().toString();

                if (textInput.length() <= 0) {
                    return;
                }

                mealsList.get(itemPosition).setAmount(Double.parseDouble(textInput));
                adapter.notifyItemChanged(itemPosition);

                if (!savePossible) {
                    savePossible = true;
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
                    saveButton.setTextColor(getColor(R.color.white));
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private String convertDataToText(double value) {
        // Convert given double to string.
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            DecimalFormat df = new DecimalFormat("#####.##");
            return String.valueOf(df.format(value));
        }
    }

}