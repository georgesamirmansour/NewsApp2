package com.example.gorgesamir.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsApp>> {

    public static String newsAppRequestUrl = "https://content.guardianapis.com/search?q=free&format=json" +
            "&order-by=newest&order-date=published&show-fields=starRating,headline,thumbnail&api-key=test\n";
    NewsAdapter newsAdapter;
    ListView listView;
    ArrayList<NewsApp> newsAppArrayList = new ArrayList<>();
    // eh el extend loader class dh

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoaderManager loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(1, null, this);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                NewsApp newsApp = newsAdapter.getItem(position);
//                Uri uri = Uri.parse(newsApp.getWebUrl());
//                if (newsApp.getWebUrl() == null || TextUtils.isEmpty(newsApp.getWebUrl())) {
//                    Toast.makeText(MainActivity.this, "No Uri found", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                    startActivity(intent);
//                }
//            }
//        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkIfConnectedToInternet();
    }

    public boolean checkIfConnectedToInternet() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo =
                connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo
                != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }

    @Override
    public Loader<List<NewsApp>> onCreateLoader(int id, Bundle args) {
        if (checkIfConnectedToInternet()) {
            Toast.makeText(MainActivity.this, getString(R.string.connection), Toast.LENGTH_SHORT).show();
// hat elcode w eddey 5 min
            return new newsAppLoaders(getApplicationContext());
            // here look
        } else {
            Toast.makeText(MainActivity.this, R.string.no_internet,
                    Toast.LENGTH_SHORT).show();
            return null;
        }

    }

    @Override
    public void onLoadFinished(Loader<List<NewsApp>> loader, List<NewsApp> data) {
        newsAdapter = new NewsAdapter(this, newsAppArrayList);
        listView = findViewById(R.id.list);
        listView.setAdapter(newsAdapter);

        if (listView.getCount() == 0) {
            TextView textView = findViewById(R.id.empty_text);
            textView.setVisibility(View.VISIBLE);
        }
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsApp>> loader) {
        newsAdapter.clear();
    }


    private class newsAppLoaders extends AsyncTaskLoader<List<NewsApp>> {

        public newsAppLoaders(Context context) {
            super(context);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        @Override
        public List<NewsApp> loadInBackground() {
            return newsAppArrayList = QueryUtils.extractNewsAppData(newsAppRequestUrl);
        }
    }
}
