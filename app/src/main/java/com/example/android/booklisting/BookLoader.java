package com.example.android.booklisting;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    private final String mWebsite;
    private static final String LOG_TAG = BookLoader.class.getSimpleName();

    public BookLoader(Context context, String webSite) {
        super(context);
        mWebsite = webSite;
        Log.e(LOG_TAG, "BookLoader()");

    }

    // We override this method to call forceLoad() which is a required step
    // to actually trigger the loadInBackground() method to execute
    @Override
    protected void onStartLoading() {
        Log.e(LOG_TAG, "onStartLoading()");

        forceLoad();
    }

    @Override
    public List<Book> loadInBackground() {
        Log.e(LOG_TAG, "loadInBackground()");

        if (mWebsite == null || mWebsite.length() < 1)
            return null;

        /**
         *
         */
        List<Book> allBooks = HelperClass.extractBooks(mWebsite) ;

        return allBooks;
    }


}

