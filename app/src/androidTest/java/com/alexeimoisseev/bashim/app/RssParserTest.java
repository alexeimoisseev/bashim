package com.alexeimoisseev.bashim.app;

import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

/**
 * Created by amois on 11.11.14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18)
public class RssParserTest {
    @Test
    public void testFetch() throws IOException, XmlPullParserException {
        RssParser parser = new RssParser("http://bash.im/rss/");
        List<QuoteBean> quotes = parser.fetch();
        Assert.assertEquals(100, quotes.size());
    }

    @Test(expected = XmlPullParserException.class)
    public void testNoFetch() throws IOException, XmlPullParserException {
        RssParser parser = new RssParser("http://www.yandex.ru");
        Assert.assertEquals(0, parser.fetch().size());
    }
}
