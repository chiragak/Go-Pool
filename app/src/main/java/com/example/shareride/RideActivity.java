package com.example.shareride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RideActivity extends AppCompatActivity {

    private Button createRideButton;
    private Button joinRideButton;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride);

        // Retrieve the userId passed from CreateProfileActivity
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // Initialize buttons
        createRideButton = findViewById(R.id.createride);
        joinRideButton = findViewById(R.id.joinride);

        // Handle create ride button click
        createRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action when "Create Ride" button is clicked
                // Add your code here
                // For example, start a new activity to create a ride
                Intent intent = new Intent(RideActivity.this, CreateRideActivity.class);
                intent.putExtra("userId", userId); // Pass the userId to the next activity
                startActivity(intent);
            }
        });

        // Handle join ride button click
        joinRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform action when "Join Ride" button is clicked
                // Add your code here
                // For example, start a new activity to join a ride
                Intent intent = new Intent(RideActivity.this, rideList.class);
                intent.putExtra("userId", userId); // Pass the userId to the next activity
                startActivity(intent);
            }
        });
    }
}
