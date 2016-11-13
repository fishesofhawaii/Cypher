package com.example.inchkyle.listviewyesno;

/**
 * Created by inchkyle on 11/13/16.
 */

public class ItemDataProvider {
    private int resource;
    private String question;

    public ItemDataProvider(int image, String q) {
        this.resource = image;
        this.question = q;
    }


    public int getResource() {
        return resource;
    }

    public String getQuestion() {
        return question;
    }
}
