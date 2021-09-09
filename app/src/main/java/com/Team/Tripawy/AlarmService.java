package com.Team.Tripawy;

import static com.Team.Tripawy.helper.HelperMethods.showAlertDialog;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class AlarmService extends Service {

    private MediaPlayer mp;

    @Override
    public void onCreate() {
        super.onCreate();


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ((mp.isPlaying())) {
            mp.stop();
        }
        mp.release();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String name = (String)intent.getExtras().get("name");
        String start = (String)intent.getExtras().get("start");
        String end = (String)intent.getExtras().get("end");
        String date = (String)intent.getExtras().get("date");
        String time = (String)intent.getExtras().get("time");
        mp = MediaPlayer.create(AlarmService.this.getApplicationContext(), R.raw.test);
        mp.start();
        showAlertDialog(AlarmService.this, AlarmService.this::stopSelf, date, time, name, start, end);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
