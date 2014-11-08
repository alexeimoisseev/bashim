package com.alexeimoisseev.bashim.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.alexeimoisseev.bashim.app.share.Share;


public class QuoteActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote);
        setTitle(getIntent().getStringExtra("ID"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView quoteText = (TextView) findViewById(R.id.quote_page_text);
        quoteText.setText(getIntent().getStringExtra("QUOTE"));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quote, menu);
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

    public void share(View view) {
        String text = getIntent().getStringExtra("QUOTE");
        String link = getIntent().getStringExtra("LINK");
        new Share().share(this, text, link);
    }
}
