package com.alexeimoisseev.bashim.app;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
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
        SharedPreferences prefs = getSharedPreferences("lastModified", MODE_PRIVATE);
        Long prevLastModified = prefs.getLong("date", new Date().getTime());
        PendingIntent intn = PendingIntent.getActivity(
                this,
                0,
                new Intent(this, MainActivity.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        Log.i("TIMER", "Timer!");
        try {
            Log.i("TIMER", "Last modified from settings is " + new Date(prevLastModified).toString());

            Date lastModified =  parser.lastModified();
            Log.i("TIMER", "Last modified from rss is " + lastModified);
            if(prevLastModified < lastModified.getTime()) {
                Log.i("TIMER", "Saved new time " + lastModified.toString());
                prefs
                        .edit()
                        .putLong("date", lastModified.getTime())
                        .apply();
                NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentText(getString(R.string.notification_newquotes))
                                .setContentTitle(getString(R.string.app_name))
                                .setAutoCancel(true)
                                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                                .setContentIntent(intn);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotificationManager.notify(1, builder.build());
            } else {
                prefs
                        .edit()
                        .putLong("date", new Date().getTime())
                        .apply();
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
