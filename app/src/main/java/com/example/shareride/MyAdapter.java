package com.example.shareride;

import static androidx.core.content.ContextCompat.startActivity;
import android.content.Intent;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>  {
    private String userId;
    private Context context;
    private ArrayList<Ride> list;
    private DatabaseReference databaseReference;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public MyAdapter(Context context, ArrayList<Ride> list, String userId) {
        this.context = context;
        this.list = list;
        this.userId = userId;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item, parent, false);
        Checkout.preload(context);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Ride ride = list.get(position);
        holder.source.setText(ride.getSource());
        holder.destination.setText(ride.getDestination());
        holder.date.setText(ride.getDate());
        holder.price.setText(ride.getPrice());

        holder.joinride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = position;
                notifyDataSetChanged();

                Ride ride = list.get(position);
                String rideID = ride.getrideId();

                if (rideID != null) {
                    double price = Double.parseDouble(ride.getPrice());

                    DatabaseReference rideRef = FirebaseDatabase.getInstance().getReference().child("rides").child(rideID);
                    DatabaseReference seatRef = rideRef.child("seat");
                    seatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Integer seats = snapshot.getValue(Integer.class);

                            if (seats != null && seats > 0) {
                                seats--;
                                final int updatedSeats = seats;
                                seatRef.setValue(updatedSeats).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (updatedSeats == 0) {
                                            Toast.makeText(context, "Seat is full", Toast.LENGTH_SHORT).show();
                                            Toast.makeText(context, "Failed booking Ride", Toast.LENGTH_SHORT).show();
                                        } else {

                                            Intent intent = new Intent(context, trial.class);
                                            intent.putExtra("price",price);
                                            context.startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(context, "Seat is full", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle onCancelled event
                        }
                    });
                }
            }
        });

        // Highlight the selected item
        if (selectedPosition == position) {
            // Set the highlighted background color or any other visual indicator
            // based on your item view's layout
            holder.itemView.setBackgroundColor(Color.LTGRAY);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    private void createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notification", "notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notification")
                .setContentTitle("Ride")
                .setContentText("New ride booked successfully")
                .setSmallIcon(R.drawable.baseline_notifications_active_24)
                .setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView source, destination, date, price;
        Button joinride;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            source = itemView.findViewById(R.id.sourceTextView);
            destination = itemView.findViewById(R.id.destinationTextView);
            date = itemView.findViewById(R.id.dateTextView);
            price = itemView.findViewById(R.id.priceTextView);
            joinride = itemView.findViewById(R.id.joinbtn);
        }
    }
}
