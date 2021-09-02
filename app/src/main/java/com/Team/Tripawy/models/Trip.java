package com.Team.Tripawy.models;

import java.util.ArrayList;
import java.util.List;





public class Trip {
    private int id;
    private String name;
    private String date;
    private String time;
    private String tripState;
    private String  tripType;
    private String  tripRepeat;
    private String from;
    private String to;
    private List<String> notes;
    public Trip() {

    }


    public Trip(int id, String name, String date, String time, String  tripState, String  tripType, String  tripRepeat, String from, String to, List<String> notes) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.tripState = tripState;
        this.tripType = tripType;
        this.tripRepeat = tripRepeat;
        this.from = from;
        this.to = to;
        this.notes = notes;
    }
    public Trip(int id, String name, String  date, String  time, String from, String to) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.from = from;
        this.to = to;
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

    public String  getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String  time) {
        this.time = time;
    }

    public String  getTripState() {
        return tripState;
    }

    public void setTripState(String  tripState) {
        this.tripState = tripState;
    }

    public String  getTripType(String s) {
        return tripType;
    }

    public void setTripType(String  tripType) {
        this.tripType = tripType;
    }

    public String  getTripRepeat(String s) {
        return tripRepeat;
    }

    public void setTripRepeat(String  tripRepeat) {
        this.tripRepeat = tripRepeat;
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }
}
