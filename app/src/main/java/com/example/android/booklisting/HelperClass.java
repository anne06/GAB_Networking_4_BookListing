package com.example.android.booklisting;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class HelperClass {

    private static final String LOG_TAG = HelperClass.class.getSimpleName();

    private HelperClass(){}

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<Book> extractBooks(String webQuery) {
        Log.e(LOG_TAG, "TEST: extractBooks()");

        // The first step is to create an url
        URL url = createURL(webQuery);
        String jsonResponse = makeHttpRequest(url);
        Log.e(LOG_TAG, "\n\n**************************************JSON String: ************************************\n"
        + jsonResponse);
        List<Book> books = parseJSON(jsonResponse);

        // Return the list of earthquakes
        return books;

    }


    private static URL createURL(String webSiteURL) {
        Log.e(LOG_TAG, "createURL()");

        if (webSiteURL == null || webSiteURL.length() == 0)
            return null;

        URL url = null;
        try {
            url = new URL(webSiteURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }

        return url;
    }

    private static String makeHttpRequest(URL url) {
        Log.e(LOG_TAG, "makeHttpRequest()");

        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            // Establishment of the HTTP connection on the webserver
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000 /* milliseconds */);
            httpURLConnection.setConnectTimeout(15000 /* milliseconds */);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the book JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
        Log.e(LOG_TAG, "readFromStream()");

        if (inputStream == null)
            return null;

        StringBuilder jsonResponse = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            String line = reader.readLine();
            while (line != null) {
                jsonResponse.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error in network reading" + e);
            return null;
        }

        return jsonResponse.toString();
    }

    /**
     * Parse the JSON String and return a list of books
     *
     * @param  jsonResponse
     * @return List<Book>
     */
    private static List<Book> parseJSON(String jsonResponse) {
        Log.e(LOG_TAG, "parseJSON()");

        List<Book> allBooks = new ArrayList();

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            if (jsonResponse != null) {
                // Creation of the ROOT JSON object
                JSONObject baseJsonResponse = new JSONObject(jsonResponse);

                // Creation of the "features" JSON array
                JSONArray bookArray = baseJsonResponse.getJSONArray("items");

                for (int i = 0; i < bookArray.length(); i++) {
                    // On recupere les donnees du tableau comme etant un JSONObject !!!
                    JSONObject currentBook = bookArray.getJSONObject(i);

                    // Pour cette ligne, on recupere le "etag"
                    String etag = currentBook.getString("etag");

                    //  Pour cette ligne, on recupere l'objet "volumeInfo"
                    JSONObject volumeInfo = currentBook.getJSONObject("volumeInfo");

                    JSONObject thumbnailObject = null;
                    String thumbnail = "";

                    if (volumeInfo.has("imageLinks")) {
                        thumbnailObject = volumeInfo.getJSONObject("imageLinks");

                        if (thumbnailObject.has("smallThumbnail")) {
                            thumbnail = thumbnailObject.getString("smallThumbnail");
                        }
                    }

                    String title = volumeInfo.getString("title");

                    String[] authors;
                    if (volumeInfo.has("authors")) {
                        JSONArray jsonAuthors = volumeInfo.getJSONArray("authors");
                        authors = new String[jsonAuthors.length()];

                        for (int ia = 0; ia < jsonAuthors.length(); ia++) {
                            authors[ia] = jsonAuthors.getString(ia);
                        }
                    } else {
                        authors = null;
                    }

                    int pageCount = 0;
                    if (volumeInfo.has("pageCount"))
                        pageCount = volumeInfo.getInt("pageCount");

                    String language = "";
                    if (volumeInfo.has("language"))
                        language= volumeInfo.getString("language");

                    String description = "";
                    if (volumeInfo.has("description"))
                        description = volumeInfo.getString("description");

                    //  Pour cette ligne, dans l'objet "volumeInfo"
                    // on recupere le tableau "industryIdentifiers"
                    JSONArray industryIdentifiers = null;
                    String isbn10 = "";
                    String isbn13 = "";
                    if (volumeInfo.has("industryIdentifiers")) {
                        industryIdentifiers = volumeInfo.getJSONArray("industryIdentifiers");

                        for (int iii = 0; iii < industryIdentifiers.length(); iii++) {
                            JSONObject isbnArray = industryIdentifiers.getJSONObject(iii);
                            String typeIsbn = isbnArray.getString("type");
                            if (typeIsbn.equals("ISBN_10")) {
                                isbn10 = isbnArray.getString("identifier");
                            } else if (typeIsbn.equals("ISBN_13")) {
                                isbn13 = isbnArray.getString("identifier");
                            }
                        }
                    }

                    String[] categorie=null;
                    if (volumeInfo.has("categories")) {
                        JSONArray jsonCategories = volumeInfo.getJSONArray("categories");
                        categorie = new String[jsonCategories.length()];
                        for (int cat = 0; cat < jsonCategories.length(); cat++) {
                            categorie[cat] = jsonCategories.getString(cat);
                        }
                    }

                    float averageRating = 0;
                    if (volumeInfo.has("averageRating"))
                        averageRating = (float) volumeInfo.getLong("averageRating");

                    Book book = new Book(etag, title, authors, thumbnail, language, description, isbn10, isbn13, pageCount, categorie, averageRating);
                    allBooks.add(book);
                    Log.e(LOG_TAG, book.toString());
                }


            } else {
                Log.e(LOG_TAG, "The source String is null");
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        return allBooks;
    }
}
