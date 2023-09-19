package com.example.shareride;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateProfileActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText ageEditText;
    private EditText aadhaarEditText;
    private EditText addressEditText;
    private Button saveButton;

    private DatabaseReference databaseReference;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        // Retrieve the userId passed from RegisterActivity
        Intent intent = getIntent();
        userId = getIntent().getStringExtra("userId");

        // Initialize the database reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize views
        nameEditText = findViewById(R.id.etMailsignup);
        ageEditText = findViewById(R.id.age);
        aadhaarEditText = findViewById(R.id.aadhaar);
        addressEditText = findViewById(R.id.address);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    private void saveProfile() {
        String name = nameEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String aadhaar = aadhaarEditText.getText().toString();
        String address = addressEditText.getText().toString();

        // Validate the input fields
        if (name.isEmpty() || age.isEmpty() || aadhaar.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
        } else {
            // Create a profile object
            Profile profile = new Profile(userId, name, age, aadhaar, address);

            // Save the profile to Firebase using the userId as the primary key
            if (userId != null) {
//                databaseReference.child("profile").setValue(profile);
                databaseReference.child("profile").child(userId).setValue(profile);
                Toast.makeText(this, "Profile saved successfully", Toast.LENGTH_SHORT).show();

                // Navigate to RideActivity and pass the userId
                Intent intent = new Intent(CreateProfileActivity.this, RideActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "User ID is null", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Profile class
    public static class Profile {
        private String userId;
        private String name;
        private String age;
        private String aadhaar;
        private String address;

        public Profile() {
            // Empty constructor required for Firebase
        }

        public Profile(String userId, String name, String age, String aadhaar, String address) {
            this.userId = userId;
            this.name = name;
            this.age = age;
            this.aadhaar = aadhaar;
            this.address = address;
        }

        public String getUserId() {
            return userId;
        }

        public String getName() {
            return name;
        }

        public String getAge() {
            return age;
        }

        public String getAadhaar() {
            return aadhaar;
        }

        public String getAddress() {
            return address;
        }
    }
}