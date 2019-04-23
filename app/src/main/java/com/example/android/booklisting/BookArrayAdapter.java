package com.example.android.booklisting;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BookArrayAdapter extends ArrayAdapter<Book> {
    private final static String LOG_TAG = BookArrayAdapter.class.getSimpleName();

    public BookArrayAdapter(@NonNull Context context, @NonNull List<Book> objects) {
        super(context, 0, objects);
        Log.e(LOG_TAG, "TEST: BookArrayAdapter()");

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Log.e(LOG_TAG, "TEST: getView()");


        // On recupere la View en cours: c'est la racine de la View (LinearLayout)
        View listItem = convertView;

        // La View est null: on en cree une a partir du fichier xml
        // elle contient donc tous les elements du xml (les 3 TextViews)
        if(listItem == null)
            listItem = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_item, null,false);


        // On recupere les infos "objet" stockees sur cette ligne
        final Book currentBook = getItem(position);

        // Et, on les affecte aux differents elements xml
        TextView title_TV = (TextView) listItem.findViewById(R.id.title_TV);
        TextView author_TV = (TextView) listItem.findViewById(R.id.author_TV);

        title_TV.setText(currentBook.getTitle());

        if (currentBook.getAuthors() == null){
            author_TV.setText(" ");
        } else {
            author_TV.setText(currentBook.getAuthors()[0]);
        }

        return listItem;
    }


}
