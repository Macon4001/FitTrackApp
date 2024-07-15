package com.app.fittrackapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.app.fittrackapp.data.DatabaseHelper;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class EditStatsActivity extends AppCompatActivity {

    private String date;
    private double[] data;
    private boolean savePossible = false;

    private Button saveButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_stats);

        // Get current date
        Intent intent = getIntent();
        if (getIntent().hasExtra("date")) {
            date = intent.getStringExtra("date");
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            date = formatter.format(new Date());
        }

        if (getIntent().hasExtra("dataBody")) {
            data = intent.getDoubleArrayExtra("dataBody");
        } else {
            data = new double[] {0, 0, 0, 0, 0, 0, 0, 0, 0};
        }


        // Set up edit-texts
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        EditText editTextChest = findViewById(R.id.editTextChest);
        EditText editTextBelly = findViewById(R.id.editTextBelly);
        EditText editTextButt = findViewById(R.id.editTextButt);
        EditText editTextWaist = findViewById(R.id.editTextWaist);
        EditText editTextArmRight = findViewById(R.id.editTextArmRight);
        EditText editTextArmLeft = findViewById(R.id.editTextArmLeft);
        EditText editTextLegRight = findViewById(R.id.editTextThighRight);
        EditText editTextLegLeft = findViewById(R.id.editTextThighLeft);




        editTextWeight.addTextChangedListener(new textWatcher(0));
        editTextChest.addTextChangedListener(new textWatcher(1));
        editTextBelly.addTextChangedListener(new textWatcher(2));
        editTextButt.addTextChangedListener(new textWatcher(3));
        editTextWaist.addTextChangedListener(new textWatcher(4));
        editTextArmRight.addTextChangedListener(new textWatcher(5));
        editTextArmLeft.addTextChangedListener(new textWatcher(6));
        editTextLegRight.addTextChangedListener(new textWatcher(7));
        editTextLegLeft.addTextChangedListener(new textWatcher(8));
        editTextWeight.setHint(convertDataToText(data[0]));
        editTextChest.setHint(convertDataToText(data[1]));
        editTextBelly.setHint(convertDataToText(data[2]));
        editTextButt.setHint(convertDataToText(data[3]));
        editTextWaist.setHint(convertDataToText(data[4]));
        editTextArmRight.setHint(convertDataToText(data[5]));
        editTextArmLeft.setHint(convertDataToText(data[6]));
        editTextLegRight.setHint(convertDataToText(data[7]));
        editTextLegLeft.setHint(convertDataToText(data[8]));


        Toolbar toolbarActivityAddStats = (Toolbar) findViewById(R.id.toolbarActivityAddStats);
        toolbarActivityAddStats.setTitle("Edit todays stats");
        setSupportActionBar(toolbarActivityAddStats);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Set up date-text
        TextView textViewDate = findViewById(R.id.textViewEditStatsDate);
        textViewDate.setText(date);

        // Set up buttons
        saveButton = findViewById(R.id.buttonSaveBodyStats);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;

                    // Save data to database
                    DatabaseHelper databaseHelper = new DatabaseHelper(EditStatsActivity.this);
                    databaseHelper.addDataBody(date, data[0] , data[1], data[2], data[3], data[4], data[5], data[6], data[7], data[8]);
                    databaseHelper.close();

                    saveButton.setBackgroundResource(R.drawable.shape_box_round_light);
                }


            }
        });

        Button cancelButton = findViewById(R.id.buttonCancelSaveBodyStats);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Main2Activity.class);
                intent.putExtra("date", date);
                startActivity(intent);
            }
        });
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
            data[id] = Double.parseDouble(editable.toString());

            saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
            savePossible = true;
        }
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