package com.example.fidelmomolo.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Recycle extends AppCompatActivity {
    RecyclerView recyclerView;
    private List<Upload>mUploads;
    DatabaseReference databaseReference;
    ImageAdapter mImageAdapter;
    ProgressBar progressCircle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle);


        progressCircle=findViewById(R.id.progress_circle);

        recyclerView=findViewById(R.id.rest);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads=new ArrayList<>();

        databaseReference= FirebaseDatabase.getInstance().getReference("uploads");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){

                    Upload upload=postSnapshot.getValue(Upload.class);
                    mUploads.add(upload);

                }



                mImageAdapter=new ImageAdapter(Recycle.this,mUploads);
                recyclerView.setAdapter(mImageAdapter);
                progressCircle.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                progressCircle.setVisibility(View.INVISIBLE);
            }
        });



    }
}
