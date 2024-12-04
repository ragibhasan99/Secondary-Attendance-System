package com.example.myattendance;

import android.content.Intent;
import android.os.Bundle;
//import android.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolBar;
    String userId;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    List<Date> dates;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EdgeToEdge.enable(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mDatabase.getReference("student/"+userId+"/attendance");

        dates = new ArrayList<Date>();
        recyclerView = findViewById(R.id.recyclerview);

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing list (to avoid duplication in case of multiple updates)
                dates.clear();

                // Iterate through all the children of the "users" node
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming the data is in the form of "User" objects
                    String str = snapshot.getValue(String.class);
                    dates.add(new Date(str));
                }
                Display();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });

        mToolBar= findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("MyAttendance");


    }
    public void Display() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(),dates) );
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId()==R.id.log_out_opt) {
                Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(logoutIntent);
                finish();
        }

        if (item.getItemId()==R.id.lost_id_opt) {
                Intent genIntent = new Intent(MainActivity.this, GenerateActivity.class);
                genIntent.putExtra("userId", userId);
                startActivity(genIntent);
                finish();
        }

        return true;
    }
}