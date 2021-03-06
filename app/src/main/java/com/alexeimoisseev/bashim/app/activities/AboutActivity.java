package com.alexeimoisseev.bashim.app.activities;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.alexeimoisseev.bashim.app.R;
import com.alexeimoisseev.bashim.app.db.QuotesDbHelper;


public class AboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public void clearDb(View view) {
        QuotesDbHelper hlp = new QuotesDbHelper(this, "quotes");
        hlp.clearQuotesTable();
        QuotesDbHelper hlp2 = new QuotesDbHelper(this, "comics");
        hlp2.clearQuotesTable();
        setResult(1);
    }
}
