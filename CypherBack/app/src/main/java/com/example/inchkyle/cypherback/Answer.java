package com.example.inchkyle.cypherback;

import java.io.Serializable;

/**
 * Created by inchkyle on 11/16/16.
 */

public class Answer implements Serializable {
    String answer_text;
    String loc_id;
    String question_id;
    String time_answered;
    String user;

    Answer(String q_id, String a, String location_id, String time_a, String user_name) {
        this.answer_text = a;
        this.loc_id = location_id;
        this.question_id = q_id;
        this.time_answered = time_a;
        this.user = user_name;

    }
    //This class sees if the answer is a note for the location, or an answer to a question
    public boolean is_note() {
        if (question_id == null){
            return true;
        }
        return false;
    }

    public void print() {
        System.out.println("User (" + user +
                            ")\tloc_id (" + loc_id +
                            ")\ttime_answered (" + time_answered +
                            ")\tquestion_id (" + question_id +
                            ")\tanswer_text (" + answer_text + ")");
    }



}
