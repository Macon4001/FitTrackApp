package com.app.fittrackapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fittrackapp.R;
import com.app.fittrackapp.recyclerview.GeneralItem;

import java.util.List;

public class GeneralItemAdapter extends RecyclerView.Adapter<GeneralItemAdapter.Viewholder> {

    private List<GeneralItem> itemsList;

    private InterfaceItemEdit interfaceItemEdit;

    public GeneralItemAdapter(List<GeneralItem> itemsList, InterfaceItemEdit interfaceItemEdit) {
        this.itemsList = itemsList;
        this.interfaceItemEdit = interfaceItemEdit;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.recyclerview_item_general, parent, false);

        return new Viewholder(view, interfaceItemEdit);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        GeneralItem item = itemsList.get(position);

        String itemTitle = item.getTitle();
        holder.textViewTitle.setText(itemTitle);  // Title
        holder.linearLayoutMain.setOnClickListener(view -> {
            holder.interfaceItemEdit.onItemClicked(holder.getAdapterPosition());
        });

        holder.buttonRemove.setOnClickListener(view -> {

            holder.interfaceItemEdit.onButtonRemoveClicked(holder.getAdapterPosition());

        });

    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder {

        TextView textViewTitle;
        ImageButton buttonRemove;
        InterfaceItemEdit interfaceItemEdit;

        LinearLayout linearLayoutMain;


        public Viewholder(@NonNull View itemView, InterfaceItemEdit interfaceItemEdit) {
            super(itemView);
            linearLayoutMain = itemView.findViewById(R.id.layoutMain);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            buttonRemove = itemView.findViewById(R.id.buttonDeleteItem);
            this.interfaceItemEdit = interfaceItemEdit;
        }
    }

    public interface InterfaceItemEdit {
        void onItemClicked(int itemPosition);
        void onButtonRemoveClicked(int itemPosition);
    }

}
