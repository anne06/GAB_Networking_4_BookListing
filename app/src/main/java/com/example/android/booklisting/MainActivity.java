package com.example.android.booklisting;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
    implements LoaderManager.LoaderCallbacks<List<Book>> {
    private final static String LOG_TAG = MainActivity.class.getSimpleName();
    private final static String NULL_STRING = "";
    private final static String GOOGLE_API = "https://www.googleapis.com/books/v1/volumes?q=";
    private static String URL;
    // private adapter for the list of books
    private BookArrayAdapter mAdapter;

    // Specific ID for the loader
    private static final int BOOK_LOADER_ID = 1;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(LOG_TAG, "onCreate");

        // How to handle the adapter
        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.books_list_LV);
        bookListView.setEmptyView(findViewById(R.id.emptyListTextView));

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new BookArrayAdapter(this, new ArrayList<Book>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        // Add a listener on a clicked item to open
        // a new activity which displays all the book details
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Globals g = Globals.getInstance();
                g.setData(mAdapter.getItem(position));

                Intent intent = new Intent(MainActivity.this, DisplayABook.class);
                startActivity(intent);

            }
        });

        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        // Hide the loading indicator
        (findViewById(R.id.loading_indicator)).setVisibility(View.GONE);

        if (searchBtn != null){
            searchBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSearch();
                }
            });
        }

    }

    private void startSearch(){
        Log.e(LOG_TAG, "startSearch");


        /* First step: return the search criteria in a String */
        constructStringUrl();

        if (URL == NULL_STRING || URL == null) {
            Log.e(LOG_TAG, "URL null or \"\"");
            return;
        }

        /*
         * Second step:
         * - verification of the internet and network permissions
         * - create a loader and a loaderManager to launch
         *   the Http request in a background task
         */
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null)
        {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnected()){
                // Show the loading indicator
                (findViewById(R.id.loading_indicator)).setVisibility(View.VISIBLE);

                // Get a reference to the LoaderManager, in order to interact with loaders.
                LoaderManager loaderManager = getLoaderManager();


                // here, we need to reset and restart the loader
                Log.e(LOG_TAG, "before restartLoader()");
                loaderManager.restartLoader(BOOK_LOADER_ID, null, this);
                Log.e(LOG_TAG, "after restartLoader()");


                // Initialize the loader. Pass in the int ID constant defined above and pass in null for
                // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
                // because this activity implements the LoaderCallbacks interface).
                Log.e(LOG_TAG, "before initLoader()");
                loaderManager.initLoader(BOOK_LOADER_ID, null, this);
                Log.e(LOG_TAG, "after initLoader()");

            } else {
                ((TextView) findViewById(R.id.emptyListTextView)).setText(R.string.no_Internet_Connectivity);
                // Hide the loading indicator
                (findViewById(R.id.loading_indicator)).setVisibility(View.GONE);
            }
        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        Log.e(LOG_TAG, "onCreateLoader()");

        return new BookLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> books) {
        Log.e(LOG_TAG, "TEST: onLoadFinished()");

        // Clear the adapter of previous books data
        mAdapter.clear();

        // Hide the loading indicator
        (findViewById(R.id.loading_indicator)).setVisibility(View.GONE);


        // If there is a valid list of {@link book}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        } else {
            TextView emptyListTV = (TextView) findViewById(R.id.emptyListTextView);
            emptyListTV.setText(getString(R.string.empty_list));
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        Log.e(LOG_TAG, "TEST: onLoaderReset()");

        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }



    /*************************************************************
     *
     * This method returns the String enter in
     * the EditText widget, avec clicking on the search Button
     *
     * @return a String with the criteria
     *      null ih there is no criteria
     *
     */
    private void constructStringUrl(){
        Log.e(LOG_TAG, "constructStringUrl()");

        URL = GOOGLE_API;

        try {

            EditText text = findViewById(R.id.searchCriteria);
            String criteria = text.getText().toString();

            if (criteria == null || criteria.length() == 0){
                URL = NULL_STRING;
            } else {
                URL +=  criteria;
            }

            Log.e(LOG_TAG, "Critere de recherche: " + URL);
        } catch (NullPointerException e){
            URL = NULL_STRING;
        }

        URL = URL.replace(" ", "+");

        // We want to see at list 25 books
        URL += "&maxResults=25";
    }
}
