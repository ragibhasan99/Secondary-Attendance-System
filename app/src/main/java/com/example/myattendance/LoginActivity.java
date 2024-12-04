package com.example.myattendance;

import android.os.Bundle;
import java.util.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    EditText editTextId, editTextPassword;
    Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        editTextId = findViewById(R.id.editTextId);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        mDatabaseReference = mDatabase.getReference("student");

        // Set click listener on login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userId = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();

                // Basic validation
                if (userId.isEmpty() || password.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please enter both ID and password", Toast.LENGTH_SHORT).show();
                } else {
                    DatabaseReference userRef = mDatabaseReference.child(userId);
                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // Extract the data under the 2012 node
                                String nName = snapshot.child("name").getValue(String.class);
                                String nPassword = snapshot.child("password").getValue(String.class);

                                if (password.equals(nPassword)) {
                                    // Login success, go to MainActivity
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    intent.putExtra("userId", userId);
                                    startActivity(intent);
                                    finish(); // Close the login activity so the user can't go back
                                } else {
                                    // Invalid credentials
                                    Toast.makeText(LoginActivity.this, "Invalid ID or password", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(LoginActivity.this, "User with this Id does not exist", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(LoginActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });


    }
}