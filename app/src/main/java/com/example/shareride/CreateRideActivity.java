package com.example.shareride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class CreateRideActivity extends AppCompatActivity {
    private TextInputEditText carModelEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText sourceEditText;
    private TextInputEditText destinationEditText;
    private TextInputEditText priceEditText;
    private TextInputEditText seatEditText;
    private Button saveButton;

    private DatabaseReference databaseReference;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ride);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("rides");

        // Initialize views
        carModelEditText = findViewById(R.id.carModelEditText);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        sourceEditText = findViewById(R.id.sourceEditText);
        destinationEditText = findViewById(R.id.destinationEditText);
        priceEditText = findViewById(R.id.priceEditText);
        seatEditText=findViewById(R.id.seatEditText);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRide();
            }
        });
    }

    private void saveRide() {
        String carModel = carModelEditText.getText().toString();
        String source = sourceEditText.getText().toString();
        String date = dateEditText.getText().toString();
        String destination = destinationEditText.getText().toString();
        String price = priceEditText.getText().toString();
        String time = timeEditText.getText().toString();



        int seat = 0;
        String seatString = seatEditText.getText().toString();

        try {
            seat = Integer.parseInt(seatString);
        } catch (NumberFormatException e) {
            // Handle the case where the input is not a valid integer
            e.printStackTrace();
            return; // Exit the method or display an error message
        }

        // Validate the input fields
        if (carModel.isEmpty() || date.isEmpty() || time.isEmpty() || source.isEmpty() || destination.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            // Generate a unique key for the ride using push()
            DatabaseReference rideRef = databaseReference.push();

            // Get the generated key
            String rideId = rideRef.getKey();
            // Create a ride object
            Ride ride = new Ride(carModel, date, time, source, destination, price,seat,rideId);



            // Save the ride to Firebase using the generated key
            if (rideId != null) {
                rideRef.setValue(ride);
                databaseReference.child(rideId).child("User ID").setValue(userId);
                Toast.makeText(this, "Ride saved successfully", Toast.LENGTH_SHORT).show();

                // Navigate to another activity if needed
                // For example, RideDetailsActivity
                Intent intent = new Intent(CreateRideActivity.this, RideActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to generate ride ID", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Ride class

}