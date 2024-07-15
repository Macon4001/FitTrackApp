package com.app.fittrackapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fittrackapp.R;
import com.app.fittrackapp.recyclerview.WorkoutExerciseItem;

import java.util.List;

public class WorkoutExerciseAdapter extends RecyclerView.Adapter<WorkoutExerciseAdapter.Viewholder> {




    public interface InterfaceWorkoutExercises {
        void onExerciseItemClick(int itemPosition);
    }


    private List<WorkoutExerciseItem> exerciseList;
    private InterfaceWorkoutExercises interfaceWorkoutExercises;

    public WorkoutExerciseAdapter(List<WorkoutExerciseItem> exerciseList, InterfaceWorkoutExercises interfaceWorkoutExercises) {
        this.exerciseList = exerciseList;
        this.interfaceWorkoutExercises = interfaceWorkoutExercises;
    }



    private String convertDataToText(double value) {

        if (value % 1 == 0) {
            return String.valueOf((int) value);
        } else {
            return String.valueOf(value);
        }
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate custom layout
        View viewExercise = inflater.inflate(R.layout.recylcerview_item_workout_exercise, parent, false);

        // Return new holder instance
        return new Viewholder(viewExercise, interfaceWorkoutExercises);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        // Get the data-class based on position
        WorkoutExerciseItem excerciseItem = exerciseList.get(position);

        // Set item views based on views and data-class
        holder.textViewTitle.setText(excerciseItem.getTitle());  // Title
        holder.textViewSets.setText(String.valueOf(excerciseItem.getSets()));  // Sets
        holder.textViewReps.setText(String.valueOf(excerciseItem.getRepetitions()));  // Repetitions
        holder.textViewWeight.setText(convertDataToText(excerciseItem.getWeight()));  // Weight

        // Set onClick-Events
        holder.linearLayoutMain.setOnClickListener(view -> {
            holder.interface_rv_exercises.onExerciseItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return exerciseList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewSets;

        TextView textViewReps;
        TextView textViewWeight;

        LinearLayout linearLayoutMain;
        InterfaceWorkoutExercises interface_rv_exercises;

        public Viewholder(@NonNull View itemView, InterfaceWorkoutExercises interface_rv_exercises) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.rvItemExerciseTitle);
            textViewSets = itemView.findViewById(R.id.rvItemExerciseSets);

            textViewReps = itemView.findViewById(R.id.rvItemExerciseRepetitions);
            textViewWeight = itemView.findViewById(R.id.rvItemExerciseWeight);
            linearLayoutMain = itemView.findViewById(R.id.rvItemExerciseMain);

            this.interface_rv_exercises = interface_rv_exercises;
        }
    }

}
