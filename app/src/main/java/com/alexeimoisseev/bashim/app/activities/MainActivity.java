package com.alexeimoisseev.bashim.app.activities;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.alexeimoisseev.bashim.app.*;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import com.alexeimoisseev.bashim.app.db.QuotesDbHelper;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;


public class MainActivity extends AppCompatActivity {

    private QuotesArrayAdapter adapter;
    final Context that = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleAnalytics.getInstance(this).getLogger()
                .setLogLevel(Logger.LogLevel.VERBOSE);
        Tracker t = ((BashImApplication) getApplication()).getTracker(BashImApplication.TrackerName.APP_TRACKER);
        t.setScreenName("main window");
        t.send(new HitBuilders.AppViewBuilder().build());

        setContentView(R.layout.activity_main);
        adapter = new QuotesArrayAdapter(this, new QuotesDbHelper(this, "quotes"));
        final ListView lv = (ListView) findViewById(R.id.list_view);
        final SwipeRefreshLayout swipe = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe);
        swipe.setColorSchemeResources(R.color.orange, R.color.blue, R.color.green);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipe.setRefreshing(true);
                load(swipe);
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.i("Endless scroll", String.valueOf(page));
                adapter.loadMore(page);
            }
        });
        swipe.setRefreshing(true);
        load(swipe);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuoteBean quote = adapter.getItem(position);
                Intent intent = new Intent(that, QuoteActivity.class);
                intent.putExtra("ID", that.getString(R.string.quote) + quote.getId().toString());
                intent.putExtra("QUOTE", quote.getDescription());
                intent.putExtra("LINK", quote.getLink());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancelAll();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GoogleAnalytics.getInstance(this).reportActivityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_about) {
            Intent about = new Intent(this, AboutActivity.class);
            startActivityForResult(about, 1);
        } else if (id == R.id.action_comics) {
            Intent comics = new Intent(this, ComicsActivity.class);
            startActivityForResult(comics, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("Result is", String.valueOf(resultCode));
        if (resultCode == 1) {
            adapter.reload();

        }
    }

    public void load(final SwipeRefreshLayout swipe) {
        QuotesFetcher fetcher = new QuotesFetcher(this, "quotes", "http://bash.im/rss/", new Callback() {
            @Override
            public void callback(Object error) {
                if(error == null) {
                    adapter.reload();
                } else {
                    Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
                swipe.setRefreshing(false);
            }
        });
        fetcher.execute();
    }

    public void clearDb(View view) {
        QuotesDbHelper hlp = new QuotesDbHelper(this, "quotes");
        hlp.clearQuotesTable();
    }

    public void clearCache(MenuItem item) {
        QuotesDbHelper hlp = new QuotesDbHelper(this, "quotes");
        hlp.clearQuotesTable();
    }
}
