package com.example.android.booklisting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.TextView;

public class DisplayABook extends AppCompatActivity {

    private static final String LOG_TAG = DisplayABook.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_abook);

        Globals g = Globals.getInstance();
        Book aBook =  g.getData();

        Log.e(LOG_TAG, "DANS LA NOUVELLE ACTIVITE: " + aBook.toString());

        TextView titleTV = (TextView) findViewById(R.id.titleForABook_TV);
        titleTV.setText(aBook.getTitle());

        ScrollView listOfAuthors = (ScrollView) findViewById(R.id.authorForABook_LV);

    }
}
