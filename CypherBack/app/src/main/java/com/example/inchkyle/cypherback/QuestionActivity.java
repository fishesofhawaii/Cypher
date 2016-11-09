package com.example.inchkyle.cypherback;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

        Intent home_intent = getIntent();

        if (home_intent != null) {
            user = (User) home_intent.getSerializableExtra("User");

        }
        TextView textView = (TextView) findViewById(R.id.textView);

        //If location barcode is equal to the scanned barcode we are looking at location questions
        if (user.location_barcode.equals(user.current_barcode)) {
            Location location = user.get_location(user.current_barcode);

            textView.setText(location.loc_barcode_name + " Location Questions " +
                    "\n(Barcode #" + user.current_barcode + ")");
            //Temporary just to get data on there... will be deleted
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            new ArrayList<String>(location.location_question_answer_map.keySet()));

            ListView listview = (ListView)findViewById(R.id.questions_listview);
            listview.setAdapter(itemsAdapter);
        }
        //Otherwise we are looking at an item barcode
        else {
            Item item = user.get_location(user.location_barcode).get_item(user.current_barcode);

            //example for below : "Fire Extinguisher Questions (Barcode #20)"
            textView.setText(item.item_type + " Questions \n(Barcode #" + item.barcode_num + ")");
            //Temporary just to get data on there... will be deleted simply sets an arraylist
            ArrayAdapter<String> itemsAdapter =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                            new ArrayList<String>(item.item_question_answer_map.keySet()));


            ListView listview = (ListView)findViewById(R.id.questions_listview);
            listview.setAdapter(itemsAdapter);

        }
    }

    public void confirm_answers(View v) {
        Intent intent = new Intent(QuestionActivity.this, ItemListActivity.class);
        intent.putExtra("User", user);


        startActivity(intent);
    }
}
