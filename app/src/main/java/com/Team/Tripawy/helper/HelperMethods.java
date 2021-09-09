package com.Team.Tripawy.helper;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.view.WindowManager;

import com.Team.Tripawy.AddNoteActivity;
import com.Team.Tripawy.AlarmService;
import com.Team.Tripawy.FloatingViewService;
import com.Team.Tripawy.Room.RDB;
import com.Team.Tripawy.SmallNotification;
import com.Team.Tripawy.Sql;
import com.Team.Tripawy.models.Trip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;

public class HelperMethods {
    public static String ACTION_PENDING = "action";
    public static void startScheduling(Context context,String date,String time,String name,String start,String end) {
        int timeInSec = 2;

        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra("name",name);
        intent.putExtra("start",start);
        intent.putExtra("end",end);
        intent.putExtra("date",date);
        intent.putExtra("time",time);
       /* FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        DatabaseReference databaseReference ;
        FirebaseDatabase firebaseDatabase;

        firebaseDatabase= FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("Temp").child(mAuth.getUid());

        Trip trip = new Trip(0,name,date,time,"Upcoming",start,end);
        databaseReference.setValue(trip);*/

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date+" "+time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PendingIntent pendingIntent = PendingIntent.getService(
                context.getApplicationContext(), (int) cal.getTimeInMillis(), intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent);

    }

    public interface OnButton{
        void onClicked();
    }
    public static void showAlertDialog(Context context, OnButton onButton,String date,String time,String name,String start,String end) {
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle("Reminder for")
                .setCancelable(false)
                .setMessage(name+"    Trip")
                .setPositiveButton("start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTrip(context,name);
                        context.startService(new Intent(context, FloatingViewService.class));

                        Executors.newSingleThreadExecutor().execute(() -> {
                            RDB.getTrips(context).insert(
                                    new Trip(name,
                                            date,
                                            time,
                                            "Done",
                                            "One Way",
                                            start,
                                            end,
                                            AddNoteActivity.notes));
                        });
                        Calendar cal = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
                        try {
                            cal.setTime(sdf.parse(date+" "+time));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, AlarmService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(
                                context.getApplicationContext(), (int) cal.getTimeInMillis(),intent , PendingIntent.FLAG_ONE_SHOT);
                        pendingIntent.cancel();
                        ///delete trip
                        Uri gmmIntentUri = Uri.parse("google.navigation:q=" + end);
                        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mapIntent.setPackage("com.google.android.apps.maps");
                        context.startActivity(mapIntent);

                        //trip state
                        dialog.dismiss();
                        onButton.onClicked();
                    }
                })
                .setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteTrip(context,name);
                        Executors.newSingleThreadExecutor().execute(() -> {
                            RDB.getTrips(context).insert(
                                    new Trip(name,
                                            date,
                                            time,
                                            "canceled",
                                            "One Way",
                                            start,
                                            end,
                                            AddNoteActivity.notes));
                        });
                        Calendar cal = Calendar.getInstance();

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
                        try {
                            cal.setTime(sdf.parse(date+" "+time));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Intent intent = new Intent(context, AlarmService.class);
                        PendingIntent pendingIntent = PendingIntent.getService(
                                context.getApplicationContext(), (int) cal.getTimeInMillis(),intent , PendingIntent.FLAG_ONE_SHOT);
                        pendingIntent.cancel();
                       //trip state
                        dialog.dismiss();
                        onButton.onClicked();
                    }
                }).setNeutralButton("snooze", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        SmallNotification.notification(context,name,start,end,date,time);

                        dialog.dismiss();
                        onButton.onClicked();
                    }
                }).create();
        alertDialog.getWindow().setType(LAYOUT_FLAG);
        alertDialog.show();
    }
    private static void deleteTrip(Context context,String name) {
        Sql helper = new Sql(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        String[] args = {name};
        String [] whereArgs = {String.valueOf(name)};
        int deletedRows = db.delete("trip", "name ==?", args);
    }
}
