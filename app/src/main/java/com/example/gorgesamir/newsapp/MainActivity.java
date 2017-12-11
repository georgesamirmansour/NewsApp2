package com.example.gorgesamir.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsApp>> {


    NewsAdapter newsAdapter;
    ListView listView;
    ArrayList<NewsApp> newsAppArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (checkIfConnectedToInternet()) {
            newsAdapter = new NewsAdapter(this, newsAppArrayList);
            listView = findViewById(R.id.list);
            listView.setAdapter(newsAdapter);
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(1, null, this);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsApp newsApp = newsAdapter.getItem(position);
                    Uri uri = Uri.parse(newsApp.getWebUrl());
                    if (newsApp.getWebUrl() == null || TextUtils.isEmpty(newsApp.getWebUrl())) {
                        Toast.makeText(MainActivity.this, "No Uri found", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                }
            });

        } else {
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
            TextView textView = findViewById(R.id.empty_text);
            textView.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, R.string.no_internet,
                    Toast.LENGTH_SHORT).show();
        }
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
    public Loader<ArrayList<NewsApp>> onCreateLoader(int id, Bundle args) {
        return new NewsAppLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsApp>> loader, ArrayList<NewsApp> data) {
        if (data.size() != 0) {
            newsAdapter.addAll(data);
            ProgressBar progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        } else {
            TextView textView = findViewById(R.id.empty_text);
            textView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsApp>> loader) {
        newsAdapter.clear();
    }
}
