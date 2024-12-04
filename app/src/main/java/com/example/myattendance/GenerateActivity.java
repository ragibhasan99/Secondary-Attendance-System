package com.example.myattendance;

import android.os.Bundle;
import java.util.*;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GenerateActivity extends AppCompatActivity {

    Toolbar mToolBar;
    TextView codeView;
    TextView oldView;
    Button generateBtn;

    String userId;


    private FirebaseDatabase mDatabase;
    private DatabaseReference codeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        //EdgeToEdge.enable(this);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        mToolBar= findViewById(R.id.toolbar);
        setSupportActionBar(mToolBar);
        getSupportActionBar().setTitle("MyAttendance");

        codeView = findViewById(R.id.code);
        oldView = findViewById(R.id.old);
        generateBtn = findViewById(R.id.generate);

        mDatabase = FirebaseDatabase.getInstance("https://myattendance-fe1f3-default-rtdb.asia-southeast1.firebasedatabase.app/");
        codeRef = mDatabase.getReference("code");

        //codeView.setText(userId);\
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Boolean exist = false;
                        String nCode = "0000";
                        // Iterate through all child nodes (each user)
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            // Get the user ID (key) and user data (name and email)
                            String rUserId = userSnapshot.getKey();
                            if(userId.equals(rUserId)) {
                                nCode = userSnapshot.getValue(String.class);
                                exist = true;
                            }
                        }
                        if (exist == false) {
                            String code = randomString();
                            codeRef.child(userId).setValue(code);
                            codeView.setText(code);
                            oldView.setText("");
                        }
                        else {
                            oldView.setText("Unused Code: " + nCode);
                            Toast.makeText(GenerateActivity.this, "Pending Code Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Handle any errors
                    }
                });

            }
        });



    }

    public String randomString() {
        Random random = new Random();

        // Generate a random number between 1 and 9999
        int randomNumber = random.nextInt(9999) + 1;  // random.nextInt(9999) gives a number from 0 to 9998, so add 1

        // Format the number as a 4-digit string (add leading zeros if necessary)
        String formattedNumber = String.format("%04d", randomNumber);
        return formattedNumber.toString();
    }


}