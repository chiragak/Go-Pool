package com.example.shareride;

import org.json.JSONObject;

public class Ride {
    private String source;
    private String time;
    private String destination;
    private String date;
    private String price;
    private String carModel;
    private String rideId;
    private int seat;

    public Ride() {
        // Default constructor required for Firebase deserialization
    }
//    Ride ride = new Ride(carModel, date, time, source, destination, price,seat);
public Ride(String carModel, String date, String time, String source, String destination, String price, int seat, String rideId) {
    this.carModel = carModel;
    this.date = date;
    this.time = time;
    this.source = source;
    this.destination = destination;
    this.price = price;
    this.seat = seat;
    this.rideId = rideId;
}
    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public String getrideId() {
        return rideId;
    }

    public String getCarModel() {
        return carModel;
    }

    public int getSeat() {
        return seat;
    }

    public void setKeyID(java.lang.String s) {

    }

    public void open(MyAdapter activity, JSONObject jsonObject) {
    }
}
