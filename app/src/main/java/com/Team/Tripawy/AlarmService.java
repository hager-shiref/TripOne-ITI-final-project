package com.ashehata.itifinalproject;

import static com.ashehata.itifinalproject.helper.HelperMethods.showAlertDialog;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.ashehata.itifinalproject.helper.HelperMethods;

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
