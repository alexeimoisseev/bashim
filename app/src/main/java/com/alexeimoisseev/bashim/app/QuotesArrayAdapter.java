package com.alexeimoisseev.bashim.app;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import com.alexeimoisseev.bashim.app.db.QuotesDbHelper;

import java.util.List;

/**
 * Created by amois on 04.11.14.
 */
public class QuotesArrayAdapter extends ArrayAdapter<QuoteBean> {
    private List<QuoteBean> quotes;
    private QuotesDbHelper hlp;
    private Context context;
    public QuotesArrayAdapter(Context context) {
        super(context, R.layout.view_post);
        this.context = context;
        hlp = new QuotesDbHelper(context);
        quotes = hlp.getSavedQuotes(0);
        for(QuoteBean quote: quotes) {
            add(quote);
            Log.i("Quote", quote.getLink());
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuoteBean quote = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.view_post, null);
        }
        ((TextView) convertView.findViewById(R.id.quote_text)).setText(quote.getDescription());
        ((TextView) convertView.findViewById(R.id.id)).setText(context.getString(R.string.quote) +
                quote.getId().toString());
        return convertView;
    }

    public void reload() {
        quotes = hlp.getSavedQuotes(0);
        clear();
        for(QuoteBean quote: quotes) {
            add(quote);
        }
    }

    public void loadMore(int offset) {
        List<QuoteBean> newQuotes = hlp.getSavedQuotes(offset * 10);
        for(QuoteBean quote: newQuotes) {
            add(quote);
        }
//        notifyDataSetChanged();
    }
}
