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
        // play your music here
        mp = MediaPlayer.create(this.getApplicationContext(), R.raw.test);
        mp.start();
        showAlertDialog(this, this::stopSelf);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if ((mp.isPlaying())) {
            mp.stop();
        }
        mp.release();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
