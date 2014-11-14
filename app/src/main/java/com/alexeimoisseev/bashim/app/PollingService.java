package com.alexeimoisseev.bashim.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import com.alexeimoisseev.bashim.app.activities.MainActivity;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

public class PollingService extends IntentService {

    RssParser parser = new RssParser("http://bash.im/rss");

    public PollingService() {
        super("pollingService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences prefs = getSharedPreferences("lastModified", 0);
        Long prevLastModified = prefs.getLong("date", 0);
        PendingIntent intn = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Log.i("TIMER", "Timer!");
        try {
            Date lastModified =  parser.lastModified();
            if(prevLastModified < lastModified.getTime()) {
                prefs.edit().putLong("date", lastModified.getTime());
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.ic_launcher)
                                .setContentText(getString(R.string.notification_newquotes))
                                .setContentTitle(getString(R.string.app_name))
                                .setAutoCancel(true)
                                .setContentIntent(intn);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, builder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
