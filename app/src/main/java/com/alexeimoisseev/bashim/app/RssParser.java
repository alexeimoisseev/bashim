package com.alexeimoisseev.bashim.app;

import android.text.Html;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amois on 04.11.14.
 */
public class RssParser {
    private String url;
    public RssParser(String url) {
        this.url = url;
    }
    public List<QuoteBean> fetch() throws IOException, XmlPullParserException {
        List<QuoteBean> items = new ArrayList<QuoteBean>();
        XmlPullParser parser = getParser();
        int event = parser.getEventType();
        QuoteBean quote = new QuoteBean();

        boolean insideItem = false;
        while (event != XmlPullParser.END_DOCUMENT) {
            if (event == XmlPullParser.START_TAG) {

                if (parser.getName().equalsIgnoreCase("item")) {
                    insideItem = true;
                } else if (parser.getName().equalsIgnoreCase("link")) {
                    if (insideItem) {
                        quote.setLink(parser.nextText());
                    }

                } else if (parser.getName().equalsIgnoreCase("description")) {
                    if (insideItem) {
                        quote.setDescription(Html.fromHtml(parser.nextText()).toString());
                    }

                }
            } else if (event == XmlPullParser.END_TAG && parser.getName().equalsIgnoreCase("item")) {
                insideItem = false;
                items.add(quote);
                quote = new QuoteBean();
            }

            event = parser.next(); // move to next element
        }
        return items;
    }

    private XmlPullParser getParser() throws IOException, XmlPullParserException {
        URL _url = new URL(this.url);
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(_url.openStream(), "CP-1251");
        return parser;
    }

}
