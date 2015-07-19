package com.alexeimoisseev.bashim.app.share;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by amois on 05.11.14.
 */
public class Share {
    public void share(Context context, String text, String link) {
        String postText = text;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format("%s %s via https://vk.com/bashorgruclub", postText, link));
        context.startActivity(intent);

    }
}
