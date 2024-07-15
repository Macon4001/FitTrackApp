package com.app.fittrackapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
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
import java.util.Locale;


public class MealsAddDailyEntryActivity extends AppCompatActivity implements MealPresetsAdapter.mealPresetItemInterface, AdapterView.OnItemSelectedListener {



    private String date;
    private boolean savePossible = false;

    private Button saveBtn;
    private Button cancelBtn;
    TextView noEntries;

    RecyclerView recyclerViewMeals;

    private String[] mealCategories;
    private int currentCategoryIndex = 0;
    private static final String allCategories = "All";
    private ArrayList<MealPresetItem> mealsPresetList;
    private MealPresetsAdapter adapterPresets;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_adddailyentry);

        FrameLayout loadingLayout = findViewById(R.id.loadingLayout);
        loadingLayout.setVisibility(View.VISIBLE);

        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        databaseHelper = new DatabaseHelper(MealsAddDailyEntryActivity.this);

        // Load categories from database
        mealCategories = loadMealCategoriesFromDatabase();

        // Set up spinner
        Spinner spinner = findViewById(R.id.spinnerMealPresetCategory);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterCategories = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_dark, mealCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);


        mealsPresetList = loadPresetMealsFromDatabase(mealCategories[currentCategoryIndex]);

        adapterPresets = new MealPresetsAdapter(mealsPresetList, this, getApplicationContext());
        recyclerViewMeals = findViewById(R.id.recyclerViewMealsPreset);
        recyclerViewMeals.setAdapter(adapterPresets);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

        noEntries = findViewById(R.id.textViewNoEntries);
        if (mealsPresetList.isEmpty()) {

            noEntries.setVisibility(View.VISIBLE);
        }

        loadingLayout.setVisibility(View.INVISIBLE);

        TextView textViewDate = findViewById(R.id.textViewDate);
        textViewDate.setText(date);

        Button buttonAddMeal = findViewById(R.id.buttonAddMeal);
        buttonAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start new activity Activity_CreateMeal
                Intent intent = new Intent(view.getContext(), MealsCreateEditPresetActivity.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
                startActivity(intent);
            }
        });

        saveBtn = findViewById(R.id.buttonAddMealSave);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;

                    for (MealPresetItem currentMeal : mealsPresetList) {
                        if (currentMeal.getAmount() > 0) {
                            databaseHelper.addOrReplaceConsumedMeal(date, currentMeal.getMealUUID(), currentMeal.getAmount());
                        }
                    }

                    saveBtn.setBackgroundResource(R.drawable.shape_box_round_light);
                    cancelBtn.setText("Back");
                    Snackbar.make(view, "Saved", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        cancelBtn = findViewById(R.id.buttonAddMealCancel);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Main2Activity.class);
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
            saveBtn.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveBtn.setTextColor(getColor(R.color.white));
        }
        adapterPresets.notifyItemChanged(itemPosition);
    }

    @Override
    public void onItemClick(String mealUUID) {
        Intent intent = new Intent(getApplicationContext(), MealsCreateEditPresetActivity.class);
        if (date != null) {
            intent.putExtra("date", date);
        }
        intent.putExtra("mode", "edit");
        intent.putExtra("uuid", mealUUID);
        startActivity(intent);
    }

    @Override
    public void onAmountClick(int itemPosition) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);

        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edittext, null);
        builder.setView(view);

        EditText editTextPlanName = view.findViewById(R.id.dialogEditText);
        if (mealsPresetList.get(itemPosition).getAmount() != 0) {
            editTextPlanName.setText(convertDataToText(mealsPresetList.get(itemPosition).getAmount()));
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

                mealsPresetList.get(itemPosition).setAmount(Double.parseDouble(textInput));
                adapterPresets.notifyItemChanged(itemPosition);

                if (!savePossible) {
                    savePossible = true;
                    saveBtn.setBackgroundResource(R.drawable.shape_box_round_pop);
                    saveBtn.setTextColor(getColor(R.color.white));
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (position == currentCategoryIndex) {
            return;
        }

        currentCategoryIndex = position;

        mealsPresetList.clear();
        adapterPresets.notifyDataSetChanged();

        mealsPresetList = loadPresetMealsFromDatabase(mealCategories[position]);

        if (mealsPresetList.isEmpty()) {
            noEntries.setVisibility(View.VISIBLE);
            return;
        }
        noEntries.setVisibility(View.INVISIBLE);

        adapterPresets = new MealPresetsAdapter(mealsPresetList, this, getApplicationContext());
        recyclerViewMeals = findViewById(R.id.recyclerViewMealsPreset);
        recyclerViewMeals.setAdapter(adapterPresets);
        recyclerViewMeals.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Pass
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


    private String[] loadMealCategoriesFromDatabase() {
        Cursor cursorCat = databaseHelper.getPresetMealCategories();
        String[] loadedCategories = new String[0];

        if (cursorCat.getCount() > 0) {
            loadedCategories = new String[cursorCat.getCount() + 1];
            loadedCategories[0] = allCategories;

            int i = 1;
            while (cursorCat.moveToNext()) {
                loadedCategories[i] = cursorCat.getString(0);
                i++;
            }
        }
        cursorCat.close();

        return loadedCategories;
    }

    private ArrayList<MealPresetItem> loadPresetMealsFromDatabase(String category) {
        Cursor cursor;

        if (category.equals(allCategories)) {
            cursor = databaseHelper.getPresetMealsSimpleAllCategories();
        } else {
            cursor = databaseHelper.getPresetMealsSimpleFromCategory(category);
        }

        ArrayList<MealPresetItem> loadedPresets = new ArrayList<MealPresetItem>();

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                loadedPresets.add(cursor.getPosition(), new MealPresetItem(
                        cursor.getString(1),
                        cursor.getString(0),
                        (int) cursor.getDouble(2),
                        0  // Amount
                ));
            }
        }
        cursor.close();

        return loadedPresets;
    }

}