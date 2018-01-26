package com.technophile.sarah;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<List<Articles>> {
    private static final String API_KEY = "your-api-key";
    private static final int NEWS_LOADER_ID = 1;
    private static final int SETTINGS_RESULTS = 12;
    private ArticleAdapter mArticleAdapter;
    private ListView listView;
    private TextView emptyTxt;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.articlesList);
        mArticleAdapter = new ArticleAdapter(this, new ArrayList<Articles>());
        emptyTxt = (TextView) findViewById(R.id.emptyTxtView);


        if (isNetworkConnected()) {
            refresh();

            emptyTxt.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.VISIBLE);

        }


        //navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {
            // Handle the settings action
            startActivityForResult(new Intent(this, SettingsActivity.class), SETTINGS_RESULTS);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        try {

            if (cm != null) {
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null) {
                    return netInfo.isConnectedOrConnecting();
                }
            }
        } catch (Exception ex) {
            Log.e("Network Avail Error", ex.getMessage());
        }
        return false;
    }

    private String getNewsString() {
        String mCategory, mTag;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mCategory = preferences.getString(getString(R.string.setting_query_key), "politics");

        mTag = preferences.getString(getString(R.string.setting_Tag_key), "debate");
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("q", mTag)
                .appendQueryParameter("show-tags", "contributor")
                .appendQueryParameter("tag", mCategory + "/" + mCategory)
                .appendQueryParameter("api-key", API_KEY);
        String myUrl = builder.build().toString();

        Log.w("news url", myUrl);

        return myUrl;
    }

    LoaderManager loaderManager;

    private void refresh() {
        setProgressBar(true);
        loaderManager = getSupportLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this).forceLoad();
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SETTINGS_RESULTS) {
            refresh();
        }
    }


    private void setProgressBar(boolean isShowing) {
        if (isShowing) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }


    @Override
    public Loader<List<Articles>> onCreateLoader(int id, Bundle args) {
        Log.w("onCreateLoader()", getNewsString());
        return new ArticleLoader(this, getNewsString());
    }


    @Override
    public void onLoadFinished(Loader<List<Articles>> loader, List<Articles> data) {
        emptyTxt.setText(R.string.no_more_articles);
        loaderManager.destroyLoader(NEWS_LOADER_ID);
        setProgressBar(false);
        mArticleAdapter.clear();
        if (data != null && !data.isEmpty()) {
            mArticleAdapter.addAll(data);
            listView.setAdapter(mArticleAdapter);
            mArticleAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "No news found", Toast.LENGTH_SHORT).show();
        }
    }

    public void onLoaderReset(Loader<List<Articles>> loader) {
        mArticleAdapter.clear();
    }
}
