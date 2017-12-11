package com.example.gorgesamir.newsapp;

import android.content.Context;

import java.util.List;

/**
 * Created by gorge samir on 2017-12-11.
 */

public class NewsAppLoader extends android.support.v4.content.AsyncTaskLoader<List<NewsApp>> {

    public static String newsAppRequestUrl =
            "https://content.guardianapis.com/search?q=free&format=json&order-by=newest&" +
                    "order-date=published&show-tags=contributor&show-fields=starRating,headline,thumbnail&api-key=test";

    public NewsAppLoader(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsApp> loadInBackground() {
        return QueryUtils.extractNewsAppData(newsAppRequestUrl);
    }
}