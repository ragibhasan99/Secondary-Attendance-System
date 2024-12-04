package com.example.myattendance;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code MainActivity} is the main screen of the attendance application.
 * It displays the attendance records of a user in a RecyclerView, where each record corresponds to a date.
 * The activity retrieves attendance data from Firebase Realtime Database and allows the user to log out or navigate
 * to the GenerateActivity for generating a new attendance code.
 */
public class MainActivity extends AppCompatActivity {

    // UI components and Firebase references
    private Toolbar mToolBar;
    private String userId;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private List<Date> dates;
    private RecyclerView recyclerView;

    /**
     * Called when the activity is first created. Initializes the UI components,
     * Firebase database references, and sets up the RecyclerView for displaying attendance dates.
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the userId passed from the LoginActivity
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // Initialize Firebase references
        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mDatabase.getReference("student/" + userId + "/attendance");

        // Initialize the list for storing dates
        dates = new ArrayList<>();

        // Set up the RecyclerView to display attendance data
        recyclerView = findViewById(R.id.recyclerview);

        // Fetch attendance data from Firebase
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Clear the existing list to avoid duplication on data update
                dates.clear();

                // Iterate through the dataSnapshot and add each attendance record to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String str = snapshot.getValue(String.class); // Assume the data is a string representing a date
                    dates.add(new Date(str));
                }
                // Display the updated data in the RecyclerView
                Display();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible database errors
                Log.w("MainActivity", "Failed to read value.", error.toException());
            }
        });

        // Set up the toolbar for the activity
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("MyAttendance");
    }

    /**
     * Configures and sets the adapter for the RecyclerView to display the list of dates.
     */
    public void Display() {
        // Set up the RecyclerView with a LinearLayoutManager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new MyAdapter(getApplicationContext(), dates));
    }

    /**
     * Inflates the options menu to provide additional user actions like logout or generating a new attendance code.
     * @param menu The menu to be inflated.
     * @return true if the menu was successfully created, false otherwise.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.options_menu, menu); // Inflate the options menu
        return true;
    }

    /**
     * Handles the selected options from the menu, such as logging out or generating a new attendance code.
     * @param item The menu item that was selected.
     * @return true if the item was handled successfully, false otherwise.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);

        // Handle the log-out action
        if (item.getItemId() == R.id.log_out_opt) {
            Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(logoutIntent);
            finish(); // Close the current activity so the user can't go back to it
        }

        // Handle the action to generate a new attendance code
        if (item.getItemId() == R.id.lost_id_opt) {
            Intent genIntent = new Intent(MainActivity.this, GenerateActivity.class);
            genIntent.putExtra("userId", userId); // Pass the userId to GenerateActivity
            startActivity(genIntent);
            finish(); // Close the current activity so the user can't go back to it
        }

        return true;
    }
}
