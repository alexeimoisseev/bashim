package com.alexeimoisseev.bashim.app;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;


/**
 * Created by amois on 06.11.14.
 */
public class BashImApplication extends Application {
    private static final String PROPERTY_ID = "UA-56488945-1";
    public enum TrackerName {
        APP_TRACKER,
        GLOBAL_TRACKER,
        ECOMMERCE_TRACKER
    }

    HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();
    public synchronized Tracker getTracker(TrackerName name) {
        if(!mTrackers.containsKey(name)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(PROPERTY_ID);
            mTrackers.put(TrackerName.APP_TRACKER, t);
        }
        return mTrackers.get(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, PollingService.class);
        PendingIntent pIntent = PendingIntent.getService(this, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime(), 1000 * 60 * 10, pIntent);

    }
}
