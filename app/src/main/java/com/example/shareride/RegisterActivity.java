package com.example.shareride;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.ims.RegistrationManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.UUID;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {
    private EditText email;
    private EditText password;
    private EditText repassword;
    private Button login;
    private Button signUpButton;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://shareride-109be-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        email = findViewById(R.id.etMailsignup);
        password = findViewById(R.id.etPassSignup);
         repassword= findViewById(R.id.etRePass);
        signUpButton = findViewById(R.id.btnSignup);
        login = findViewById(R.id.Loginbtn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpUser();
            }
        });
    }


    private void signUpUser() {
        final String userEmail = email.getText().toString().trim();
        String userPassword = password.getText().toString().trim();
        String userRepassword = repassword.getText().toString().trim();

        if (userEmail.isEmpty() || !userEmail.contains("@")) {
            showError(email, "Email is not valid");
        } else if (userPassword.isEmpty() || userPassword.length() < 7 || !isValidPassword(userPassword)) {
            showError(password, "Password must be at least 7 characters and contain at least one uppercase letter, one lowercase letter, and one digit.");
        } else {
            if (userPassword.equals(userRepassword)) {
                final String rideId = UUID.randomUUID().toString();
                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(rideId)) {
                            Toast.makeText(RegisterActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        } else {
                            databaseReference.child("users").child(rideId).child("Email").setValue(userEmail);
                            databaseReference.child("users").child(rideId).child("Password").setValue(userPassword);

                            Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, CreateProfileActivity.class);
                            intent.putExtra("rideId", rideId);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle onCancelled if needed
                    }
                });
            } else {
                showError(repassword, "Passwords do not match");
            }
        }
    }

    private boolean isValidPassword(String password) {
        // Use regular expression to enforce constraints
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";
        return password.matches(passwordPattern);
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}