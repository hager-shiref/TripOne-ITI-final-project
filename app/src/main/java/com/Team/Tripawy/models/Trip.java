package com.Team.Tripawy.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.Team.Tripawy.Room.Converter;

import java.util.ArrayList;


@Entity (tableName = "trip")
public class Trip {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String date;
    private String time;
    private String tripState;
    private String tripType;
    private String from;
    private String to;
    @TypeConverters({Converter.class})
    private ArrayList<String> notes;

    public Trip(){}
    public Trip(int id, String name, String date, String time, String tripState, String tripType, String from, String to, ArrayList<String> notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripState = tripState;
        this.tripType = tripType;
        this.from = from;
        this.to = to;
        this.notes = notes;
    }

    @Ignore
    public Trip(String name, String date, String time, String tripState, String tripType, String from, String to, ArrayList<String> notes) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripState = tripState;
        this.tripType = tripType;
        this.from = from;
        this.to = to;
        this.notes = notes;
    }
    public Trip(int id,String name, String date, String time, String tripState, String from, String to) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripState = tripState;
        this.id=id;
        this.from = from;
        this.to = to;

    }

    public String getTripState() {
        return tripState;
    }

    public void setTripState(String tripState) {
        this.tripState = tripState;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }
}