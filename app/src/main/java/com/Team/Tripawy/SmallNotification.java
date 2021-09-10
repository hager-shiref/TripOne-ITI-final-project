package com.Team.Tripawy;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SmallNotification extends Activity {
    Context context;
    final int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }
    public static void notification(Context context,String name,String start,String end , String date,String time){



        Intent intent = new Intent(context, AlarmService.class);
        intent.putExtra("name",name);
        intent.putExtra("start",start);
        intent.putExtra("end",end);
        intent.putExtra("date",date);
        intent.putExtra("time",time);

        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            cal.setTime(sdf.parse(date+" "+time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getService(context, (int) cal.getTimeInMillis() , intent,
                PendingIntent.FLAG_ONE_SHOT);




        String channelId = "id";

        Uri defaultSoundUri2 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context, channelId)
                        .setContentTitle("You are waiting for")
                        .setSmallIcon(R.drawable.logo)
                        .setContentText(name+" Trip")
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setSound(defaultSoundUri2)
                        .setOngoing(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = notificationBuilder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);

        }
        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-M-yy HH:mm", Locale.ENGLISH);
        try {
            calendar.setTime(sdf.parse(date+" "+time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        notificationManager.notify( (int) calendar.getTimeInMillis(), notificationBuilder.build());

    }




}