package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by inchkyle on 11/3/16.
 */

//The purpose of this activity is to show all the Items, when clicked user will get popup
//to scan or type, then will be redirected to specific item questions
public class ItemListActivity extends AppCompatActivity {
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items_screen);

        // BELOW is from LOGIN
        Intent home_intent = getIntent();

        if (home_intent != null) {
            Bundle bundle = home_intent.getExtras();

            String payroll_id = bundle.getString("ID");
            String json_str = bundle.getString("JSON_str");

            user = new User(payroll_id, json_str);
            user.set_locations();

        }

    }

}
