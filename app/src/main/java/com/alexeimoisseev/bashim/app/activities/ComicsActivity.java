package com.alexeimoisseev.bashim.app.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.alexeimoisseev.bashim.app.*;
import com.alexeimoisseev.bashim.app.beans.QuoteBean;
import com.alexeimoisseev.bashim.app.db.QuotesDbHelper;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ComicsActivity extends ActionBarActivity {

    QuotesArrayAdapter adapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comics);
        final PullToRefreshListView lv = (PullToRefreshListView) findViewById(R.id.list_comics);
        final Context that = this;
        adapter = new QuotesArrayAdapter(this, new QuotesDbHelper(this, "comics")) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = LayoutInflater
                            .from(getContext())
                            .inflate(R.layout.view_comics, null);
                }
                final ImageView img = (ImageView) convertView.findViewById(R.id.comics_image);
                QuoteBean bean = getItem(position);
                ((TextView)convertView.findViewById(R.id.comics_text)).setText("По мотивам цитаты #" + bean.getId().toString());
                final String imageUrl = bean.getDescription().replace("<img src=\"", "").replace("\">", "");
                UrlImageViewHelper.setUrlDrawable(img, imageUrl, null, 100000000l);

                return convertView;
            }

        };

        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> listViewPullToRefreshBase) {
                load(lv);
            }
        });

        lv.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                adapter.loadMore(page);
            }
        });

        lv.setAdapter(adapter);
        if(adapter.isEmpty()) {
            load(lv);
        }
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                QuoteBean bean = adapter.getItem(position - 1);
                final String imageUrl = bean.getDescription().replace("<img src=\"", "").replace("\">", "");
                Bundle bundle = new Bundle();
                bundle.putString("url", imageUrl);

                Intent fullScreenIntent = new Intent(that, ComicFullScreenActivity.class);
                fullScreenIntent.putExtras(bundle);
                startActivityForResult(fullScreenIntent, 0);
            }
        });
    }

    public void load(final PullToRefreshListView lv) {
        QuotesFetcher fetcher = new QuotesFetcher(this, "comics", "http://bash.im/rss/comics.xml", new Callback() {
            @Override
            public void callback(Object error) {
                if(error == null) {
                    adapter.reload();
                } else {
                    Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();
                }
                lv.onRefreshComplete();
            }
        });
        fetcher.execute();
    }



}
