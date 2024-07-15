package com.app.fittrackapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fittrackapp.R;
import com.app.fittrackapp.recyclerview.MealPresetItem;

import java.util.List;

public class MealPresetsAdapter extends RecyclerView.Adapter<MealPresetsAdapter.Viewholder> {





    public interface mealPresetItemInterface {
        void onItemClick(String mealUUID);
        void onAmountClick(int itemPosition);

        void updateItemAmount(int itemPosition, String mealUUID, double newAmount);
    }


    private List<MealPresetItem> mealPresetsList;
    private mealPresetItemInterface itemInterface;
    private Context context;
    public MealPresetsAdapter(List<MealPresetItem> mealPresetsList, mealPresetItemInterface itemInterface, Context context) {
        this.mealPresetsList = mealPresetsList;
        this.itemInterface = itemInterface;
        this.context = context;
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

        View viewMealPreset = inflater.inflate(R.layout.recyclerview_item_mealpreset, parent, false);

        return new Viewholder(viewMealPreset, itemInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        MealPresetItem mealPreset = mealPresetsList.get(position);

        holder.textViewMealTitle.setText(mealPreset.getMealTitle());  // Title
        holder.textViewCalories.setText(String.valueOf(mealPreset.getCalories()));  // Calories
        holder.textViewAmount.setText(convertDataToText(mealPreset.getAmount()));

        if (mealPreset.getAmount() > 0) {
            holder.buttonAdd.setColorFilter(ContextCompat.getColor(context, R.color.text_middle));
            holder.buttonRemove.setColorFilter(ContextCompat.getColor(context, R.color.text_middle));
            holder.textViewAmount.setTextColor(ContextCompat.getColor(context, R.color.text_middle));
        } else {
            holder.buttonAdd.setColorFilter(ContextCompat.getColor(context, R.color.text_low));
            holder.buttonRemove.setColorFilter(ContextCompat.getColor(context, R.color.text_low));
            holder.textViewAmount.setTextColor(ContextCompat.getColor(context, R.color.text_low));
        }


        holder.mealPresetFrame.setOnClickListener(view -> {
            holder.itemInterface.onItemClick(mealPreset.getMealUUID());

        });

        holder.buttonAdd.setOnClickListener(view -> {
            mealPreset.setAmount(mealPreset.getAmount() + 1);

            holder.itemInterface.updateItemAmount(holder.getAdapterPosition(), mealPreset.getMealUUID(), mealPreset.getAmount());
        });

        holder.buttonRemove.setOnClickListener(view -> {
            double currentAmount = mealPreset.getAmount();

            double newAmount = currentAmount - 1;
            if (newAmount < 0) { newAmount = 0; }
            mealPreset.setAmount(newAmount);

            holder.itemInterface.updateItemAmount(holder.getAdapterPosition(), mealPreset.getMealUUID(), newAmount);
        });

        holder.textViewAmount.setOnClickListener(view -> {
            holder.itemInterface.onAmountClick(holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return mealPresetsList.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder {
        ImageButton buttonRemove;
        LinearLayout mealPresetFrame;
        mealPresetItemInterface itemInterface;

        TextView textViewMealTitle;
        TextView textViewCalories;
        TextView textViewAmount;

        ImageButton buttonAdd;



        public Viewholder(@NonNull View itemView, mealPresetItemInterface itemInterface) {
            super(itemView);

            textViewMealTitle = (TextView) itemView.findViewById(R.id.textRVAddMealTitle);

            textViewCalories = (TextView) itemView.findViewById(R.id.textViewRVAddMealCalories);
            textViewAmount = (TextView) itemView.findViewById(R.id.mealAmount);

            buttonAdd = (ImageButton) itemView.findViewById(R.id.buttonRVAddMealAdd);
            buttonRemove = (ImageButton) itemView.findViewById(R.id.buttonRVAddMealSubtract);
            mealPresetFrame = (LinearLayout) itemView.findViewById(R.id.linearLayoutMealPreset);

            this.itemInterface = itemInterface;
        }
    }
}
