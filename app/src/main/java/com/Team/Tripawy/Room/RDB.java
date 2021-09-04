package com.Team.Tripawy.Room;

import android.app.Application;
import android.content.Context;

import androidx.room.Room;

public class RDB {
    static AppDB db = null;
    public static TripDao getTrips(Context application) {
        if (db == null) {
            db = Room.databaseBuilder(application,
                    AppDB.class, "database-name").build();
            return db.tripDao();
        } else return db.tripDao();

    }
}
