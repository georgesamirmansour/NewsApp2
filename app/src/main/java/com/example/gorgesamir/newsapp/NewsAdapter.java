package com.example.gorgesamir.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by gogos on 2017-12-05.
 */

public class NewsAdapter extends ArrayAdapter<NewsApp> {

    public NewsAdapter(Context context, List<NewsApp> newsAppList) {
        super(context, 0, newsAppList);
    }

    public View getView(int position, View listItemView, ViewGroup parent) {
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.list_items, parent, false);
        }
        NewsApp currentNewApp = getItem(position);
        TextView sectionNameTextView = listItemView.findViewById(R.id.sectionName);
        sectionNameTextView.setText(currentNewApp.getSectionName());
        TextView webPublicationDateTextView = listItemView.findViewById(R.id.webPublicationDate);
        webPublicationDateTextView.setText(currentNewApp.getWebPublicationDate());
        TextView webTitleTextView = listItemView.findViewById(R.id.webTitle);
        webTitleTextView.setText(currentNewApp.getWebTitle());
        ImageView thumbnailImageView = listItemView.findViewById(R.id.thumbnail);
        thumbnailImageView.setImageBitmap(currentNewApp.getThumbnail());
        return listItemView;
    }
}
