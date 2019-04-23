package com.example.android.booklisting;

import java.util.Arrays;

public class Book {
    private String mEtag;
    private String mTitle;
    private String[] mAuthors;
    private String mThumbnail;
    private String mLanguage;
    private String mDescription;
    private String mIsbn10;
    private String mIsbn13;
    private int mPageCount;
    private String[] mCategories;
    private float mAverageRating;

    public Book(String etag, String title, String[] authors, String thumbnail, String language,
                String description, String isbn10, String isbn13,
                int pageCount, String[] categories, float averageRating) {
        this.mEtag = etag;
        this.mTitle = title;
        this.mAuthors = authors;
        this.mThumbnail = thumbnail;
        this.mLanguage = language;
        this.mDescription = description;
        this.mIsbn10 = isbn10;
        this.mIsbn13 = isbn13;
        this.mPageCount = pageCount;
        this.mCategories = categories;
        this.mAverageRating = averageRating;
    }

    @Override
    public String toString() {
        return "Book{" +
                "Title='" + mTitle + '\'' +
                ", Authors=" + Arrays.toString(mAuthors) +
                ", Description='" + mDescription + '\'' +
                ", Isbn10='" + mIsbn10 + '\'' +
                ", Isbn13='" + mIsbn13 + '\'' +
                ", PageCount=" + mPageCount +
                ", Categories=" + Arrays.toString(mCategories) +
                '}';
    }

    public String getmThumbnail() {
        return mThumbnail;
    }

    public String getEtag() {
        return mEtag;
    }

    public String getTitle() {
        return mTitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public String getLanguage() {
        return mLanguage;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getIsbn10() {
        return mIsbn10;
    }

    public String getIsbn13() {
        return mIsbn13;
    }

    public int getPageCount() {
        return mPageCount;
    }

    public String[] getCategories() {
        return mCategories;
    }

    public float getAverageRating() {
        return mAverageRating;
    }

}

