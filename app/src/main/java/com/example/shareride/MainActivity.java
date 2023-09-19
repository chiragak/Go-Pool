package com.example.shareride;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Delay navigation to the next page
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the next activity
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        }, 1000); // Delay in milliseconds (2 seconds)
    }
}
