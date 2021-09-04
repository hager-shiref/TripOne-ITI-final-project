package com.Team.Tripawy.Room;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.Team.Tripawy.models.Trip;

@Database(entities = Trip.class, version = 1)
@TypeConverters({Converter.class})
public abstract class AppDB extends RoomDatabase {
    public abstract TripDao tripDao();
}