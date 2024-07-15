package com.app.fittrackapp.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.fittrackapp.R;
import com.app.fittrackapp.Records;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class RecordFragment extends Fragment {

    Button btn_save;
    EditText tvTitle, tvSets;
    RecyclerView recyclerView;
    FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record, container, false);

        btn_save = view.findViewById(R.id.btn_save);
        tvSets = view.findViewById(R.id.tvSets);
        tvTitle = view.findViewById(R.id.tvTitle);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String formattedDate = df.format(c);
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("records").push();

                databaseReference.child("title").setValue(tvTitle.getText().toString());
                databaseReference.child("date").setValue(formattedDate);

                databaseReference.child("sets").setValue(tvSets.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        Query mUsersDatabaseReference3= FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("records");
        FirebaseRecyclerOptions<Records> options4 = new FirebaseRecyclerOptions.Builder<Records>().setQuery(mUsersDatabaseReference3, Records.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Records, SpViewHolder>(options4) {
            @Override
            public SpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_row, parent, false);


                return new SpViewHolder(view);


            }

            @Override
            protected void onBindViewHolder(final SpViewHolder viewHolder, int position, final Records users) {

                viewHolder.title.setText(users.getTitle());
                viewHolder.sets.setText("Sets: "+users.getSets());
                viewHolder.date.setText("Date: "+users.getDate());



            }



        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();




        return view;
    }

    public static class SpViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView title,date,sets;
        public SpViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            title = mView.findViewById(R.id.tvTitle);
            date = mView.findViewById(R.id.tvDate);
            sets = mView.findViewById(R.id.tvSets);

        }


    }

}

