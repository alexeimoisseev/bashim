package com.alexeimoisseev.bashim.app.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import com.alexeimoisseev.bashim.app.R;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

public class ComicFullScreenActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comic_full_screen);
        String url = getIntent().getExtras().getString("url");
        ImageViewTouch img = (ImageViewTouch) findViewById(R.id.comics_image_fullscreen);
        img.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);
        UrlImageViewHelper.setUrlDrawable(img, url, null, 100000000l);
    }


    public void close(View view) {
        finishActivity(0);
    }
}
