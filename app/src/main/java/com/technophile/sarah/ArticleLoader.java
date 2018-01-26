package com.technophile.sarah;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;


public class ArticleLoader extends AsyncTaskLoader<List<Articles>> {

    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();

    private String articleURL;

    public ArticleLoader(Context context, String mUrl) {
        super(context);
        articleURL = mUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Articles> loadInBackground() {
        Log.d(LOG_TAG, "loadInBackground()");
        if (articleURL == null) {
            return null;
        }
        System.out.println("articleURL = " + articleURL);
        List<Articles> articlesList = HttpHandler.fetchArticles(articleURL);
        if (articlesList==null||articlesList.isEmpty()) {
            Log.d(LOG_TAG, "Nothing found");
        } else {
            Log.d(LOG_TAG, articlesList.get(0).getTitle());
        }
        return articlesList;
    }
}