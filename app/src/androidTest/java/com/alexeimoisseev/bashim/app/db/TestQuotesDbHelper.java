package com.alexeimoisseev.bashim.app.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.alexeimoisseev.bashim.app.activities.MainActivity;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by amois on 10.11.14.
 */

@RunWith(RobolectricTestRunner.class)
public class TestQuotesDbHelper {

    QuotesDbHelper helper = new QuotesDbHelper(new MainActivity(), "quotes");


    @Test
    public void testGetReadableDatabase() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Assert.assertTrue(db.isOpen());
    }

    @Test
    public void testClearQuotesTable() {
        helper.clearQuotesTable();
        Assert.assertEquals(0, helper.getSavedQuotes(0).size());

    }

    @Test
    public void testSaveQuotes() {
        helper.clearQuotesTable();
        List<QuoteBean> quotes = new ArrayList<QuoteBean>();
        QuoteBean quote = new QuoteBean();
        quote.setId(1l);
        quotes.add(quote);
        helper.saveQuotes(quotes);
        Assert.assertEquals(1, helper.getSavedQuotes(0).size());
    }
    @Test
    public void testGetSavedQuotes() {
        helper.clearQuotesTable();
        List<QuoteBean> quotes = new ArrayList<QuoteBean>();
        QuoteBean quote = new QuoteBean();
        quote.setId(1l);
        quotes.add(quote);
        helper.saveQuotes(quotes);

        List<QuoteBean> fetchedQuotes = helper.getSavedQuotes(0);
        Assert.assertTrue(fetchedQuotes.size() > 0);
    }

}
