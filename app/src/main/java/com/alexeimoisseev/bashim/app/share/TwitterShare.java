package com.alexeimoisseev.bashim.app.share;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by amois on 05.11.14.
 */
public class TwitterShare {
    public void share(Context context, String text, String link) {
        String postText = text;
        if(postText.length() > 114) {
            postText = postText.substring(0, 114) + "...";
        }
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                        "twitter://post?message=" + Uri.encode(postText) +
                                "&url=" + Uri.encode(link)
                    )));
        } catch (ActivityNotFoundException exception) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(
                        "https://twitter.com/intent/tweet?" +
                                "text=" + Uri.encode(postText) + "&" +
                                "url=" + Uri.encode(link)

                    )));

        }


    }
}
