package com.example.myattendance;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

/**
 * {@code GenerateActivity} is an activity that allows a user to generate an attendance code.
 * The activity checks if the user already has a pending code in the Firebase Realtime Database.
 * If no code exists, it generates a new random code, saves it to Firebase, and displays it on the screen.
 * If a pending code exists, it displays the existing code.
 */
public class GenerateActivity extends AppCompatActivity {

    // UI elements
    private Toolbar mToolBar;
    private TextView codeView;
    private TextView oldView;
    private Button generateBtn;

    // User ID passed via Intent
    private String userId;

    // Firebase Database references
    private FirebaseDatabase mDatabase;
    private DatabaseReference codeRef;

    /**
     * Called when the activity is first created.
     * Initializes the UI, sets up the Firebase references, and sets the onClick listener for the generate button.
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);

        // Get the user ID from the previous activity's intent
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // Set up the toolbar
        mToolBar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("MyAttendance");

        // Initialize UI components
        codeView = findViewById(R.id.code);
        oldView = findViewById(R.id.old);
        generateBtn = findViewById(R.id.generate);

        // Initialize Firebase database references
        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        codeRef = mDatabase.getReference("code");

        // Set up the "Generate" button click listener
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Query the Firebase database to check if the user already has a pending code
                codeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean exist = false;
                        String nCode = "0000"; // Default code in case no code is found

                        // Iterate through all child nodes (users) in the "code" reference
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String rUserId = userSnapshot.getKey();
                            if (userId.equals(rUserId)) {
                                // If the user already has a code, store it
                                nCode = userSnapshot.getValue(String.class);
                                exist = true;
                            }
                        }

                        if (!exist) {
                            // If no pending code exists, generate a new code, save it to Firebase, and display it
                            String code = randomString();
                            codeRef.child(userId).setValue(code);
                            codeView.setText(code);
                            oldView.setText(""); // Clear any old code message
                        } else {
                            // If a pending code exists, display it and show a toast message
                            oldView.setText("Unused Code: " + nCode);
                            Toast.makeText(GenerateActivity.this, "Pending Code Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors that occur while reading from Firebase
                    }
                });
            }
        });
    }

    /**
     * Generates a random 4-digit string code between 1 and 9999.
     * The code is formatted as a 4-digit string with leading zeros if necessary.
     *
     * @return A 4-digit formatted string representing a random code.
     */
    public String randomString() {
        Random random = new Random();

        // Generate a random number between 1 and 9999
        int randomNumber = random.nextInt(9999) + 1;

        // Format the number as a 4-digit string (with leading zeros if necessary)
        return String.format("%04d", randomNumber);
    }
}
