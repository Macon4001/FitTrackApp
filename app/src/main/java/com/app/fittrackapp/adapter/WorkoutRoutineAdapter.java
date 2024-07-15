package com.app.fittrackapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fittrackapp.R;
import com.app.fittrackapp.recyclerview.WorkoutRoutineItem;

import java.util.List;

public class WorkoutRoutineAdapter extends RecyclerView.Adapter<WorkoutRoutineAdapter.Viewholder> {




    private List<WorkoutRoutineItem> routineList;
    private WorkoutRoutineAdapter.interfaceWorkoutRoutine interfaceWorkoutRoutine;

    public WorkoutRoutineAdapter(List<WorkoutRoutineItem> routineList, WorkoutRoutineAdapter.interfaceWorkoutRoutine interfaceWorkoutRoutine) {
        this.routineList = routineList;
        this.interfaceWorkoutRoutine = interfaceWorkoutRoutine;
    }



    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View viewRoutine = inflater.inflate(R.layout.recyclerview_item_workout_routine, parent, false);

        return new Viewholder(viewRoutine, interfaceWorkoutRoutine);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

        WorkoutRoutineItem routineItem = routineList.get(position);

        String routineTitle = "  " + routineItem.getTitle().toUpperCase() + "  ";
        holder.textViewTitle.setText(routineTitle);


        if (routineItem.getIsSelected()) {


            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_light);
            holder.textViewTitle.setTextColor(Color.parseColor("#ffffff"));

        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_middle);
            holder.textViewTitle.setTextColor(Color.parseColor("#000000"));
        }

        holder.linearLayout.setOnClickListener(view -> {
            holder.interfaceWorkoutRoutine.onRoutineItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return routineList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        LinearLayout linearLayout;
        WorkoutRoutineAdapter.interfaceWorkoutRoutine interfaceWorkoutRoutine;

        public Viewholder(@NonNull View itemView, WorkoutRoutineAdapter.interfaceWorkoutRoutine interfaceWorkoutRoutine) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.routineTitle);
            linearLayout = itemView.findViewById(R.id.routineLayoutBackground);
            this.interfaceWorkoutRoutine = interfaceWorkoutRoutine;
        }
    }

    public interface interfaceWorkoutRoutine {
        void onRoutineItemClick(int itemPosition);

    }

}
