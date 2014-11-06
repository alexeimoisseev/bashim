package com.alexeimoisseev.bashim.app;

import android.app.Application;
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
    synchronized Tracker getTracker(TrackerName name) {
        if(!mTrackers.containsKey(name)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            Tracker t = analytics.newTracker(PROPERTY_ID);
        }
        return mTrackers.get(name);
    }
}
