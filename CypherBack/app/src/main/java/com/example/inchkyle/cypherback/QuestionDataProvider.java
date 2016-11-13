package com.example.inchkyle.cypherback;

/**
 * Created by inchkyle on 11/13/16.
 */

public class QuestionDataProvider {
    private int resource;
    private String question;

    public QuestionDataProvider(int image, String q) {
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


