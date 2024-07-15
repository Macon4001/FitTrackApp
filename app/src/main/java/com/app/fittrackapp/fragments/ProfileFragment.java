package com.app.fittrackapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.app.fittrackapp.BmicalcActivity;
import com.app.fittrackapp.Main2Activity;
import com.app.fittrackapp.R;
import com.app.fittrackapp.data.Helper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private double[] dataGoals;
    private String[] languages;
    private String currentLanguage;
    private boolean savePossible = false;
    private boolean firstSelect = true;

    private Button saveButton,btn_save_body;
    private CardView bmi;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Cursor cursorGoals = ((Main2Activity) requireContext()).databaseHelper.getSettingsGoals();
        if (cursorGoals.getCount() > 0) {
            cursorGoals.moveToFirst();
            dataGoals = new double[] {
                    cursorGoals.getDouble(0),
                    cursorGoals.getDouble(1),
                    cursorGoals.getDouble(2),
                    cursorGoals.getDouble(3)
            };
        } else {
            dataGoals = new double[] {0, 0, 0, 0};
        }
        cursorGoals.close();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        EditText editTextCalories = getView().findViewById(R.id.editTextSettingsGoalsCal);
        EditText editTextProtein = getView().findViewById(R.id.editTextSettingsGoalsProtein);
        EditText editTextFat = getView().findViewById(R.id.editTextSettingsGoalsFat);
        EditText editTextCarbs = getView().findViewById(R.id.editTextSettingsGoalsCarbs);
        TextView name = getView().findViewById(R.id.name);
        TextView email = getView().findViewById(R.id.email);

        TextView etAge = getView().findViewById(R.id.etAge);
        TextView etHeight = getView().findViewById(R.id.etHeight);
        TextView etWeight = getView().findViewById(R.id.etWeight);

        btn_save_body = getView().findViewById(R.id.btn_save_body);
        Helper helper=new Helper(getActivity());
        SharedPreferences prfs = getActivity().getSharedPreferences("AUTHENTICATION_FILE_NAME", Context.MODE_PRIVATE);
        String stremail = prfs.getString("email", "example@exaMPLE");

        Cursor cursor=helper.getWritableDatabase().rawQuery("select * from student_record where Email=?" , new String[]{stremail});


        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    String n = snapshot.child("name").getValue().toString();
                    String e = snapshot.child("email").getValue().toString();
                    email.setText(e);
                    name.setText(n);

                }
                if(snapshot.child("body").exists()){

                    String w = snapshot.child("weight").getValue().toString();
                    String h = snapshot.child("height").getValue().toString();
                    String a = snapshot.child("age").getValue().toString();

                    etAge.setText(a);
                    etHeight.setText(h);
                    etWeight.setText(w);


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        editTextCalories.setText(convertDataToText(dataGoals[0]));
        editTextCalories.addTextChangedListener(new textWatcher(0));
        editTextFat.setText(convertDataToText(dataGoals[1]));
        editTextFat.addTextChangedListener(new textWatcher(1));
        editTextCarbs.setText(convertDataToText(dataGoals[2]));
        editTextCarbs.addTextChangedListener(new textWatcher(2));
        editTextProtein.setText(convertDataToText(dataGoals[3]));
        editTextProtein.addTextChangedListener(new textWatcher(3));


        Spinner spinner = getView().findViewById(R.id.spinnerLanguages);
        spinner.setOnItemSelectedListener(this);


        // Button
        saveButton = getView().findViewById(R.id.buttonSaveSettings);
        saveButton.setVisibility(View.INVISIBLE);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (savePossible) {
                    savePossible = false;
                    saveButton.setBackgroundResource(R.drawable.shape_box_round_middle);
                    saveButton.setVisibility(View.INVISIBLE);

                    ((Main2Activity) requireContext()).databaseHelper.setSettingsGoals(dataGoals[0], dataGoals[1], dataGoals[2], dataGoals[3]);
                    ((Main2Activity) requireContext()).databaseHelper.setSettingsLanguage(currentLanguage);


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("daily_goal");

                    databaseReference.child("calories").setValue(editTextCalories.getText().toString());
                    databaseReference.child("fats").setValue(editTextFat.getText().toString());
                    databaseReference.child("carbs").setValue(editTextCarbs.getText().toString());
                    databaseReference.child("protein").setValue(editTextProtein.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Daily Goals Saved", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });
        btn_save_body.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etHeight.getText().toString().isEmpty() && !etAge.getText().toString().isEmpty() && !etWeight.getText().toString().isEmpty()) {


                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("body");

                    databaseReference.child("age").setValue(etAge.getText().toString());
                    databaseReference.child("height").setValue(etHeight.getText().toString());
                    databaseReference.child("weight").setValue(etWeight.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getActivity(), "Body Stats Saved", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


        bmi = getView().findViewById(R.id.bmi);
        bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getActivity(), BmicalcActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if (firstSelect) {
            firstSelect = false;
            return;
        }

        switch (position) {
            case 0: currentLanguage = "de"; break;
            case 1: currentLanguage = "en"; break;
        }

        enableSaveButton();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


    private String convertDataToText(double value) {
        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
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
            dataGoals[id] = Double.parseDouble(editable.toString());

            // Update background resource of save button
            enableSaveButton();
        }
    }



    private void enableSaveButton() {
        saveButton.setBackgroundResource(R.drawable.shape_box_round_pop);
        saveButton.setVisibility(View.VISIBLE);
        savePossible = true;
    }

}