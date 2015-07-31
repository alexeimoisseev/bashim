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
    private String dbName;
    public QuotesDbHelper(Context context, String name) {
        super(context, name, null, 2);
        this.dbName = name;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + dbName + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, quote TEXT, link TEXT, date INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + dbName + " ADD COLUMN date INTEGER");
        }

    }

    public void clearQuotesTable() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + dbName);
        db.close();
    }

    public List<QuoteBean> getSavedQuotes(int offset) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                dbName,
                new String[]{"id", "quote", "link", "date"},
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

            quote.setDate(c.getLong(
                    c.getColumnIndex("date")
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
            vals.put("date", quote.getDate().getTime());
            try {
                db.insertOrThrow(dbName, null, vals);
            } catch (SQLiteConstraintException e) {
                //this quote already saved. skipping
            }

        }
        db.close();

    }

}
