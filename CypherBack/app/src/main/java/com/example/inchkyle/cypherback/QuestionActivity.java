package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by inchkyle on 11/3/16.
 */

//this is set aside for questions to be displayed on the screen, answers will be recorded here
public class QuestionActivity extends AppCompatActivity {

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.questions_screen);

        System.out.println("I'm in the q act");
        Intent home_intent = getIntent();

        if (home_intent != null) {

            user = (User) home_intent.getSerializableExtra("User");
            user.set_locations();

        }

    }
}
