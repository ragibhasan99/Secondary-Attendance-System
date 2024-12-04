package com.example.myattendance;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * {@code LoginActivity} handles user login functionality.
 * It allows users to input their ID and password to authenticate via Firebase.
 * If the credentials are valid, the user is redirected to the main activity.
 * If invalid, the user is shown an error message.
 */
public class LoginActivity extends AppCompatActivity {

    // Firebase database reference
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    // UI components
    private EditText editTextId, editTextPassword;
    private Button buttonLogin;

    /**
     * Called when the activity is created. Sets up the layout and UI components,
     * initializes Firebase database references, and sets the login button listener.
     * @param savedInstanceState The saved state of the activity (if any).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components for user input
        editTextId = findViewById(R.id.editTextId);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        // Initialize Firebase database references
        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mDatabase.getReference("student");

        // Set click listener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the user input values
                String userId = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();

                // Basic validation to ensure that both fields are filled
                if (userId.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter both ID and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Reference the specific user from the Firebase database
                    DatabaseReference userRef = mDatabaseReference.child(userId);

                    // Add a listener to retrieve the user data
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange( DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // If the user exists, retrieve the stored name and password
                                String nName = snapshot.child("name").getValue(String.class);
                                String nPassword = snapshot.child("password").getValue(String.class);

                                // Check if the entered password matches the stored password
                                if (password.equals(nPassword)) {
                                    // If passwords match, navigate to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userId", userId); // Pass the userId to MainActivity
                                    startActivity(intent);
                                    finish(); // Close the login activity so the user can't navigate back
                                } else {
                                    // Invalid password entered
                                    Toast.makeText(LoginActivity.this, "Invalid ID or password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                // If the user does not exist in the database
                                Toast.makeText(LoginActivity.this, "User with this ID does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Handle any errors that occur during the database query
                            Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
