package com.alexeimoisseev.bashim.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amois on 04.11.14.
 */
public class QuotesFetcher extends AsyncTask<String, Integer, List<PostBean>> {
    private Context context;
    private Callback callback;
    public QuotesFetcher(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected List<PostBean> doInBackground(String... params) {
        List<PostBean> items = new ArrayList<PostBean>();
        try {
            items = (new RssParser("http://bash.im/rss/")).fetch();
            Log.i("QuotesFetcher", "Fetched data from http://bash.im/rss/");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("RSS FETCH", e.getMessage());
            cancel(true);
        } catch (XmlPullParserException e) {
            Log.e("RSS FETCH", e.getMessage());
            e.printStackTrace();
        }
        return items;
    }

    @Override
    protected void onCancelled() {
        callback.callback("Ошибка сети.");
    }

    @Override
    protected void onPostExecute(List<PostBean> quotes) {
        super.onPostExecute(quotes);
        QuotesDbHelper hlp = new QuotesDbHelper(context);
        hlp.saveQuotes(quotes);
        Log.i("QuotesFetcher", "Saved quotes to SQL. callback() now.");
        callback.callback(null);
    }
}
