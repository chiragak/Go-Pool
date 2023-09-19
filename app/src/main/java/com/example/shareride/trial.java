package com.example.shareride;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class trial extends AppCompatActivity implements PaymentResultListener {
    private double price;
    public trial() throws URISyntaxException {
    }
    // ...
    TextView pricespace;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trial);

        // Initialize Razorpay
        Checkout.preload(getApplicationContext());
        pricespace=findViewById(R.id.priceid);
        Button joinButton = findViewById(R.id.join);
        Intent intent=getIntent();
        price=intent.getDoubleExtra("price",0.0);
        pricespace.setText(String.valueOf(price) );
        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRazorpayPayment();
            }
        });

    }

    private void startRazorpayPayment() {
        // Create a new instance of Checkout
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_s09yzKgRBxBJcA");

        // Set payment options
        JSONObject options = new JSONObject();
        try {
            options.put("name", "Go Pool");
            options.put("description", "Car Pooling Applocation");
            options.put("currency", "INR");
            options.put("amount", (price * 100)); // amount in paise (e.g., Rs. 10)
            options.put("prefill.email", "customer@example.com");
            options.put("prefill.contact", "9876543210");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Start the payment activity
        checkout.open(trial.this, options);
    }

    @Override
    public void onPaymentSuccess(String paymentId) {
        // Payment succeeded, handle the success event here
        Toast.makeText(this, "Payment successful. ID: " + paymentId, Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "notification")
                .setContentTitle("Ride")
                .setContentText("New ride booked successfully")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        managerCompat.notify(1, builder.build());
        Intent intent=new Intent(this,rideList.class);
        startActivity(intent);
    }


    @Override
    public void onPaymentError(int code, String description) {
        // Payment failed, handle the error event here
        Toast.makeText(this, "Payment failed. Code: " + code + ", Description: " + description, Toast.LENGTH_SHORT).show();
    }
}
