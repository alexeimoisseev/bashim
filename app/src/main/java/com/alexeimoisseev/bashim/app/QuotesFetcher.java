package com.alexeimoisseev.bashim.app;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import com.alexeimoisseev.bashim.app.db.QuotesDbHelper;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by amois on 04.11.14.
 */
public class QuotesFetcher extends AsyncTask<String, Integer, List<QuoteBean>> {
    private Context context;
    private Callback callback;
    public QuotesFetcher(Context context, Callback callback) {
        this.context = context;
        this.callback = callback;
    }

    @Override
    protected List<QuoteBean> doInBackground(String... params) {
        List<QuoteBean> items = new ArrayList<QuoteBean>();
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
    protected void onPostExecute(List<QuoteBean> quotes) {
        super.onPostExecute(quotes);
        QuotesDbHelper hlp = new QuotesDbHelper(context);
        hlp.saveQuotes(quotes);
        Log.i("QuotesFetcher", "Saved quotes to SQL. callback() now.");
        callback.callback(null);
    }
}
