package com.alexeimoisseev.bashim.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amois on 04.11.14.
 */
public class QuotesDbHelper extends SQLiteOpenHelper {
    protected Context context;
    public QuotesDbHelper(Context context) {
        super(context, "quotes", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE quotes (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, quote TEXT, link TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE quotes");
        onCreate(db);
    }

    public void clearQuotesTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM quotes");
        db.close();
    }

    public List<QuoteBean> getSavedQuotes(int offset) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                "quotes",
                new String[]{"id", "quote", "link"},
                null,
                null,
                null,
                null,
                "id DESC",
                String.valueOf(offset) + ",10");
        List<QuoteBean> res = new ArrayList<QuoteBean>();
        c.moveToFirst();
        while(!c.isAfterLast()) {
            QuoteBean quote = new QuoteBean();
            quote.setDescription(c.getString(
                    c.getColumnIndex("quote")
            ));
            quote.setLink(c.getString(
                    c.getColumnIndex("link")
            ));
            res.add(quote);
            c.moveToNext();
        }
        db.close();
        return  res;
    }

    public void saveQuotes(List<QuoteBean> quotes) {
        SQLiteDatabase db = getWritableDatabase();

        for(QuoteBean quote: quotes) {
            ContentValues vals = new ContentValues();
            vals.put("id", quote.getId());
            vals.put("quote", quote.getDescription());
            vals.put("link", quote.getLink());
            try {
                db.insertOrThrow("quotes", null, vals);
            } catch (SQLiteConstraintException e) {
                //this quote already saved. skipping
            }

        }
        db.close();

    }

}
