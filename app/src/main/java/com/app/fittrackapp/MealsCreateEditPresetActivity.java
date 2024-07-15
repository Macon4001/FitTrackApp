package com.app.fittrackapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.fittrackapp.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


public class MealsCreateEditPresetActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {





    private double[] mealData = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    private boolean savePossible = false;
    private String date;


    private String mode = "create";

    private Button saveButton;
    private Button cancelButton;
    private EditText editTextMealName;

    private DatabaseHelper databaseHelper;

    private String mealName;
    private String mealUUID;
    private String[] mealCategories;
    private String selectedMealCategory;




    private String[] loadMealCategoriesFromDatabase() {
        Cursor cursorCat = databaseHelper.getPresetMealCategories();
        String[] loadedCategories = new String[0];

        if (cursorCat.getCount() > 0) {
            loadedCategories = new String[cursorCat.getCount()];
            int i = 0;
            while (cursorCat.moveToNext()) {
                loadedCategories[i] = cursorCat.getString(0);
                i++;
            }
        }
        cursorCat.close();

        return loadedCategories;
    }

    private String convertDataToText(double value) {
        // Convert given double to string.
        if (value % 1 == 0) {
            // -> Value has only .0 decimals. Cut it out by converting to int.
            return String.valueOf((int) value);
        } else {
            // -> Value has decimals. Round up to 2 decimal-digits.
            DecimalFormat df = new DecimalFormat("#####.##");
            return String.valueOf(df.format(value));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_createeditpreset);

        databaseHelper = new DatabaseHelper(MealsCreateEditPresetActivity.this);

        mealCategories = loadMealCategoriesFromDatabase();
        selectedMealCategory = mealCategories[0];

        Spinner spinner = findViewById(R.id.spinnerMealCategory);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter adapterCategories = new ArrayAdapter(getApplicationContext(), R.layout.spinner_item_purple_middle, mealCategories);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterCategories);

        // -----------------------------------------------------------------------------------------
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        }

        if (getIntent().hasExtra("mode")) {
            if (intent.getStringExtra("mode").equals("edit")) {
                mode = "edit";

                String uuid = intent.getStringExtra("uuid");
                Cursor cursorData = databaseHelper.getPresetMealDetails(uuid);
                if (cursorData.getCount() > 0) {
                    cursorData.moveToFirst();
                    mealUUID = cursorData.getString(0);
                    mealName = cursorData.getString(1);
                    selectedMealCategory = cursorData.getString(2);
                    mealData = new double[]{
                            cursorData.getDouble(3), cursorData.getDouble(4),
                            cursorData.getDouble(5), cursorData.getDouble(6), cursorData.getDouble(7), cursorData.getDouble(8),
                            cursorData.getDouble(9), cursorData.getDouble(10), cursorData.getDouble(11), cursorData.getDouble(12),
                            cursorData.getDouble(13), cursorData.getDouble(14), cursorData.getDouble(15), cursorData.getDouble(16),
                            cursorData.getDouble(17), cursorData.getDouble(18), cursorData.getDouble(19), cursorData.getDouble(20),
                            cursorData.getDouble(21), cursorData.getDouble(22), cursorData.getDouble(23), cursorData.getDouble(24),
                            cursorData.getDouble(25), cursorData.getDouble(26), cursorData.getDouble(27), cursorData.getDouble(28),
                            cursorData.getDouble(29), cursorData.getDouble(30), cursorData.getDouble(31), cursorData.getDouble(32),
                            cursorData.getDouble(33)
                    };
                }
                cursorData.close();

                // Set spinner item
                List<String> mealsList = Arrays.asList(mealCategories);
                spinner.setSelection(mealsList.indexOf(selectedMealCategory));
            }
        }

        // -----------------------------------------------------------------------------------------
        // Set up edit-texts
        editTextMealName = findViewById(R.id.editTextMealName);
        if (mealName != null) {
            editTextMealName.setText(mealName);
        }
        editTextMealName.addTextChangedListener(new textWatcher(0));

        // TODO: Add new edit texts for new items
        EditText editTextCalories = findViewById(R.id.editTextCalories);
        EditText editTextFat = findViewById(R.id.editTextFat);
        EditText editTexFatSat = findViewById(R.id.editTextFatSat);
        EditText editTextCarbs = findViewById(R.id.editTextCarbs);
        EditText editTextSugar = findViewById(R.id.editTextSugar);

        editTextCalories.setHint(convertDataToText(mealData[0]));
        EditText editTextProtein = findViewById(R.id.editTextProtein);
        EditText editTextSalt = findViewById(R.id.editTextSalt);
        EditText editTextFiber = findViewById(R.id.editTextFiber);



        editTextCalories.addTextChangedListener(new textWatcher(1));
        editTextFat.setHint(convertDataToText(mealData[1]));
        editTextFat.addTextChangedListener(new textWatcher(2));
        editTexFatSat.setHint(convertDataToText(mealData[2]));
        editTexFatSat.addTextChangedListener(new textWatcher(3));
        editTextCarbs.setHint(convertDataToText(mealData[3]));
        editTextCarbs.addTextChangedListener(new textWatcher(4));
        editTextSugar.setHint(convertDataToText(mealData[4]));
        editTextSugar.addTextChangedListener(new textWatcher(5));
        editTextProtein.setHint(convertDataToText(mealData[5]));
        editTextProtein.addTextChangedListener(new textWatcher(6));
        editTextSalt.setHint(convertDataToText(mealData[6]));
        editTextSalt.addTextChangedListener(new textWatcher(7));
        editTextFiber.setHint(convertDataToText(mealData[7]));
        editTextFiber.addTextChangedListener(new textWatcher(8));



        Toolbar toolbarActivityCreateMeal = (Toolbar) findViewById(R.id.toolbarActivityCreateMeal);
        if (mode.equals("create")) {
            toolbarActivityCreateMeal.setTitle(getResources().getString(R.string.dn_button_add));
        } else if (mode.equals("edit")) {
            toolbarActivityCreateMeal.setTitle(getResources().getString(R.string.dn_button_edit));
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setDisplayShowHomeEnabled(true);


        saveButton = findViewById(R.id.buttonSaveNewMeal);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {

                    if (mealName == null) {


                        Toast.makeText(getApplicationContext(), "Please enter a name first!", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    savePossible = false;

                    if (mealUUID == null) {
                        mealUUID = UUID.randomUUID().toString().substring(0, 8);
                    }


                    databaseHelper.addOrReplacePresetMeal(mealUUID, mealName, selectedMealCategory, mealData);
                    databaseHelper.close();

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                    saveButton.setTextColor(getColor(R.color.white));
                    cancelButton.setText(R.string.button_text_back);
                }
            }
        });

        cancelButton = findViewById(R.id.buttonCancelNewMeal);


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MealsAddDailyEntryActivity.class);
                if (date != null) {
                    intent.putExtra("date", date);
                }
                // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);  // Start activity without animation
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
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedMealCategory = mealCategories[position];
        savePossible = true;
    }


    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
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
            if (id == 0) {
                if (editable.toString().length() > 28) {
                    editTextMealName.setText(mealName);
                    Toast.makeText(getApplicationContext(), "Text limit reached!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mealName = editable.toString();
            } else {
                if(!editable.toString().isEmpty()){
                    mealData[id - 1] = Double.parseDouble(editable.toString());

                }
            }

            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            saveButton.setTextColor(getColor(R.color.white));
            savePossible = true;
            cancelButton.setText(R.string.button_text_cancel);
        }
    }
}