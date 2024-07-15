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
import com.app.fittrackapp.recyclerview.WorkoutPlanItem;

import java.util.List;

public class WorkoutPlansAdapter extends RecyclerView.Adapter<WorkoutPlansAdapter.Viewholder> {





    public interface InterfaceWorkoutPlans {
        void onPlanItemClick(int itemPosition);
    }


    private List<WorkoutPlanItem> plansList;
    private InterfaceWorkoutPlans interfaceWorkoutPlans;

    public WorkoutPlansAdapter(List<WorkoutPlanItem> plansList, InterfaceWorkoutPlans interfaceWorkoutPlans) {
        this.plansList = plansList;
        this.interfaceWorkoutPlans = interfaceWorkoutPlans;
    }




    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_item_workout_routine, parent, false);

        return new Viewholder(view, interfaceWorkoutPlans);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        WorkoutPlanItem planItem = plansList.get(position);

        String planTitle = "  " + planItem.getTitle().toUpperCase() + "  ";
        holder.textViewTitle.setText(planTitle);

        if (planItem.getIsSelected()) {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_light);
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.shape_box_round_middle);
        }

        // Set onClick-Events
        holder.linearLayout.setOnClickListener(view -> {
            holder.interfaceWorkoutPlans.onPlanItemClick(holder.getAdapterPosition());
        });

    }

    @Override
    public int getItemCount() {
        return plansList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        LinearLayout linearLayout;

        InterfaceWorkoutPlans interfaceWorkoutPlans;

        public Viewholder(@NonNull View itemView, InterfaceWorkoutPlans interfaceWorkoutPlans) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.routineTitle);
            linearLayout = itemView.findViewById(R.id.routineLayoutBackground);
            this.interfaceWorkoutPlans = interfaceWorkoutPlans;
        }
    }
}
