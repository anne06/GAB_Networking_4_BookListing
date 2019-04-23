package com.example.android.booklisting;

public class Globals {

    private static Globals instance;

    // Global variable
    private Book book;

    // Restrict the constructor from being instantiated
    private Globals(){}

    public void setData(Book d){
        this.book=d;
    }
    public Book getData(){
        return this.book;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}

