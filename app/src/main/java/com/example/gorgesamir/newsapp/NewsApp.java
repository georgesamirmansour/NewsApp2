package com.example.gorgesamir.newsapp;

import android.graphics.Bitmap;

/**
 * Created by gogos on 2017-12-05.
 */

public class NewsApp {
    private String sectionName;
    private String webPublicationDate;
    private String authors;
    private String webUrl;
    private String webTitle;
    private Bitmap thumbnail;

    public NewsApp(String sectionName, String webPublicationDate, String webTitle, String webUrl, String authors, Bitmap thumbnail) {
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.thumbnail = thumbnail;
        this.authors = authors;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public String getWebPublicationDate() {
        return webPublicationDate;
    }

    public void setWebPublicationDate(String webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

}
