package com.example.inchkyle.cypherback;

/**
 * Created by inchkyle on 11/13/16.
 */

public class QuestionDataProvider {
    private int resource;
    private String question;
    private String answer;

    public QuestionDataProvider(int image, String q, String answer) {
        this.resource = image;
        this.question = q;
        this.answer = answer;
    }


    public int getResource() {
        return resource;
    }

    public String getQuestion() {
        return question;
    }
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String ans) {
        this.answer = ans;
    }
}


